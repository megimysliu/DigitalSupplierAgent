package co.almotech.digitalsupplieragent.ui.orders;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.BottomNavGraphDirections;
import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.databinding.FragmentOrdersBinding;
import co.almotech.digitalsupplieragent.data.model.ModelOrders;
import co.almotech.digitalsupplieragent.data.model.ModelOrdersResponse;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
@AndroidEntryPoint
public class OrdersFragment extends Fragment implements OrdersAdapter.OnClickOrderListener {
    private FragmentOrdersBinding mBinding;
    private OrdersViewModel mViewModel;
    private List<ModelOrders> mOrders = new ArrayList<>();
    private OrdersAdapter mAdapter;
    private NavController mController;
    private LoginViewModel mLoginViewModel;

    public OrdersFragment() {

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.agentFragment:
                mController.navigate(BottomNavGraphDirections.actionAgent());
                return true;
            case R.id.cartFragment:
                mController.navigate(BottomNavGraphDirections.actionCart());
                return true;
            case R.id.action_logout:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_confirm_txt)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, ((dialog, which) -> logout())).show();
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(OrdersViewModel.class);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mViewModel.getOrders();
        mViewModel.orders().observe(getViewLifecycleOwner(),this::consumeOrders);
        setHasOptionsMenu(true);

        mBinding = FragmentOrdersBinding.inflate(inflater,container,false);
        setupRecyclerView();
        mController = NavHostFragment.findNavController(this);

        return mBinding.getRoot();
    }

    private void setupRecyclerView(){

        RecyclerView recyclerView = mBinding.ordersRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        mAdapter = new OrdersAdapter(mOrders,this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClickOrder(ModelOrders order) {

        mViewModel.setOrder(order);
        mController.navigate(OrdersFragmentDirections.actionOrderDetail());

    }

    private void consumeOrders(ModelOrdersResponse response){
        if(!response.getError()){

            List<ModelOrders> orders = response.getOrders();
            mOrders.clear();
            mOrders.addAll(orders);
            mAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
            Timber.e(response.getMessage());
        }
    }

    private void logout(){

        mLoginViewModel.logout();
        ProcessPhoenix.triggerRebirth(requireContext());

    }
}

package co.almotech.digitalsupplieragent.ui.orders;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.transition.MaterialElevationScale;
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
import java8.util.stream.StreamSupport;
import timber.log.Timber;
import static android.widget.LinearLayout.VERTICAL;
import static java8.util.stream.Collectors.toList;

/**
 * A simple {@link Fragment} subclass.
 */
@AndroidEntryPoint
public class OrdersFragment extends Fragment implements OrderListAdapter.OnClickOrderListener {
    private FragmentOrdersBinding mBinding;
    private OrdersViewModel mViewModel;
    private List<ModelOrders> mOrders = new ArrayList<>();
    private OrderListAdapter mAdapter;
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
        mController = NavHostFragment.findNavController(this);
        mBinding = FragmentOrdersBinding.inflate(inflater,container,false);
        setupRecyclerView();

        mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                searchOrders(editable.toString());

            }
        });


        return mBinding.getRoot();
    }

    private void setupRecyclerView(){

        RecyclerView recyclerView = mBinding.ordersRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        mAdapter = new OrderListAdapter(this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClickOrder(ModelOrders order,View v) {



        String transitionName =  getString(R.string.order_details_transition_name);
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(v,transitionName).build();
        mViewModel.setOrder(order);
        mController.navigate(OrdersFragmentDirections.actionOrderDetail(),extras);
         MaterialElevationScale exitTransition =  new MaterialElevationScale(false);
         exitTransition.setDuration(200);
        MaterialElevationScale reenterTransition = new MaterialElevationScale(true);
        reenterTransition.setDuration(200);


    }

    private void consumeOrders(ModelOrdersResponse response){
        mBinding.progressCircular.setVisibility(View.GONE);
        if(!response.getError()){

            List<ModelOrders> orders = response.getOrders();
            mOrders.clear();
            mOrders.addAll(orders);
            mAdapter.submitList(orders);
            mAdapter.notifyDataSetChanged();
            if(orders.isEmpty()){
                mBinding.ordersRelative.setVisibility(View.GONE);
                mBinding.errorLinear.setVisibility(View.VISIBLE);
            }

        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
            Timber.e(response.getMessage());
        }
    }

    private void logout(){

        mLoginViewModel.logout();
        mController.navigate(BottomNavGraphDirections.actionLogout());



    }

    private void searchOrders(String s) {
        List<ModelOrders> data = StreamSupport.stream(mOrders)
                .filter(modelOrders -> modelOrders.getClientName()!=null)
                .filter(modelOrders -> modelOrders.getClientName().toLowerCase().contains(s.toLowerCase())).collect(toList());

        mAdapter = new OrderListAdapter(this);
        mAdapter.submitList(data);
        mBinding.ordersRecyclerview.setAdapter(mAdapter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        postponeEnterTransition();
//        final ViewTreeObserver observer = view.getViewTreeObserver();
//        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                startPostponedEnterTransition();
//                return true;
//            }
//        });
    }
}

package co.almotech.digitalsupplieragent.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.databinding.FragmentOrderDetailsBinding;
import co.almotech.digitalsupplieragent.data.model.ModelOrderItems;
import co.almotech.digitalsupplieragent.data.model.ModelOrderItemsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelOrders;
import dagger.hilt.android.AndroidEntryPoint;

import static android.widget.LinearLayout.VERTICAL;

@AndroidEntryPoint
public class OrderDetailsFragment extends Fragment {

    FragmentOrderDetailsBinding mBinding;
    private OrdersViewModel mViewModel;
    private List<ModelOrderItems> mItems = new ArrayList<>();
    private OrderItemAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(OrdersViewModel.class);
        mBinding = FragmentOrderDetailsBinding.inflate(inflater,container,false);
        mViewModel.order().observe(getViewLifecycleOwner(), modelOrders -> mBinding.setOrder(modelOrders));
        ModelOrders order = mViewModel.order().getValue();
        mBinding.setOrder(order);
        mViewModel.getOrderItems(order.getId());

        mViewModel.orderItems().observe(getViewLifecycleOwner(),this::consumeItems);
        setupRecyclerView();
        return mBinding.getRoot();
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = mBinding.itemsRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
        //recyclerView.addItemDecoration(itemDecor);
        mAdapter = new OrderItemAdapter(mItems);
        recyclerView.setAdapter(mAdapter);

    }

    private void consumeItems(ModelOrderItemsResponse response){

        if(!response.getError()){
            List<ModelOrderItems> items = response.getData();
            mItems.clear();
            mItems.addAll(items);
        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
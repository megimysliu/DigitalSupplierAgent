package co.almotech.digitalsupplieragent.ui.cart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.data.model.ModelItem;
import co.almotech.digitalsupplieragent.data.model.ModelPrices;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.databinding.FragmentCartBinding;
import co.almotech.digitalsupplieragent.utils.SwipeToDeleteCallback;
import timber.log.Timber;


public class CartFragment extends Fragment {
    private FragmentCartBinding mBinding;
    private CartViewModel mViewModel;
    private CartAdapter mAdapter;
    private List<ModelItem> mItems = new ArrayList<>();
    private List<ModelProducts> mProducts = new ArrayList<>();

    public CartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        mBinding = FragmentCartBinding.inflate(inflater,container,false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewmodel(mViewModel);
        mViewModel.getItems().observe(getViewLifecycleOwner(),this::consumeItems);
        mViewModel.getProducts().observe(getViewLifecycleOwner(),this::consumeProducts);
        setupRecyclerView();
        System.out.println("Items: " + mItems.toString());
        View view = mBinding.getRoot();
        return view;
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = mBinding.cartRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CartAdapter(mItems, mProducts, mViewModel,getContext());
        recyclerView.setAdapter(mAdapter);
        enableSwipeToDeleteAndUndo();

    }

    private void consumeItems(List<ModelItem> items){
        mItems.clear();
        mItems.addAll(items);
        mAdapter.notifyDataSetChanged();

    }

    private void consumeProducts(List<ModelProducts> products){
        mProducts.clear();
        mProducts.addAll(products);
        mAdapter.notifyDataSetChanged();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final ModelItem item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(mBinding.getRoot(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(item, position);
                        mBinding.cartRecyclerview.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mBinding.cartRecyclerview);
    }
}

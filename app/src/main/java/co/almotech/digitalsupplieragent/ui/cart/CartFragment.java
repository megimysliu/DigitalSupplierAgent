package co.almotech.digitalsupplieragent.ui.cart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.data.model.ModelItem;
import co.almotech.digitalsupplieragent.data.model.ModelPrices;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.databinding.FragmentCartBinding;
import co.almotech.digitalsupplieragent.ui.categories.CategoryAdapter;
import co.almotech.digitalsupplieragent.ui.categories.ProductAdapter;
import co.almotech.digitalsupplieragent.utils.SwipeToDeleteCallback;
import java8.util.stream.StreamSupport;
import timber.log.Timber;

import static java8.util.stream.Collectors.toList;


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
        mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchCartItems(editable.toString());

            }
        });
        View view = mBinding.getRoot();
        return view;
    }

    private void setupRecyclerView(){

        mAdapter = new CartAdapter(mItems, mProducts, mViewModel,getContext());
        mBinding.cartRecyclerview.setAdapter(mAdapter);
        enableSwipeToDeleteAndUndo();

    }

    private void consumeItems(List<ModelItem> items){

        mBinding.progressCircular.setVisibility(View.GONE);
        mItems.clear();
        mItems.addAll(items);
        if(mItems.isEmpty()){
            mBinding.errorLinear.setVisibility(View.VISIBLE);
        }else{
            mBinding.errorLinear.setVisibility(View.GONE);
        }
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

    private void searchCartItems(String s) {
        List<ModelProducts> data = StreamSupport.stream(mProducts)
                .filter(model -> model.getName() != null)
                .filter(model -> model.getName().toLowerCase().contains(s.toLowerCase())).collect(toList());
       List<ModelItem> items = new ArrayList<>();

       for( ModelItem item : mItems){

           if(isItem(data,item)){
               items.add(item);
           }

       }

        mAdapter = new CartAdapter(items, data, mViewModel,getContext());
        mBinding.cartRecyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private boolean isItem(List<ModelProducts> products, ModelItem item){

        for(ModelProducts product: products){
            if(product.getId() == item.getId()){
                return true;
            }
        }
        return false;
    }
}

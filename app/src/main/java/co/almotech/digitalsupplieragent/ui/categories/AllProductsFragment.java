package co.almotech.digitalsupplieragent.ui.categories;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import co.almotech.digitalsupplieragent.databinding.FragmentAllProductsBinding;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse;
import co.almotech.digitalsupplieragent.ui.cart.CartViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import java8.util.stream.StreamSupport;
import timber.log.Timber;

import static java8.util.stream.Collectors.toList;

@AndroidEntryPoint
public class AllProductsFragment extends Fragment {

    private FragmentAllProductsBinding mBinding;
    private CategoriesViewModel mViewModel;
    private ProductAdapter mProductAdapter;
    private List<ModelProducts> mProducts = new ArrayList<>();
    private NavController mNavController;
    private CartViewModel mCartViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAllProductsBinding.inflate(inflater,container,false);
        mViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        mCartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        mViewModel.products().observe(getViewLifecycleOwner(), this::consumeProducts);
        mViewModel.getAllProducts();
       mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {

               searchProduct(editable.toString());

           }
       });
        setupProductsRecyclerView();
        return mBinding.getRoot();
    }

    private void setupProductsRecyclerView(){

        mProductAdapter = new ProductAdapter(mProducts,mCartViewModel,getContext());
        mBinding.productsRecyclerview.setAdapter(mProductAdapter);

    }

    private void consumeProducts(ModelProductsResponse response){

        mBinding.progressCircular.setVisibility(View.GONE);
        if(!response.getError()){
            List<ModelProducts> products = response.getData();
            mProducts.clear();
            mProducts.addAll(products);
            if(mProducts.isEmpty()){
                mBinding.productsRelative.setVisibility(View.GONE);
                mBinding.errorLinear.setVisibility(View.VISIBLE);
            }
            mProductAdapter.notifyDataSetChanged();

        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
            Timber.e(response.getMessage());
        }

    }


    private void searchProduct(String s) {
        List<ModelProducts> data = StreamSupport.stream(mProducts)
                .filter(modelProducts -> modelProducts.getName()!=null)
                .filter(modelProducts -> modelProducts.getName().toLowerCase().contains(s.toLowerCase())).collect(toList());

        mProductAdapter = new ProductAdapter( data,mCartViewModel,getContext());
        mBinding.productsRecyclerview.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();
    }

}
package co.almotech.digitalsupplieragent.ui.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import timber.log.Timber;

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
        mViewModel.getAllProducts();
        mViewModel.products().observe(getViewLifecycleOwner(), this::consumeProducts);
        setupProductsRecyclerView();
        return mBinding.getRoot();
    }

    private void setupProductsRecyclerView(){
        RecyclerView recyclerView = mBinding.productsRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProductAdapter = new ProductAdapter(mProducts,mCartViewModel,getContext());
        recyclerView.setAdapter(mProductAdapter);

    }

    private void consumeProducts(ModelProductsResponse response){

        if(!response.getError()){
            List<ModelProducts> products = response.getData();
            mProducts.clear();
            mProducts.addAll(products);
            mProductAdapter.notifyDataSetChanged();

        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
            Timber.e(response.getMessage());
        }

    }
}
package co.almotech.digitalsupplieragent.ui.categories;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
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
import co.almotech.digitalsupplieragent.databinding.FragmentProductsBinding;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.data.model.ModelCategoriesResponse;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.data.model.ModelProductsResponse;
import co.almotech.digitalsupplieragent.ui.cart.CartViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;


@AndroidEntryPoint
public class ProductsFragment extends Fragment implements CategoriesAdapter.CategoryClickListener {

    private FragmentProductsBinding mBinding;
    private List<ModelCategories> mCategories = new ArrayList<>();
    private CategoriesAdapter mAdapter;
    private CategoriesViewModel mViewModel;
    private CartViewModel mCartViewModel;
    private ProductAdapter mProductAdapter;
    private List<ModelProducts> mProducts = new ArrayList<>();
    private NavController mNavController;
    private LoginViewModel mLoginViewModel;



    public ProductsFragment() {}


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.agentFragment:
                mNavController.navigate(BottomNavGraphDirections.actionAgent());
                return true;
            case R.id.cartFragment:
                mNavController.navigate(BottomNavGraphDirections.actionCart());
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
        mViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mCartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        mBinding = FragmentProductsBinding.inflate(inflater,container,false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mViewModel.categories().observe(getViewLifecycleOwner(), this::consumeCategories);
        setHasOptionsMenu(true);
        mViewModel.categoryProducts().observe(getViewLifecycleOwner(),this::consumeProducts);
        setupRecyclerView();
        setupProductsRecyclerView();
        mNavController = NavHostFragment.findNavController(this);
        mBinding.viewAll.setOnClickListener(v -> mNavController.navigate(ProductsFragmentDirections.actionCategories()));
        mBinding.viewAllProducts.setOnClickListener(v -> mNavController.navigate(ProductsFragmentDirections.actionAllProducts()));

        return mBinding.getRoot();
    }
    private void setupRecyclerView(){

        RecyclerView recyclerView = mBinding.categoriesRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mAdapter = new CategoriesAdapter(this,mCategories);
        recyclerView.setAdapter(mAdapter);

    }

    private void setupProductsRecyclerView(){
        RecyclerView recyclerView = mBinding.productsRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProductAdapter = new ProductAdapter(mProducts,mCartViewModel,getContext());
        recyclerView.setAdapter(mProductAdapter);

    }

    private void consumeCategories(ModelCategoriesResponse response){

        if(!response.getError()){
            List<ModelCategories> categories = response.getData();
            mCategories.clear();
            mCategories.addAll(categories);
            if(mViewModel.selectedCategory != -1){
                mAdapter.setSelected(mViewModel.selectedCategory);
                mViewModel.selectedCategory = -1;
            }else{
                mAdapter.setSelected(categories.get(0).getId());
            }

            mAdapter.notifyDataSetChanged();

        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void onCategoryClick(int id) {

        mViewModel.selectedCategory = id;
        mAdapter.setSelected(id);
        updateProducts(id);
    }

    private void updateProducts(int id){
        mViewModel.getProductsByCategory(id);

    }

    private void logout(){

        mLoginViewModel.logout();
        ProcessPhoenix.triggerRebirth(requireContext());

    }
}

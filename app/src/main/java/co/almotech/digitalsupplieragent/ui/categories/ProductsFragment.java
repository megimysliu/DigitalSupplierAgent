package co.almotech.digitalsupplieragent.ui.categories;

import android.app.AlertDialog;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
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
import android.widget.Toast;


import com.google.android.material.transition.MaterialFadeThrough;
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
import java8.util.stream.StreamSupport;
import timber.log.Timber;

import static java8.util.stream.Collectors.toList;


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
    private int categoryId = -1;



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

        mViewModel.getFirstCategory().observe(getViewLifecycleOwner(), integer -> {
            categoryId = integer;
            System.out.println("Id value " + categoryId);
            mViewModel.getCategoriesAndProducts(integer);

        });

        System.out.println("First category id: " + String.valueOf(categoryId));
        mViewModel.categories().observe(getViewLifecycleOwner(), this::consumeCategories);
        setHasOptionsMenu(true);
        mViewModel.categoryProducts().observe(getViewLifecycleOwner(),this::consumeProducts);
        setupRecyclerView();
        setupProductsRecyclerView();
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
        mNavController = NavHostFragment.findNavController(this);
        mBinding.viewAll.setOnClickListener(v -> mNavController.navigate(ProductsFragmentDirections.actionCategories()));
        mBinding.viewAllProducts.setOnClickListener(v -> mNavController.navigate(ProductsFragmentDirections.actionAllProducts()));

        return mBinding.getRoot();
    }
    private void setupRecyclerView(){

        mAdapter = new CategoriesAdapter(this,mCategories);
        mBinding.categoriesRecyclerview.setAdapter(mAdapter);

    }

    private void setupProductsRecyclerView(){

        mProductAdapter = new ProductAdapter(mProducts,mCartViewModel,getContext());
        mBinding.productsRecyclerview.setAdapter(mProductAdapter);

    }

    private void consumeCategories(ModelCategoriesResponse response){

        mBinding.progressCircular.setVisibility(View.GONE);

        if(!response.getError()){
            List<ModelCategories> categories = response.getData();
            mCategories.clear();
            mCategories.addAll(categories);
            if(mViewModel.selectedCategory != -1){
                mAdapter.setSelected(mViewModel.selectedCategory);
                mViewModel.selectedCategory = -1;
            }else{
                mAdapter.setSelected(categories.get(0).getId());
                //updateProducts(categories.get(0).getId());
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
            if(mProducts.isEmpty()){
                mBinding.productsRecyclerview.setVisibility(View.GONE);
                mBinding.errorProductsLinear.setVisibility(View.VISIBLE);
            }else{
                mBinding.productsRecyclerview.setVisibility(View.VISIBLE);
                mBinding.errorProductsLinear.setVisibility(View.GONE);
            }
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

    private void searchProduct(String s) {
        List<ModelProducts> data = StreamSupport.stream(mProducts)
                .filter(modelProducts -> modelProducts.getName()!=null)
                .filter(modelProducts -> modelProducts.getName().toLowerCase().contains(s.toLowerCase())).collect(toList());

        mProductAdapter = new ProductAdapter( data,mCartViewModel,getContext());
        mBinding.productsRecyclerview.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();

        List<ModelCategories> dataCat = StreamSupport.stream(mCategories)
                .filter(modelCategories -> modelCategories.getName() != null)
                .filter(modelCategories -> modelCategories.getName().toLowerCase().contains(s.toLowerCase())).collect(toList());

        mAdapter = new CategoriesAdapter(this, dataCat);
        mBinding.categoriesRecyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialFadeThrough());
    }
}

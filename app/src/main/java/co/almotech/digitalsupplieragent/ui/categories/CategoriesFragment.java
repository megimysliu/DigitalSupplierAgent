package co.almotech.digitalsupplieragent.ui.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.databinding.FragmentCategoriesBinding;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.data.model.ModelCategoriesResponse;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding mBinding;
    private List<ModelCategories> mCategories = new ArrayList<>();
    private CategoryAdapter mAdapter;
    CategoriesViewModel mViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        mBinding = FragmentCategoriesBinding.inflate(inflater,container,false);
        setupRecyclerView();
        mViewModel.categories().observe(getViewLifecycleOwner(),this::consumeCategories);
        return mBinding.getRoot();
    }

    private void setupRecyclerView(){

        RecyclerView recyclerView = mBinding.categoriesRecyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new CategoryAdapter(mCategories);
        recyclerView.setAdapter(mAdapter);
    }

    private void consumeCategories(ModelCategoriesResponse response){

        if(! response.getError()){

            List<ModelCategories> categories = response.getData();
            mCategories.clear();
            mCategories.addAll(categories);
        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
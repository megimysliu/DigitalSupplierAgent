package co.almotech.digitalsupplieragent.ui.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import java8.util.stream.StreamSupport;

import static java8.util.stream.Collectors.toList;

@AndroidEntryPoint
public class CategoriesFragment extends Fragment implements CategoryListAdapter.CategoryClickListener{

    private FragmentCategoriesBinding mBinding;
    private List<ModelCategories> mCategories = new ArrayList<>();
    private CategoryListAdapter mAdapter;

    CategoriesViewModel mViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        mBinding = FragmentCategoriesBinding.inflate(inflater,container,false);
        mViewModel.categories().observe(getViewLifecycleOwner(),this::consumeCategories);
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

                searchCategories(editable.toString());

            }
        });

        return mBinding.getRoot();
    }

    private void setupRecyclerView(){

        RecyclerView recyclerView = mBinding.categoriesRecyclerview;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new CategoryListAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    private void consumeCategories(ModelCategoriesResponse response){

        mBinding.progressCircular.setVisibility(View.GONE);
        if(! response.getError()){

            List<ModelCategories> categories = response.getData();
            mCategories.clear();
            mCategories.addAll(categories);
            if(categories.isEmpty()){
                mBinding.errorLinear.setVisibility(View.VISIBLE);
            }else{
                mBinding.errorLinear.setVisibility(View.GONE);

            }
            mAdapter.submitList(categories);
        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void searchCategories(String s) {
        List<ModelCategories> data = StreamSupport.stream(mCategories)
                .filter(modelCategories -> modelCategories.getName()!=null)
                .filter(modelCategories -> modelCategories.getName().toLowerCase().contains(s.toLowerCase())).collect(toList());

        mAdapter = new CategoryListAdapter(this);
        mAdapter.submitList(data);
        mBinding.categoriesRecyclerview.setAdapter(mAdapter);

    }

    @Override
    public void onCategoryClick(int id) {

    }
}
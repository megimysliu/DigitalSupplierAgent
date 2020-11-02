package co.almotech.digitalsupplieragent.ui.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.almotech.digitalsupplieragent.databinding.CategoryItemBinding;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private CategoryClickListener mListener;
    private List<ModelCategories> mCategories;
    private int categorySelected;

    public CategoriesAdapter(CategoryClickListener listener, List<ModelCategories> categories){
        this.mListener = listener;
        this.mCategories = categories;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CategoryItemBinding binding = CategoryItemBinding.inflate(inflater,parent,false);
        return new CategoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        ModelCategories category = mCategories.get(position);
        holder.bind(category,mListener);

    }

    @Override
    public int getItemCount() {
        return mCategories != null ? mCategories.size() : 0;
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{

        private CategoryItemBinding mBinding;

        public CategoriesViewHolder(CategoryItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelCategories category, CategoryClickListener listener){

            mBinding.setCategory(category);
            mBinding.executePendingBindings();
            View v = mBinding.getRoot();
            v.setOnClickListener(view ->
                    listener.onCategoryClick(category.getId()));
        }
    }

    public interface CategoryClickListener{
        void onCategoryClick(int id);
    }

    public void setSelected(int id){
        categorySelected = id;
    }
}

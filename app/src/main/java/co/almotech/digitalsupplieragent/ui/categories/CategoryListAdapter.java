package co.almotech.digitalsupplieragent.ui.categories;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.databinding.CategoryItemLargeBinding;

public class CategoryListAdapter extends ListAdapter<ModelCategories, CategoryListAdapter.ViewHolder> {
    private CategoryClickListener mListener;
    private int categorySelected;
    public CategoryListAdapter(CategoryClickListener listener){
        super(diffCallback);
        mListener = listener;
    }

    public interface CategoryClickListener{
        void onCategoryClick(int id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CategoryItemLargeBinding binding = CategoryItemLargeBinding.inflate(inflater,parent,false);
        return  new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCategories category = getItem(position);
        holder.bind(category,mListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CategoryItemLargeBinding mBinding;

        public ViewHolder(CategoryItemLargeBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelCategories category,CategoryClickListener listener){

            mBinding.setCategory(category);
            mBinding.executePendingBindings();
            View v = mBinding.getRoot();
            v.setOnClickListener(view ->
                    listener.onCategoryClick(category.getId()));
        }
    }

    public static final DiffUtil.ItemCallback<ModelCategories>  diffCallback = new DiffUtil.ItemCallback<ModelCategories>(){

        @Override
        public boolean areItemsTheSame(@NonNull ModelCategories oldItem, @NonNull ModelCategories newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull ModelCategories oldItem, @NonNull ModelCategories newItem) {
            return (oldItem == newItem);
        }
    };

    public void setSelected(int id){
        categorySelected = id;
    }
}

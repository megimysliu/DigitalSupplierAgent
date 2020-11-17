package co.almotech.digitalsupplieragent.ui.categories;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.databinding.CategoryItemBinding;

public class CategoriesListAdapter  extends ListAdapter<ModelCategories, CategoriesListAdapter.CategoriesViewHolder> {
    private CategoryClickListener mListener;
    private int categorySelected;

    public CategoriesListAdapter(CategoryClickListener listener) {
        super(diffCallback);
        mListener = listener;
    }


    public interface CategoryClickListener{
        void onCategoryClick(int id);
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
        ModelCategories category = getItem(position);
        holder.bind(category,mListener,categorySelected == category.getId());
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{

        private CategoryItemBinding mBinding;

        public CategoriesViewHolder(CategoryItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelCategories category, CategoryClickListener listener,boolean clicked){

            mBinding.setCategory(category);
            mBinding.executePendingBindings();
            View v = mBinding.getRoot();
            int colorDefault = android.R.color.white;
            if(clicked){
                colorDefault = R.color.colorLightGray;
            }
            v.setOnClickListener(view ->
                    listener.onCategoryClick(category.getId()));

            mBinding.categoryLinear.setBackgroundColor(ContextCompat.getColor(v.getContext(), colorDefault));
            mBinding.cardView.setBackgroundColor(ContextCompat.getColor(v.getContext(), colorDefault));
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

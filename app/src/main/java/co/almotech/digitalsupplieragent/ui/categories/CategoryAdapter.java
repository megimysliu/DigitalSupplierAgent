package co.almotech.digitalsupplieragent.ui.categories;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import co.almotech.digitalsupplieragent.databinding.CategoryItemLargeBinding;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<ModelCategories> mCategories;

    public CategoryAdapter(List<ModelCategories> categories){
        mCategories = categories;
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

        ModelCategories category = mCategories.get(position);
        holder.bind(category);

    }

    @Override
    public int getItemCount() {
        return  mCategories != null ? mCategories.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CategoryItemLargeBinding mBinding;

        public ViewHolder(CategoryItemLargeBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelCategories category){
            mBinding.setCategory(category);
            mBinding.executePendingBindings();
        }
    }
}

package co.almotech.digitalsupplieragent.ui.categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.databinding.ProductItemBinding;
import co.almotech.digitalsupplieragent.ui.cart.CartViewModel;

public class ProductListAdapter extends ListAdapter<ModelProducts, ProductListAdapter.ViewHolder> {

    private CartViewModel mCartViewModel;
    private Context mContext;

    public ProductListAdapter( CartViewModel cartViewModel, Context context) {
        super(diffCallback);
        mCartViewModel = cartViewModel;
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ProductItemBinding binding = ProductItemBinding.inflate(inflater,parent,false);
        return  new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelProducts product = getItem(position);
        holder.bind(product,mCartViewModel,mContext);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ProductItemBinding mBinding;


        public ViewHolder(ProductItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelProducts product, CartViewModel viewModel, Context context){

            mBinding.setProduct(product);
            mBinding.addToCart.setOnClickListener(v -> {
                viewModel.addToCart(product);
                Toast.makeText(context, R.string.added_to_cart,Toast.LENGTH_SHORT).show();

            });
            mBinding.setViewmodel(viewModel);
            mBinding.executePendingBindings();
        }
    }


    public static final DiffUtil.ItemCallback<ModelProducts>  diffCallback = new DiffUtil.ItemCallback<ModelProducts>(){

        @Override
        public boolean areItemsTheSame(@NonNull ModelProducts oldItem, @NonNull ModelProducts newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull ModelProducts oldItem, @NonNull ModelProducts newItem) {
            return (oldItem == newItem);
        }
    };

}

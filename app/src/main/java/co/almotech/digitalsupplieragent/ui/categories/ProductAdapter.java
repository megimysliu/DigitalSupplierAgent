package co.almotech.digitalsupplieragent.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.ProductItemBinding;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.ui.cart.CartViewModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ModelProducts> mProducts;
    private CartViewModel mCartViewModel;
    private Context mContext;

    public ProductAdapter(List<ModelProducts> products, CartViewModel cartViewModel, Context context){

        mProducts = products;
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

        ModelProducts product = mProducts.get(position);
        holder.bind(product,mCartViewModel,mContext);
    }

    @Override
    public int getItemCount() {
        return mProducts !=null ? mProducts.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ProductItemBinding mBinding;


        public ViewHolder(ProductItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelProducts product,CartViewModel viewModel,Context context){

            mBinding.setProduct(product);
            mBinding.addToCart.setOnClickListener(v -> {
                viewModel.addToCart(product);
                Toast.makeText(context, R.string.added_to_cart,Toast.LENGTH_SHORT).show();

            });
            mBinding.setViewmodel(viewModel);
            mBinding.executePendingBindings();
        }
    }
}


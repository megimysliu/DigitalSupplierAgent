package co.almotech.digitalsupplieragent.ui.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.data.model.ModelItem;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.databinding.CartItemBinding;
import java8.util.stream.StreamSupport;

public class CartListAdapter extends ListAdapter<ModelItem, CartListAdapter.ViewHolder> {

    private List<ModelProducts> products;
    private CartViewModel viewModel;
    private Context mContext;
    public CartListAdapter( List<ModelProducts> products, CartViewModel viewModel, Context context){
        super(diffCallback);
        this.products = products;
        this.viewModel = viewModel;
        mContext = context;
    }


    public Context getContext() {
        return mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CartItemBinding binding = CartItemBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ModelItem item = getItem(position);
        ModelProducts product = StreamSupport.stream(products)
                .filter(p-> p.getId() == item.getId())
                .findFirst()
                .orElse(null);

        holder.bind(item,product,viewModel);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CartItemBinding mBinding;

        public ViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelItem item, ModelProducts product, CartViewModel viewModel){
            mBinding.setItem(item);
            mBinding.setProduct(product);
            mBinding.setViewmodel(viewModel);


            mBinding.executePendingBindings();
        }
    }


    public static final DiffUtil.ItemCallback<ModelItem>  diffCallback = new DiffUtil.ItemCallback<ModelItem>(){

        @Override
        public boolean areItemsTheSame(@NonNull ModelItem oldItem, @NonNull ModelItem newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull ModelItem oldItem, @NonNull ModelItem newItem) {
            return (oldItem == newItem);
        }
    };
    public void removeItem(int position) {
        ModelItem item = getItem(position);
        //List<ModelItem> items = getCurrentList();
        //items.remove(item);
        viewModel.deleteItem(item);
        //notifyItemRemoved(position);
    }

    public void restoreItem(ModelItem item, int position) {

        //getCurrentList().add(position,item);
        ModelProducts product = StreamSupport.stream(products)
                .filter(p-> p.getId() == item.getId())
                .findFirst()
                .orElse(null);
        viewModel.addToCartWithQuantity(product,item.getQuantity());
        //notifyItemInserted(position);
    }

    public void setProducts(List<ModelProducts> products){
        this.products = products;
    }

}

package co.almotech.digitalsupplieragent.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.almotech.digitalsupplieragent.data.model.ModelItem;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.databinding.CartItemBinding;
import java8.util.stream.StreamSupport;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<ModelItem> items;
    private List<ModelProducts> products;
    private CartViewModel viewModel;
    private Context mContext;

    public CartAdapter(List<ModelItem> items, List<ModelProducts> products, CartViewModel viewModel, Context context){

        this.items = items;
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
        ModelItem item = items.get(position);
        ModelProducts product = StreamSupport.stream(products)
                .filter(p-> p.getId() == item.getId())
                .findFirst()
                .orElse(null);

        holder.bind(item,product,viewModel);

    }

    @Override
    public int getItemCount() {
        return items !=null ? items.size() : 0 ;
    }

    public void removeItem(int position) {
        ModelItem item = items.get(position);
        items.remove(position);
        viewModel.deleteItem(item);
        notifyItemRemoved(position);
    }

    public void restoreItem(ModelItem item, int position) {
        items.add(position, item);
        ModelProducts product = StreamSupport.stream(products)
                .filter(p-> p.getId() == item.getId())
                .findFirst()
                .orElse(null);
         viewModel.addToCart(product);
        notifyItemInserted(position);
    }

    public List<ModelItem> getData(){
        return  items;
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
}

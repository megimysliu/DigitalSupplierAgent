package co.almotech.digitalsupplieragent.ui.orders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.almotech.digitalsupplieragent.data.model.ModelItem;
import co.almotech.digitalsupplieragent.data.model.ModelProducts;
import co.almotech.digitalsupplieragent.databinding.OrderItemSendBinding;
import java8.util.stream.StreamSupport;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.ViewHolder> {

    private List<ModelItem> mItems;
    private List<ModelProducts> mProducts;

    public AddItemAdapter(List<ModelItem> items, List<ModelProducts> products){
        mItems = items;
        mProducts = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderItemSendBinding binding = OrderItemSendBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelItem item = mItems.get(position);
        ModelProducts product = StreamSupport.stream(mProducts)
                .filter(p -> p.getId() == item.getId())
                .findFirst()
                .orElse(null);
        holder.bind(item,product);

    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        OrderItemSendBinding mBinding;

        public ViewHolder(OrderItemSendBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

        }

        public void bind(ModelItem item, ModelProducts product){
            mBinding.setItem(item);
            mBinding.setProduct(product);
            mBinding.executePendingBindings();
        }
    }
}

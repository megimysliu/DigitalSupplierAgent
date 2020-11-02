package co.almotech.digitalsupplieragent.ui.orders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.almotech.digitalsupplieragent.databinding.OrderItemBinding;
import co.almotech.digitalsupplieragent.data.model.ModelOrderItems;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private List<ModelOrderItems> mItems;

    public OrderItemAdapter(List<ModelOrderItems> items){

        mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderItemBinding binding = OrderItemBinding.inflate(inflater,parent,false);
        return  new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelOrderItems item = mItems.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0 ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private OrderItemBinding mBinding;

        public ViewHolder(OrderItemBinding binding) {

            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelOrderItems item){

            mBinding.setItem(item);
            mBinding.executePendingBindings();
        }
    }
}

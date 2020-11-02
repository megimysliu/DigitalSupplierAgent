package co.almotech.digitalsupplieragent.ui.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import co.almotech.digitalsupplieragent.databinding.InvoiceItemBinding;
import co.almotech.digitalsupplieragent.data.model.ModelOrders;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<ModelOrders> mOrders;
    private OnClickOrderListener mListener;

    public OrdersAdapter(List<ModelOrders> orders, OnClickOrderListener listener) {
        mOrders = orders;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        InvoiceItemBinding binding = InvoiceItemBinding.inflate(inflater,parent,false);
        return  new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelOrders order= mOrders.get(position);
        holder.bind(order,mListener);

    }

    @Override
    public int getItemCount() {
        return mOrders != null ? mOrders.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        InvoiceItemBinding mBinding;


        public ViewHolder(InvoiceItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ModelOrders order,OnClickOrderListener listener){
            mBinding.setOrder(order);
            mBinding.executePendingBindings();
            View v = mBinding.getRoot();
            v.setOnClickListener(view -> listener.onClickOrder(order));
        }

    }

    public  interface OnClickOrderListener{
        void onClickOrder(ModelOrders order);
    }
}

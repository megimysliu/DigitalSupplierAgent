package co.almotech.digitalsupplieragent.ui.orders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.almotech.digitalsupplieragent.data.model.ModelCategories;
import co.almotech.digitalsupplieragent.data.model.ModelOrders;
import co.almotech.digitalsupplieragent.databinding.InvoiceItemBinding;

public class OrderListAdapter  extends ListAdapter<ModelOrders, OrderListAdapter.ViewHolder> {

    private OnClickOrderListener mListener;

    public OrderListAdapter(OnClickOrderListener listener){
        super(diffCallback);
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
        ModelOrders order= getItem(position);
        holder.bind(order,mListener);
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
            v.setOnClickListener(view -> listener.onClickOrder(order,v));
        }

    }

    public  interface OnClickOrderListener{
        void onClickOrder(ModelOrders order,View v);
    }

    public static final DiffUtil.ItemCallback<ModelOrders>  diffCallback = new DiffUtil.ItemCallback<ModelOrders>(){

        @Override
        public boolean areItemsTheSame(@NonNull ModelOrders oldItem, @NonNull ModelOrders newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull ModelOrders oldItem, @NonNull ModelOrders newItem) {
            return (oldItem == newItem);
        }
    };
}

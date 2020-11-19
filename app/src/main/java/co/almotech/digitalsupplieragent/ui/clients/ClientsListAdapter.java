package co.almotech.digitalsupplieragent.ui.clients;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.databinding.ClientItemBinding;
import timber.log.Timber;

public class ClientsListAdapter extends ListAdapter<ModelClients, ClientsListAdapter.ClientsViewHolder> {

    private ClientClickListener mListener;

    public ClientsListAdapter(ClientClickListener listener){
        super(diffCallback);
        mListener = listener;
    }


    @NonNull
    @Override
    public ClientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ClientItemBinding binding = ClientItemBinding.inflate(inflater,parent,false);
        return new ClientsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsViewHolder holder, int position) {
        ModelClients client = getItem(position);
        holder.bind(client,mListener);
    }

    public static class ClientsViewHolder extends RecyclerView.ViewHolder{

        private ClientItemBinding mBinding;

        public ClientsViewHolder(ClientItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind(ModelClients client, ClientClickListener listener){

            mBinding.setClient(client);
            mBinding.executePendingBindings();

            View v = mBinding.getRoot();
            v.setOnClickListener(view ->

                    listener.onClientClick(client,v)
            );



        }
    }

    public interface ClientClickListener{

        void onClientClick(ModelClients client, View view);
    }

    public static final DiffUtil.ItemCallback<ModelClients>  diffCallback = new DiffUtil.ItemCallback<ModelClients>(){

        @Override
        public boolean areItemsTheSame(@NonNull ModelClients oldItem, @NonNull ModelClients newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull ModelClients oldItem, @NonNull ModelClients newItem) {
            return (oldItem == newItem);
        }
    };

}


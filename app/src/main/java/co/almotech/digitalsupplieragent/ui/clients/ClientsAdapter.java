package co.almotech.digitalsupplieragent.ui.clients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.almotech.digitalsupplieragent.databinding.ClientItemBinding;
import co.almotech.digitalsupplieragent.data.model.ModelClients;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientsViewHolder>{

    private ClientClickListener mListener;
    private List<ModelClients> mClients;

    public ClientsAdapter(ClientClickListener listener, List<ModelClients> clients){
        mListener = listener;
        mClients = clients;
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
         ModelClients client = mClients.get(position);
         holder.bind(client,mListener);

    }

    @Override
    public int getItemCount() {
        return  mClients != null ? mClients.size() : 0 ;
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

                listener.onClientClick(client)
            );



        }
    }

    public interface ClientClickListener{

        void onClientClick(ModelClients client);
    }
}

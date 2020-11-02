package co.almotech.digitalsupplieragent.ui.clients;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.databinding.FragmentClientBinding;
import co.almotech.digitalsupplieragent.data.model.ModelClients;


public class ClientFragment extends Fragment {

    private FragmentClientBinding mBinding;
    private ClientsViewModel mModel;

    public ClientFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mModel = new ViewModelProvider(requireActivity()).get(ClientsViewModel.class);
        mBinding = FragmentClientBinding.inflate(inflater,container,false);
        mBinding.setClient(mModel.getClient().getValue());
        mModel.getClient().observe(getViewLifecycleOwner(), new Observer<ModelClients>() {
            @Override
            public void onChanged(ModelClients modelClients) {
                mBinding.setClient(modelClients);
            }
        });

        return mBinding.getRoot();

    }
}

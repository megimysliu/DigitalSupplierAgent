package co.almotech.digitalsupplieragent.ui.clients;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.BottomNavGraphDirections;
import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.databinding.FragmentClientsBinding;
import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import java8.util.stream.StreamSupport;

import static android.view.View.resolveSizeAndState;
import static android.widget.LinearLayout.VERTICAL;
import static java8.util.stream.Collectors.toList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientsFragment extends Fragment  implements  ClientsAdapter.ClientClickListener{

     private ClientsViewModel mMainViewModel;
     private FragmentClientsBinding mBinding;

     private List<ModelClients> mClients = new ArrayList<>();
     private ClientsAdapter mClientsAdapter;
     NavController mNavController;
     private LoginViewModel mLoginViewModel;

    public ClientsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainViewModel = new ViewModelProvider(requireActivity()).get(ClientsViewModel.class);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        setHasOptionsMenu(true);

        mBinding = FragmentClientsBinding.inflate(inflater,container,false);

        mNavController = NavHostFragment.findNavController(this);
        mMainViewModel.getClients();
        setupRecyclerView();
        mMainViewModel.clients().observe(getViewLifecycleOwner(),this::consumeClients);

        View v = mBinding.getRoot();

        mBinding.addClient.setOnClickListener(view ->
             mNavController.navigate(ClientsFragmentDirections.actionAddClient())
                );

        mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchClient(s.toString());

            }
        });


        System.out.println("Clients : " + mClients);
       return v;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.agentFragment:
                mNavController.navigate(BottomNavGraphDirections.actionAgent());
                return true;
            case R.id.cartFragment:
                mNavController.navigate(BottomNavGraphDirections.actionCart());
                return true;
            case R.id.action_logout:
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_confirm_txt)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, ((dialog, which) -> logout())).show();

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void setupRecyclerView(){
        RecyclerView recyclerView =  mBinding.clientsRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        mClientsAdapter = new ClientsAdapter(this,mClients);
        recyclerView.setAdapter(mClientsAdapter);


    }

    @Override
    public void onClientClick(ModelClients client) {
        mMainViewModel.setClient(client);
        mNavController.navigate(ClientsFragmentDirections.actionClient());

    }

    private void consumeClients(ModelClientsResponse response){

        if(!response.getError()){
            List<ModelClients> clients = response.getData();
            if(clients != null){
                mClients.clear();
                mClients.addAll(clients);

            }
        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void logout(){

        mLoginViewModel.logout();
        ProcessPhoenix.triggerRebirth(requireContext());

    }

    private void searchClient(String s) {
        List<ModelClients> data = StreamSupport.stream(mClients)
                .filter(modelClients -> modelClients.getName().contains(s)).collect(toList());

        mClientsAdapter = new ClientsAdapter(this, data);
        mBinding.clientsRecyclerview.setAdapter(mClientsAdapter);
    }


}

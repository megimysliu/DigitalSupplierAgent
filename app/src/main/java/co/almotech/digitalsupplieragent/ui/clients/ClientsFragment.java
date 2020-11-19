package co.almotech.digitalsupplieragent.ui.clients;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import co.almotech.digitalsupplieragent.BottomNavGraphDirections;
import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.databinding.FragmentClientsBinding;
import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import dagger.hilt.android.AndroidEntryPoint;
import java8.util.stream.StreamSupport;
import timber.log.Timber;

import static android.widget.LinearLayout.VERTICAL;
import static java8.util.stream.Collectors.toList;



@AndroidEntryPoint
public class ClientsFragment extends Fragment  implements  ClientsListAdapter.ClientClickListener{

     private ClientsViewModel mMainViewModel;
     private FragmentClientsBinding mBinding;
     private List<ModelClients> mClients = new ArrayList<>();
     private  NavController mNavController;
     private LoginViewModel mLoginViewModel;
     private ClientsListAdapter mListAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentClientsBinding.inflate(inflater,container,false);
        View v = mBinding.getRoot();
        mMainViewModel = new ViewModelProvider(requireActivity()).get(ClientsViewModel.class);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setHasOptionsMenu(true);

        mNavController = NavHostFragment.findNavController(this);

        mMainViewModel.getClients();
        mMainViewModel.clients().observe(getViewLifecycleOwner(),this::consumeClients);

        setupRecyclerView();



        mBinding.addClient.setOnClickListener(view -> {

            mNavController.navigate(ClientsFragmentDirections.actionAddClient());
        });


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
                new MaterialAlertDialogBuilder(getContext())
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
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        mListAdapter = new ClientsListAdapter(this);
        recyclerView.setAdapter(mListAdapter);


    }

    @Override
    public void onClientClick(ModelClients client ,View view) {
        String transitionName =  getString(R.string.client_fragment_transition_name);

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(view,transitionName).build();

        mMainViewModel.setClient(client);
        mNavController.navigate(ClientsFragmentDirections.actionClient(),extras);

    }

    private void consumeClients(ModelClientsResponse response){
        Timber.e("Response: " + response.toString());

        mBinding.progressCircular.setVisibility(View.GONE);
        if(!response.getError()){
            List<ModelClients> clients =response.getData();
            if(clients != null){
                Timber.e("Clients: " + mClients.toString());

                mListAdapter.submitList(clients);
                mListAdapter.notifyDataSetChanged();
                mClients.clear();
                mClients.addAll(clients);
                if(clients.isEmpty()){

                    mBinding.clientsRelative.setVisibility(View.GONE);
                    mBinding.errorLinear.setVisibility(View.VISIBLE);
                }

            }
        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void logout(){

        mLoginViewModel.logout();


        mNavController.navigate(BottomNavGraphDirections.actionLogout());


    }

    private void searchClient(String s) {
        List<ModelClients> data = StreamSupport.stream(mClients)
                .filter(modelClients -> modelClients.getName()!=null)
                .filter(modelClients -> modelClients.getName().toLowerCase().contains(s.toLowerCase())).collect(toList());

        mListAdapter = new ClientsListAdapter(this);
        mListAdapter.submitList(data);
        mListAdapter.notifyDataSetChanged();
        mBinding.clientsRecyclerview.setAdapter(mListAdapter);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





    }


}

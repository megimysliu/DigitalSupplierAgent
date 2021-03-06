package co.almotech.digitalsupplieragent.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.transition.MaterialElevationScale;

import java.util.ArrayList;
import java.util.List;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import co.almotech.digitalsupplieragent.databinding.FragmentAgentBinding;
import co.almotech.digitalsupplieragent.ui.clients.ClientsAdapter;
import co.almotech.digitalsupplieragent.ui.clients.ClientsViewModel;
import dagger.hilt.android.AndroidEntryPoint;

import static android.widget.LinearLayout.VERTICAL;


@AndroidEntryPoint
public class AgentFragment extends Fragment implements ClientsAdapter.ClientClickListener{

    private FragmentAgentBinding mBinding;
    private LoginViewModel mViewModel;
    private NavController navController;
    private ClientsViewModel mClientsViewModel;
    private List<ModelClients> mClientsList = new ArrayList<>();
    private ClientsAdapter mAdapter;

    public AgentFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mClientsViewModel = new ViewModelProvider(requireActivity()).get(ClientsViewModel.class);
        mBinding = FragmentAgentBinding.inflate(inflater,container,false);
       navController = NavHostFragment.findNavController(this);
        mBinding.editProfile.setOnClickListener(v -> navController.navigate(AgentFragmentDirections.actionChangePassword()));
        mClientsViewModel.loadClients();
        mClientsViewModel.getMyClients().observe(getViewLifecycleOwner(),this::consumeClients);
        setupRecyclerView();
        mBinding.setViemodel(mViewModel);
        mViewModel.getUserData();
        return mBinding.getRoot();
    }

    private void setupRecyclerView(){

        mAdapter = new ClientsAdapter(this,mClientsList);
        mBinding.clientsRecyclerview.setAdapter(mAdapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), VERTICAL);
        mBinding.clientsRecyclerview.addItemDecoration(itemDecor);
    }


    @Override
    public void onClientClick(ModelClients client,View view) {
        String transitionName =  getString(R.string.client_fragment_transition_name);

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(view,transitionName).build();
        MaterialElevationScale exitTransition =  new MaterialElevationScale(false);
        exitTransition.setDuration(200);
        MaterialElevationScale reenterTransition = new MaterialElevationScale(true);
        reenterTransition.setDuration(200);

        mClientsViewModel.setClient(client);
        navController.navigate(AgentFragmentDirections.actionClient(),extras);

    }

    private  void consumeClients(ModelClientsResponse response){

        if(!response.getError()){
            List<ModelClients> clients = response.getData();
            mClientsList.clear();
            mClientsList.addAll(clients);
            if(mClientsList.isEmpty()){
                mBinding.errorClientsLinear.setVisibility(View.VISIBLE);
            }else{
                mBinding.errorClientsLinear.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.getViewTreeObserver().addOnPreDrawListener(() -> {
            startPostponedEnterTransition();
            return true;
        });

    }
}

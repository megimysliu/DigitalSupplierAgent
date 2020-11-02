package co.almotech.digitalsupplieragent.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.databinding.FragmentAgentBinding;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AgentFragment extends Fragment{

    private FragmentAgentBinding mBinding;
    LoginViewModel mViewModel;
    private NavController navController;

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
        mBinding = FragmentAgentBinding.inflate(inflater,container,false);
       navController = NavHostFragment.findNavController(this);
        mBinding.editProfile.setOnClickListener(v -> navController.navigate(AgentFragmentDirections.actionAgentFragmentToEditProfileFragment()));

        mBinding.setViemodel(mViewModel);
        mViewModel.getUserData();
        return mBinding.getRoot();
    }


}

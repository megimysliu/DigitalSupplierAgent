package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Objects;
import co.almotech.digitalsupplieragent.BottomNavGraphDirections;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.databinding.FragmentLoginBinding;
import co.almotech.digitalsupplieragent.data.model.ModelUserResponse;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding mBinding;
    private NavController navController;
    LoginViewModel mLoginViewModel;


    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater,container,false);

        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        mBinding.logInBtn.setOnClickListener(v -> {
        mLoginViewModel.login(Objects.requireNonNull(mBinding.email.getText().toString()),
                             Objects.requireNonNull(mBinding.password.getText().toString()));


        });
        mLoginViewModel.loginLiveData.observe(getViewLifecycleOwner(),this::verifyLoginCredentials);

        mBinding.forgotPassword.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment());
        });

        mBinding.signUpTextView.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment());

        });


        return  mBinding.getRoot();
    }


    private void verifyLoginCredentials(ModelUserResponse response){
        if(!response.getError()){
           mLoginViewModel.setToken(response.getToken());
           mLoginViewModel.changeUserData(response.getData());
            NavHostFragment.findNavController(this).navigate(BottomNavGraphDirections.actionHome());
        }else{

            if(response.getMessage().equals("Unauthorized Access")){
                Toast.makeText(getContext(),"Incorrect email/password",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
            }




        }
    }


    }

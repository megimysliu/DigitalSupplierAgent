package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private FragmentLoginBinding mBinding;


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
        mBinding.signUpTextView.setOnClickListener(this);
        return  mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registrationFragment);
    }
}
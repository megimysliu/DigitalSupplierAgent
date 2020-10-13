package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentRegistrationBinding;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private FragmentRegistrationBinding mBinding;


    public RegistrationFragment() {
        // Required empty public constructor
    }



    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRegistrationBinding.inflate(inflater,container,false);
        mBinding.signInTextView.setOnClickListener(this);

       return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_loginFragment);
    }
}
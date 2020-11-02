package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.databinding.FragmentChangePasswordBinding;


public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding mBinding;


    public ChangePasswordFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentChangePasswordBinding.inflate(inflater,container,false);
        View view = mBinding.getRoot();
        return view;
    }
}
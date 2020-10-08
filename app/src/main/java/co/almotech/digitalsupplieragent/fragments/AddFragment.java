package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentAddBinding;


public class AddFragment extends Fragment {
    private FragmentAddBinding mBinding;

    public AddFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddBinding.inflate(inflater,container,false);
        View v= mBinding.getRoot();
        return v;
    }
}

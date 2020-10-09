package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentClientsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientsFragment extends Fragment {
    private FragmentClientsBinding mBinding;

    public ClientsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentClientsBinding.inflate(inflater,container,false);
        View v = mBinding.getRoot();
       return v;
    }
}
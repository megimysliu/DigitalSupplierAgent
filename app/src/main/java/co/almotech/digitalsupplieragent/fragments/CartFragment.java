package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentCartBinding;


public class CartFragment extends Fragment {
    private FragmentCartBinding mBinding;

    public CartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentCartBinding.inflate(inflater,container,false);
        View view = mBinding.getRoot();
        return view;
    }
}

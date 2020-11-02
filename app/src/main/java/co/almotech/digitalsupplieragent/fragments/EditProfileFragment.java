package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentEditProfileBinding;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding mBinding;
    private NavController mNavController;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentEditProfileBinding.inflate(inflater,container,false);
        mNavController = NavHostFragment.findNavController(this);
        mBinding.changePasswordBtn.setOnClickListener( v -> EditProfileFragmentDirections.actionChangePassword());
        return mBinding.getRoot();
    }
}
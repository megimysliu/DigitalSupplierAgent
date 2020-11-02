package co.almotech.digitalsupplieragent.ui.clients;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentAddClientBinding;
import co.almotech.digitalsupplieragent.ui.maps.SharedViewModel;


public class AddClientFragment extends Fragment {

    private FragmentAddClientBinding mBinding;
    NavController mNavController;
    private double lat;
    private double lng;
    private SharedViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddClientBinding.inflate(inflater,container,false);
        mNavController = NavHostFragment.findNavController(this);


        mBinding.locationText.setOnClickListener(v ->{

            mNavController.navigate(AddClientFragmentDirections.actionAddClientFragmentToMapsFragment());
                });

        mBinding.addClient.setOnClickListener(v ->
                System.out.println("Text "+ mBinding.locationText.getText().toString()));

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getLocation().observe(getViewLifecycleOwner(), locationData -> {

            System.out.println("Location:  " + locationData.toString());
            lat = locationData.getLat();
            lng = locationData.getLng();
            mBinding.locationText.setText(locationData.getAddress());


        });

    }


}
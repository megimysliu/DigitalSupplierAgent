package co.almotech.digitalsupplieragent.ui.clients;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.transition.Transition;

import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.regex.Pattern;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.data.model.ModelCreateClientResponse;
import co.almotech.digitalsupplieragent.databinding.FragmentAddClientBinding;
import co.almotech.digitalsupplieragent.ui.maps.SharedViewModel;

import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;


public class AddClientFragment extends Fragment {

    private FragmentAddClientBinding mBinding;
    private NavController mNavController;
    private double lat = 9999;
    private double lng = 9999;
    private SharedViewModel mViewModel;
    private ClientsViewModel mClientsViewModel;
    private static final int ACCOUNT_TYPE = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddClientBinding.inflate(inflater, container, false);
        mNavController = NavHostFragment.findNavController(this);
        mViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        mClientsViewModel = new ViewModelProvider(requireActivity()).get(ClientsViewModel.class);

        mViewModel.getLocation().observe(getViewLifecycleOwner(), locationData -> {

            System.out.println("Location:  " + locationData.toString());
            lat = locationData.getLat();
            lng = locationData.getLng();
            mBinding.locationText.setText(locationData.getAddress());


        });
        mClientsViewModel.getCreateClientRes().observe(getViewLifecycleOwner(), this::consumeResponse);


        mBinding.locationText.setOnClickListener(v -> {


            if (isGooglePlayServicesInstalled()) {
                mNavController.navigate(AddClientFragmentDirections.actionAddClientFragmentToMapsFragment());
            } else {
                Toast.makeText(getContext(), "You need to have Google Play Services installed for the map to work", Toast.LENGTH_SHORT).show();
            }
        });


        mBinding.addClient.setOnClickListener(v -> {

           if(!checkName() | !checkEmail() | !checkPhone() | !checkNuis() | !checkAddress()) {
               Toast.makeText(getContext(),"Please check your inputs",Toast.LENGTH_SHORT).show();
               return;
           }else {

               mClientsViewModel.createClient(mBinding.clientName.getText().toString(), mBinding.clientEmail.getText().toString(),
                       mBinding.phoneNumber.getText().toString(), ACCOUNT_TYPE, mBinding.nuis.getText().toString(), String.valueOf(lat),
                       String.valueOf(lng), mBinding.locationText.getText().toString());
               System.out.println("Text " + mBinding.locationText.getText().toString());
           }

        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialContainerTransform transform = new MaterialContainerTransform();

    }


    private void consumeResponse(ModelCreateClientResponse response) {

        if (!response.getError()) {
            Toast.makeText(getContext(), "Client created", Toast.LENGTH_SHORT).show();
            mNavController.navigate(AddClientFragmentDirections.actionClients());

        }else {
            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isGooglePlayServicesInstalled() {
        GoogleApiAvailability apiAvailability = new GoogleApiAvailability();
        return apiAvailability.isGooglePlayServicesAvailable(getContext()) == ConnectionResult.SUCCESS;

    }


    private boolean checkName() {

        if (mBinding.clientName.getText().toString().equals("")) {
            mBinding.clientTextLayout.setError("Field is required");
            mBinding.clientName.requestFocus();
            return false;
        } else {
            mBinding.clientTextLayout.setError(null);
            return true;
        }
    }

    private boolean checkEmail(){
        if(mBinding.clientEmail.getText().toString().equals("")){
            mBinding.clientEmailLayout.setError("Field is required");
            mBinding.clientEmail.requestFocus();
            return false;
        }else if(!mBinding.clientEmail.getText().toString().contains("@")){
            mBinding.clientEmailLayout.setError("Please enter a valid email");
            mBinding.clientEmail.requestFocus();
            return false;
        }else{
            mBinding.clientEmailLayout.setError(null);
            return true;
        }
    }

    private boolean checkNuis(){

        if (mBinding.nuis.getText().toString().equals("")) {
            mBinding.nuisLayout.setError("Field is required");
            mBinding.nuis.requestFocus();
            return false;
        } else {
            mBinding.nuisLayout.setError(null);
            return true;
        }

    }

    private boolean checkPhone(){

        TextInputEditText phoneNo = mBinding.phoneNumber;
        String regex = "\\d+";

        if (phoneNo.getText().toString().equals("")) {
            mBinding.phoneNumberLayout.setError("Field is required");
            phoneNo.requestFocus();
            return false;
        }else if(phoneNo.getText().toString().length()!=10){
            mBinding.phoneNumberLayout.setError("Invalid phone number");
            phoneNo.requestFocus();
            return false;

        } else  if(!Pattern.matches(regex,phoneNo.getText().toString())){
            mBinding.phoneNumberLayout.setError("Invalid phone number");
            phoneNo.requestFocus();
            return false;
        }else{
            mBinding.phoneNumberLayout.setError(null);
            return true;
        }

    }

    private boolean checkAddress(){
        if(lat == 9999 && lng == 9999){
            mBinding.addressLayout.setError("Please select a location on the map");
            mBinding.locationText.requestFocus();
            return false;
        }else if(mBinding.locationText.toString().equals("")){
            mBinding.addressLayout.setError("Field is required");
            mBinding.locationText.requestFocus();
            return false;
        }else{
            mBinding.addressLayout.setError(null);
            return true;
        }
    }



}

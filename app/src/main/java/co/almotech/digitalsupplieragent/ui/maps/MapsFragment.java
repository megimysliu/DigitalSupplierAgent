package co.almotech.digitalsupplieragent.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentMapsBinding;
import co.almotech.digitalsupplieragent.utils.LocationData;

public class MapsFragment extends Fragment {

    private FragmentMapsBinding mBinding;
    private double lat;
    private double lng;
    NavController mNavController;
    private static final String ADDRESS = "Address";
    private static final String LAT = "Lat";
    private static final String LNG = "Lng";
    private SharedViewModel mViewModel;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location currentLocation;
    private static final int REQUEST_CODE = 178;
    private boolean permissionGranted = false;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng tirana = new LatLng(41.328422, 19.818684);
            LatLng currentPos;
            if(permissionGranted){
                currentPos = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(currentPos));
            }else{
                googleMap.addMarker(new MarkerOptions().position(tirana));
            }


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( tirana,15));

            final List<Address>[] addresses = new List[]{new ArrayList<>()};

            googleMap.setOnMapClickListener(latLng-> {

                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng));
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                try{
                    addresses[0] =  geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);

                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    mBinding.location.setText(addresses[0].get(0).getAddressLine(0));

                }catch (IOException exception){
                    exception.printStackTrace();
                }

            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        fetchLastlocation();


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapsBinding.inflate(inflater,container,false);
        mNavController = NavHostFragment.findNavController(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fetchLastlocation();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        return mBinding.getRoot();
    }

    private void fetchLastlocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (currentLocation != null) {
                    currentLocation = location;
                    Toast.makeText(getContext(), "Current location " + currentLocation.getLatitude() + "," +
                            currentLocation.getLongitude(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.addLocationBtn.setOnClickListener(v ->{

            LocationData locationData = new LocationData(lat,lng,mBinding.location.getText().toString());
            mViewModel.setLocation(locationData);
            mViewModel.setAddress(mBinding.location.getText().toString());
            requireActivity().onBackPressed();

        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    fetchLastlocation();
                }
                break;
        }
    }
}
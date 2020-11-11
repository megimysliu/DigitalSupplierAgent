package co.almotech.digitalsupplieragent.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentMapsBinding;
import co.almotech.digitalsupplieragent.utils.LocationData;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    private FragmentMapsBinding mBinding;
    private double lat;
    private double lng;
    NavController mNavController;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private SharedViewModel mViewModel;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleMap mGoogleMap;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                Log.d("MapsFragment", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }


                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                final List<Address>[] addresses = new List[]{new ArrayList<>()};
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                 mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                try {
                    addresses[0] = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    mBinding.location.setText(addresses[0].get(0).getAddressLine(0));

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    };

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            final List<Address>[] addresses = new List[]{new ArrayList<>()};
            getLocation();

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(60000); // two minute interval
            mLocationRequest.setFastestInterval(60000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



            mGoogleMap.setOnMapClickListener(latLng -> {

                mGoogleMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


                try {
                    addresses[0] = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    mBinding.location.setText(addresses[0].get(0).getAddressLine(0));

                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapsBinding.inflate(inflater, container, false);
        mNavController = NavHostFragment.findNavController(this);


        return mBinding.getRoot();
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.addLocationBtn.setOnClickListener(v -> {

            LocationData locationData = new LocationData(lat, lng, mBinding.location.getText().toString());
            mViewModel.setLocation(locationData);

            requireActivity().onBackPressed();

        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }




    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_LOCATION)
    private void getLocation(){


        if(EasyPermissions.hasPermissions(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)){
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {

                if (location != null) {

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);

                }
            });


                } else {

            EasyPermissions.requestPermissions(
                    this, getString(R.string.location_rationale),
                    MY_PERMISSIONS_REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }


            }









    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//
//        if(EasyPermissions.hasPermissions(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)){
//
//        }



    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if(EasyPermissions.somePermissionDenied(this,Manifest.permission.ACCESS_FINE_LOCATION)){

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            final List<Address>[] addresses = new List[]{new ArrayList<>()};
            LatLng latLng = new LatLng(41.328422, 19.818684);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            try {
                addresses[0] = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                lat = latLng.latitude;
                lng = latLng.longitude;
                mBinding.location.setText(addresses[0].get(0).getAddressLine(0));

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            final List<Address>[] addresses = new List[]{new ArrayList<>()};
            LatLng latLng = new LatLng(41.328422, 19.818684);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            try {
                addresses[0] = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                lat = latLng.latitude;
                lng = latLng.longitude;
                mBinding.location.setText(addresses[0].get(0).getAddressLine(0));

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }


    }
}
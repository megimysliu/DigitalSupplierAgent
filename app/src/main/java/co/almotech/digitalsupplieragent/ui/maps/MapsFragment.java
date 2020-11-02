package co.almotech.digitalsupplieragent.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng tirana = new LatLng(41.328422, 19.818684);
            googleMap.addMarker(new MarkerOptions().position(tirana));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( tirana,12));

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


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapsBinding.inflate(inflater,container,false);
        mNavController = NavHostFragment.findNavController(this);



        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.addLocationBtn.setOnClickListener(v ->{
//            Bundle bundle = new Bundle();
//            bundle.putString(ADDRESS, mBinding.location.getText().toString());
//            bundle.putDouble(LAT, lat);
//            bundle.putDouble(LNG, lng);
            //mNavController.navigate(R.id.actionSendLocation,bundle);
            LocationData locationData = new LocationData(lat,lng,mBinding.location.getText().toString());
            mViewModel.setLocation(locationData);
            mViewModel.setAddress(mBinding.location.getText().toString());
            requireActivity().onBackPressed();

        });
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
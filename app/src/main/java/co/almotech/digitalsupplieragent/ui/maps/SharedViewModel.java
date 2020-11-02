package co.almotech.digitalsupplieragent.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import co.almotech.digitalsupplieragent.utils.LocationData;

public class SharedViewModel  extends ViewModel {

    private MutableLiveData<LocationData> location = new MutableLiveData<>();

    private MutableLiveData<Double> lat = new MutableLiveData<>();
    private MutableLiveData<Double> lng = new MutableLiveData<>();
    private MutableLiveData<String> address = new MutableLiveData<>();

    public MutableLiveData<String> getAddress() {
        return address;
    }

    public void setAddress(String address) {

        this.address.setValue(address);
    }

    public void setLocation(LocationData location){

        this.location.setValue(location);
    }

    public LiveData<LocationData> getLocation(){
        return location;
    }
}

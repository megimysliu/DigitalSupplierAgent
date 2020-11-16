package co.almotech.digitalsupplieragent.ui.clients;

import androidx.databinding.ObservableInt;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import co.almotech.digitalsupplieragent.data.model.ModelCreateClientResponse;
import co.almotech.digitalsupplieragent.repo.MainRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ClientsViewModel extends ViewModel {

    private MainRepository mRepository;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private final MutableLiveData<ModelClientsResponse> clients = new MutableLiveData<>();
    private final MutableLiveData<ModelClientsResponse> myClients = new MutableLiveData<>();
    private final MutableLiveData<ModelCreateClientResponse> createClient = new MutableLiveData<>();

    private final MutableLiveData<ModelClients> client = new MutableLiveData<>();
    public final ObservableInt selectedClient = new ObservableInt(-1);
    private final SavedStateHandle savedStateHandle;

    @ViewModelInject
    public ClientsViewModel(MainRepository mainRepository,@Assisted SavedStateHandle savedStateHandle){
        mRepository = mainRepository;
        this.savedStateHandle = savedStateHandle;
    }

    public void getClients(){

        mDisposable.add(mRepository.getAllClients()
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(clients::setValue,
                        throwable ->
                        clients.setValue(ModelClientsResponse.modelError(throwable))
                ));
    }

    public void setSelectedClient(int id){
        selectedClient.set(id);
    }

    public void createClient(String name,String email, String phoneNumber, int accountType,
                             String nuis, String lat,String lng, String address){

        mDisposable.add(mRepository.createClient(name, email, phoneNumber, accountType, nuis, lat, lng, address)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(createClient::setValue, throwable -> createClient.setValue( new ModelCreateClientResponse(true,throwable.toString())) ));

    }

    public void loadClients(){
        mDisposable.add(mRepository.getMyClients()
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(response -> myClients.setValue(response), throwable -> myClients.setValue(ModelClientsResponse.modelError(throwable)))
        );
    }

    public void setClient(ModelClients client){
        this.client.setValue(client);
    }

    public LiveData<ModelClients> getClient(){
        return client;
    }

    @Override
    protected void onCleared() {

        mDisposable.clear();
    }

    public LiveData<ModelClientsResponse> clients(){
        return clients;
    }
    public LiveData<ModelClientsResponse> getMyClients(){
        return myClients;
    }
    public LiveData<ModelCreateClientResponse> getCreateClientRes(){
        return createClient;
    }

}

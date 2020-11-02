package co.almotech.digitalsupplieragent.ui.clients;

import androidx.databinding.ObservableInt;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import co.almotech.digitalsupplieragent.data.model.ModelClients;
import co.almotech.digitalsupplieragent.data.model.ModelClientsResponse;
import co.almotech.digitalsupplieragent.repo.MainRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ClientsViewModel extends ViewModel {

    private MainRepository mRepository;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private final MutableLiveData<ModelClientsResponse> clients = new MutableLiveData<>();

    private final MutableLiveData<ModelClients> client = new MutableLiveData<>();
    public final ObservableInt selectedClient = new ObservableInt(-1);

    @ViewModelInject
    public ClientsViewModel(MainRepository mainRepository){
        mRepository = mainRepository;
    }

    public void getClients(){

        mDisposable.add(mRepository.getAllClients()
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(clients::setValue,
                        throwable ->
                        clients.setValue(ModelClientsResponse.Companion.modelError(throwable))
                ));
    }

    public void setSelectedClient(int id){
        selectedClient.set(id);
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
}

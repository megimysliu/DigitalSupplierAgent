package co.almotech.digitalsupplieragent.ui.profile;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import co.almotech.digitalsupplieragent.data.model.ModelChangePasswordResponse;
import co.almotech.digitalsupplieragent.repo.RepositoryLogin;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class AgentViewModel extends ViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private RepositoryLogin mRepository;
    private final MutableLiveData<ModelChangePasswordResponse> mPassword = new MutableLiveData<>();

    @ViewModelInject
    public AgentViewModel(RepositoryLogin repository){
        mRepository = repository;

    }


    public void changePassword(String oldPassword, String password, String newPassword){

        mDisposable.add(mRepository.changePassword(oldPassword,password,newPassword)
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(mPassword::setValue,
                ModelChangePasswordResponse::modelError));
    }

    public LiveData<ModelChangePasswordResponse> getPassword(){
        return mPassword;
    }

    @Override
    protected void onCleared() {
        mDisposable.clear();
    }
}

package co.almotech.digitalsupplieragent.auth;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import co.almotech.digitalsupplieragent.data.model.ModelUser;
import co.almotech.digitalsupplieragent.data.model.ModelUserResponse;
import co.almotech.digitalsupplieragent.repo.RepositoryLogin;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class LoginViewModel extends ViewModel {

    private final CompositeDisposable mDisposables = new CompositeDisposable();
    public final MutableLiveData<ModelUserResponse> loginLiveData = new MutableLiveData<>();
    public final MutableLiveData<ModelUser> user = new MutableLiveData<>();

    private RepositoryLogin mRepositoryLogin;
    private SharedPreferences mPreferences;

    public static final String PREFERENCE_TOKEN = "Token";

    @ViewModelInject
    public LoginViewModel(RepositoryLogin repositoryLogin, SharedPreferences preferences){
        this.mRepositoryLogin = repositoryLogin;
        this.mPreferences = preferences;

        getUserData();

    }

    public void login(String email,String password){

        mDisposables.add(mRepositoryLogin.userLogin(email, password)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loginLiveData::setValue,
                throwable -> loginLiveData.setValue(ModelUserResponse.modelError(throwable))
        ));


    }

    public void getUserData(){

        this.user.setValue(new ModelUser(mPreferences.getInt("user_id",-1),
                mPreferences.getString("name",""),
                mPreferences.getString("email","")));
    }

    public void changeUserData(ModelUser user){
         mPreferences.edit()
                 .putInt("user_id", user.getId())
                 .putString("name",user.getName())
                 .putString("email", user.getEmail())
                 .apply();
         getUserData();

    }


    @SuppressLint("ApplySharedPref")
    public void setToken(String token){
        mPreferences.edit().putString(PREFERENCE_TOKEN,token)
                .commit();

    }



    public String getToken(){

        return mPreferences.getString(PREFERENCE_TOKEN,null);
    }



    public void setId(int id){
        mPreferences.edit().putInt("id",id).apply();
    }

    public int getId(){

        return mPreferences.getInt("id",-1);
    }

    @SuppressLint("ApplySharedPref")
    public void logout(){
        mPreferences.edit().clear().commit();

    }

    @Override
    protected void onCleared() {
        mDisposables.clear();
    }
}

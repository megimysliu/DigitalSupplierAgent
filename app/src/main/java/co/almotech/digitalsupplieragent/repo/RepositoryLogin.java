package co.almotech.digitalsupplieragent.repo;

import javax.inject.Inject;

import co.almotech.digitalsupplieragent.data.model.ModelChangePasswordResponse;
import co.almotech.digitalsupplieragent.data.model.ModelUserResponse;
import co.almotech.digitalsupplieragent.network.API;
import io.reactivex.Single;

public class RepositoryLogin {

    private API mAPI;

    @Inject
    public RepositoryLogin(API api){
        this.mAPI = api;
    }

    public Single<ModelUserResponse> userLogin(String email, String password){

        return  mAPI.userLogin(email,password);
    }

    public Single<ModelChangePasswordResponse> changePassword(String oldPassword, String password, String passwordConfirmation){

        return mAPI.changePassword(oldPassword,password,passwordConfirmation);
    }
}

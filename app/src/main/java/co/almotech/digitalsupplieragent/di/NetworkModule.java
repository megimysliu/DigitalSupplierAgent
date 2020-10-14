package co.almotech.digitalsupplieragent.di;

import com.google.gson.Gson;

import co.almotech.digitalsupplieragent.network.API;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {

    private static final String BASE_URL = "baseurl";

    @Provides
    public Retrofit provideRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        return retrofit;

    }
    @Provides
    public API provideApi(Retrofit retrofit){

        return retrofit.create(API.class);


    }
}

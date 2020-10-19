package co.almotech.digitalsupplieragent.di;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import co.almotech.digitalsupplieragent.network.API;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {

    private static final String BASE_URL = "baseurl";



    @Singleton
    @Provides
    public OkHttpClient provideHttpClient(){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient =   new OkHttpClient().newBuilder();
        okHttpClient.callTimeout(40, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS);
        okHttpClient.readTimeout(40, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(loggingInterceptor);
        return okHttpClient.build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        return retrofit;

    }
    @Singleton
    @Provides
    public API provideApi(Retrofit retrofit){

        return retrofit.create(API.class);


    }
}

package co.almotech.digitalsupplieragent.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;


import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import co.almotech.digitalsupplieragent.network.API;
import co.almotech.digitalsupplieragent.utils.TokenInterceptor;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.reactivex.schedulers.Schedulers.io;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {

    private static final String BASE_URL = "https://develop.almotech.co/digital_supplier/public/api/agent/";




    @Singleton
    @Provides
    public TokenInterceptor provideTokenInterceptor(SharedPreferences preferences, @ApplicationContext Context context){
        return  new TokenInterceptor(preferences,context);

    }

    @Singleton
    @Provides
    public OkHttpClient provideHttpClient(TokenInterceptor tokenInterceptor){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient =   new OkHttpClient().newBuilder();
        okHttpClient.callTimeout(40, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS);
        okHttpClient.readTimeout(40, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(loggingInterceptor);
        okHttpClient.addInterceptor(tokenInterceptor);
        return okHttpClient.build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(io()))
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

package co.almotech.digitalsupplieragent.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jakewharton.processphoenix.ProcessPhoenix;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static co.almotech.digitalsupplieragent.auth.LoginViewModel.PREFERENCE_TOKEN;

@Singleton
public class TokenInterceptor implements Interceptor {
    private SharedPreferences mPreferences;
    private Context mContext;

    @Inject
    public TokenInterceptor(SharedPreferences preferences, Context context){
        this.mPreferences = preferences;
        this.mContext = context;
    }
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        if(request.header("No-Authentication") == null){

            String token =  mPreferences.getString(PREFERENCE_TOKEN,null);
            if(token == null){
                Timber.e("Authentication token must be defined!");
                ProcessPhoenix.triggerRebirth(mContext);
            }
            builder.addHeader("Authorization",String.format("Bearer %s", token));
        }else{

            builder.removeHeader("No-Authentication");
        }

        return  chain.proceed(builder.build());
    }
}

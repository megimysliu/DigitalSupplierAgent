package co.almotech.digitalsupplieragent.ui.splash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;

import co.almotech.digitalsupplieragent.BottomNavGraphDirections;
import co.almotech.digitalsupplieragent.auth.LoginViewModel;
import co.almotech.digitalsupplieragent.databinding.FragmentSplashBinding;
import co.almotech.digitalsupplieragent.ui.splash.SplashFragmentDirections;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class SplashFragment extends Fragment {


     LoginViewModel mLoginViewModel;
     private static final long SPLASH_TIMER = 2000;

    private FragmentSplashBinding mBinding;


    public SplashFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mBinding = FragmentSplashBinding.inflate(inflater,container,false);
        View v = mBinding.getRoot();
       mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        Timber.e("Splash token: " + mLoginViewModel.getToken());
        new Handler().postDelayed(() -> {

            NavDirections directions;
            if(mLoginViewModel.getToken()== null){


                directions = SplashFragmentDirections.actionLogin();



            }
            else{
                directions = BottomNavGraphDirections.actionHome();
            }
            NavHostFragment.findNavController(this).navigate(directions);

        },SPLASH_TIMER);
        return v;
    }
}
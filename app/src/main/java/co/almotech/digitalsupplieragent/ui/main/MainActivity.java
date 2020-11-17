package co.almotech.digitalsupplieragent.ui.main;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.ActivityMainBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private NavController mNavController;
    private MaterialToolbar mToolbar;
    private AppBarConfiguration mAppBarConfiguration;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        setViews();


    }

    private void setViews(){

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragNavHost);
        mNavController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(mBinding.bottomNavView, mNavController);

        mToolbar = mBinding.topAppBar;
        setSupportActionBar(mToolbar);
         mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.clientsFragment,R.id.meetingsFragment,
                R.id.addFragment,R.id.productsFragment,R.id.ordersFragment).build();
        NavigationUI.setupActionBarWithNavController(this,mNavController,mAppBarConfiguration);
        List<Integer> noActionBarDestinations = Arrays.asList(R.id.splashFragment,
                R.id.loginFragment, R.id.registrationFragment,R.id.addMeetingFragment,R.id.forgotPasswordFragment);
        List<Integer> noBottomNavBarDestinations = Arrays.asList(R.id.splashFragment,
                R.id.loginFragment,R.id.registrationFragment,R.id.addMeetingFragment,R.id.forgotPasswordFragment);

        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
            if(noActionBarDestinations.contains(destination.getId())){
                actionBar.hide();
            }else{
                actionBar.show();
            }

            mBinding.bottomNavView.setVisibility(noBottomNavBarDestinations.contains(destination.getId()) ? View.GONE : View.VISIBLE);
            mBinding.bottomNavView.setOnNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                NavOptions navOptions;
                if(id == R.id.clientFragment){
                     navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.splashFragment, false)
                            .build();
                }else{
                 navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.clientsFragment, false)
                        .build();

                }



                mNavController.navigate(id,null, navOptions);
                return true;
            });




        });



    }



    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController,mAppBarConfiguration);
   }



//
//    @Override
//    public void onBackPressed() {
//        NavDestination navDestination = mNavController.getCurrentDestination();
//        if (navDestination != null
//                && navDestination.getId() == R.id.clientsFragment) {
//            finish();
//            return;
//        }
//        super.onBackPressed();
//    }
}

package co.almotech.digitalsupplieragent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
       // getSupportActionBar().setTitle(getString(R.string.digital_supplier));
        setViews();


    }

    private void setViews(){
         mNavController = Navigation.findNavController(this,R.id.fragNavHost);
        NavigationUI.setupWithNavController(mBinding.bottomNavView, mNavController);
        mToolbar = mBinding.topAppBar;
        setSupportActionBar(mToolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.clientsFragment,R.id.meetingsFragment,
                R.id.addFragment,R.id.productsFragment,R.id.ordersFragment).build();
        NavigationUI.setupWithNavController(mToolbar,mNavController,mAppBarConfiguration);
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




        });



    }



    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp() || super.onSupportNavigateUp();
    }
}

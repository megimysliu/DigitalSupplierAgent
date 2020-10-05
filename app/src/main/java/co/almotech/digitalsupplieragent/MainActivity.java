package co.almotech.digitalsupplieragent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.almotech.digitalsupplieragent.activities.LoginActivity;
import co.almotech.digitalsupplieragent.activities.RegistrationActivity;
import co.almotech.digitalsupplieragent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);


    }
}

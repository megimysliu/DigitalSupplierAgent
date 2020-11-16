package co.almotech.digitalsupplieragent.ui.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.almotech.digitalsupplieragent.databinding.FragmentForgotPasswordBinding;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding mBinding;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentForgotPasswordBinding.inflate(inflater,container,false);
        View view = mBinding.getRoot();
        NavController mNavController = NavHostFragment.findNavController(this);
        mBinding.arrow.setOnClickListener( v ->
                mNavController.navigate(ForgotPasswordFragmentDirections.actionLogin()));
        return view;
    }
}
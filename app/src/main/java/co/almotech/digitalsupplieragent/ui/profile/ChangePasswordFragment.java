package co.almotech.digitalsupplieragent.ui.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Objects;
import co.almotech.digitalsupplieragent.data.model.ModelChangePasswordResponse;
import co.almotech.digitalsupplieragent.databinding.FragmentChangePasswordBinding;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding mBinding;
    private NavController mNavController;
    private AgentViewModel mModel;


    public ChangePasswordFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentChangePasswordBinding.inflate(inflater,container,false);
        mNavController = NavHostFragment.findNavController(this);
        mModel = new ViewModelProvider(requireActivity()).get(AgentViewModel.class);

        mBinding.sendPasswordsBtn.setOnClickListener( v-> {


            mModel.changePassword(Objects.requireNonNull(mBinding.oldPassword.getText().toString()),
                    Objects.requireNonNull(mBinding.newPassword.getText().toString()),
                    Objects.requireNonNull(mBinding.confirmPassword.getText().toString()));
            mModel.getPassword().observe(getViewLifecycleOwner(), this::consumeResponse);

        });
            return mBinding.getRoot();
        }


    private boolean checkOldPassword() {

        if (mBinding.oldPassword.getText().toString().isEmpty()) {
            mBinding.oldPassword.setError("Field can't be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkNewPassword(){

        if(mBinding.newPassword.getText().toString().isEmpty()){
            mBinding.newPassword.setError("Field can't be empty");
            return false;
        }else{
            return true;
        }
    }


    private boolean checkPasswordConfirmation(){

        if(mBinding.confirmPassword.getText().toString().isEmpty()){
            mBinding.confirmPassword.setError("Field can't be empty");
            return false;
        }else{
            return true;
        }

    }



    private void consumeResponse(ModelChangePasswordResponse response){

        if(!response.getError()){

            Toast.makeText(getContext(),"Password changed", Toast.LENGTH_SHORT).show();
            mNavController.navigate(ChangePasswordFragmentDirections.actionProfile());

        }else{
            Toast.makeText(getContext(),response.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
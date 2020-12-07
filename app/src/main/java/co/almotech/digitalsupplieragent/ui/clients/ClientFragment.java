package co.almotech.digitalsupplieragent.ui.clients;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.transition.MaterialContainerTransform;
import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentClientBinding;




public class ClientFragment extends Fragment {

    private FragmentClientBinding mBinding;
    private ClientsViewModel mModel;
    private String lat;
    private String lng;

    public ClientFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaterialContainerTransform transition = new MaterialContainerTransform();
        transition.setDrawingViewId(R.id.fragNavHost);
        transition.setDuration(300);
        transition.setScrimColor(Color.TRANSPARENT);
        transition.setAllContainerColors(requireContext().getColor(R.color.colorLightGray));
        setSharedElementEnterTransition(transition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mModel = new ViewModelProvider(requireActivity()).get(ClientsViewModel.class);
        mBinding = FragmentClientBinding.inflate(inflater,container,false);
        mBinding.setClient(mModel.getClient().getValue());
        mModel.getClient().observe(getViewLifecycleOwner(), modelClients ->{

            mBinding.setClient(modelClients);
        lat = modelClients.getLat();
        lng = modelClients.getLng();


        }) ;
        mBinding.phoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mBinding.phoneNumber.getText().toString()));
            getContext().startActivity(intent);
        });

        mBinding.email.setOnClickListener(v -> {

            try{
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + mBinding.email.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                getContext().startActivity(intent);
            }catch(ActivityNotFoundException e){
                e.printStackTrace();
            }
        });

        mBinding.location.setOnClickListener(v ->{
            Uri gmmIntentUri = Uri.parse("geo:<" + lat +">,<" + lng + ">?q=" + mBinding.location.getText().toString().trim());
            System.out.println("Address txt" + mBinding.location.getText().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            }

        });
        return mBinding.getRoot();

    }
}

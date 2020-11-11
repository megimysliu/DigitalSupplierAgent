package co.almotech.digitalsupplieragent.ui.clients;

import android.graphics.Color;
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
        mModel.getClient().observe(getViewLifecycleOwner(), modelClients -> mBinding.setClient(modelClients));

        return mBinding.getRoot();

    }
}

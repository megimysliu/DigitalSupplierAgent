package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.almotech.digitalsupplieragent.databinding.FragmentInvoiceBinding;


public class InvoiceFragment extends Fragment {
    FragmentInvoiceBinding mBinding;

    public InvoiceFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentInvoiceBinding.inflate(inflater,container,false);
        View v = mBinding.getRoot();
        return v;
    }
}

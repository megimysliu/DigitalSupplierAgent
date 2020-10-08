package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentMeetingsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingsFragment extends Fragment {
    private FragmentMeetingsBinding mBinding;

    public MeetingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMeetingsBinding.inflate(inflater,container,false);
        View v = mBinding.getRoot();
        return v;
    }
}

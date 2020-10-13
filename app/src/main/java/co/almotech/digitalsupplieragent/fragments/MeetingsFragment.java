package co.almotech.digitalsupplieragent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.almotech.digitalsupplieragent.R;
import co.almotech.digitalsupplieragent.databinding.FragmentMeetingsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingsFragment extends Fragment implements View.OnClickListener {
    private FragmentMeetingsBinding mBinding;

    public MeetingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMeetingsBinding.inflate(inflater,container,false);
         mBinding.addMeeting.setOnClickListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        Navigation.findNavController(v).navigate(R.id.action_meetingsFragment_to_addMeetingFragment);
    }
}

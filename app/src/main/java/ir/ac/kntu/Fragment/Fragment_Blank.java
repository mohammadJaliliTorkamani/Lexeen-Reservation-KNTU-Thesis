package ir.ac.kntu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ir.ac.kntu.R;

/**
 * blank fragment to show as default
 */
public class Fragment_Blank extends Fragment {

    /**
     * inflates layout
     *
     * @param inflater           inflater object
     * @param container          container view
     * @param savedInstanceState savedinstance
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}

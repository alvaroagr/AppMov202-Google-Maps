package com.example.holetrackingapp.activity;

import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.holetrackingapp.R;
import com.example.holetrackingapp.model.Position;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHoleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHoleFragment extends DialogFragment {

    private static final String ARG_LATITUDE = "lat";
    private static final String ARG_LONGITUDE = "lng";
    private static final String ARG_ADDRESS = "address";

    private double lat;
    private double lng;
    private String address;

    private TextView latLngTV, addressTV;
    private Button addHoleBtn;
    private OnAddHoleListener listener;

    public AddHoleFragment() {
        // Required empty public constructor
    }

    public static AddHoleFragment newInstance(double lat, double lng, String address) {
        AddHoleFragment fragment = new AddHoleFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, lat);
        args.putDouble(ARG_LONGITUDE, lng);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            lat = getArguments().getDouble(ARG_LATITUDE);
            lng = getArguments().getDouble(ARG_LONGITUDE);
            address = getArguments().getString(ARG_ADDRESS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_hole, container, false);
        latLngTV = root.findViewById(R.id.latLngTV);
        addressTV = root.findViewById(R.id.addressTV);
        addHoleBtn = root.findViewById(R.id.addHoleBtn);

        latLngTV.setText(lat + ", " + lng);
        addressTV.setText(address);
        addHoleBtn.setOnClickListener(
                v -> {
                    if(listener==null){
                        Log.e(">>>ERROR", "There's no observer");
                    } else {
                        listener.onAddHole(lat, lng);
                    }
                }
        );

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setListener(OnAddHoleListener listener){
        this.listener = listener;
    }

    public interface OnAddHoleListener{
        void onAddHole(double lat, double lng);
    }
}
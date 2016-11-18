package com.example.gestalt.insulinpumpulator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodPumpFragment extends Fragment {
    private LinearLayout mLayout;
    private ArrayList<String> subMenuNames;
    private OmniPodPump pump;
    private TextView subMenu1;
    private TextView subMenu2;
    private TextView subMenu3;
    private TextView menuName;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pump_test_omnipod, container, false);

        pump = (OmniPodPump) ScenarioPlaythrough.mPump;

        subMenuNames = pump.getSubMenuNames();


        return view;
    }
}

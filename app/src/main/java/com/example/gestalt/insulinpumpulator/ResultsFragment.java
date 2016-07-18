package com.example.gestalt.insulinpumpulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Alex on 7/10/2016.
 */
public class ResultsFragment extends Fragment{

    private Button replayButton;
    private Button responseButton;
    private Button mainmenuButton;

    private TextView resultsText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        resultsText = (TextView) view.findViewById(R.id.results_text);
        resultsText.setText("Score: " + ScenarioPlaythrough._playerScore);

        responseButton = (Button) view.findViewById(R.id.results_to_response);
        responseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseFragment newResponse = new ResponseFragment();

                ((MainPageActivity) getActivity()).swapFragment(newResponse);
                System.out.println("Response Selected");
            }
        });

        mainmenuButton = (Button) view.findViewById(R.id.results_to_main);
        mainmenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageNavButtons fragment = new MainPageNavButtons();
                ((MainPageActivity) getActivity()).swapFragment(fragment);
            }
        });

        return view;
    }
}

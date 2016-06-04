package com.example.gestalt.insulinpumpulator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_section) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            AvatarSelectionFragment firstFragment = new AvatarSelectionFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_section, firstFragment).commit();
        }
        Button scenarioSelect = (Button) findViewById(R.id.scenario_select);
        scenarioSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch Fragment out
                System.out.println("Scenario select pressed");
            }
        });
        Button connections = (Button) findViewById(R.id.bConnections);
        connections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch fragment out
                System.out.println("Connections pressed");
            }
        });
        Button customize = (Button) findViewById(R.id.bCustomize);
        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch fragment out
                System.out.println("Customize pressed");
            }
        });
        Button accountInfo = (Button) findViewById(R.id.bAccountInfo);
        accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch fragment out
                System.out.println("Account info pressed");
            }
        });
    }
}

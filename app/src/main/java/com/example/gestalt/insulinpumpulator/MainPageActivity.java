package com.example.gestalt.insulinpumpulator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends FragmentActivity {

    private String msg = "Android : ";
    private int[] _buttonIDs = {R.id.bAccountInfo, R.id.bConnections, R.id.bCustomize, R.id.scenario_select};

    private void selectView(int viewID) {
        for (int i : _buttonIDs) {
            Button b = (Button) findViewById(i);
            if (i == viewID) {
                b.setEnabled(false);
            } else if (!b.isEnabled()) {
                b.setEnabled(true);
            }
        }
    }

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
                selectView(R.id.scenario_select);

                //Switch Fragment out
                // Create new fragment and give it an argument specifying the article it should show
                ScenarioSelectionFragment newFragment = new ScenarioSelectionFragment();

                //No arguments to set as of yet

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_section, newFragment);
//                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                System.out.println("Scenario select pressed");
            }
        });
        Button connections = (Button) findViewById(R.id.bConnections);
        connections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectView(R.id.bConnections);
                //Switch fragment out
                System.out.println("Connections pressed");
            }
        });
        Button customize = (Button) findViewById(R.id.bCustomize);
        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectView(R.id.bCustomize);
                //Switch Fragment out
                // Create new fragment and give it an argument specifying the article it should show
                AvatarSelectionFragment newFragment = new AvatarSelectionFragment();

                //No arguments to set as of yet

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_section, newFragment);
//                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                System.out.println("Customize pressed");
            }
        });
        Button accountInfo = (Button) findViewById(R.id.bAccountInfo);
        accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectView(R.id.bAccountInfo);
                //Switch fragment out
                System.out.println("Account info pressed");
            }
        });
        selectView(R.id.bAccountInfo);
    }

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }
}

package com.example.gestalt.insulinpumpulator;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainPageActivity extends FragmentActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
                // Create new fragment and give it an argument specifying the article it should show
                ScenarioSelectionFragment newFragment = new ScenarioSelectionFragment();

                //No arguments to set as of yet

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_section, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainPage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.gestalt.insulinpumpulator/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainPage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.gestalt.insulinpumpulator/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

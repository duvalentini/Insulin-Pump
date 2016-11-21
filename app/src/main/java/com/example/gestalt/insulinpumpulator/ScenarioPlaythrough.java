package com.example.gestalt.insulinpumpulator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
//AppCompatActivity
public class ScenarioPlaythrough extends FragmentActivity {

    public static int _playerScore;
    public static AInsulinPump mPump;
    public static boolean start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _playerScore = 500;
        int selectedPump = getIntent().getExtras().getInt("pump");
        if(selectedPump == R.string.medtronic_pump_name) {
            mPump = new MedtronicPump(this);
        } else if(selectedPump == R.string.omnipod_name) {
            mPump = new OmniPodPump(this);
        }
        start = true;

        setContentView(R.layout.activity_scenario_playthrough);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        if (findViewById(R.id.fragment_section) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ScenarioPlaythroughFragment firstFragment = new ScenarioPlaythroughFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_section, firstFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(false); // disable the button
            getActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
            getActionBar().setDisplayShowHomeEnabled(false); // remove the icon
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
//            NavUtils.navigateUpFromSameTask(this);
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void swapFragment(Fragment fragment) { //add int containerID to specify which fragment container
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back


        transaction.replace(R.id.fragment_section, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}

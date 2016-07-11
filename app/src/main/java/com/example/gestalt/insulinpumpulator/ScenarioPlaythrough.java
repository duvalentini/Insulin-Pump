package com.example.gestalt.insulinpumpulator;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
public class ScenarioPlaythrough extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int _currentSceneIndex = 0;
    private JSONArray _sceneOptions;
    public static int _playerScore;
    private String _fileName;

    private class OptionListEntry {
        private String _str;
        private int _val;

        private OptionListEntry(JSONObject keyValPair) {
            _str = keyValPair.keys().next(); //guaranteed to work if required to have a single entry
            _val = keyValPair.optInt(_str);
        }

        public int getVal() {
            return _val;
        }

        @Override
        public String toString() {
            return _str;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _playerScore = 0;

        setContentView(R.layout.activity_scenario_playthrough);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        JSONObject configJson = null;
        try {
            configJson = new JSONObject(getIntent().getStringExtra("config"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        _sceneOptions = configJson.optJSONArray("sceneOptions");
        _fileName = configJson.optString("fileName");

        renderScene();
    }

    private void renderScene() {
        if (_currentSceneIndex < _sceneOptions.length()) {
            ImageView sceneImage = (ImageView) findViewById(R.id.scene_image);
            Resources resources = getBaseContext().getResources();
            String sceneImageName = _fileName + _currentSceneIndex;
            final int resourceId = resources.getIdentifier(sceneImageName, "drawable",
                    getBaseContext().getPackageName());
            sceneImage.setImageResource(resourceId);

            JSONArray options = _sceneOptions.optJSONArray(_currentSceneIndex);
            OptionListEntry[] optionListEntries = new OptionListEntry[options.length()];
            for (int i = 0; i < optionListEntries.length; i++) {
                optionListEntries[i] = new OptionListEntry(options.optJSONObject(i));
            }

            ArrayAdapter<OptionListEntry> optionsAdapter = new ArrayAdapter<>(this.getBaseContext(), R.layout.scenario_item, optionListEntries);
            ListView optionList = (ListView) findViewById(R.id.optionList);
            optionList.setAdapter(optionsAdapter);
            optionList.setOnItemClickListener(this);
        } else {
            results();
//            String[] responseOptions = {"Write a reply", "Record a message", "Skip"};
//
//            ArrayAdapter<String> optionsAdapter = new ArrayAdapter<>(this.getBaseContext(), R.layout.scenario_item, responseOptions);
//            ListView optionList = (ListView) findViewById(R.id.optionList);
//            optionList.setAdapter(optionsAdapter);
//            optionList.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (_currentSceneIndex < _sceneOptions.length()) {
            OptionListEntry selectedOption = (OptionListEntry) parent.getItemAtPosition(position);
            _playerScore += selectedOption.getVal();
            _currentSceneIndex++;
            renderScene();
        } else {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void results() {
        MainPageActivity.resultsBool = true;
        Intent resultsIntent = new Intent(this, MainPageActivity.class);
        startActivity(resultsIntent);
    }
}

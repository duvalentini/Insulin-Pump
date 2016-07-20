package com.example.gestalt.insulinpumpulator;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aloverfield on 5/25/16.
 */
public class ScenarioSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout
        View frag = inflater.inflate(R.layout.fragment_scernario_selection, container, false);

        ScenarioArgs[] scenarios = ScenarioArgs.getScenarioConfigs();

        ArrayAdapter<ScenarioArgs> scenarioAdapter = new ArrayAdapter<>(this.getContext(), R.layout.scenario_item, scenarios);
        ListView scenarioList = (ListView) frag.findViewById(R.id.scenario_list);
        scenarioList.setAdapter(scenarioAdapter);
        scenarioList.setOnItemClickListener(this);
        return frag;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScenarioArgs scenario = (ScenarioArgs) parent.getItemAtPosition(position);
        Intent scenarioIntent = new Intent(view.getContext(), ScenarioPlaythrough.class);
        scenarioIntent.putExtra("config", scenario.getConfig().toString());
        view.getContext().startActivity(scenarioIntent);
        ScenarioPlaythrough._playerScore = 0;
        getActivity().finish();
    }
}

package com.example.gestalt.insulinpumpulator;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by aloverfield on 5/25/16.
 */
public class ScenarioPlaythroughFragment extends Fragment implements AdapterView.OnItemClickListener {

    private int _currentSceneIndex = 0;
    private JSONArray _sceneOptions;
    private String _fileName;
    private static final String NEXT_SCENE = "next_scene";


    private class OptionListEntry {
        private String _str;
        private int _val;
        private int _nextScene;

        private OptionListEntry(JSONObject jsonMap) {
            Iterator<String> it = jsonMap.keys();
            for (int i = 0; i < 2; i++) {
                String candidate = it.next();
                if (candidate.equals(NEXT_SCENE)) {
                    String throwaway = candidate;
                    _nextScene = jsonMap.optInt(throwaway);
                } else {
                    _str = candidate;
                    _val = jsonMap.optInt(_str);
                }
            }
        }

        public int getVal() {
            return _val;
        }

        public int getNextScene() {
            return _nextScene;
        }

        @Override
        public String toString() {
            return _str;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout
        View frag = inflater.inflate(R.layout.fragment_scernario_playthrough, container, false);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JSONObject configJson = null;
        Bundle bundle = this.getArguments();
        try {
            if (bundle != null) {
                String json = bundle.getString("config");
                configJson = new JSONObject(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        _sceneOptions = configJson.optJSONArray("sceneOptions");
        _fileName = configJson.optString("fileName");

        renderScene();
    }

    private void renderScene() {
        ImageView sceneImage = (ImageView) getActivity().findViewById(R.id.scene_image);
        Resources resources = getContext().getResources();
        String sceneImageName = _fileName + _currentSceneIndex;
        final int resourceId = resources.getIdentifier(sceneImageName, "drawable",
                getContext().getPackageName());
        sceneImage.setImageResource(resourceId);

        JSONArray options = _sceneOptions.optJSONArray(_currentSceneIndex);
        OptionListEntry[] optionListEntries = new OptionListEntry[options.length()]; //todo - make this 'length() - 1' if text is required on each scene
        for (int i = 0; i < optionListEntries.length; i++) {
            JSONObject item = options.optJSONObject(i);
            String text = item.optString("text");
            if (!text.isEmpty()) {
                //make text field
            } else {
                optionListEntries[i] = new OptionListEntry(item);
            }
        }

        ArrayAdapter<OptionListEntry> optionsAdapter = new ArrayAdapter<>(getContext(), R.layout.scenario_item, optionListEntries);
        ListView optionList = (ListView) getActivity().findViewById(R.id.optionList);
        optionList.setAdapter(optionsAdapter);
        optionList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionListEntry selectedOption = (OptionListEntry) parent.getItemAtPosition(position);
        ScenarioPlaythrough._playerScore += selectedOption.getVal();
        String sign = "+";
        if(selectedOption.getVal() < 0) {
            sign = "-";
        }
        Toast t = Toast.makeText(getContext(), sign + selectedOption.getVal(), Toast.LENGTH_SHORT);
        t.show();
        _currentSceneIndex = selectedOption.getNextScene();
        System.out.println("Next scene index: " + _currentSceneIndex);
        System.out.println("Number of scenes: " + _sceneOptions.length());
        if (_currentSceneIndex < _sceneOptions.length()) {
            System.out.println("Rendering...");
            renderScene();
        } else {
            System.out.println("Loading new fragment...");
            ((ScenarioPlaythrough) getActivity()).swapFragment(new ResultsFragment());
        }
    }
}

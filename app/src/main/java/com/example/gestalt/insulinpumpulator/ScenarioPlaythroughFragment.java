package com.example.gestalt.insulinpumpulator;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

    private Button mPumpButton;
    private Button mCheckBGButton;

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

        mPumpButton = (Button) view.findViewById(R.id.pump);
        mPumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PUMP PRESSED");
                // Create a new Fragment to be placed in the activity layout
                PumpTestFragment fragment = new PumpTestFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_section, fragment);
                transaction.commit();
            }
        });

        mCheckBGButton = (Button) view.findViewById(R.id.check_bg);
        mCheckBGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CHECK BG PRESSED");

                TextView bg = (TextView) getActivity().findViewById(R.id.bg);
                bg.setText(((int) ScenarioPlaythrough.mPump.bloodGlucose) + " mg/dl");
            }
        });

        if (_currentSceneIndex != 0 && _currentSceneIndex != 1) {
            TextView bg = (TextView) getActivity().findViewById(R.id.bg);
            bg.setText(((int) ScenarioPlaythrough.mPump.bloodGlucose) + " mg/dl");
        }

        renderScene();
    }

    private void renderScene() {
        ImageView sceneImage = (ImageView) getActivity().findViewById(R.id.scene_image);
        Resources resources = getContext().getResources();
        String sceneImageName = _fileName + _currentSceneIndex;
        final int resourceId = resources.getIdentifier(sceneImageName, "drawable",
                getContext().getPackageName());
        sceneImage.setImageResource(resourceId);

        System.out.println("BG = " + ScenarioPlaythrough.mPump.bloodGlucose);

        JSONArray options = _sceneOptions.optJSONArray(_currentSceneIndex);
        OptionListEntry[] optionListEntries = new OptionListEntry[options.length()-1]; //todo - make this 'length() - 1' if text is required on each scene
        int nextOptionIndex = 0;
        for (int i = 0; i < options.length(); i++) {
            JSONObject item = options.optJSONObject(i);
            String text = item.optString("text");
            if (!text.isEmpty()) {
                // make text field
                TextView instructions = (TextView) getActivity().findViewById(R.id.instructions);
                instructions.setText(text);
            } else {
                optionListEntries[nextOptionIndex] = new OptionListEntry(item);
                nextOptionIndex++;
            }
        }


        ArrayAdapter<OptionListEntry> optionsAdapter = new ArrayAdapter<>(getContext(), R.layout.scenario_item, optionListEntries);
        ListView optionList = (ListView) getActivity().findViewById(R.id.optionList);
        optionList.setAdapter(optionsAdapter);
        optionList.setOnItemClickListener(this);


        // Do scenario specific things

        // Hide the pump on the hypoglycemia scenario
//        if (_fileName.equals("hypoglycemia_")) {
//            mPumpButton.setVisibility(View.GONE);
//        } else {
//            mPumpButton.setVisibility(View.VISIBLE);
//        }

        if (_currentSceneIndex == 2) {
            mCheckBGButton.setEnabled(true);
        } else {
            mCheckBGButton.setEnabled(false);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionListEntry selectedOption = (OptionListEntry) parent.getItemAtPosition(position);
        ScenarioPlaythrough._playerScore -= 50;
//        String sign = "+";
//        if(selectedOption.getVal() < 0) {
//            sign = "";
//        }
//        Toast t = Toast.makeText(getContext(), "       " + sign + selectedOption.getVal() + "       ", Toast.LENGTH_SHORT);
//        if (selectedOption.getVal() > 0) {
//            t.getView().setBackgroundColor(Color.GREEN);
//        } else if (selectedOption.getVal() < 0) {
//            t.getView().setBackgroundColor(Color.RED);
//        }
//        t.show();

        // Update pump based on choice
        if (selectedOption._str.equals("Eat 15-20g of carbs")) {
            ScenarioPlaythrough.mPump.eatFood(18);
        } else if (selectedOption._str.equals("Eat a lot")) {
            ScenarioPlaythrough.mPump.eatFood(100);
        } else if (selectedOption._str.equals("Play outside") || selectedOption._str.equals("Keep playing basketball")) {
            ScenarioPlaythrough.mPump.exercising = true;
        } else if (selectedOption._str.equals("Administer glucagon") || selectedOption._str.equals("Call 911")) {
            ScenarioPlaythrough.mPump.setBloodGlucose(115);
        }
        else {
            ScenarioPlaythrough.mPump.exercising = false;
        }


        _currentSceneIndex = selectedOption.getNextScene();


        // Go to scenes based on BG
        if (_fileName.equals("hypoglycemia_") && ScenarioPlaythrough.mPump.bloodGlucose < 70) {
            _currentSceneIndex = 5;
        }
        if (_fileName.equals("hypoglycemia_") && ScenarioPlaythrough.mPump.bloodGlucose < 55 ) {
            _currentSceneIndex = 6;
        }
        // End game when BG is in target range
        if (_fileName.equals("hypoglycemia_") && ScenarioPlaythrough.mPump.bloodGlucose < 120 && ScenarioPlaythrough.mPump.bloodGlucose > 110) {
            _currentSceneIndex = 7;
        }

        // PASS TIME
        if (_currentSceneIndex == 2 || _currentSceneIndex == 1 || _currentSceneIndex == 0 || _currentSceneIndex == 5) {
            ScenarioPlaythrough.mPump.passTime(15);
        }

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

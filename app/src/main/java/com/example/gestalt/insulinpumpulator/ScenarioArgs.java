package com.example.gestalt.insulinpumpulator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gestalt on 6/27/16.
 */
public class ScenarioArgs {

    private static ScenarioArgs[] SCENARIO_ARGS;

    static {
        SCENARIO_ARGS = parseScenarios();
    }

    private static ScenarioArgs[] parseScenarios() {
        List<ScenarioArgs> scenarios = new ArrayList<>();
        try {
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"A Day at the Field\",\"fileName\":\"field_day_\",\"sceneOptions\":[[{\"Give points\":200},{\"Give less points\":50},{\"Take points away\":-30},{\"Do nothing\":0}],[{\"Here's a menu\":50},{\"With less options\":200}],[{\"And\":10},{\"Here's\":10},{\"One\":10},{\"With\":10},{\"More\":10}]]}")));
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Camping Trip\",\"fileName\":\"camp_trip_\",\"sceneOptions\":[[{\"These\":20,\"ones\":5},{\"Give different\":-15},{\"Values\":40}],[{\"Here's a level\":50},{\"With less menus\":200}]]}")));
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Space Journey\",\"fileName\":\"space_journey_\",\"sceneOptions\":[[{\"0_o\":200},{\"@_@\":50},{\"(V)(;,,;)(V)\":-30},{\"#_#\":42}]]}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return scenarios.toArray(new ScenarioArgs[scenarios.size()]);
    }

    private String _scenarioTitle;
    private JSONObject _config;

    public ScenarioArgs(JSONObject config) {
        _scenarioTitle = config.optString("title");
        _config = config;
    }

    public JSONObject getConfig() {
        return _config;
    }

    @Override
    public String toString() {
        return _scenarioTitle;
    }

    public static ScenarioArgs[] getScenarioConfigs() {
        return SCENARIO_ARGS;
    }
}

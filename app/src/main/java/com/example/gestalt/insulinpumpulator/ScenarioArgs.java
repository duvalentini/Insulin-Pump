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
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"A Day at the Field\",\"fileName\":\"field_day_\",\"sceneOptions\":[[{\"Give points\":200, next_scene: 1},{\"Give less points\":50, next_scene: 1},{\"Take points away\":-30, next_scene: 2},{\"Do nothing\":0, next_scene: 0}],[{\"Here's a menu\":50, next_scene: 2},{\"With less options\":200, next_scene: 3}],[{\"And\":10, next_scene: 3},{\"Here's\":10, next_scene: 3},{\"One\":10, next_scene: 3},{\"With\":10, next_scene: 3},{\"More\":10, next_scene: 3}]]}")));
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Camping Trip\",\"fileName\":\"camp_trip_\",\"sceneOptions\":[[{\"These\":20, next_scene: 0},{\"ones\":5, next_scene: 1},{\"Give different\":-15, next_scene: 1},{\"Values\":40, next_scene: 1}],[{\"Here's a level\":50, next_scene: 2},{\"With less menus\":200, next_scene: 0}]]}")));
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

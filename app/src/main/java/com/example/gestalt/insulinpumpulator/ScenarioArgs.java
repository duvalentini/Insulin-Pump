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
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Hypoglycemia\",\"fileName\":\"hypoglycemia_\",\"sceneOptions\": [ [{\"text\":\"It's a nice hot summer day, so you are outside playing basketball to get some exercise.\"}, {\"(click here to continue)\":0, \"next_scene\":1}], [{\"text\":\"You start becoming shaky, lightheaded, and irritable, so you suspect you may be experiencing hypoglycemia. What should you do now?\"}, {\"Check BG\":50, \"next_scene\":2}, {\"Keep playing basketball\":-50, \"next_scene\":0}], [{\"text\":\"15 minutes have passed. Check your BG.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"You have BG > 70mg/dl with CGM trending downwards, what should you do now?\"}, {\"Eat 15-20g of carbs\":50, \"next_scene\":4}, {\"Eat a lot\":-50, \"next_scene\":3}], [{\"text\":\"What do you want to do now?\"}, {\"Play outside\":-50, \"next_scene\":2}, {\"Eat 15-20g of carbs\":50, \"next_scene\":2}, {\"Eat a lot\":-50, \"next_scene\":2}, {\"Do nothing and wait\":-25, \"next_scene\":2}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You are still conscious and feeling well enough to eat food, so what should you do?\"}, {\"Wait and see if you get better\":-50, \"next_scene\":5}, {\"Eat 15-20g of carbs\":50, \"next_scene\":4}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You have become unconscious! What needs to be done now?\"}, {\"Administer glucagon\":50, \"next_scene\":7}, {\"Wait and see if you get better\":-50, \"next_scene\":6}, {\"Call 911\":50, \"next_scene\":7}]]}")));
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Hyperglycemia\",\"fileName\":\"hyperglycemia_\",\"sceneOptions\": [ [{\"text\":\"You are out at dinner and you are very hungry so you eat a huuuge meal (about 100g carbs!).\"}, {\"(click here to continue)\":0, \"next_scene\":1}], [{\"text\":\"You start to have difficulty concentrating, feel fatigued, and your vision becomes slightly blurry, so you suspect you may be experiencing hyperglycemia. What should you do now?\"}, {\"Check BG\":50, \"next_scene\":2}], [{\"text\":\"Check your BG. You want to lower it to the target range of 130-160mg/dl.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"60 minutes have passed. Check your BG. You want to lower it to the target range of 130-160mg/dl.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"What do you want to do now? You can access your pump above if you would like to.\"}, {\"Play outside\":-50, \"next_scene\":3}, {\"Eat 15-20g of carbs\":50, \"next_scene\":3}, {\"Eat a lot\":-50, \"next_scene\":3}, {\"Do nothing and wait\":-25, \"next_scene\":3}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You are still conscious and feeling well enough to eat food, so what should you do?\"}, {\"Wait and see if you get better\":-50, \"next_scene\":5}, {\"Eat 15-20g of carbs\":50, \"next_scene\":4}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You have become unconscious! What needs to be done now?\"}, {\"Administer glucagon\":50, \"next_scene\":7}, {\"Wait and see if you get better\":-50, \"next_scene\":6}, {\"Call 911\":50, \"next_scene\":7}]]}")));
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

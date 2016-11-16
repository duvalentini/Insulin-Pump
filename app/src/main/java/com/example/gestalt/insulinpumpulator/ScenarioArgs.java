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
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Scenario1\",\"fileName\":\"hypoglycemia_\",\"sceneOptions\": [ [{\"text\":\"It's a nice hot summer day, so you are outside playing basketball to get some exercise.\"}, {\"(click here to continue)\":0, \"next_scene\":1}], [{\"text\":\"You start becoming shaky, lightheaded, and irritable. What should you do now?\"}, {\"Check BG\":50, \"next_scene\":2}, {\"Keep playing basketball\":-50, \"next_scene\":0}], [{\"text\":\"15 minutes have passed. Check your BG.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"You have BG > 70mg/dl with CGM trending downwards, what should you do now?\"}, {\"Eat 15-20g of carbs\":50, \"next_scene\":4}, {\"Eat a lot\":-50, \"next_scene\":3}], [{\"text\":\"What do you want to do now?\"}, {\"Play outside\":-50, \"next_scene\":2}, {\"Eat 15-20g of carbs\":50, \"next_scene\":2}, {\"Eat a lot\":-50, \"next_scene\":2}, {\"Do nothing and wait\":-25, \"next_scene\":2},{\"Take a drink of water\":-25, \"next_scene\":2}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You are still conscious and feeling well enough to eat food, so what should you do?\"}, {\"Wait and see if you get better\":-50, \"next_scene\":5}, {\"Eat 15-20g of carbs\":50, \"next_scene\":4}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You have become unconscious! What needs to be done now?\"}, {\"Administer glucagon\":50, \"next_scene\":7}, {\"Wait and see if you get better\":-50, \"next_scene\":6}, {\"Call 911\":50, \"next_scene\":7}]]}")));
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Scenario2\",\"fileName\":\"hyperglycemia_\",\"sceneOptions\": [ [{\"text\":\"You are out at dinner and you are very hungry so you eat a huuuge meal.\"}, {\"(click here to continue)\":0, \"next_scene\":1}], [{\"text\":\"You start to have difficulty concentrating, feel fatigued, and your vision becomes slightly blurry. What should you do now?\"}, {\"Check BG\":50, \"next_scene\":2}], [{\"text\":\"Check your BG. You want to lower it to the target range of 130-160mg/dl.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"60 minutes have passed. Check your BG. You want to lower it to the target range of 130-160mg/dl.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"What do you want to do now? You can access your pump above if you would like to.\"}, {\"Play outside\":-50, \"next_scene\":3}, {\"Eat 15-20g of carbs\":50, \"next_scene\":3}, {\"Eat a lot\":-50, \"next_scene\":3}, {\"Do nothing and wait\":-25, \"next_scene\":3},{\"Take a drink of water\":-25, \"next_scene\":3}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You are still conscious and feeling well enough to eat food, so what should you do?\"}, {\"Wait and see if you get better\":-50, \"next_scene\":5}, {\"Eat 15-20g of carbs\":50, \"next_scene\":4}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You have become unconscious! What needs to be done now?\"}, {\"Administer glucagon\":50, \"next_scene\":7}, {\"Wait and see if you get better\":-50, \"next_scene\":6}, {\"Call 911\":50, \"next_scene\":7}]]}")));
            scenarios.add(new ScenarioArgs(new JSONObject("{\"title\":\"Scenario3\",\"fileName\":\"hyperglycemia_\",\"sceneOptions\": [ [{\"text\":\"You decide to check your blood sugar after eating dinner and notice that it is unusually high\"}, {\"(click here to continue)\":0, \"next_scene\":1}], [{\"text\":\"You start to feel tired, thirsty, and a little nauseous. What should you do now?\"}, {\"Check BG\":50, \"next_scene\":2}], [{\"text\":\"Check your BG. You want to lower it to the target range of 130-160mg/dl.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"60 minutes have passed. Check your BG. You want to lower it to the target range of 130-160mg/dl.\"}, {\"(click to continue)\":0, \"next_scene\":4}], [{\"text\":\"What do you want to do now? You can access your pump above if you would like to.\"}, {\"Play outside\":-100, \"next_scene\":7}, {\"Bolus(use the pump first)\":-150, \"next_scene\":10}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You are still conscious and feeling well enough to eat food, so what should you do?\"}, {\"Wait and see if you get better\":-150, \"next_scene\":5}, {\"Eat 15-20g of carbs\":-150, \"next_scene\":4}], [{\"text\":\"Uh Oh! Your BG has dropped to a dangerous level, below 70mg/dl. You have become unconscious! What needs to be done now?\"}, {\"Administer glucagon\":-150, \"next_scene\":7}, {\"Wait and see if you get better\":-150, \"next_scene\":6}, {\"Call 911\":-150, \"next_scene\":7}],  [{\"text\":\"Uh Oh! You start peeing and drinking a lot and feel tired and nauseated\"}, \n" +
                    " {\"Take a nap\":-150, \"next_scene\":8}, {\"Check ketones\":-150, \"next_scene\":4}], [{\"text\":\"You begin to throw up and feel sick!\"}, \n" +
                    " {\"Proceed to ED\":-150, \"next_scene\":9}],[{\"text\":\"Ketones are Large!\"}, \n" +
                    " {\"Start DKA protocol\":-150, \"next_scene\":9}],  [{\"text\":\"What do you want to do now?\"}, \n" +
                    " {\"Play outside\":-50, \"next_scene\":7}, \n" +
                    " {\"Do nothing and wait 1 hour\":-25, \"next_scene\":11}] , [{\"text\":\"Blood Sugar is still over 250\"}, \n" +
                    " {\"Bolus again and wait 1 hour\":-50, \"next_scene\":12}, \n" +
                    " {\"Check your ketones\":-25, \"next_scene\":3}]   ]}")));

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

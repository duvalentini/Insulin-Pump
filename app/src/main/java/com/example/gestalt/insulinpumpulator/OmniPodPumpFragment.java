package com.example.gestalt.insulinpumpulator;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 11/6/2016.
 */

public class OmniPodPumpFragment extends Fragment {
    private LinearLayout mLayout;
    private ArrayList<String> subMenuNames;
    private OmniPodPump pump;
    private ListView itemList;
    private TextView leftText;
    private TextView centerText;
    private TextView rightText;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pump_test_omnipod, container, false);

        pump = (OmniPodPump) ScenarioPlaythrough.mPump;

        subMenuNames = pump.getSubMenuNames();

        itemList = (ListView) view.findViewById(R.id.menu_list);
        itemList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.omnipod_menu_item, subMenuNames) {
            public View getView(int position, View convertView, ViewGroup parent)
            {
                final View highlightItem = super.getView(position, convertView, parent);
                if (position == pump.getSubCurrent())
                {
                    highlightItem.setBackgroundResource(R.color.colorPrimary);
                } else {
                    highlightItem.setBackgroundColor(Color.TRANSPARENT);
                }
                return highlightItem;
            }
        };
        itemList.setAdapter(menuAdapter);

        leftText = (TextView) view.findViewById(R.id.left_text);
        centerText = (TextView) view.findViewById(R.id.center_text);
        rightText = (TextView) view.findViewById(R.id.right_text);

        updateText();
        updateHighlight();

        Button up = (Button) view.findViewById(R.id.up_button);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Up pressed");
                pump.up();
                updateHighlight();
            }
        });

        Button down = (Button) view.findViewById(R.id.down_button);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Down Pressed");
                pump.down();
                updateHighlight();
            }
        });

        ImageButton home = (ImageButton) view.findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Home Pressed");
                pump.home();
                updateText();
            }
        });

        Button left = (Button) view.findViewById(R.id.left_button);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button 1 Pressed");
                pump.button1();
                updateText();
            }
        });

        Button middle = (Button) view.findViewById(R.id.middle_button);
        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button 2 Pressed");
                pump.button2();
                updateText();
            }
        });

        Button right = (Button) view.findViewById(R.id.right_button);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button 3 Pressed");
                pump.button3();
                updateText();
            }
        });


        return view;
    }

    private void updateHighlight() {
        ((ArrayAdapter<String>)itemList.getAdapter()).notifyDataSetChanged();
    }

    private void updateText() {
        subMenuNames.removeAll(subMenuNames);
        subMenuNames.addAll(pump.getSubMenuNames());
        ((ArrayAdapter<String>)itemList.getAdapter()).notifyDataSetChanged();
        OmniPodMenu menu = (OmniPodMenu) pump.currentMenu;
        ArrayList<String> buttonText = menu.getButtonText();
        leftText.setText(buttonText.get(0));
        centerText.setText(buttonText.get(1));
        rightText.setText(buttonText.get(2));

        updateHighlight();
    }
}

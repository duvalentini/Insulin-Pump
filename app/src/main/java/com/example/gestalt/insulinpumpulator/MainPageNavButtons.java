package com.example.gestalt.insulinpumpulator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainPageNavButtons.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainPageNavButtons#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageNavButtons extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainPageNavButtons() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageNavButtons.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageNavButtons newInstance(String param1, String param2) {
        MainPageNavButtons fragment = new MainPageNavButtons();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page_nav_buttons, container, false);

        Button scenarioSelect = (Button) view.findViewById(R.id.scenario_select);
        scenarioSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create fragment
                ScenarioSelectionFragment newFragment = new ScenarioSelectionFragment();

                //No arguments to set as of yet

                //Swap fragments out
                ((MainPageActivity) getActivity()).swapFragment(newFragment);
                System.out.println("Scenario select pressed");
            }
        });

        Button connections = (Button) view.findViewById(R.id.bConnections);
        connections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch fragment out

                System.out.println("Connections pressed");
            }
        });
        Button customize = (Button) view.findViewById(R.id.bCustomize);
        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new fragment
                AvatarSelectionFragment newFragment = new AvatarSelectionFragment();

                ((MainPageActivity) getActivity()).swapFragment(newFragment);
                System.out.println("Customize pressed");
            }
        });
        Button accountInfo = (Button) view.findViewById(R.id.bAccountInfo);
        accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch fragment out
                System.out.println("Account info pressed");
            }
        });

        Button pumpSettings = (Button) view.findViewById(R.id.bPumpSettings);
        pumpSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch fragment out
                PumpSettingsFragment newFragment = new PumpSettingsFragment();
                ((MainPageActivity) getActivity()).swapFragment(newFragment);

                System.out.println("Pump Settings pressed");
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

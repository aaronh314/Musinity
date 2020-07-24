package com.example.musinity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class SettingsFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Switch pianoRollSwitch;
    boolean pianoRollState;
    final static String SETTINGS_NAME = "PREFS";
    final static String PIANO_ROLL_KEY = "show_piano_roll";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = getActivity().getSharedPreferences(SETTINGS_NAME, 0);
        editor = sharedPreferences.edit();

        pianoRollSwitch = getView().findViewById(R.id.piano_roll_switch);

        pianoRollState = sharedPreferences.getBoolean(PIANO_ROLL_KEY, true);

        pianoRollSwitch.setChecked(pianoRollState);

        pianoRollSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianoRollState = !pianoRollState;
                pianoRollSwitch.setChecked(pianoRollState);
                editor.putBoolean(PIANO_ROLL_KEY, pianoRollState);
                editor.apply();
            }
        });

    }
}

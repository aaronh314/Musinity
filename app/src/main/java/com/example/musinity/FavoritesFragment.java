package com.example.musinity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    static final String SAVED_SONGS_KEY = "saved_songs";
    static final String SAVED_SONGS_PREF_KEY = "saved_songs_pref";
    static final String SAVED_SONGS_FILE_DIR = "SAVED_SONGS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        LinearLayout linearLayout = getView().findViewById(R.id.saved_songs_list);
        ArrayList<SavedSong> savedSongs = loadSongs();
        if (savedSongs != null) {
            for (SavedSong song : savedSongs) {
                linearLayout.addView(song);
            }
        }
    }

    public ArrayList<SavedSong> loadSongs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SAVED_SONGS_PREF_KEY, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(SAVED_SONGS_KEY, null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SavedSong>>() {}.getType();
        ArrayList<SavedSong> savedSongs = gson.fromJson(json, type);
        return savedSongs;
    }
}

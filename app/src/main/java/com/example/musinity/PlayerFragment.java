package com.example.musinity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerFragment extends Fragment {
    private PianoRollView pianoRollView;
    private MusicGenerator model;
    private NotePlayer notePlayer;
    private Handler handler;
    private Runnable runnable;
    private ImageView playPauseButton;
    private boolean playing = false;
    private TextView nowGeneratingTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.player_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notePlayer = new NotePlayer(getActivity());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                notePlayer.playNotes();
                Log.i("playing", "playing notes");
                if (notePlayer.getQueueSize() < 10) {
                    generateMeasure();
                }
                handler.postDelayed(this, 10);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        playPauseButton = getView().findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model == null) return;
                if (playing) {
                    pause();
                } else {
                    play();
                }
            }
        });

        nowGeneratingTextView = getView().findViewById(R.id.now_generating_text_view);
        nowGeneratingTextView.setText("");
    }

    public void newGenreSelected(String genre) throws IOException {
        model = new MusicGenerator(genre.toLowerCase(), getContext());
        nowGeneratingTextView.setText("Now Generating " + genre);
        model.loadModel(getActivity());
        generateMeasure();
        play();
    }

    public void generateMeasure() {
        float[][][] measures = model.generateMeasure();
        final ArrayList<ArrayList<Integer>> notes = new ArrayList<>();
        for(float[][] measure : measures) {
            for (int y = 0; y < measure.length; y++) {
                ArrayList<Integer> currNotes = new ArrayList<>();
                for (int x = 0; x < measure[y].length; x++) {
                    if (measure[y][x] > 0.3) {
                        currNotes.add(x + (128 - 96) / 2);
                    }
                }
                notePlayer.addNotes(currNotes);
            }
        }
    }

    public void play() {
        playPauseButton.setImageResource(R.drawable.ic_pause);
        playing = true;
        handler.postDelayed(runnable, 10);
    }

    public void pause() {
        playPauseButton.setImageResource(R.drawable.ic_play_arrow);
        playing = false;
        handler.removeCallbacks(runnable);
    }
}
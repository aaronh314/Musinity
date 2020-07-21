package com.example.musinity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
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
                if (notePlayer.getQueueSize() < 3) {
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
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setAlpha(0.8f);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setAlpha(1.0f);
                }
                return false;
            }
        };
        ImageView skipForwardButton = getView().findViewById(R.id.skip_forward_button);
        skipForwardButton.setOnTouchListener(onTouchListener);
        skipForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notePlayer.skipForward();
            }
        });

        ImageView skipBackwardButton = getView().findViewById(R.id.skip_previous_button);
        skipBackwardButton.setOnTouchListener(onTouchListener);
        skipBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notePlayer.skipBackward();
            }
        });

        SeekBar seekBar = getView().findViewById(R.id.threshold_seekBar);
        seekBar.setProgress(50);
        notePlayer.setThreshold(0.30 + (50.0 / 100.0) * 0.70);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double threshold = 0.30 + ((double)progress / 100.0) * 0.70;
                notePlayer.setThreshold(threshold);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        final ArrayList<ArrayList<Float>> notes = new ArrayList<>();

        for(float[][] measure : measures) {
            for (int y = 0; y < measure.length; y++) {
                ArrayList<Float> noteProb = new ArrayList<>();
                for (int x = 0; x < measure[y].length; x++) {
                    noteProb.add(measure[y][x]);
                }
                notes.add(noteProb);
            }
        }

        notePlayer.addNotesArray(notes);
    }

    private void play() {

        playPauseButton.setImageResource(R.drawable.ic_pause);
        playing = true;
        handler.postDelayed(runnable, 10);
    }

    public void pause() {
        playPauseButton.setImageResource(R.drawable.ic_play_arrow);
        playing = false;
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.closeModel();
    }
}
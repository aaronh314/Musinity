package com.example.musinity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class PlayerFragment extends Fragment{
    private MusicGenerator model;
    private NotePlayer notePlayer;
    private Handler handler;
    private Runnable runnable;
    private ImageView playPauseButton;
    private boolean playing = false;
    private TextView nowGeneratingTextView;
    private PianoRollView pianoRollView;
    private double MIN_THRESHOLD = 0.01;
    private double MAX_THRESHOLD = 0.95;
    private double currThreshold;

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
        Log.i("PlayerFragment", "onCreate");
        try {
            notePlayer = new NotePlayer(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                pianoRollView.update(notePlayer.getTop(), notePlayer.getThreshold(), notePlayer.getMeasureIndex());
                notePlayer.playNotes();
                if (notePlayer.getQueueSize() < 3) {
                    generateMeasure();
                }
                handler.postDelayed(this, 10);
            }
        };

        currThreshold = MIN_THRESHOLD + (50.0 / 100.0) * MAX_THRESHOLD;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("PlayerFragment", "onStart");
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
                if (notePlayer.getQueueSize() < 3) {
                    generateMeasure();
                }
                pianoRollView.update(notePlayer.getTop(), notePlayer.getThreshold(), notePlayer.getMeasureIndex());
            }
        });

        ImageView skipBackwardButton = getView().findViewById(R.id.skip_previous_button);
        skipBackwardButton.setOnTouchListener(onTouchListener);
        skipBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notePlayer.skipBackward();
                pianoRollView.update(notePlayer.getTop(), notePlayer.getThreshold(), notePlayer.getMeasureIndex());
            }
        });

        SeekBar seekBar = getView().findViewById(R.id.threshold_seekBar);
        Log.i("PlayerFragment", ""+(int)((currThreshold - MIN_THRESHOLD) / (MAX_THRESHOLD - MIN_THRESHOLD) * 100));
        int progress = (int)((currThreshold - MIN_THRESHOLD) / MAX_THRESHOLD * 100);
        seekBar.setProgress(progress);
        notePlayer.setThreshold(currThreshold);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currThreshold = MIN_THRESHOLD + ((double)progress / 100.0) * MAX_THRESHOLD;
                pianoRollView.update(notePlayer.getTop(), currThreshold, notePlayer.getMeasureIndex());
                notePlayer.setThreshold(currThreshold);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pianoRollView = getView().findViewById(R.id.piano_roll_view);


    }

    public void newGenreSelected(String genre) throws IOException {
        if (playing) {
            pause();
            notePlayer.clear();
        }
        model = new MusicGenerator(genre.toLowerCase(), getContext());
        nowGeneratingTextView.setText("Now Generating " + genre);
        model.loadModel(getActivity());
        generateMeasure();
        play();
    }

    public void generateMeasure() {
        float[][][] measures = model.generateMeasure();
        //ArrayList<ArrayList<Float>> notes = new ArrayList<>();

        float[][] notes = new float[measures.length * measures[0].length][measures[0][0].length];
        int row = 0;

        for(float[][] measure : measures) {
            for (int y = 0; y < measure.length; y++) {
                for (int x = 0; x < measure[y].length; x++) {
                    notes[row][x] = measure[y][x];
                }
                row++;
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
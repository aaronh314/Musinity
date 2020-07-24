package com.example.musinity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NotePlayer {
    private SoundPool soundPool;
    private int[] soundPoolNoteIDs;
    private Queue<float[][]> measuresQueue;
    private int measureIndex = 0;
    private double threshold;

    public NotePlayer(Context context) throws IOException {
        soundPoolNoteIDs = new int[128];
        measuresQueue = new LinkedList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(128).build();
        } else {
            soundPool = new SoundPool(128, AudioManager.STREAM_MUSIC, 0);
        }
        for (int i = 0; i < soundPoolNoteIDs.length; i++) {
            soundPoolNoteIDs[i] = soundPool.load(context.getAssets().openFd("notes/note_" + i + ".mp3"), 1);
        }
    }

    public void playNotes() {

        if (measureIndex < measuresQueue.peek().length) {
            for (int note = 0; note < measuresQueue.peek()[measureIndex].length; note++) {
                if (measuresQueue.peek()[measureIndex][note] > threshold &&
                        (note == measuresQueue.peek()[measureIndex].length - 1 || measuresQueue.peek()[measureIndex][note+1] <= threshold)) {
                    soundPool.play(soundPoolNoteIDs[note + (128 - 96) / 2], 1, 1, 1, 0, 1);
                }
            }
            measureIndex++;
        } else {
            measuresQueue.remove();
            measureIndex = 0;
        }
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void addNotesArray(float[][] noteProbs) {

        Log.i("NotePlayer", "addNotes");
        measuresQueue.add(noteProbs);
    }

    public int getQueueSize() {
        return measuresQueue.size();
    }

    public void skipForward() {
        measureIndex = 0;
        measuresQueue.remove();
    }

    public float[][] getTop() {
        return measuresQueue.peek();
    }

    public double getThreshold() {
        return threshold;
    }

    public void skipBackward() {
        measureIndex = 0;
    }

    public int getMeasureIndex() {
        return measureIndex;
    }

    public void clear() {
        measuresQueue.clear();
        measureIndex = 0;
    }
}

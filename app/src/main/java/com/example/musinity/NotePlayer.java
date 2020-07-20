package com.example.musinity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class NotePlayer {
    private SoundPool soundPool;
    private int[] soundPoolNoteIDs;
    private Context context;
    private Queue<ArrayList<Integer>> noteQueue;

    public NotePlayer(Context context) {
        this.context = context;
        soundPoolNoteIDs = new int[128];
        noteQueue = new LinkedList<>();

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
            soundPoolNoteIDs[i] = soundPool.load(context, noteIDs[i], 1);
        }
    }

    public void playNotes() {
        if (noteQueue.size() == 0) return;
        for(int note : noteQueue.remove()) {
            soundPool.play(soundPoolNoteIDs[note], 1, 1, 1, 0, 1);
        }
    }

    public void addNotes(ArrayList<Integer> notes) {
        noteQueue.add(notes);
    }

    public int getQueueSize() {
        return noteQueue.size();
    }

    private int[] noteIDs = new int[]    {
                R.raw.note_0,
                R.raw.note_1,
                R.raw.note_2,
                R.raw.note_3,
                R.raw.note_4,
                R.raw.note_5,
                R.raw.note_6,
                R.raw.note_7,
                R.raw.note_8,
                R.raw.note_9,
                R.raw.note_10,
                R.raw.note_11,
                R.raw.note_12,
                R.raw.note_13,
                R.raw.note_14,
                R.raw.note_15,
                R.raw.note_16,
                R.raw.note_17,
                R.raw.note_18,
                R.raw.note_19,
                R.raw.note_20,
                R.raw.note_21,
                R.raw.note_22,
                R.raw.note_23,
                R.raw.note_24,
                R.raw.note_25,
                R.raw.note_26,
                R.raw.note_27,
                R.raw.note_28,
                R.raw.note_29,
                R.raw.note_30,
                R.raw.note_31,
                R.raw.note_32,
                R.raw.note_33,
                R.raw.note_34,
                R.raw.note_35,
                R.raw.note_36,
                R.raw.note_37,
                R.raw.note_38,
                R.raw.note_39,
                R.raw.note_40,
                R.raw.note_41,
                R.raw.note_42,
                R.raw.note_43,
                R.raw.note_44,
                R.raw.note_45,
                R.raw.note_46,
                R.raw.note_47,
                R.raw.note_48,
                R.raw.note_49,
                R.raw.note_50,
                R.raw.note_51,
                R.raw.note_52,
                R.raw.note_53,
                R.raw.note_54,
                R.raw.note_55,
                R.raw.note_56,
                R.raw.note_57,
                R.raw.note_58,
                R.raw.note_59,
                R.raw.note_60,
                R.raw.note_61,
                R.raw.note_62,
                R.raw.note_63,
                R.raw.note_64,
                R.raw.note_65,
                R.raw.note_66,
                R.raw.note_67,
                R.raw.note_68,
                R.raw.note_69,
                R.raw.note_70,
                R.raw.note_71,
                R.raw.note_72,
                R.raw.note_73,
                R.raw.note_74,
                R.raw.note_75,
                R.raw.note_76,
                R.raw.note_77,
                R.raw.note_78,
                R.raw.note_79,
                R.raw.note_80,
                R.raw.note_81,
                R.raw.note_82,
                R.raw.note_83,
                R.raw.note_84,
                R.raw.note_85,
                R.raw.note_86,
                R.raw.note_87,
                R.raw.note_88,
                R.raw.note_89,
                R.raw.note_90,
                R.raw.note_91,
                R.raw.note_92,
                R.raw.note_93,
                R.raw.note_94,
                R.raw.note_95,
                R.raw.note_96,
                R.raw.note_97,
                R.raw.note_98,
                R.raw.note_99,
                R.raw.note_100,
                R.raw.note_101,
                R.raw.note_102,
                R.raw.note_103,
                R.raw.note_104,
                R.raw.note_105,
                R.raw.note_106,
                R.raw.note_107,
                R.raw.note_108,
                R.raw.note_109,
                R.raw.note_110,
                R.raw.note_111,
                R.raw.note_112,
                R.raw.note_113,
                R.raw.note_114,
                R.raw.note_115,
                R.raw.note_116,
                R.raw.note_117,
                R.raw.note_118,
                R.raw.note_119,
                R.raw.note_120,
                R.raw.note_121,
                R.raw.note_122,
                R.raw.note_123,
                R.raw.note_124,
                R.raw.note_125,
                R.raw.note_126,
                R.raw.note_127
    };
}

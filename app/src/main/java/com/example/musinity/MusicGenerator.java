package com.example.musinity;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.Random;

public class MusicGenerator {
    private String genreName;
    private Interpreter model;

    public MusicGenerator(String genreName, Context context) throws IOException {
        this.genreName = genreName;
    }

    public void loadModel(Activity activity) throws IOException {
        String modelPath = genreName + "_net.tflite";
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        model = new Interpreter(fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength));
    }

    public float[][][] generateMeasure() {
        float[] encoding = new float[120];
        float[][][][] measures = new float[1][16][96][96];
        for (int i = 0; i < 120; i++) {
            encoding[i] = (float) new Random().nextGaussian();
        }
        model.run(encoding, measures);
        //model.close();
        return measures[0];
    }

    public static int getResId(String variableName, Class<?> c) {
        Field field = null;
        int resId = 0;
        try {
            field = c.getField(variableName);
            try {
                resId = field.getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;
    }
}

package com.example.musinity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SavedSong extends FrameLayout {
    private String name;
    private String genre;
    private String date;

    private ArrayList<float[][]> measures;

    public SavedSong(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SavedSong(@NonNull Context context, String name, String genre, String date, ArrayList<float[][]> measures) {
        super(context);
        this.name = name;
        this.genre = genre;
        this.date = date;
        this.measures = measures;
        init(context);
    }

    public SavedSong(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SavedSong(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SavedSong(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(getContext(), R.layout.saved_item_layout, this);
        TextView nameTextView = findViewById(R.id.saved_item_name);
        nameTextView.setText(name);

        TextView genreTextView = findViewById(R.id.item_genre_name);
        genreTextView.setText(genre);

        TextView dataTextView = findViewById(R.id.item_date);
        dataTextView.setText(date);

    }

    public void addMeasure(float[][] measure) {
        measures.add(measure);
    }

    public ArrayList<float[][]> getMeasures() {
        return measures;
    }
}

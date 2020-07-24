package com.example.musinity;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SongListFragment extends Fragment implements View.OnTouchListener {

    Animation cardPressed;
    Animation cardReleased;
    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.song_list_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = getView().findViewById(R.id.title_textview);
        title = getArguments().getString("title");
        textView.setText(title);

        CoverView coverView = (CoverView) getArguments().getSerializable("cover_view");
        ImageView original = (ImageView) coverView.getChildAt(0);
        ImageView imageView = getView().findViewById(R.id.cover_image_view);
        imageView.setImageDrawable(original.getDrawable());

        String genreTitle = getArguments().getString("title");
        try {
            loadSongTitles(genreTitle.toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }

        cardPressed = AnimationUtils.loadAnimation(getContext(), R.anim.card_pressed);
        cardReleased = AnimationUtils.loadAnimation(getContext(), R.anim.card_released);
        TextView generate = getView().findViewById(R.id.generate_textview);
        generate.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("touch", event.toString());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.startAnimation(cardPressed);
                break;
            case MotionEvent.ACTION_UP:
                v.startAnimation(cardReleased);
                PlayerFragment player = (PlayerFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.player_fragment);
                ((MainActivity) getActivity()).expandBottomSheet();
                try {
                    player.newGenreSelected(title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    public void loadSongTitles(String file) throws IOException {
        AssetManager assetManager = getContext().getAssets();
        InputStream inputStream = assetManager.open("titles/" + file + ".csv");

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        LinearLayout linearLayout = getView().findViewById(R.id.linear_layout);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                Log.i("CSV Reading", line);
                View custLayout= inflater.inflate(R.layout.song_item, null, false);
                TextView textView = custLayout.findViewById(R.id.song_title_text_view);
                textView.setText(line);
                linearLayout.addView(custLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            linearLayout.invalidate();
        } catch (IOException e) {
            Log.i("error", e.toString());
            e.printStackTrace();
        }
    }
}

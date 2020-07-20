package com.example.musinity;

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
import android.widget.LinearLayout;

public class HomeFragment extends Fragment implements View.OnTouchListener {
    private int[] genre_lists = {R.id.oldies_list, R.id.classical_list, R.id.jazz_list};
    Animation cardPressed;
    Animation cardReleased;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        cardPressed = AnimationUtils.loadAnimation(getContext(), R.anim.card_pressed);
        cardReleased = AnimationUtils.loadAnimation(getContext(), R.anim.card_released);

        for (int id : genre_lists) {
            LinearLayout linearLayout = getView().findViewById(id);
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                LinearLayout genre = (LinearLayout) linearLayout.getChildAt(i);
                genre.getChildAt(0).setOnTouchListener(this);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                v.startAnimation(cardReleased);
                break;
            case MotionEvent.ACTION_DOWN:
                v.startAnimation(cardPressed);
                break;
            case MotionEvent.ACTION_UP:
                CoverView coverView = (CoverView) v;
                String title = coverView.getTitle();
                Bundle bundle = new Bundle();
                bundle.putSerializable("cover_view", coverView);
                bundle.putString("title", title);
                coverView.startAnimation(cardReleased);
                Navigation.findNavController(getView()).navigate(R.id.action_home_fragment_to_song_list_fragment, bundle);
                break;
        }
        return true;
    }
}

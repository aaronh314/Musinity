package com.example.musinity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView playerFragment;
    private BottomSheetBehavior bottomSheetBehavior;
    private float initialCornerRadius = 32.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        playerFragment = findViewById(R.id.player_fragment);
        GradientDrawable drawable = (GradientDrawable) playerFragment.getBackground();
        drawable.setCornerRadius(initialCornerRadius);


        bottomSheetBehavior = BottomSheetBehavior.from(playerFragment);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                GradientDrawable drawable = (GradientDrawable) bottomSheet.getBackground();
                if (drawable == null){
                    Log.i("Drawable", "drawable is null");
                    return;
                }

                float newRadii = initialCornerRadius - initialCornerRadius * slideOffset;
                float values[] = {newRadii, newRadii, newRadii, newRadii, 0, 0, 0, 0};
                drawable.setCornerRadii(values);
            }
        });

        playerFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("player fragment", "moving");
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    public void expandBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            super.onBackPressed();
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}

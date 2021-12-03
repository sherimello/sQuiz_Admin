package com.example.squizadmin.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.squizadmin.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    private ConstraintLayout constraint_main;
    private CardView card1, card2, card3, card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);

        constraint_main = findViewById(R.id.constraint_main);

        constraint_main.setAlpha(0);

        new Handler().postDelayed(this::animateLayout, 500);

    }

    private void animateLayout() {
        card1.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        card2.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        card3.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        constraint_main.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        constraint_main.animate().alpha(1).setDuration(701).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                card1.animate().translationX(card1.getMeasuredWidth()
                        + getResources().getDimensionPixelSize(R.dimen._margin_for_splash)).setDuration(500).setInterpolator(new OvershootInterpolator());
                card2.animate().translationX(-(card2.getMeasuredWidth()
                        + getResources().getDimensionPixelSize(R.dimen._margin_for_splash))).setDuration(500).setInterpolator(new OvershootInterpolator()).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        new Handler().postDelayed(Splash.this::reverseAnimation, 601);
                    }
                });
                card3.animate().rotation(45).setDuration(500).setStartDelay(100).setInterpolator(new OvershootInterpolator());

            }
        });
    }

    private void reverseAnimation() {
        card1.animate().translationX(0).setInterpolator(new DecelerateInterpolator());
        card2.animate().translationX(0).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                card1.setLayerType(View.LAYER_TYPE_NONE, null);
                card2.setLayerType(View.LAYER_TYPE_NONE, null);
                card3.setLayerType(View.LAYER_TYPE_NONE, null);

                new Handler().postDelayed(Splash.this::changeActivity,501);
            }
        });
        card3.animate().rotation(0).setDuration(500).setInterpolator(new OvershootInterpolator());
    }

    private void changeActivity() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            new Handler().postDelayed(() -> {
                final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        Pair.create(card1, "card1"),
                        Pair.create(card2, "card2"),
                        Pair.create(card3, "card3"));
                startActivity(new Intent(getApplicationContext(), SignIn.class), options.toBundle());
                new Handler().postDelayed(this::finish, 2000);

            }, 700);
            return;
        }
        card1.setVisibility(View.GONE);
        card2.setVisibility(View.GONE);
        new Handler().postDelayed(() -> {

        }, 1000);
        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,card3, "card1");
        startActivity(new Intent(getApplicationContext(), activity_quiz_list.class), options.toBundle());
        new Handler().postDelayed(this::finish, 2000);


//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        finish();
    }
}
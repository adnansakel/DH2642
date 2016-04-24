package com.example.adnansakel.masterpiece;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by Adnan Sakel on 4/24/2016.
 */
public class AnimUtil {
    View view;
    public AnimUtil(View view){
        this.view = view;

    }
    public void MakeInvisibleWithSlideUp(){
        view.animate().translationY(-view.getHeight())
                .setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void MakeVisibleWithSlideDown(){
        view.setVisibility(View.VISIBLE);
        view.animate().translationY(0)
                .setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
    }
}

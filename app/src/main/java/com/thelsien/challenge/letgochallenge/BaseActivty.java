package com.thelsien.challenge.letgochallenge;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Window;

public abstract class BaseActivty extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition exitTransition = new Explode();
            TransitionSet sharedElementTransition = new TransitionSet()
                    .addTransition(new ChangeImageTransform())
                    .addTransition(new ChangeBounds());

            exitTransition.excludeTarget(android.R.id.navigationBarBackground, true);
            exitTransition.excludeTarget(android.R.id.statusBarBackground, true);
            exitTransition.excludeTarget(getWindow().getDecorView().findViewById(R.id.action_bar_container), true);

            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(exitTransition);
            getWindow().setSharedElementEnterTransition(sharedElementTransition);
            getWindow().setSharedElementReenterTransition(sharedElementTransition);
            getWindow().setSharedElementExitTransition(sharedElementTransition);
            getWindow().setSharedElementReturnTransition(sharedElementTransition);
        }
    }
}

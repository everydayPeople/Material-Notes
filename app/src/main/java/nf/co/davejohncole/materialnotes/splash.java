package nf.co.davejohncole.materialnotes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Teacher on 10/31/2014.
 */
public class splash extends Activity {



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);

        ActionBar actionBar = getActionBar();
        actionBar.hide();


        setContentView(R.layout.activity_splash);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int wWidth = size.x;
        final int wHeight = size.y;

        ImageView iv = (ImageView) findViewById(R.id.blueTop);
        int width = wWidth;
        double tmp = wHeight / 1.6;
        int height = (int) tmp;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        iv.setLayoutParams(parms);

        final Context context;
        context = this;

        final ImageView bluetop = (ImageView) findViewById(R.id.blueTop);
        final TextView materialTXT = (TextView) findViewById(R.id.materialTXT);
        final TextView notesTXT = (TextView) findViewById(R.id.notesTXT);
        Animation slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        Animation slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidedown);
        bluetop.startAnimation(slideup);
        materialTXT.startAnimation(slideup);
        notesTXT.startAnimation(slidedown);

        bluetop.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetop.setVisibility(View.GONE);
                materialTXT.setVisibility(View.GONE);
                notesTXT.setVisibility(View.GONE);
            }
        }, 3500);

        bluetop.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, 4000);


    }

    public void startMainActivity () {
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent,options.toBundle());
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

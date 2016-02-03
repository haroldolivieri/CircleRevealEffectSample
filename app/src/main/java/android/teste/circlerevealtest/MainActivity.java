package android.teste.circlerevealtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView itemClearToolbar, iconTest;
    View contentAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemClearToolbar = (ImageView) findViewById(R.id.item_close);
        iconTest = (ImageView) findViewById(R.id.icon_test);
        contentAnimation = (View) findViewById(R.id.content_reveal);

        itemClearToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterReveal();
            }
        });
    }

    void enterReveal() {
        iconTest.setVisibility(View.INVISIBLE);

        int centerX = contentAnimation.getLeft();
        int centerY = contentAnimation.getTop();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        int finalRadius = Math.max(width, height);

        Animator circleOut = ViewAnimationUtils.createCircularReveal(contentAnimation, centerX, centerY, 0, finalRadius);
        circleOut.setDuration(500);

        Animator circleIn = ViewAnimationUtils.createCircularReveal(contentAnimation, width / 2, height / 2, finalRadius, 50);
        circleIn.setDuration(500);

        contentAnimation.setVisibility(View.VISIBLE);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(circleOut, circleIn);

        circleIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contentAnimation.setVisibility(View.INVISIBLE);
                iconTest.setVisibility(View.VISIBLE);

                Animator circleIcon = ViewAnimationUtils.createCircularReveal(iconTest, iconTest.getMeasuredWidth()/2,
                        iconTest.getMeasuredHeight()/2, 0, iconTest.getMeasuredWidth());
                circleIcon.setDuration(300);
                circleIcon.start();

            }
        });

        animatorSet.start();
    }
}

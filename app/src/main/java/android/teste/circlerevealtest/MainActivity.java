package android.teste.circlerevealtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toolbar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    ImageView itemClearToolbar, iconTest, receipt;
    View contentAnimation, contentFinal;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemClearToolbar = (ImageView) findViewById(R.id.item_close);
        iconTest = (ImageView) findViewById(R.id.icon_test);
        receipt = (ImageView) findViewById(R.id.receipt);

        contentAnimation = (View) findViewById(R.id.content_reveal);
        contentFinal = (View) findViewById(R.id.final_place);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        itemClearToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterReveal();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void enterReveal() {
        iconTest.clearAnimation();
        contentAnimation.clearAnimation();
        contentFinal.clearAnimation();

        iconTest.setVisibility(View.INVISIBLE);

        int centerX = contentAnimation.getLeft();
        int centerY = contentAnimation.getTop();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        int finalRadius = Math.max(width, height);

        Animator circleOut = ViewAnimationUtils.createCircularReveal(contentAnimation, centerX, centerY, 0, finalRadius * 1.5f);
        circleOut.setInterpolator(new AccelerateDecelerateInterpolator());
        circleOut.setDuration(500);

        Animator circleIn = ViewAnimationUtils.createCircularReveal(contentAnimation, width/2, height/2, finalRadius, 50);
        circleIn.setInterpolator(new AccelerateDecelerateInterpolator());
        circleIn.setDuration(500);

        contentAnimation.setVisibility(View.VISIBLE);

        AnimatorSet firstAnimatorSet = new AnimatorSet();
        firstAnimatorSet.playSequentially(circleOut, circleIn);

        final Animator circleIcon = ViewAnimationUtils.createCircularReveal(iconTest, iconTest.getMeasuredWidth()/2,
                iconTest.getMeasuredHeight()/2, 0, iconTest.getMeasuredWidth());

        circleOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                toolbar.setVisibility(View.INVISIBLE);
                receipt.setVisibility(View.INVISIBLE);
            }
        });

        circleIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contentAnimation.setVisibility(View.INVISIBLE);
                iconTest.setVisibility(View.VISIBLE);

                circleIcon.setInterpolator(new AccelerateDecelerateInterpolator());
                circleIcon.setDuration(300);
                circleIcon.start();
            }
        });

        final AnimatorSet secondAnimatorSet = new AnimatorSet();
        circleIcon.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(iconTest, "scaleX", 1f, .3f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(iconTest, "scaleY", 1f, .3f);

                ObjectAnimator moveX = ObjectAnimator.ofFloat(iconTest, "X", contentFinal.getLeft() - contentFinal.getMeasuredWidth());
                ObjectAnimator moveY = ObjectAnimator.ofFloat(iconTest, "Y", contentFinal.getBottom());
                moveX.setInterpolator(new AccelerateDecelerateInterpolator());
                moveY.setInterpolator(new DecelerateInterpolator());

                secondAnimatorSet.playTogether(scaleX, scaleY,moveX, moveY);
                secondAnimatorSet.setDuration(400);
                secondAnimatorSet.start();
            }
        });

        secondAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    recreate();
                }
            }
        });

        firstAnimatorSet.start();
    }
}

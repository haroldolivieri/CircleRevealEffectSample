package android.teste.circlerevealtest;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ProjectApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Museo500.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

}

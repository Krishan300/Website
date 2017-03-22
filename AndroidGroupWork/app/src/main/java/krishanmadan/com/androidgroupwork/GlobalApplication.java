package krishanmadan.com.androidgroupwork;

/**
 * Created by krishanmadan on 2/21/17.
 */

import android.app.Application;
import android.content.Context;

/**
 * Created by krishanmadan on 2/17/17.
 */

public class GlobalApplication extends Application {

    private static GlobalApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Context context() {
        return sApplication.getApplicationContext();
    } //needed to use .context

}


package net.razorblade446.android.libertadoresnavigator;

import android.app.Application;
import android.util.Log;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import net.razorblade446.android.libertadoresnavigator.services.NewsService;

public class NavigatorApplication extends Application{
    private static NavigatorApplication instance;
    private static String DB_NAME = "full_navigator";
    public static String TAG = "FULL_NAVIGATOR";

    private Database cbDatabase = null;

    private NewsService newsService = null;


    String getDbName() {
        return DB_NAME;
    }

    public Database getCbDatabase() {
        return cbDatabase;
    }

    public static NavigatorApplication getInstance () {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initializeCBL();

        newsService = NewsService.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initializeCBL () {
        Manager manager = null;

        NavigatorApplication myApp = (NavigatorApplication) getApplicationContext();

        try {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            cbDatabase = manager.getDatabase(this.getDbName());
        } catch (Exception ex) {
            Log.e(NavigatorApplication.TAG, "Error creating DB", ex);
        }
    }

}

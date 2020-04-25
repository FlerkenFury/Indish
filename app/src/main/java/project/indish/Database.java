package project.indish;

import android.app.Application;

import com.firebase.client.Firebase;

public class Database extends Application {

    public static String Database = "https://indish-b2180.firebaseio.com/";
    public static String User = "https://indish-b2180.firebaseio.com/User";

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}

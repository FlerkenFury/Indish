package project.indish;

import android.content.Context;
import android.content.SharedPreferences;

import project.indish.model.User;

public class SharedPref {

    private SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

    }

    public void save(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserEmail", user.getEmail());
        editor.putString("UserName", user.getName());
        editor.putString("UserImage", user.getImage());
        editor.putString("UserPassword", user.getPassword());
        editor.apply();
    }

    public User load() {
        User user = new User();
        user.setEmail(sharedPreferences.getString("UserEmail", ""));
        user.setName(sharedPreferences.getString("UserName", ""));
        user.setImage(sharedPreferences.getString("UserImage", ""));
        user.setPassword(sharedPreferences.getString("UserPassword", ""));
        return user;
    }

    public void clearAll(Context context) {
        sharedPreferences.edit().clear().commit();
    }
}

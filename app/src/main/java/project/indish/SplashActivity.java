package project.indish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.indish.model.User;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                if (mAuth.getCurrentUser() != null) {

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapShot:
                                    dataSnapshot.getChildren()) {
                                User user = snapShot.getValue(User.class);

                                if (user.getEmail() != null && !user.getEmail().isEmpty() && user.getEmail().equalsIgnoreCase(mAuth.getCurrentUser().getEmail())){
                                    Log.d(TAG, "asd email: " + user.getEmail());

                                    SharedPref sharedPref = new SharedPref(SplashActivity.this);
                                    sharedPref.save(user);

                                    Intent intent = new Intent(SplashActivity.this, HomeDrawerActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;

                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                else{
                    Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, 3000);

    }
}

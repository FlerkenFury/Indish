package project.indish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.indish.model.Ingredient;
import project.indish.model.Recipe;
import project.indish.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private EditText mEmail, mPassword;
    private Button mSignIn;
    private TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.login_et_email);
        mPassword = findViewById(R.id.login_et_password);
        mSignIn = findViewById(R.id.login_bt_signin);
        mRegister = findViewById(R.id.login_tv_regis);

        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {

            DatabaseReference userRef = database.getReference("user");

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapShot:
                            dataSnapshot.getChildren()) {
                        User user = snapShot.getValue(User.class);

                        if (user.getEmail() != null && !user.getEmail().isEmpty() && user.getEmail().equalsIgnoreCase(mAuth.getCurrentUser().getEmail())){
                            Log.d(TAG, "asd email: " + user.getEmail());

                            SharedPref sharedPref = new SharedPref(LoginActivity.this);
                            sharedPref.save(user);

                            Intent intent = new Intent(LoginActivity.this, HomeDrawerActivity.class);
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

//            userRef.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                    Log.d(TAG, "onDataChange: asd: " + mAuth.getCurrentUser().getEmail());
//
//                    User user = dataSnapshot.getValue(User.class);
//                    Log.d(TAG, "onDataChange: asd user: " + user.getEmail());
//                    SharedPref sharedPref = new SharedPref(LoginActivity.this);
//                    sharedPref.save(user);
//
//                    Intent intent = new Intent(LoginActivity.this, HomeDrawerActivity.class);
//                    startActivity(intent);
//                    finish();
//                    return;
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        }

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                logIn(mAuth, email, password);

//                if(!email.isEmpty() && !password.isEmpty()){
//                    mAuth.signInWithEmailAndPassword(email, password);
//                    Toast.makeText(LoginActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainFragment.class);
//                    startActivity(intent);
//                    finish();
//                }
//                else {
//                    Toast.makeText(LoginActivity.this, "Please fill in the data", Toast.LENGTH_SHORT).show();
//                }

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid() );
                    Toast.makeText(LoginActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };


    }

    public void logIn(FirebaseAuth mAuth, String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail)
                                    {
                                        Log.d(TAG, "onComplete: invalid_email");
                                        Toast.makeText(LoginActivity.this, "incorrect email format", Toast.LENGTH_LONG).show();

                                        // TODO: take your actions!
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                    {
                                        Log.d(TAG, "onComplete: wrong_password");
                                        Toast.makeText(LoginActivity.this, "email or password incorrect", Toast.LENGTH_LONG).show();

                                        // TODO: Take your action
                                    }
                                    catch (Exception e)
                                    {
                                        Log.d(TAG, "onComplete: " + e.getMessage());
                                    }
                                }
                                else {
                                    Intent intent = new Intent(LoginActivity.this, HomeDrawerActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                );
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onStop(){
        super.onStop();
        if ( mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

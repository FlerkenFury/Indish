package project.indish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.indish.model.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    
    private EditText name, email, password;
    private Button submit;

    private FirebaseAuth mAuth;

    private String nameText, emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        name = findViewById(R.id.regis_et_name);
        email = findViewById(R.id.regis_et_email);
        password = findViewById(R.id.regis_et_password);
        submit = findViewById(R.id.regis_bt_signup);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = name.getText().toString();
                emailText = email.getText().toString();
                passwordText = password.getText().toString();

                if (nameText == null || nameText.trim().equals("") || emailText == null || emailText.trim().equals("") || passwordText == null || passwordText.trim().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please input data correctly", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth = FirebaseAuth.getInstance();


                mAuth.createUserWithEmailAndPassword(emailText.trim(), passwordText.trim())
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
                                            catch (FirebaseAuthWeakPasswordException weakPassword)
                                            {
                                                Log.d(TAG, "onComplete: weak_password");
                                                Toast.makeText(RegisterActivity.this, "password is too weak", Toast.LENGTH_LONG).show();
                                                // TODO: take your actions!
                                            }
                                            // if user enters wrong password.
                                            catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                            {
                                                Log.d(TAG, "onComplete: malformed_email");
                                                Toast.makeText(RegisterActivity.this, "incorrect email format", Toast.LENGTH_LONG).show();

                                                // TODO: Take your action
                                            }
                                            catch (FirebaseAuthUserCollisionException existEmail)
                                            {
                                                Log.d(TAG, "onComplete: exist_email");
                                                Toast.makeText(RegisterActivity.this, "email already existed", Toast.LENGTH_LONG).show();

                                                // TODO: Take your action
                                            }
                                            catch (Exception e)
                                            {
                                                Log.d(TAG, "onComplete: " + e.getMessage());

                                            }
                                        }
                                        else{

                                            Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference userRef = database.getReference("user");
                                            String upKey = userRef.push().getKey();
                                            Log.d(TAG, "onComplete: adsasd" + upKey);

                                            User user = new User(nameText, emailText, passwordText, "");

                                            userRef.child(upKey).setValue(user);

                                            SharedPref sharedPref = new SharedPref(RegisterActivity.this);
                                            sharedPref.save(user);

                                            logIn(mAuth, emailText.trim(), passwordText.trim());

                                        }
                                    }
                                }
                        );

            }
        });

    }

    public void logIn(final FirebaseAuth mAuth, final String email, String password){

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
                                        Toast.makeText(RegisterActivity.this, "incorrect email format", Toast.LENGTH_LONG).show();

                                        // TODO: take your actions!
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                    {
                                        Log.d(TAG, "onComplete: wrong_password");
                                        Toast.makeText(RegisterActivity.this, "email or password incorrect", Toast.LENGTH_LONG).show();

                                        // TODO: Take your action
                                    }
                                    catch (Exception e)
                                    {
                                        Log.d(TAG, "onComplete: " + e.getMessage());

                                    }
                                }

                                else {

                                    if (mAuth.getCurrentUser() != null) {
                                        Intent intent = new Intent(RegisterActivity.this, HomeDrawerActivity.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }


                                }

                            }
                        }
                );


    }

}

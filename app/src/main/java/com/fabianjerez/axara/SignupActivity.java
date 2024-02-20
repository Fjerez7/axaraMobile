package com.fabianjerez.axara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    // Create object of DBreference class to access firebase DB
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://axara-android-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText firstName = findViewById(R.id.firstName);
        final EditText lastName = findViewById(R.id.lastName);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final Button signUpBtn = findViewById(R.id.signupBtn);
        final TextView loginNowBtn = findViewById(R.id.loginNowBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from editTexts into var
                final String firstNameTxt = firstName.getText().toString();
                final String lastNameTxt = lastName.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                //check if user fill all the fields before sending data to DB
                if (firstNameTxt.isEmpty() || lastNameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Please fill all fields", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTxt,passwordTxt)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User registration successful, get the UID
                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        // Save the user's data in the database using the UID as the key
                                        databaseReference.child("users").child(uid).child("firstName").setValue(firstNameTxt);
                                        databaseReference.child("users").child(uid).child("lastName").setValue(lastNameTxt);
                                        databaseReference.child("users").child(uid).child("email").setValue(emailTxt);
                                        databaseReference.child("users").child(uid).child("password").setValue(passwordTxt);

                                        Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        // If registration fails, display a message to the user.
                                        Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
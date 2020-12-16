package com.example.groupexerciseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInPage extends AppCompatActivity {
    Switch mySwitch;
    EditText username, password, activity, code, date;
    LinearLayout register;
    TextView tvRegister;
    ImageButton imgAct, imgDate;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        getSupportActionBar().hide();

        mySwitch = findViewById(R.id.switch1);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        activity = findViewById(R.id.editTextActivity);
        code = findViewById(R.id.editTextSpecialCode);
        date = findViewById(R.id.editTextDate);
        register = findViewById(R.id.btnRegister);
        tvRegister = findViewById(R.id.tvRegister);
        imgAct = findViewById(R.id.imageButtonAct);
        imgDate = findViewById(R.id.imageButtonDate);

        imgAct.setVisibility(View.INVISIBLE);
        imgDate.setVisibility(View.INVISIBLE);

        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mySwitch.isChecked()){
                    mySwitch.setText("Login");
                    changing(true);
                }else{
                    mySwitch.setText("Registration");
                    changing(false);
                }
            }
        });
        imgAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myCode = code.getText().toString();
                String myAct = activity.getText().toString().trim();
                ref = FirebaseDatabase.getInstance().getReference("activity");
                ref.child(myCode).child("activity").setValue(myAct);
                Toast.makeText(SignInPage.this, "Successful update", Toast.LENGTH_LONG).show();
            }
        });
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myCode = code.getText().toString();
                String myDate = date.getText().toString().trim();
                ref = FirebaseDatabase.getInstance().getReference("activity");
                ref.child(myCode).child("date").setValue(myDate);
                Toast.makeText(SignInPage.this, "Successful update", Toast.LENGTH_LONG).show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String switching = mySwitch.getText().toString();
                if(switching.equals("Registration")){
                    String myUsername = username.getText().toString();
                    String myPassword = password.getText().toString();
                    String myActivity = activity.getText().toString();
                    String myCode = code.getText().toString();
                    String myDate = date.getText().toString();
                    if(myCode.length() > 4 || myCode.length() < 4){
                        code.setError("Code should be 4 numbers, please try again");
                    }else{
                        detail d1= new detail(myUsername, myPassword, myActivity, myCode, myDate);
                        ref = FirebaseDatabase.getInstance().getReference("activity");
                        ref.child(myCode).setValue(d1);
                        Toast.makeText(SignInPage.this, "Successfully Added", Toast.LENGTH_LONG).show();
                        goMain(v);
                    }
                }else{
                    String myUsername = username.getText().toString();
                    String myPassword = password.getText().toString();
                    String myCode = code.getText().toString();
                    Query query = FirebaseDatabase.getInstance().getReference("activity")
                            .orderByChild("code")
                            .equalTo(myCode);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String dataUser = snapshot.child(myCode).child("username").getValue().toString();
                                String dataPass = snapshot.child(myCode).child("password").getValue().toString();
                                if(dataUser.equals(myUsername) && dataPass.equals(myPassword)){
                                    String dataAct = snapshot.child(myCode).child("activity").getValue().toString();
                                    String dataDate = snapshot.child(myCode).child("date").getValue().toString();
                                    activity.setVisibility(View.VISIBLE);
                                    date.setVisibility(View.VISIBLE);

                                    closingDown();
                                    username.setText(dataUser);
                                    password.setText(dataPass);
                                    code.setText(myCode);
                                    activity.setText(dataAct);
                                    date.setText(dataDate);
                                    imgAct.setVisibility(View.VISIBLE);
                                    imgDate.setVisibility(View.VISIBLE);
                                    Toast.makeText(SignInPage.this, "Login Successful", Toast.LENGTH_LONG).show();
                                }else{
                                    username.setError("Username or password not valid");
                                }
                            }else{
                                code.setError("No code founded");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    public void closingDown(){
        username.setEnabled(false);
        password.setEnabled(false);
        code.setEnabled(false);
    }

    public void changing(Boolean check){
        if(check){
            activity.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
            tvRegister.setText("Login");
        }else{
            username.setEnabled(true);
            password.setEnabled(true);
            code.setEnabled(true);

            activity.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            imgAct.setVisibility(View.INVISIBLE);
            imgDate.setVisibility(View.INVISIBLE);

            tvRegister.setText("Register");
        }
    }
    public void goMain(View v){
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
    }
}
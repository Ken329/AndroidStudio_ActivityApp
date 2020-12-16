package com.example.groupexerciseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements dialog.ExampleDialogListener{
    LinearLayout signIn;
    EditText code;
    ImageButton login;
    DatabaseReference ref;
    String myCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        signIn = findViewById(R.id.linearLayoutSignIn);
        code = findViewById(R.id.editTextCode);
        login = findViewById(R.id.imageButtonLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCode = code.getText().toString();
                if(myCode.length() > 4 || myCode.length() < 4){
                    Toast.makeText(MainActivity.this, "Code should be 4 numbers, please try again", Toast.LENGTH_LONG).show();
                }else{
                    Query query = FirebaseDatabase.getInstance().getReference("activity")
                            .orderByChild("code")
                            .equalTo(myCode);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                openDialog();
                            }else{
                                Toast.makeText(MainActivity.this, "Invalid code", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignIn(v);
            }
        });
    }
    public void openDialog(){
        dialog dialog = new dialog();
        dialog.show(getSupportFragmentManager(), "Example Dialog");
    }
    public void goSignIn(View v){
        Intent intent = new Intent(v.getContext(), SignInPage.class);
        startActivity(intent);
    }
    @Override
    public void applyTexts(String name, String id) {
        userDetail user = new userDetail(id, name);
        ref = FirebaseDatabase.getInstance().getReference("participants");
        ref.child(myCode).child(id).setValue(user);
        Toast.makeText(MainActivity.this, "Successful Getting in", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), activityPage.class);
        intent.putExtra("code", myCode);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
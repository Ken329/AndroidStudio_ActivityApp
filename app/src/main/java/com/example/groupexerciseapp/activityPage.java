package com.example.groupexerciseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class activityPage extends AppCompatActivity {
    TextView act, date;
    TextView comTitle, comDesc;
    LinearLayout linearStream, linearParti, stream, participant;
    ImageButton comEnter;
    ListView list;

    String myCode, myName;
    DatabaseReference ref;
    ArrayList<String> arrayTitle, arrayDesc, arrayDate, arrayName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        getSupportActionBar().hide();

        myCode = getIntent().getStringExtra("code");
        myName = getIntent().getStringExtra("name");

        act = findViewById(R.id.textViewAct);
        date = findViewById(R.id.textViewDate);
        linearStream = findViewById(R.id.linearStream);
        linearParti = findViewById(R.id.linearParti);
        stream = findViewById(R.id.stream);
        participant = findViewById(R.id.participant);
        comTitle = findViewById(R.id.commentTitle);
        comDesc = findViewById(R.id.commentDesc);
        comEnter = findViewById(R.id.imageButtonEnter);
        list = findViewById(R.id.commentList);

        linearParti.setVisibility(View.INVISIBLE);

        ref = FirebaseDatabase.getInstance().getReference("activity");
        ref.child(myCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myAct = snapshot.child("activity").getValue().toString();
                String myDate = snapshot.child("date").getValue().toString();
                act.setText(myAct);
                date.setText(myDate);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        arrayTitle  = new ArrayList<>();
        arrayDesc  = new ArrayList<>();
        arrayDate  = new ArrayList<>();
        arrayName  = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("comment").child(myCode);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayTitle.clear();
                arrayDesc.clear();
                arrayDate.clear();
                arrayName.clear();
                for(DataSnapshot s1 : snapshot.getChildren()){
                    arrayTitle.add(s1.child("title").getValue().toString());
                    arrayDesc.add(s1.child("describe").getValue().toString());
                    arrayDate.add(s1.child("post_date").getValue().toString());
                    arrayName.add(s1.child("post_name").getValue().toString());
                }
                customAdapter custom = new customAdapter();
                list.setAdapter(custom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        comEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = comTitle.getText().toString();
                String desc = comDesc.getText().toString();
                if(title.equals("") || desc.equals("")){
                    comDesc.setError("Please fill up all the field");
                }else{
                    participantDetail detail = new participantDetail(title, desc, myName, getDate());
                    ref = FirebaseDatabase.getInstance().getReference("comment");
                    ref.child(myCode).child(title).setValue(detail);
                    Toast.makeText(activityPage.this, "Successful inserted", Toast.LENGTH_SHORT).show();
                    comTitle.setText("");
                    comDesc.setText("");
                }
            }
        });
        participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goParti(v);
            }
        });
    }
    public String getDate(){
        Calendar calender = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calender.getTime());
        return currentDate;
    }
    public void goParti(View v){
        Intent intent = new Intent(v.getContext(), participantPage.class);
        intent.putExtra("code", myCode);
        startActivity(intent);
    }
    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayTitle.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = getLayoutInflater().inflate(R.layout.comment_layout, null);

            TextView title = view1.findViewById(R.id.tvTitle);
            TextView desc = view1.findViewById(R.id.tvDesc);
            TextView date = view1.findViewById(R.id.tvDate);
            TextView name = view1.findViewById(R.id.tvName);

            title.setText(arrayTitle.get(position));
            desc.setText(arrayDesc.get(position));
            date.setText(arrayDate.get(position));
            name.setText(arrayName.get(position));
            return view1;
        }
    }
}
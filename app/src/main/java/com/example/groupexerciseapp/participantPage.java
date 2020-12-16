package com.example.groupexerciseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class participantPage extends AppCompatActivity {
    TextView act, date;
    LinearLayout linearStream, linearParti, stream, participant;
    ListView listview;
    String myCode;
    DatabaseReference ref;
    ArrayList<String> myId, myName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_page);
        getSupportActionBar().hide();

        myCode = getIntent().getStringExtra("code");
        act = findViewById(R.id.textViewAct);
        date = findViewById(R.id.textViewDate);
        linearStream = findViewById(R.id.linearStream);
        linearParti = findViewById(R.id.linearParti);
        stream = findViewById(R.id.stream);
        participant = findViewById(R.id.participant);
        listview = findViewById(R.id.listView);

        linearStream.setVisibility(View.INVISIBLE);

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

        myId = new ArrayList<>();
        myName = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("participants").child(myCode);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s1 : snapshot.getChildren()){
                    myId.add(s1.child("id").getValue().toString());
                    myName.add(s1.child("name").getValue().toString());
                }
                customAdapter custom = new customAdapter();
                listview.setAdapter(custom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goStream(v);
            }
        });
    }
    public void goStream(View v){
        Intent intent = new Intent(v.getContext(), activityPage.class);
        intent.putExtra("code", myCode);
        startActivity(intent);
    }
    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return myId.size();
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
            View view1 = getLayoutInflater().inflate(R.layout.userlisting_layout, null);

            TextView tvId = (TextView)view1.findViewById(R.id.participantId);
            TextView tvName = (TextView)view1.findViewById(R.id.participantName);
            tvId.setText(myId.get(position));
            tvName.setText(myName.get(position));
            return view1;
        }
    }
}
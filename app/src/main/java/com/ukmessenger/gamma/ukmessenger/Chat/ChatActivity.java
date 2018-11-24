package com.ukmessenger.gamma.ukmessenger.Chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ukmessenger.gamma.ukmessenger.Model.User;
import com.ukmessenger.gamma.ukmessenger.R;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    DatabaseReference reference;
    FirebaseUser fuser;

    AppCompatTextView mTxtContactLetter;
    TextView mTxtContactName, mTxtContactStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String userid = getIntent().getStringExtra("userId");

        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTxtContactLetter = findViewById(R.id.txt_contact_letter);
        mTxtContactName = findViewById(R.id.txt_contact_name);
        mTxtContactStatus = findViewById(R.id.txt_contact_caption);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mTxtContactName.setText(user.getName());
                mTxtContactLetter.setText(String.valueOf(user.getName().charAt(0)));
                mTxtContactStatus.setText(user.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }
}

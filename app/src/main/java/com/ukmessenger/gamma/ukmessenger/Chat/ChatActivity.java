package com.ukmessenger.gamma.ukmessenger.Chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ukmessenger.gamma.ukmessenger.Model.Chat;
import com.ukmessenger.gamma.ukmessenger.Model.User;
import com.ukmessenger.gamma.ukmessenger.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    DatabaseReference reference;
    FirebaseUser fuser;

    AppCompatTextView mTxtContactLetter;
    TextView mTxtContactName, mTxtContactStatus;

    ImageButton btnSend;
    EditText txtMessage;

    ChatAdapter adapter;
    List<Chat> mChat;

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String userid = getIntent().getStringExtra("userId");

        btnSend = findViewById(R.id.btn_send);
        txtMessage = findViewById(R.id.txt_message);

        rv = findViewById(R.id.chat_recycler);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rv.setLayoutManager(llm);

        btnSend.setOnClickListener(v -> {
            String message = txtMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(fuser.getUid(), userid, message);
                txtMessage.setText("");
            } else {
                Toast.makeText(ChatActivity.this, "No se pueden enviar mensajes vacíos", Toast.LENGTH_SHORT);
            }
        });

        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
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
        readMessages(fuser.getUid(), userid);
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> chat = new HashMap<>();
        chat.put("sender", sender);
        chat.put("receiver", receiver);
        chat.put("message", message);
        reference.child("Chats").push().setValue(chat);
    }

    private void readMessages(final String myid, final String userid) {
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(myid) && chat.getReceiver().equals(userid)
                            || chat.getSender().equals(userid) && chat.getReceiver().equals(myid)) {
                        mChat.add(chat);
                    }
                    adapter = new ChatAdapter(ChatActivity.this, mChat);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

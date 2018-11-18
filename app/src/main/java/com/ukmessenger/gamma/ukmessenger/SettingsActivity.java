package com.ukmessenger.gamma.ukmessenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class SettingsActivity extends AppCompatActivity {

    TextView txvId, txvName;
    TextInputEditText txtUserStatus;
    CircularProgressButton btnSave;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;
    private String currentUser;

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
        retrieveUserInfo();
        updateSettings();
    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference();

        txvId = findViewById(R.id.txt_carnet);
        txvName = findViewById(R.id.txt_name);
        txtUserStatus = findViewById(R.id.txt_user_status);
        btnSave = findViewById(R.id.btnSave);

        setSupportActionBar(findViewById(R.id.main_toolbar));
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void retrieveUserInfo(){
        dbRef.child("Users").child(currentUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if (dataSnapshot.hasChild("carnet")) txvId.setText(dataSnapshot.child("carnet").getValue().toString());
                            if (dataSnapshot.hasChild("name")) txvName.setText(dataSnapshot.child("name").getValue().toString());
                            if (dataSnapshot.hasChild("status")) txtUserStatus.setText(dataSnapshot.child("status").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateSettings(){
        btnSave.setOnClickListener(v -> {
            txtUserStatus.setEnabled(false);
            btnSave.startAnimation();

            String userstatus = Objects.requireNonNull(txtUserStatus.getText()).toString();

            //HashMap<String, String> profileMap = new HashMap<>();
            //profileMap.put("uid", currentUser);
            //profileMap.put("status", userstatus);

            dbRef.child("Users").child(currentUser).child("status").setValue(userstatus)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this, getResources().getString(R.string.m_save_success),Toast.LENGTH_SHORT).show();
                        } else {
                            new AlertDialog.Builder(SettingsActivity.this)
                                    .setTitle("Error")
                                    .setMessage(task.getException().getMessage())
                                    .setPositiveButton("Ok", ((dialog, which) -> dialog.dismiss()))
                                    .show();
                        }

                        btnSave.revertAnimation();
                        txtUserStatus.setEnabled(true);
                    });

        });
    }

    @Override
    protected void onDestroy() {
        btnSave.dispose();
        super.onDestroy();
    }
}

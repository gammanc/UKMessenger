package com.ukmessenger.gamma.ukmessenger.Contacts;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ukmessenger.gamma.ukmessenger.Model.User;
import com.ukmessenger.gamma.ukmessenger.R;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private List<User> userList;

    public ContactsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = view.findViewById(R.id.contacts_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();

        readUsers();

        return view;
    }

    void readUsers(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    Log.d("CONTACTS", "onDataChange: "+user.getName());
                    if (!user.getUid().equals(firebaseUser.getUid())){
                        userList.add(user);
                    }
                }

                if (userList.isEmpty()) Log.d("CONTACTS", "onDataChange: lista vacía");
                contactsAdapter = new ContactsAdapter(getContext(),userList);
                recyclerView.setAdapter(contactsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

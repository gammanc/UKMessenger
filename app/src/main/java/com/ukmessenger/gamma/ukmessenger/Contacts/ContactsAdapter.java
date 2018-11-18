package com.ukmessenger.gamma.ukmessenger.Contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ukmessenger.gamma.ukmessenger.Model.User;
import com.ukmessenger.gamma.ukmessenger.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{

    private Context context;
    private List<User> userList;

    public ContactsAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.contact_item, viewGroup, false);
        return new ContactsAdapter.ContactsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int i) {
        User user = userList.get(i);
        holder.contactLetter.setText(String.valueOf(user.getName().charAt(0)));
        holder.contactName.setText(user.getName());
        holder.contactCaption.setText(user.getCarnet() + " - " + user.getStatus());
    }

    @Override
    public int getItemCount() {
        return userList.size() > 0 ? userList.size() : 0;
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView contactLetter;
        TextView contactName, contactCaption;

        ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            contactLetter = itemView.findViewById(R.id.txt_contact_letter);
            contactName = itemView.findViewById(R.id.txt_contact_name);
            contactCaption = itemView.findViewById(R.id.txt_contact_caption);


        }
    }
}

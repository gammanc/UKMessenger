package com.ukmessenger.gamma.ukmessenger.Chat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ukmessenger.gamma.ukmessenger.Model.Chat;
import com.ukmessenger.gamma.ukmessenger.Model.User;
import com.ukmessenger.gamma.ukmessenger.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> chatList;

    FirebaseUser fuser;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == MSG_TYPE_RIGHT) {
            v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, viewGroup, false);
        }

        return new ChatAdapter.ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int i) {
        Chat chat = chatList.get(i);
        holder.chatMessage.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size() > 0 ? chatList.size() : 0;
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView chatMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMessage = itemView.findViewById(R.id.chat_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}

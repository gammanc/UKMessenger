package com.ukmessenger.gamma.ukmessenger.Main;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ukmessenger.gamma.ukmessenger.ChatsFragment;
import com.ukmessenger.gamma.ukmessenger.Contacts.ContactsFragment;
import com.ukmessenger.gamma.ukmessenger.GroupsFragment;
import com.ukmessenger.gamma.ukmessenger.R;

public class TabsAdapter extends FragmentPagerAdapter{
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            case 2:
                ContactsFragment contactsFragment= new ContactsFragment();
                return contactsFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Chats";
            case 1: return "Grupos";
            case 2: return  "Contactos";
            default: return null;
        }

    }

    private String getString(int id){
        return Resources.getSystem().getString(id);
    }
}

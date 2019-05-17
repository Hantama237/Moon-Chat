package com.hantama.climberschat;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

public class MyDiffCallback extends DiffUtil.Callback {

    List<Chat> oldChat;
    List<Chat> newChat;

    public MyDiffCallback(List<Chat> oldChat, List<Chat> newChat) {
        this.oldChat = oldChat;
        this.newChat = newChat;
    }

    @Override
    public int getOldListSize() {
        return oldChat.size();
    }

    @Override
    public int getNewListSize() {
        return newChat.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return oldChat.get(i).getMessage().equals(newChat.get(i1).getMessage());
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return oldChat.get(i).equals(newChat.get(i1));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}

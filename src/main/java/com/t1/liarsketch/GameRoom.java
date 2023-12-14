package com.t1.liarsketch;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class GameRoom {
    private Long roomId;
    private HashMap<String, String> userList = new HashMap<>(6);

    @Builder
    public GameRoom(Long roomId) {
        this.roomId = roomId;
    }

    public void addUser(String sessionId, String nickName) {
        userList.put(sessionId, nickName);
    }

    public void removeUser(String sessionId) {
        userList.remove(sessionId);
    }

    public String getUser(String sessionId) {
        return userList.get(sessionId);
    }

    public int getNumberOfUser() {
        return userList.size();
    }
}

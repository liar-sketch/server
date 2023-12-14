package com.t1.liarsketch;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;

public class UserSessionsUtil {
    HashMap<String, GameRoom> gameRooms;
    HashMap<String, String> sessionAndCodeList;

    @PostConstruct
    private void init() {
        // 세션별 게임방 코드 저장 해쉬맵
        sessionAndCodeList = new HashMap<>();

        // 게임 코드 - 게임방 해쉬맵
        gameRooms = new HashMap<>();
    }

    public void addSessionAndCode(String sessionId, String code) {
        sessionAndCodeList.put(sessionId, code);
    }

    public void removeSessionAndCode(String sessionId) {
        sessionAndCodeList.remove(sessionId);
    }

    public String getGameCode(String sessionId) {
        return sessionAndCodeList.get(sessionId);
    }

    public boolean isContain(String code) {
        return gameRooms.containsKey(code);
    }

    public GameRoom getGameRoom(String code) {
        return gameRooms.get(code);
    }

    public void addGameRoom(String code, GameRoom gameRoom) {
        gameRooms.put(code, gameRoom);
    }

    public void removeGameRoom(String code) {
        gameRooms.remove(code);
    }

}

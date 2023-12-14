package com.t1.liarsketch;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final UserSessionsUtil userSessionsUtil;


    @EventListener(SessionConnectEvent.class)
    public void onConnect(SessionConnectEvent event) {
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();
        String nickName = event.getMessage().getHeaders().get("nativeHeaders").toString().split("\\{nickName=\\[")[1].split("]")[0];
        System.out.println(sessionId + " - " + nickName + " 님이 연결 되었습니다." );
    }


    @EventListener(SessionDisconnectEvent.class)
    public void onDisConnect(SessionDisconnectEvent event) {
        System.out.println(event);
        String sessionId = event.getMessage().getHeaders().get("simpSessionId").toString();

        // 1. 세션 id로 room code가져오기
        String code = userSessionsUtil.getGameCode(sessionId);

        // 2. 코드로 게임룸 가져오기
        GameRoom gameRoom = userSessionsUtil.getGameRoom(code);

        // 3-1. 해당 클라이언트가 접속한 gameRoom에서 user삭제
        String nickName = gameRoom.getUser(sessionId);
        gameRoom.removeUser(sessionId);

        // 3-2. 해당 클라이언트와 방코드 맵핑 정보 삭제
        userSessionsUtil.removeSessionAndCode(sessionId);

        // 4. 해당 gameRoom에 인원이 0명 일 경우 게임룸리스트에서 게임방  삭제
        System.out.println(code + " 게임방 남은 인원 = " + gameRoom.getNumberOfUser() + "명 입니다.");
        if (gameRoom.getNumberOfUser() == 0) {
            userSessionsUtil.removeGameRoom(code);
            System.out.println(code + " 게임방이 게임방목록에서 제거 되었습니다.");
        }

        System.out.println(sessionId + " - " + nickName + " 님이 나갔습니다." );
    }


    @MessageMapping("/room/{roomId}/chat")
    @SendTo("/sub/room/{roomId}/chat")
    public ChatMessage chat(@DestinationVariable Long roomId, ChatMessage chatMessage) {
        System.out.println(roomId);
        System.out.println(chatMessage.getNickName());
        System.out.println(chatMessage.getMsg());
        return ChatMessage.builder()
                        .roomId(roomId)
                        .nickName(chatMessage.getNickName())
                        .msg(chatMessage.getMsg())
                        .build();
    }

}

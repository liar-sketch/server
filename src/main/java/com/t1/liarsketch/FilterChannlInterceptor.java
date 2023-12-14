package com.t1.liarsketch;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;



import java.util.HashMap;

@RequiredArgsConstructor
public class FilterChannlInterceptor implements ChannelInterceptor {
//    HashMap<String, GameRoom> gameRooms = new HashMap<>();
    private final UserSessionsUtil userSessionsUtil;

    public static boolean isFourDigitNumber(String str) {
        if (str.matches("[0-9]+")) {
            return str.length() == 4;
        }
        return false;
    }


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (headerAccessor.getCommand() == StompCommand.CONNECT) {
            String nickName = String.valueOf(headerAccessor.getNativeHeader("nickName")).replaceAll("[\\[\\]]", "");
            String code = String.valueOf(headerAccessor.getNativeHeader("code")).replaceAll("[\\[\\]]", "");
            String sessionId = headerAccessor.getSessionId();
            System.out.println(headerAccessor);
            System.out.println(headerAccessor.getSessionId());


            // 0. code가 숫자 4자리 인지 판단
            if (!isFourDigitNumber(code)) {
                System.out.println(">>>>>>>>>>>>>>>>>> 코드가 유효하지 않습니다. ::: " + code);
                throw new MessageDeliveryException("코드가 유효하지 않습니다.");
            }

            // 1. 게임방이 있는지 없는지 확인
            if (userSessionsUtil.isContain(code)) {
                // 방이 이미 존재하면 해방 방에 인원이 가득 찼는지 아닌지 확인
                GameRoom gameRoom = userSessionsUtil.getGameRoom(code);
                if (gameRoom.getNumberOfUser() < 6) {
                    gameRoom.addUser(sessionId, nickName);
                    userSessionsUtil.addSessionAndCode(sessionId, code);
                    System.out.println(">>>> 현재인원 ::: " + gameRoom.getNumberOfUser());
                } else {
                    System.out.println(">>>>>>>>>>>>>>>>>> 인원이 가득 찼습니다.");
                    throw new MessageDeliveryException("인원이 가득 찼습니다.");
                }

            } else {
                // 방이 존재하지 않으면 방을 생성해서
                GameRoom gameRoom = GameRoom.builder()
                        .roomId(Long.parseLong(code))
                        .build();
                // 유저를 넣고
                gameRoom.addUser(sessionId, nickName);
                System.out.println(gameRoom.getNumberOfUser());
                // 해쉬테이블에 넣는다.
                userSessionsUtil.addGameRoom(code, gameRoom);
                userSessionsUtil.addSessionAndCode(sessionId, code);
            }

            System.out.println("<< ================= 연결되기전 =================");
            System.out.println("<< ================= 연결되기전 =================");
        }


        return message;
    }

}

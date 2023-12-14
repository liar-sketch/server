package com.t1.liarsketch;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;


// 이런 것도 있다..
// 2023.12.14 현재 사용 안하는 중
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            System.out.println(">>>>>>>>>>>>>>>> beforeHandshake");
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletServerHttpRequest.getServletRequest().getSession();
            attributes.put(SESSION, session);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        System.out.println("<<<<<<<<<<<<<<<<<<< afterHandshake");
        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
        HttpSession session = servletServerHttpRequest.getServletRequest().getSession();
        System.out.println(session.getAttribute(SESSION));
    }
}

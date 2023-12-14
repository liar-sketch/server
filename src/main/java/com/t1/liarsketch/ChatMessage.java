package com.t1.liarsketch;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private Long roomId;
    private  String nickName;
    private  String msg;
}

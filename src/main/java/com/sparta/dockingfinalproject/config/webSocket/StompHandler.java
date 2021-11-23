package com.sparta.dockingfinalproject.config.webSocket;

import com.sparta.dockingfinalproject.exception.DockingException;
import com.sparta.dockingfinalproject.exception.ErrorCode;
import com.sparta.dockingfinalproject.security.jwt.JwtReturn;
import com.sparta.dockingfinalproject.security.jwt.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  public StompHandler(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    if (accessor.getCommand() == StompCommand.CONNECT) {
      if (isTokenSuccess(accessor)) {
        throw new DockingException(ErrorCode.LOGIN_REQUIRED);
      }
    }
    return message;
  }

  private boolean isTokenSuccess (StompHeaderAccessor accessor){
    return jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("token"))!= JwtReturn.SUCCESS;
  }
}

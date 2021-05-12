/*
 * Original Author -> 杨海健 (taketoday@foxmail.com) https://taketoday.cn
 * Copyright © TODAY & 2017 - 2021 All Rights Reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/]
 */

package cn.taketoday.web.socket;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

/**
 * Standard javax.websocket.Session WebSocketSession
 *
 * @author TODAY 2021/4/5 14:25
 * @since 3.0
 */
public class StandardWebSocketSession extends NativeWebSocketSession {

  @Override
  public Session obtainNativeSession() {
    return (Session) super.obtainNativeSession();
  }

  @Override
  public void sendText(String text) throws IOException {
    obtainNativeSession().getBasicRemote().sendText(text);
  }

  @Override
  public void sendPartialText(String partialMessage, boolean isLast) throws IOException {
    obtainNativeSession().getBasicRemote().sendText(partialMessage, isLast);
  }

  @Override
  public void sendBinary(BinaryMessage data) throws IOException {
    obtainNativeSession().getBasicRemote().sendBinary(data.getPayload());
  }

  @Override
  public void sendPartialBinary(ByteBuffer partialByte, boolean isLast) throws IOException {
    obtainNativeSession().getBasicRemote().sendBinary(partialByte, isLast);
  }

  @Override
  public void sendPing(PingMessage message) throws IOException {
    obtainNativeSession().getBasicRemote().sendPing(message.getPayload());
  }

  @Override
  public void sendPong(PongMessage message) throws IOException {
    obtainNativeSession().getBasicRemote().sendPong(message.getPayload());
  }

  @Override
  public int getMaxBinaryMessageBufferSize() {
    return obtainNativeSession().getMaxBinaryMessageBufferSize();
  }

  @Override
  public int getMaxTextMessageBufferSize() {
    return obtainNativeSession().getMaxTextMessageBufferSize();
  }

  @Override
  public long getMaxIdleTimeout() {
    return obtainNativeSession().getMaxIdleTimeout();
  }

  @Override
  public void setMaxBinaryMessageBufferSize(int max) {
    obtainNativeSession().setMaxBinaryMessageBufferSize(max);
  }

  @Override
  public void setMaxIdleTimeout(long timeout) {
    obtainNativeSession().setMaxIdleTimeout(timeout);
  }

  @Override
  public void setMaxTextMessageBufferSize(int max) {
    obtainNativeSession().setMaxTextMessageBufferSize(max);
  }

  @Override
  public boolean isSecure() {
    return obtainNativeSession().isSecure();
  }

  @Override
  public boolean isOpen() {
    return obtainNativeSession().isOpen();
  }

  @Override
  public void close() throws IOException {
    obtainNativeSession().close();
  }

  @Override
  public void close(final CloseStatus status) throws IOException {
    final CloseReason closeReason = new CloseReason(
            CloseCodes.getCloseCode(status.getCode()), status.getReason());
    obtainNativeSession().close(closeReason);
  }

}

package com.lprevidente.permissio.entity;

public interface Handler<HandlerId> {

  <T extends HandlerEntity<HandlerId, ?>> T getHandler();
}

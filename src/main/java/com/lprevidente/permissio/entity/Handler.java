package com.lprevidente.permissio.entity;


public interface Handler<ID> {

  <T extends HandlerEntity<ID>> T getHandler();
}

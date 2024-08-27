package com.lprevidente.permissio.entity;


public interface Handler {

  <T extends HandlerEntity> T getHandler();
}

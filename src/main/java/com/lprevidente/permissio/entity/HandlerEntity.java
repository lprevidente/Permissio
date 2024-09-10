package com.lprevidente.permissio.entity;

public interface HandlerEntity<HandlerId, TypeId> {

  HandlerId getHandlerId();

  TypeId getType();
}

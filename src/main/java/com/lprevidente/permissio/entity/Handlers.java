package com.lprevidente.permissio.entity;

import java.util.Collection;

public interface Handlers<ID> {

  <T extends HandlerEntity<ID>> Collection<T> getHandlers();
}

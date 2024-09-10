package com.lprevidente.permissio.entity;

import java.util.Collection;

public interface Handlers<HandlerId> {

  <T extends HandlerEntity<HandlerId, ?>> Collection<T> getHandlers();
}

package com.lprevidente.permissio.entity;

import java.util.Collection;

public interface Handlers {

  <T extends HandlerEntity> Collection<T> getHandlers();
}

package com.mail.backend.Managers;

import java.util.List;
import java.util.Map;

public interface ManagerInterface<T> {
    public T get(Object id);
    public Map<Object,T> getAll();
    public void add(T t);
    public void remove(Object id);
}

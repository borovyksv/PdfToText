package com.borovyksv.mongo.observer;

public interface Observer<T> {
  void update(T converter);
}

package com.borovyksv.mongo.observer;

public interface Observable {

  void addObserver(Observer observer);
  void notifyAllObservers();

}

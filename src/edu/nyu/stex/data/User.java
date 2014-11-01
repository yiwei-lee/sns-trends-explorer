package edu.nyu.stex.data;

public class User {
  private final String id;
  private final String name;

  public User(String id, String name) {
    this.id = id;
    this.name = name;
  }

  String getId() {
    return id;
  }

  String getName() {
    return name;
  }
}

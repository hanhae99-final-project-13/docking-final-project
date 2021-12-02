package com.sparta.dockingfinalproject.post.pet.model;

public enum Sex {
  M("m"),
  F("f");

  private final String sex;

  Sex(String sex) {
    this.sex = sex;
  }

  public static Sex of(String sex) {
    for (Sex petSex : Sex.values()) {
      if (sex.equalsIgnoreCase(petSex.toString())) {
        return petSex;
      }
    }

    throw new IllegalArgumentException("올바른 성별이 아닙니다.");
  }
}

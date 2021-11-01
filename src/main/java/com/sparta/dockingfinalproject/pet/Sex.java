package com.sparta.dockingfinalproject.pet;

public enum PetSex {
  M("m"),
  F("f");

  private final String sex;

  PetSex(String sex) {
    this.sex = sex;
  }

  public static PetSex of(String sex) {
    for (PetSex petSex : PetSex.values()) {
      if (sex.equalsIgnoreCase(petSex.toString())) {
        return petSex;
      }
    }

    throw new IllegalArgumentException("올바른 성별이 아닙니다.");
  }
}

package com.sparta.dockingfinalproject.pet;

public enum IsAdopted {
  abandoned("abandoned"),
  adopted("adopted");

  private final String isAdopted;

  IsAdopted(String isAdopted) {
    this.isAdopted = isAdopted;
  }

  public static IsAdopted of(String isAdopted) {
    for (IsAdopted isAdopt : IsAdopted.values()) {
      if (isAdopted.equalsIgnoreCase(isAdopt.toString())) {
        return isAdopt;
      }
    }
    throw new IllegalArgumentException("올바른 보호상태가 아닙니다.");
  }
}

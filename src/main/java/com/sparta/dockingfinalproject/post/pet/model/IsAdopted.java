package com.sparta.dockingfinalproject.post.pet.model;

public enum IsAdopted {
  ABANDONED("abandoned"),
  ADOPTED("adopted"),
  EXPIRED("expired");

  private final String isAdopted;

  IsAdopted(String isAdopted) {
    this.isAdopted = isAdopted;
  }

  public static IsAdopted of(String isAdopted) {
    for (IsAdopted isAdopt : IsAdopted
        .values()) {
      if (isAdopted.equalsIgnoreCase(isAdopt.toString())) {
        return isAdopt;
      }
    }
    throw new IllegalArgumentException("올바른 보호상태가 아닙니다.");
  }
}
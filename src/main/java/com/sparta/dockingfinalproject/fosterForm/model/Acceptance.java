package com.sparta.dockingfinalproject.fosterForm.model;

public enum Acceptance {
  waiting("waiting"),
  accepted("accepted"),
  rejected("rejected");

  private final String acceptance;

  Acceptance(String acceptance) {
    this.acceptance = acceptance;
  }

  public static Acceptance of(String acceptance) {
    for (Acceptance accept : Acceptance.values()) {
      if (acceptance.equalsIgnoreCase(accept.toString())) {
        return accept;
      }
    }

    throw new IllegalArgumentException("accepted 혹은 rejected 가 아닙니다.");
  }
}

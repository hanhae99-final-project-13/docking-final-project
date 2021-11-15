package com.sparta.dockingfinalproject.token;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {
  @Id
  @Column(name = "refresh_token_key")
  private String key;

  @Column
  private String value;

  @Builder
  public RefreshToken(String key, String value) {

    this.key = key;
    this.value = value;
  }


}

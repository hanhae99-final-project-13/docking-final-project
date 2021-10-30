package com.sparta.dockingfinalproject.common;

import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

  @CreatedDate
  @JsonFormat
  private LocalDateTime createdAt;

  @LastModifiedDate
  @JsonFormat
  private LocalDateTime modifiedAt;
}
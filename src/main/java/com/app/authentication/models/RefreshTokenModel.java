package com.app.authentication.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshTokenModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;
  private String refreshToken;
  private LocalDateTime createdAt;
  private LocalDateTime expiredIn;
  private boolean tokenUsed;
  @ManyToOne(targetEntity=UserModel.class, fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private UserModel user;

}

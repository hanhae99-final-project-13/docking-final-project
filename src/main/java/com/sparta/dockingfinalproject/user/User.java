package com.sparta.dockingfinalproject.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.dockingfinalproject.wish.Wish;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column
    private String badge;


    @Column(unique = true)
    private Long kakaoId;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<Wish> wishList;

    public User(String username, String password, String nickname, String email){

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.kakaoId = null;
    }

    public User(String username, String password, String nickname, String email, Long kakaoId){

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.kakaoId = kakaoId;

    }


    public User(String password, String nickname, String email, Long kakaoId){

        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.kakaoId = kakaoId;

    }
}

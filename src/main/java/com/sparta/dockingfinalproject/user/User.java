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
    private Long userId;

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

    //추가부분
    @Column
    private String userImgUrl;


    @Column(unique = true)
    private Long kakaoId;

    @Column(nullable = false)
    private String authKey;

    @Column(nullable = false)
    private boolean authCheck;



    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<Wish> wishList;

    public User(String username, String password, String nickname, String email, String userImgUrl, String authKey){

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.userImgUrl = userImgUrl;
        this.kakaoId = null;
        this.authKey = authKey;
        this.authCheck = false;

    }

    public User(String username, String password, String nickname, String email, Long kakaoId, String userImgUrl){

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.kakaoId = kakaoId;
        this.userImgUrl = userImgUrl;
        this.authKey = "";
        this.authCheck = true;


    }


    public User(String password, String nickname, String email, Long kakaoId){

        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.kakaoId = kakaoId;
        this.authKey = "";
        this.authCheck = true;

    }

    public User confirm(){
        this.authKey ="";
        this.authCheck = true;
        return this;

    }
}

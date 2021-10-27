package com.sparta.dockingfinalproject.user;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String badge;


    public User(String username, String password, String nickname, String email){

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;

    }
}

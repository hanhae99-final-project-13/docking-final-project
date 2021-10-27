package com.sparta.dockingfinalproject.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.dockingfinalproject.wish.Wish;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<Wish> wishList;

    public User(String username, String password, String nickname, String email){

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;

    }
}

package com.sparta.dockingfinalproject.alarm;


import com.sparta.dockingfinalproject.common.Timestamped;
import com.sparta.dockingfinalproject.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long alarmId;

    @Column(nullable = false)
    private String alarmContent;

    @Column(nullable = false)
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public void addUser(User user) {
        this.user = user;
    }

    public void updateStatus() {
        this.status = false;
    }

    public Alarm(String alarmContent){

        this.alarmContent = alarmContent;
        this.status = false;
    }
}

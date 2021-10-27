package com.sparta.dockingfinalproject.alarm;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int alarmId;

    @Column(nullable = false)
    private String alarmContent;

    @Column(nullable = false)
    private boolean status;


}

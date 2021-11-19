package com.sparta.dockingfinalproject;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DockingFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockingFinalProjectApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("애플리케이션이 정상 실행 되었습니다.");
    }

}

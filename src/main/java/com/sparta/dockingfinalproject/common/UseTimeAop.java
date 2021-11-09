package com.sparta.dockingfinalproject.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UseTimeAop {
  private static final Logger logger = LoggerFactory.getLogger(UseTimeAop.class);

  @Around("execution(public * com.sparta.dockingfinalproject..*Controller..*(..))")
  public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

    long startTime = System.currentTimeMillis();

    try {
      Object output = joinPoint.proceed();
      return output;
    } finally {
      long endTime = System.currentTimeMillis();
      long runTime = endTime - startTime;

      logger.info("   [[   APIUseTime   >>>   "+ runTime +"   ]]   ");
    }
  }
}
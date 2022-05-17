package com.dih008.dihel.aspects;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceAspect {

	@Before("execution(* com.dih008.dihel.services.*.*(..))")
	public void logService(JoinPoint joinPoint) throws Throwable {
		System.out.println("Logging");
		System.out.println(joinPoint.getSignature());
		System.out.println(Arrays.toString(joinPoint.getArgs()));
	}
}

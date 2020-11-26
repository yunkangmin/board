//aop를 적용할 실제 Advice 클래스를 작성한다.
package org.tams.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//Advice 클래스로 사용되기 위해 @Aspect를 추가하며
//스프링의 빈으로 인식되기 위해 @Component을 선언한다.
@Component
@Aspect
public class SampleAdvice {

  private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
  
  //target 메소드 실행전 실행됨.
  //Pointcut으로 org.project.tams.MessageService로 시작하는 모든 클래스의 모든 메소드를 지정한다.
  //설정이 올바르게 되었다면 메소드 앞에 화살표 아이콘이 표시된다.
  //MessageServiceImpl에 화살표 아이콘이 모든 메소드에 표시되는 것을 확인할 수 있다.
  //@Before("execution(* org.tams.service.MessageService*.*(..))")
  //JoinPoint를 사용하여 전달되는 모든 파라미터를 확인할 수 있다.
  public void startLog(JoinPoint jp) {

    logger.info("----------------------------");
    logger.info("----------------------------");
    //getArgs()는 전달되는 모든 파라미터들을 Object 배열로 가져온다.
    logger.info(Arrays.toString(jp.getArgs()));

  } 
  
  //Around는 파라미터로 ProceedingJoinPoint을 사용한다.
  //ProceedingJoinPoint는 JoinPoint의 하위 인터페이스이다.
  //Exception보다 상위인 Throwable을 처리해야 한다.
  //리턴타입은 반드시 Object로 사용해야 한다.
  //Around는 메소드를 직접 호출하고 결과를 반환해야 한다.
  @Around("execution(* org.tams.service.MessageService*.*(..))")
  public Object timeLog(ProceedingJoinPoint pjp)throws Throwable{
    
    long startTime = System.currentTimeMillis();
    logger.info(Arrays.toString(pjp.getArgs()));
    
    //proceed()는 직접 target 메소드를 실행한다.
    Object result = pjp.proceed();
    
    long endTime = System.currentTimeMillis();
    //getSignature()는 target 메소드에 대한 정보를 알아낼 때 사용한다.
    //getSignature().getName()은 메소드 이름을 알아낼 때 사용한다.
    logger.info( pjp.getSignature().getName()+ " : " + (endTime - startTime) );
    logger.info("=============================================");
    
    return result;
  }   

}

package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }

        /**
         * joinPoint.getArgs{}[0] 과 같이 매개변수 전달 받음
         */
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1] {}, arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        /**
         * arg(arg,..) 와 같이 매개변수 전달 받음
         */
        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2] {}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        /**
         * @Before 를 사용한 축약버전
         */
        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) { // 타입을 String 으로 제한함
            log.info("[logArgs3] arg={}", arg);
        }

        /**
         * this 프록시 객체를 전달받음
         */
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * target 실제 대상 객체 전달받음
         */
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * @target 타입의 어노테이션을 전달받음
         */
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target] {}, obj={}", joinPoint.getSignature(), annotation);
        }

        /**
         * @within 타입의 어노테이션을 전달받음
         */
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within] {}, obj={}", joinPoint.getSignature(), annotation);
        }

        /**
         * @annotation 메소드의 어노테이션을 전달 받음
         */
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            // annotation.value() : 해당 어노테이션의 값을 출력
            log.info("[@annotation] {}, annotationValue={}", joinPoint.getSignature(), annotation.value());
        }
    }
}
// 포인트컷 표현식을 사용해서 어드바이스에 매개변수를 전달 가능
// this, target, args, @target, @within, @annotation, @args
// 포인트컷 이름과 매개변수 이름을 맞추어야한다.
// 타입이 메소드에 지정한 타입으로 제한된다.

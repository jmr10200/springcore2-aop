package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(AtTargetAtWithinTest.Config.class)
@SpringBootTest
public class AtTargetAtWithinTest {

    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child Proxy={}", child.getClass());
        log.info("==== child.childMethod() ====");
        child.childMethod(); // 부모, 자식 모두 잇는 메소드
        log.info("==== child.parentMethod() ====");
        child.parentMethod(); // 부모 클래스에만 있는 메소드
        // parentMethod() 는 Parent 클래스 정의 O, Child 클래스 정의 X 이므로
        // @within 에서 AOP 적용 대상이 되지 않는다.
    }

    static class Config {

        @Bean
        public Parent parent() {
            return new Parent();
        }

        @Bean
        public Child child() {
            return new Child();
        }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }

    static class Parent {
        public void parentMethod() {
        } // 부모에만 있는 메소드
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod() {
        }
    }

    /**
     * @target : 실행 객체의 클래스에 주어진 타입의 어노테이션이 있는 조인포인트
     * @within : 주어는 어노테이션이 있는 타입 내 조인 포인트
     *
     * 타입이 있는 어노테이션으로 AOP 적용여부를 판단한다.
     *
     * @target @within 지시자는 파라미터 바인딩에서 함께 사용
     */
    @Aspect
    static class AtTargetAtWithinAspect {

        // @target : 인스턴스 기준으로 모든 메소드의 조인포인트를 선정, 부모타입 메소드 적용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // @within : 선택된 클래스 내부에 있는 메소드만 조인포인트로 선정, 부모타임의 메소드는 적용 X
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

}
// 주의
// args, @args, @target : 단독 사용 X
// -> 실제 객체 인스턴스가 생성되고 실행될 때 어드바이스 적용 여부를 확인할 수 있다.
// 실행시점에 시행하는 포인트컷 적용 여부도 프록시가 있어야 실행시점에 판단할 수 있다.
// 스프링 컨테이너가 프록시를 생성하는 시점은 스프링 컨테이너가 만들어지는 어플이케이션 로딩시점이다.
// 따라서 args, @args, @target 포인트컷 지시자는 스프링이 모든 스프링빈에 AOP 를 적용하려고 시도한다.
// 문제는 이렇게 모든 스프링빈에 AOP 를 적용하려고 하는 시점에서 발생한다.
// 스프링이 내부에서 사용하는 빈 중에는 final 로 선언된 빈들도 있어 오류가 발생할 수 있다.
// 따라서 이러한 표현식은 최대한 프록시 적용 대상을 축소하는 표현식과 함께 사용해야 한다.
package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    // hello.aop.order 패키지와 하위 패키지
    @Around("execution(* hello.aop.order..*(..))") // 포인트컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable { // 어드바이스
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
    // 위 설정으로 hello.aop.order 패키지와 하위패키지의 모든 메소드는 AOP 적용 대상이 된다.
}
/* 참고 */
// 스프링 AOP 는 AspectJ 의 문법을 차용, 프록시 방식의 AOP 제공 (AspectJ 직접사용 아니다)
// @AspectJ 어노테이션도 패키지명을 보면 AspectJ 가 제공하는 것을 알 수 있다.
// org.aspectj 패키지 관련 기능은 aspectjweaver.jar 라이브러리가 제공한다.(spring-boot-starter-aop)
// 스프링에서는 AspectJ 제공 어노테이션이나 인터페이스만 사용하는 것이다. (컴파일, 로드타임 위버등 X)


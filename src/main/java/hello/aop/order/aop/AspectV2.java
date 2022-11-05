package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 { // AspectV1 과 같은 기능

    // hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))") // pointcut expression
    private void allOrder() { // pointcut signature
        // @Pointcut 에 포인트컷 표현식 지정한다.
        // 메소드명 + 파라미터 = 포인트컷 시그니처 ( 여기서는 allOrder() )
        // 메소드 리턴 타입 : void
    }

    @Around("allOrder()") // 포인트컷 시그니처를 사용해도 OK
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }
    // 이렇게 분리하면 하나의 포인트컷 표현식을 여러 어드바이스에서 함께 사용할 수 있다.
    // 또한 다른 클래스에 있는 외부 어드바이스에서도 포인트컷을 함께 사용할 수 있다.
}

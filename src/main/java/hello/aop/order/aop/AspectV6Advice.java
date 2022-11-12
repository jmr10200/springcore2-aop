package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    // 메소드 호출 전후 수행. 가장 강력한 어드바이스
    // 조인 포인트 실행 여부 선택, 전달 값 변환, 반환 값 변환, 예외 변환 등이 가능
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // @Before
            log.info("[around][트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            // @AfterReturning
            log.info("[around][트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            // @AfterThrowing
            log.info("[around][트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            // @After
            log.info("[around][리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }
    // @Around 의 일부 기능을 제공한다. 따라서 @Around 만으로 필요 기능을 모두 수행할 수 있다.

    // 조인 포인트 실행 이전에 실행
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    // 조인 포인트가 정상 완료된 후 실행
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    // 메소드가 예외를 던지는 경우 실행
    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    // 조인 포인트가 정상 또는 예외에 관계없이 실행 (finally)
    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }

    // 모든 어드바이스는 org.aspectj.lang.JoinPoint 를 첫번째 파라미터에 사용할 수 있다.(생략가능)
    // 단, @Around 는 ProceedingJoinPoint 를 사용해야 한다. (JoinPoint 의 하위타입)
}
/* 실행순서 */
// @Around , @Before , @After , @AfterReturning , @AfterThrowing 순
// 호출순서와 리턴순서는 반대이다.

// @Around 만으로 해결이 되는데, 왜 다른 어드바이스도 존재하는가?
// @Around 는 타겟을 호출하지 않는다. 즉, 항상 joinPoint.proceed() 를 호출해야 한다.
// 그 외에는 호출하지 않아도 된다. @Around 가 가장 넓은 기능을 제공하지만, 실수할 가능성이 존재한다.
// 또한 어노테이션명에서 의도가 명확히 드러난다는 것이 큰 장점이다.

// 좋은 설계란 제약이 있는 것이다. 명확한 의도가 표현되고 역할을 수행하는 것이 좋다.
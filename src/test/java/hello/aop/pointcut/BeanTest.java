package hello.aop.pointcut;

import hello.aop.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(BeanTest.BeanAspect.class)
@Slf4j
@SpringBootTest
public class BeanTest {

    @Autowired
    OrderService orderService;

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    /**
     * bean : 스프링 전용 포인트컷 지시자. 빈의 이름으로 지정
     * 스프링 빈의 이름으로 AOP 적용 여부를 지정한다.
     * 스프링에서만 사용가능한 특별한 지시자
     */
    @Aspect
    static class BeanAspect {
        // *Repository(OrderRepository) 메소드에 AOP 적용
        @Around("bean(orderService) || bean(*Repository)")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[bean] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}

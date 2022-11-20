package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV1Test {

    @Autowired
    CallServiceV1 callServiceV1;

    @Test
    void external() {
        callServiceV1.external();
        // 실행결과
        // CallLogAspect     : aop=void hello.aop.internalcall.CallServiceV1.external()
        // CallServiceV1     : call external
        // CallLogAspect     : aop=void hello.aop.internalcall.CallServiceV1.internal()
        // CallServiceV1     : call internal
        // 클라이언트가 프록시를 호출한다. 따라서 CallLogAspect 어드바이스가 호출된다.
        // 그리고 AOP Proxy 는 target.external() 을 호출한다.
        // external() 은 callService.internal() 외부호출로 CallLogAspect 어드바이스가 호출된다.
        // 그리고 AOP Proxy 는 target.internal() 을 호출한다.
        // 즉, 자신의 인스턴스가 아니라 프록시 인스턴스를 통해서 호출한다.
    }

}
/* 참고 */
// 스프링 부트 2.6부터 순환 참조 금지로 아래와 같은 에러 발생
// Error creating bean with name 'callServiceV1': Requested bean is currently in creation: Is there an unresolvable circular reference?
// 이 문제를 해결하기 위해서는 application.properties 설정
// spring.main.allow-circular-reference=true

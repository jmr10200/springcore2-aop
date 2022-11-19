package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;

    @Test
    void external() {
        callServiceV0.external();
        // 실행 결과
        // CallLogAspect     : aop=void hello.aop.internalcall.CallServiceV0.external()
        // CallServiceV0     : call external
        // CallServiceV0     : call internal
        // 클라이언트가 프록시를 호출한다. 따라서 CallLogAspect 어드바이스가 호출된다.
        // 그리고 AOP Proxy 는 target.external() 을 호출한다.
        // target 안에서 internal() 을 호출하는데, 이때는 CallLogAspect 어드바이스가 호출되지 않는다.

        // java 에서 메소드앞에 별도 참조가 없으면 this 라는 뜻으로 자기자신의 인스턴스를 가르킨다.
        // 즉 자기자신의 내부 메소드를 호출하는 this.internal() 인데, 여기서 this 는 target 이다.
        // 결과적으로 이런 내부 호출은 프록시를 거치지 않으므로 어드바이스도 적용할 수 없다.
    }

    @Test
    void internal() {
        callServiceV0.internal();
        // 실행 결과
        // CallLogAspect     : aop=void hello.aop.internalcall.CallServiceV0.internal()
        // CallServiceV0     : call internal
        // 외부에서 internal() 을 호출하면 프록시를 거치기 때문에 internal() 도 CallLogAspect 어드바이스가 적용된다.
    }

}

/* 프록시 방식의 AOP 한계 */
// 스프링은 프록시 방식의 AOP 를 사용한다.
// 프록시 방식의 AOP 는 메소드 내부 호출에 프록시를 적용할 수 없다.
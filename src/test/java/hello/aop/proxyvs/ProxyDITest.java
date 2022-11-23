package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * JDK 동적 프록시의 한계
 * JDK 동적 프록시를 사용하면 의존관계 주입시 타입문제 발생
 */
@Slf4j
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) // JDK 동적 프록시, DI 예외 발생
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) // CGLIB 프록시, 성공
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService; // JDK 동적 프록시 OK, CGLIB OK
    @Autowired
    MemberServiceImpl memberServiceImpl; // JDK 동적 프록시 X, CGLIB OK

    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
/* JDK 동적 프록시에 구체 클래스 타입 주입 : spring.aop.proxy-target-class=true */
// JDK 동적 프록시에 구체 클래스타입을 주입하면 BeanNotOfRequiredTypeException 발생
// 타입과 관련된 예외가 발생한다.
// 기대값 : MemberServiceImpl
// 실제 : com.sun.proxy.$Proxy56

// JDK 동적 프록시 : 인터페이스 기반으로 생성
// MemberService = JDK Proxy : OK
// MemberServiceImpl = JDK Proxy : NG

/* CGLIB 프록시에 구체 클래스 타입 주입 : spring.aop.proxy-target-class=false */
// CGLIB 프록시에 구체 클래스 타입을 주입하면 타입변환 OK

// CGLIB 프록시 : 구체 클래스 기반으로 생성
// MemberService = CGLIB Proxy : OK
// MemberServiceImpl = CGLIB Proxy : OK

/* 정리 */
// 인터페이스가 있으면 인터페이스를 기반으로 의존관계를 주입받는 것이 맛다.
// DI 의 장점은 DI 받는 클라이언트 코드의 변경 없이 구현 클래스를 변경할 수 있는 것이다.
// 이렇게 하려면 인터페이스를 기반으로 의존관계를 주입 받아야 한다.
// 구체 클래스를 주입받으면 변경이 있을때 의존관계 주입을 받는 클라이언트의 코드도 함께 변경해야한다.
// 그럼에도 불구하고 여러이유로 AOP 프록시가 적용된 구체 클래스를 직접 의존관계에
// 주입받아야 하는 경우가 있을 수 있다. 이때 구체 클래스 기반으로 AOP 프록시를 적용하면 된다.
package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * ObjectProvider(Provider), ApplicationContext 를 사용해서 지연 (LAZY) 조회
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2 {

    //    private final ApplicationContext applicationContext;
    // ObjectProvider : 객체를 스프링 컨테이너에서 조회하는 것을 실제 사용 시점으로 지연할 수 있음
    private final ObjectProvider<CallServiceV2> callServiceV2ObjectProvider;

    public void external() {
        log.info("call external");
//        CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);
        // getObject() 를 호출하는 시점에 스프링 컨테이너에서 빈을 조회함
        // 자기자신을 주입 받는 것이 아니므로 순환 사이클이 발생하지 않음
        CallServiceV2 callServiceV2 = callServiceV2ObjectProvider.getObject();
        callServiceV2.internal(); // 외부 메소드 호출
    }

    public void internal() {
        log.info("call internal");
    }
}

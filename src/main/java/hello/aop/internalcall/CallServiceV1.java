package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 참고 : 생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
 * 수정자(setter) 주입은 스프링이 생성된 이후에 주입할 수 있기 때문에 에러가 발생하지 않음
 */
@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        this.callServiceV1 = callServiceV1; // proxy
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // 외부 메소드 호출
    }

    public void internal() {
        log.info("call internal");
    }

}

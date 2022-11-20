package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 구조를 변경(분리)
 * 내부호출문제의 가장 나은 대안은 내부호출이 발생하지 않도록 구조를 변경하는 것
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {

    private final InternalService internalService;

    public void external() {
        log.info("call external");
        internalService.internal(); // 외부 메소드 호출
        // 내부 호출 자체가 사라지고 callService -> internalService 를 호출 하는 구조
        // 외부 호출을 하므로 자연스럽게 AOP 가 적용된다.
        // 이렇게 단순하게 분리하는 방식도 있지만, 클라이언트에서 각각 호출하는 방식을 생각할 수도 있다.
        // 물론 external() 이 internal() 을 호출하지 않도록 구조변경이 요구된다.
    }
}
// 참고
// AOP 는 주로 트랜젝션 적용이나 주요 컴포넌트의 로그 출력 기능에 사용된다.
// 즐, 인터페이스에 메소드가 나올 정도의 규모에 AOP 를 적용하는 것이 적당하다.
// AOP 는 public 메소드에만 적용한다. private 메소드처럼 작은 단위에는 AOP 를 적용하지 않는다.
// AOP 적용을 위해 private 메소드를 외부 클래스로 변경하고 public 으로 변경하는 일은 거의 없다.
// 그러나 위 예제처럼 public 메소드에서 public 메소드를 내부 호출하는 경우 문제가 발생한다.
// AOP 가 잘 적용되지 않으면 내부호출을 의심해보자.

package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("통합 테스트: Application의 전체 실행 흐름 검증")
class ApplicationTest extends NsTest {

    private static final String ERROR_MESSAGE = "[ERROR]";

    @Test
    void 기능_테스트() {
        // Given: 8000원 구매, 당첨 1,2,3,4,5,6, 보너스 7
        // When: 랜덤 번호 목록을 주입하여 실행
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run("8000", "1,2,3,4,5,6", "7"); // 구입금액, 당첨번호, 보너스번호 입력

                    // Then: 당첨 통계와 수익률을 검증
                    assertThat(output()).contains(
                            "8개를 구매했습니다.",
                            // (주입된 랜덤 번호에 따라 달라지는 출력)
                            "3개 일치 (5,000원) - 1개",
                            "4개 일치 (50,000원) - 0개",
                            "5개 일치 (1,500,000원) - 0개",
                            "5개 일치, 보너스 볼 일치 (30,000,000원) - 0개",
                            "6개 일치 (2,000,000,000원) - 0개",
                            "총 수익률은 62.5%입니다." // 5000 / 8000 = 0.625
                    );
                },
                // 8장의 로또 번호 주입 (첫 번째 로또가 3개 일치해야 62.5% 나옴)
                List.of(1, 2, 3, 40, 41, 42), // 3개 일치 (5등)
                List.of(8, 21, 23, 41, 42, 43),
                List.of(3, 5, 11, 16, 32, 38),
                List.of(7, 11, 16, 35, 36, 44),
                List.of(1, 8, 11, 31, 41, 42),
                List.of(13, 14, 16, 38, 42, 45),
                List.of(7, 11, 30, 40, 42, 43),
                List.of(2, 13, 22, 32, 38, 45) // 총 8개
        );
    }

    @Test
    @DisplayName("구입 금액이 숫자가 아닐 때 재입력 후 정상 진행되는지 확인")
    void 예외_테스트_재입력_후_정상_진행() {
        assertSimpleTest(() -> {
            // 1. 비정상 입력 ("1000j") -> [ERROR] 출력 후 재입력 대기
            // 2. 정상 입력 ("2000")
            // 3. 당첨 번호 ("1,2,3,4,5,6")
            // 4. 보너스 번호 ("7")
            run("1000j", "2000", "1,2,3,4,5,6", "7");

            // Then: 에러 메시지가 출력되고, 최종적으로 2개가 구매되었는지 확인
            assertThat(output()).contains(ERROR_MESSAGE);
            assertThat(output()).contains("2개를 구매했습니다.");
        });
    }

    @Override
    public void runMain() {
        // main 메서드는 여기서 호출만 담당합니다.
        Application.main(new String[]{});
    }
}
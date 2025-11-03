package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Rank Enum 테스트")
class RankTest {

    @DisplayName("일치 개수와 보너스 일치 여부에 따라 정확한 등수를 반환한다.")
    @ParameterizedTest(name = "일치 {0}개, 보너스 {1} -> {2}")
    @CsvSource(value = {
            "6, false, FIRST",
            "5, true, SECOND",
            "5, false, THIRD",
            "4, false, FOURTH",
            "3, false, FIFTH",
            "2, false, MISS",
            "0, true, MISS"
    })
    void checkValueOf(int matchCount, boolean matchBonus, Rank expectedRank) {
        Rank actualRank = Rank.valueOf(matchCount, matchBonus);
        assertThat(actualRank).isEqualTo(expectedRank);
    }

    @DisplayName("각 등수가 정확한 상금 출력 메시지를 반환하는지 확인한다.")
    @ParameterizedTest(name = "{0}의 메시지 확인")
    @EnumSource(value = Rank.class, names = {"FIRST", "SECOND", "FIFTH"})
    void checkDisplayMessage(Rank rank) {
        // 1등
        if (rank == Rank.FIRST) {
            assertThat(rank.getDisplayMessage()).isEqualTo("6개 일치 (2,000,000,000원)");
        }
        // 2등 (보너스 포함)
        if (rank == Rank.SECOND) {
            assertThat(rank.getDisplayMessage()).isEqualTo("5개 일치, 보너스 볼 일치 (30,000,000원)");
        }
        // 5등 (상금 포맷 확인)
        if (rank == Rank.FIFTH) {
            assertThat(rank.getDisplayMessage()).isEqualTo("3개 일치 (5,000원)");
        }
    }
}

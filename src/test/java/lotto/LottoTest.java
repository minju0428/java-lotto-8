package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("로또 도메인 테스트")
class LottoTest {

    @DisplayName("로또 번호의 개수가 6개가 넘어가면 예외가 발생한다.")
    @Test
    void createLottoByOverSize() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호는 6개여야 합니다.");
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void createLottoByDuplicatedNumber() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호는 중복될 수 없습니다.");
    }

    @DisplayName("로또 번호가 1~45 범위를 벗어나면 예외가 발생한다.")
    @Test
    void createLottoByInvalidRange() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
    }

    @DisplayName("당첨 번호와 보너스 번호를 기준으로 올바른 등수를 반환하는지 확인한다.")
    @ParameterizedTest(name = "일치 개수 {0}, 보너스 {1}, 예상 등수 {2}")
    @MethodSource("provideLottoRankArguments")
    void checkDetermineRank(List<Integer> myNumbers, int bonusNumber, Rank expectedRank) {
        // Given
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6)); // 당첨 번호 고정
        Lotto myLotto = new Lotto(myNumbers);

        // When
        Rank actualRank = myLotto.determineRank(winningLotto, bonusNumber);

        // Then
        assertThat(actualRank).isEqualTo(expectedRank);
    }

    static Stream<Arguments> provideLottoRankArguments() {
        return Stream.of(
                // 1등 (6개 일치)
                Arguments.of(List.of(1, 2, 3, 4, 5, 6), 7, Rank.FIRST),
                // 2등 (5개 일치 + 보너스 7)
                Arguments.of(List.of(1, 2, 3, 4, 5, 7), 7, Rank.SECOND),
                // 3등 (5개 일치, 보너스 X)
                Arguments.of(List.of(1, 2, 3, 4, 5, 10), 7, Rank.THIRD),
                // 4등 (4개 일치)
                Arguments.of(List.of(1, 2, 3, 4, 10, 11), 7, Rank.FOURTH),
                // 5등 (3개 일치)
                Arguments.of(List.of(1, 2, 3, 10, 11, 12), 7, Rank.FIFTH),
                // 꽝 (2개 일치)
                Arguments.of(List.of(1, 2, 10, 11, 12, 13), 7, Rank.MISS)
        );
    }
}
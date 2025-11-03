package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.EnumMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("통계 계산 테스트")
class StatisticsTest {

    // LottoGameController에 있던 통계 계산 로직을 테스트하기 위해
    // Controller 메서드를 복사하거나, 별도의 유틸리티 클래스로 분리해야 합니다.
    // 여기서는 Controller의 calculateStatistics 로직을 기반으로 테스트 데이터를 만듭니다.

    private LottoResult calculateTestStatistics(List<Lotto> myLottos, Lotto winningLotto, int bonusNumber, int purchasePrice) {
        Map<Rank, Integer> rankCounts = new EnumMap<>(Rank.class);
        for (Rank rank : Rank.values()) {
            if (rank != Rank.MISS) {
                rankCounts.put(rank, 0);
            }
        }

        long totalPrize = 0;
        for (Lotto myLotto : myLottos) {
            Rank rank = myLotto.determineRank(winningLotto, bonusNumber);
            if (rank != Rank.MISS) {
                rankCounts.put(rank, rankCounts.get(rank) + 1);
                totalPrize += rank.getPrize();
            }
        }

        double returnRate = ((double) totalPrize / purchasePrice) * 100.0;
        return new LottoResult(rankCounts, returnRate);
    }

    @DisplayName("다양한 등수와 꽝을 포함한 통계가 정확하게 계산되는지 확인한다.")
    @Test
    void checkFullStatisticsCalculation() {
        // Given: 당첨 및 보너스 번호 설정
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        int bonusNumber = 7;
        int purchasePrice = 4000; // 4장 구매 가정

        // Given: 테스트용 구매 로또 (1등, 2등, 4등, 5등)
        List<Lotto> myLottos = new ArrayList<>();
        myLottos.add(new Lotto(List.of(1, 2, 3, 4, 5, 6)));      // 1등 (6개 일치)
        myLottos.add(new Lotto(List.of(1, 2, 3, 4, 5, 7)));      // 2등 (5개 + 보너스)
        myLottos.add(new Lotto(List.of(1, 2, 3, 4, 10, 11)));    // 4등 (4개 일치)
        myLottos.add(new Lotto(List.of(10, 20, 30, 40, 41, 42)));// 꽝 (0개 일치)

        // When
        LottoResult result = calculateTestStatistics(myLottos, winningLotto, bonusNumber, purchasePrice);
        Map<Rank, Integer> rankCounts = result.getRankCounts();

        // Then 1: 당첨 횟수 검증
        assertThat(rankCounts.get(Rank.FIRST)).isEqualTo(1);
        assertThat(rankCounts.get(Rank.SECOND)).isEqualTo(1);
        assertThat(rankCounts.get(Rank.THIRD)).isEqualTo(0);
        assertThat(rankCounts.get(Rank.FOURTH)).isEqualTo(1);
        assertThat(rankCounts.get(Rank.FIFTH)).isEqualTo(0);
    }

    @DisplayName("총 수익률이 소수점 첫째 자리까지 정확하게 계산되는지 확인한다.")
    @Test
    void checkReturnRateCalculation() {
        // Given
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        int bonusNumber = 7;
        int purchasePrice = 8000; // 8장 구매

        // Given: 테스트용 구매 로또 (5등 1개, 꽝 7개)
        List<Lotto> myLottos = new ArrayList<>();
        myLottos.add(new Lotto(List.of(1, 2, 3, 10, 11, 12))); // 5등 (5,000원)
        for (int i = 0; i < 7; i++) {
            myLottos.add(new Lotto(List.of(40 + i, 41, 42, 43, 44, 45))); // 꽝
        }

        // When
        LottoResult result = calculateTestStatistics(myLottos, winningLotto, bonusNumber, purchasePrice);

        // Then 2: 수익률 검증 (5000원 / 8000원 = 0.625 * 100 = 62.5%)
        assertThat(result.getReturnRate()).isEqualTo(62.5);
    }
}
package lotto;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;

public class OutputView {

    // 1. 구매 내역 출력
    public void printMyLottos(int count, List<Lotto> lottos) {
        System.out.println(String.format("\n%d개를 구매했습니다.", count));
        for (Lotto lotto : lottos) {
            System.out.println(lotto.toString());
        }
    }

    // 2. 통계 출력
    public void printStatistics(LottoResult result) {
        System.out.println("\n당첨 통계");
        System.out.println("---");

        printRankCounts(result.getRankCounts());
        printReturnRate(result.getReturnRate());
    }

    // 통계 출력을 15라인 미만으로 분리
    private void printRankCounts(Map<Rank, Integer> rankCounts) {
        // 5등부터 1등 순으로 출력을 위한 리스트
        List<Rank> displayOrder = List.of(
                Rank.FIFTH, Rank.FOURTH, Rank.THIRD, Rank.SECOND, Rank.FIRST
        );

        for (Rank rank : displayOrder) {
            String message = rank.getDisplayMessage();
            int count = rankCounts.getOrDefault(rank, 0);
            System.out.printf("%s - %d개%n", message, count);
        }
    }

    // 수익률 출력을 15라인 미만으로 분리
    private void printReturnRate(double returnRate) {
        System.out.printf("총 수익률은 %.1f%%입니다.%n", returnRate);
    }
}
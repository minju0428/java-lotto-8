// LottoResult.java (신규 생성)
package lotto;

import java.util.Map;

public class LottoResult {
    private final Map<Rank, Integer> rankCounts;
    private final double returnRate;

    public LottoResult(Map<Rank, Integer> rankCounts, double returnRate) {
        this.rankCounts = rankCounts;
        this.returnRate = returnRate;
    }

    public Map<Rank, Integer> getRankCounts() {
        return rankCounts;
    }

    public double getReturnRate() {
        return returnRate;
    }
}

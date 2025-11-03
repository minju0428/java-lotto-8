package lotto;

import java.text.NumberFormat;
import java.util.Locale;

public enum Rank {
    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    MISS(0, false, 0);

    private final int matchCount;
    private final boolean matchBonus;
    private final int prize;

    Rank(int matchCount, boolean matchBonus, int prize) {
        this.matchCount = matchCount;
        this.matchBonus = matchBonus;
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }

    public static Rank valueOf(int matchCount, boolean matchBonus) {
        if (matchCount == 6) {
            return FIRST;
        }
        if (matchCount == 5 && matchBonus) {
            return SECOND;
        }
        if (matchCount == 5) {
            return THIRD;
        }
        if (matchCount == 4) {
            return FOURTH;
        }
        if (matchCount == 3) {
            return FIFTH;
        }
        return MISS;
    }

    // 당첨 통계 출력용 메시지 생성 (Req 3: 15라인 미만)
    public String getDisplayMessage() {
        // NumberFormat을 사용하여 콤마가 포함된 문자열을 만듭니다.
        String formattedPrize = NumberFormat.getInstance(Locale.KOREA).format(prize);

        if (this == SECOND) {
            // 이미 formattedPrize가 콤마가 포함된 문자열이므로, %s를 사용합니다.
            return String.format("%d개 일치, 보너스 볼 일치 (%s원)", matchCount, formattedPrize);
        }

        // 1등, 3등, 4등, 5등 처리
        return String.format("%d개 일치 (%s원)", matchCount, formattedPrize);
    }
}
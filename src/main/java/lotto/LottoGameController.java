package lotto;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LottoGameController {

    private final InputView inputView;
    private final OutputView outputView;

    public LottoGameController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    // 프로그램 전체 흐름 (Req 3: 15라인 미만)
    public void run() {
        int purchasePrice = inputView.readPurchasePrice();

        int lottoCount = getLottoCount(purchasePrice);
        List<Lotto> myLottos = purchaseLottos(lottoCount);
        outputView.printMyLottos(lottoCount, myLottos);

        Lotto winningLotto = inputView.readWinningLotto();
        int bonusNumber = inputView.readBonusNumber(winningLotto);

        LottoResult result = calculateStatistics(myLottos, winningLotto, bonusNumber, purchasePrice);
        outputView.printStatistics(result);
    }

    // --- Controller 비즈니스 로직 (Req 3: 15라인 미만) ---

    private int getLottoCount(int price) {
        return price / 1000;
    }

    private List<Lotto> purchaseLottos(int lottCount) {
        List<Lotto> myLottos = new ArrayList<>();
        for (int i = 0; i < lottCount; i++) {
            myLottos.add(generateSingleLotto());
        }
        return myLottos;
    }

    private Lotto generateSingleLotto() {
        List<Integer> numbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
        return new Lotto(numbers);
    }

    // 통계 계산 로직 (Req 3: 15라인 미만)
    private LottoResult calculateStatistics(List<Lotto> myLottos, Lotto winningLotto, int bonusNumber, int purchasePrice) {
        Map<Rank, Integer> rankCounts = initializeRankCounts();
        long totalPrize = 0;

        for (Lotto myLotto : myLottos) {
            Rank rank = myLotto.determineRank(winningLotto, bonusNumber);
            totalPrize += updateCountsAndPrize(rank, rankCounts);
        }

        double returnRate = ((double) totalPrize / purchasePrice) * 100.0;
        return new LottoResult(rankCounts, returnRate);
    }

    // 통계 계산 서브 로직 (Req 3: 15라인 미만)
    private long updateCountsAndPrize(Rank rank, Map<Rank, Integer> rankCounts) {
        if (rank != Rank.MISS) {
            rankCounts.put(rank, rankCounts.get(rank) + 1);
            return rank.getPrize();
        }
        return 0;
    }

    private Map<Rank, Integer> initializeRankCounts() {
        Map<Rank, Integer> rankCounts = new EnumMap<>(Rank.class);
        for (Rank rank : Rank.values()) {
            if (rank != Rank.MISS) {
                rankCounts.put(rank, 0);
            }
        }
        return rankCounts;
    }
}
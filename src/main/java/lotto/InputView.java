package lotto;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InputView {

    // 1. 구입 금액 입력 (Req 3: 15라인 미만)
    public int readPurchasePrice() {
        System.out.println("구입금액을 입력해 주세요.");
        while (true) {
            try {
                String input = Console.readLine();
                validateAndParsePrice(input);
                return Integer.parseInt(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 2. 당첨 로또 번호 입력 (Req 3: 15라인 미만)
    public Lotto readWinningLotto() {
        System.out.println("\n당첨 번호를 입력해 주세요.");
        while (true) {
            try {
                String input = Console.readLine();
                List<Integer> numbers = parseWinningNumbers(input);
                // Lotto 생성자에서 나머지 유효성 검사 (범위, 중복)가 수행됩니다.
                return new Lotto(numbers);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 3. 보너스 번호 입력 (Req 3: 15라인 미만)
    public int readBonusNumber(Lotto winningLotto) {
        System.out.println("\n보너스 번호를 입력해 주세요.");
        while (true) {
            try {
                String input = Console.readLine().trim();
                int bonusNumber = parseBonusNumber(input);
                validateBonusDuplicate(winningLotto, bonusNumber);
                return bonusNumber;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // --- private 유효성 검사 및 파싱 메서드 (Req 3: 15라인 미만) ---

    // 금액 검증 로직을 작은 메서드로 분리
    private void validateAndParsePrice(String input) {
        validateIsNumber(input);
        int price = Integer.parseInt(input);
        validatePriceUnit(price);
        validatePricePositive(price);
    }

    private void validateIsNumber(String input) {
        // 1. 빈 문자열이거나 null일 경우 (trim() 후 확인)
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 값을 입력해야 합니다.");
        }

        // 2. 숫자로 변환 가능한지 확인
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 숫자 형식입니다.");
        }
    }

    private void validatePriceUnit(int price) {
        if (price % 1000 != 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위여야 합니다.");
        }
    }

    private void validatePricePositive(int price) {
        if (price <= 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 0보다 커야 합니다.");
        }
    }

    // 당첨 번호 파싱 (Req 3: 15라인 미만)
    private List<Integer> parseWinningNumbers(String input) {
        String[] parts = input.split(",");

        if (parts.length != 6) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 6개여야 합니다.");
        }

        List<Integer> numbers = new ArrayList<>();
        for (String part : parts) {
            validateIsNumber(part.trim()); // 숫자인지 검사
            numbers.add(Integer.parseInt(part.trim()));
        }
        return numbers;
    }

    // 보너스 번호 파싱 및 범위 검사 (Req 3: 15라인 미만)
    private int parseBonusNumber(String input) {
        validateIsNumber(input);
        int number = Integer.parseInt(input);

        if (number < 1 || number > 45) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
        return number;
    }

    // 보너스 번호 중복 검사 (Req 3: 15라인 미만)
    private void validateBonusDuplicate(Lotto winningLotto, int bonusNumber) {
        if (winningLotto.getNumbers().contains(bonusNumber)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }
}
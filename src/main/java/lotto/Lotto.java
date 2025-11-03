package lotto;

import java.util.List;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
    }


    // TODO: 추가 기능 구현
    @Override
    public String toString(){

        return numbers.toString();
    }

    public List<Integer> getNumbers(){
        return  java.util.Collections.unmodifiableList(this.numbers);
    }

    private int countMatchingNumbers(Lotto otherLotto) {
        int count = 0;
        // 1. 상대방의 번호 리스트를 가져옴
        List<Integer> otherNumbers = otherLotto.getNumbers();

        // 2. '내 번호'(this.numbers)를 기준으로
        for (int myNumber : this.numbers) {
            // 3. '내 번호'가 '상대방 리스트'에 "포함"되어 있는지 확인
            if (otherNumbers.contains(myNumber)) {
                count++;
            }
        }
        return count; // 겹치는 개수 반환 (순서 상관 X)
    }

    public boolean containsNumber(int number) {
        // List가 기본으로 제공하는 .contains() 기능을 사용합니다.
        return this.numbers.contains(number);
    }

    public Rank determineRank(Lotto winningLotto, int bonusNumber) {
        List<Integer> myNumbers = this.numbers;
        List<Integer> winNumbers = winningLotto.getNumbers();

        long matchCount = myNumbers.stream().filter(winNumbers::contains).count();

        if (matchCount == 6) return Rank.FIRST;
        if (matchCount == 5 && myNumbers.contains(bonusNumber)) return Rank.SECOND;
        if (matchCount == 5) return Rank.THIRD;
        if (matchCount == 4) return Rank.FOURTH;
        if (matchCount == 3) return Rank.FIFTH;
        return Rank.MISS;
    }












}

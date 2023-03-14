package domain.board;

import java.util.Arrays;

public enum Rank {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public static Rank findRank(int rankCoordinate) {
        return Arrays.stream(Rank.values())
                .filter(rank -> rank.ordinal() == rankCoordinate)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("adfa"));
    }
}

package backend.academy.hangman;

import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

/**
 * Game difficulty setting.
 *
 * @author alnmlbch
 */
@Getter
public enum Difficulty implements Predicate<String> {

    EASY(1, 3, "easy"),
    MEDIUM(4, 6, "medium"),
    HARD(7, 9, "hard"),
    VERY_HARD(10, 12, "very hard");

    private final int minLength;
    private final int maxLength;
    private final String description;

    Difficulty(final int minLength, final int maxLength, @NonNull final String description) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.description = description;
    }

    public static Difficulty of(final String description) {
        return Arrays.stream(values())
            .filter(difficulty -> difficulty.description().equalsIgnoreCase(description))
            .findFirst()
            .orElse(EASY);
    }

    @Override
    public boolean test(@NonNull final String word) {
        return minLength <= word.length() && word.length() <= maxLength;
    }

    @Override
    public String toString() {
        return description();
    }
}

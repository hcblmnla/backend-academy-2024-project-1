package backend.academy.hangman;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import static backend.academy.hangman.Difficulty.EASY;
import static backend.academy.hangman.Difficulty.HARD;
import static backend.academy.hangman.Difficulty.MEDIUM;
import static backend.academy.hangman.Difficulty.VERY_HARD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DifficultyTest {

    private static Stream<Arguments> provideDifficultyNames() {
        return Stream.of(
            Arguments.of("easy", EASY),
            Arguments.of("medium", MEDIUM),
            Arguments.of("Hard", HARD),
            Arguments.of("VERY hard", VERY_HARD),
            Arguments.of("", EASY),
            Arguments.of("harder", EASY),
            Arguments.of(null, EASY)
        );
    }

    @ParameterizedTest
    @EnumSource(Difficulty.class)
    void minLengthIsAlwaysPositive(final Difficulty difficulty) {
        assertThat(difficulty.minLength()).isGreaterThan(0);
    }

    @ParameterizedTest
    @EnumSource(Difficulty.class)
    void maxLengthIsAlwaysGreaterThanMinLength(final Difficulty difficulty) {
        assertThat(difficulty.maxLength()).isGreaterThan(difficulty.minLength());
    }

    @ParameterizedTest
    @MethodSource("provideDifficultyNames")
    void difficultyFromCorrectness(final String description, final Difficulty difficulty) {
        assertThat(Difficulty.of(description)).isEqualTo(difficulty);
    }
}

package backend.academy.hangman;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

public class GameSettingsTest {

    private static final int COUNT_RANDOM_CASES = 25;

    private static Stream<Game.Settings> getDefaultSettings() {
        return Stream.generate(() -> new Game.Settings(null, null))
            .limit(COUNT_RANDOM_CASES);
    }

    private static Stream<Arguments> settingsExamples() {
        return Stream.of(
            Arguments.of("animals", null),
            Arguments.of("science", "medium"),
            Arguments.of("fp", "hard")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"animals", "fp", "programming", "science"})
    void defaultCategoryIsAlwaysRandom() {
        assertThat(
            getDefaultSettings()
                .filter(settings -> "animals".equals(settings.category()))
                .toList()
                .isEmpty()
        )
            .isFalse();
    }

    @Test
    void defaultDifficultyIsAlwaysEasy() {
        assertThat(getDefaultSettings()
            .allMatch(settings -> settings.difficulty() == Difficulty.EASY)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("settingsExamples")
    void wordWithFixedParamsAlwaysRandom(@NonNull final String category, @NonNull final String difficulty) {
        assertThat(Stream.generate(() -> new Game.Settings(category, difficulty))
            .limit(COUNT_RANDOM_CASES)
            .map(Game.Settings::word)
            .map(HiddenWord::open)
            .collect(Collectors.toSet())
            .size()).isGreaterThan(1);
    }
}

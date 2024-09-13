package backend.academy.hangman;

import java.util.function.BiConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import static org.assertj.core.api.Assertions.assertThat;

public class WordsTest {

    private static void forEachCategoryAndDifficulty(final BiConsumer<String, Difficulty> consumer) {
        for (var category : Words.CATEGORIES) {
            for (var difficulty : Difficulty.values()) {
                consumer.accept(category, difficulty);
            }
        }
    }

    @Test
    void categoryNamesNotEmpty() {
        assertThat(Words.CATEGORIES).isNotEmpty();
    }

    @Test
    void eachCategoryContainsEachDifficulty() {
        forEachCategoryAndDifficulty(this::categoryContainsDifficulty);
    }

    @TestFactory
    void categoryContainsDifficulty(final String category, final Difficulty difficulty) {
        assertThat(Words.getUnitsBy(category, difficulty))
            .describedAs("Not found words for category '%s' and difficulty '%s'", category, difficulty)
            .isNotEmpty();
    }

    @Test
    void eachCategoryContainsOnlyCorrectWords() {
        forEachCategoryAndDifficulty((category, difficulty) -> {
            for (var unit : Words.getUnitsBy(category, difficulty)) {
                categoryWordSatisfiesDifficulty(unit.word(), category, difficulty);
            }
        });
    }

    @TestFactory
    void categoryWordSatisfiesDifficulty(final String word, final String category, final Difficulty difficulty) {
        assertThat(difficulty.test(word))
            .describedAs("Word '%s' in category '%s' not satisfies difficulty '%s'", word, category, difficulty)
            .isTrue();
    }
}

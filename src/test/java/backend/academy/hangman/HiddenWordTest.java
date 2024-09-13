package backend.academy.hangman;

import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HiddenWordTest {

    private static Stream<Arguments> programmingExampleArguments() {
        var p = new HiddenWord("programming");
        return Stream.of(
            Arguments.of(p, 'p', true, "p__________"),
            Arguments.of(p, 'g', true, "p__g_______"),
            Arguments.of(p, 'p', false, "p__g_______"),
            Arguments.of(p, 'r', true, "pr_g_______"),
            Arguments.of(p, 'o', true, "prog_______"),
            Arguments.of(p, 'g', true, "prog______g"),
            Arguments.of(p, 'm', true, "prog__m___g")
        );
    }

    private static Stream<Arguments> catExampleArguments() {
        var cat = new HiddenWord(new Words.Unit("cat", "meow meow meow meow meow meow meow"));
        return Stream.of(
            Arguments.of(cat, 't', true, "__t"),
            Arguments.of(cat, 't', false, "__t"),
            Arguments.of(cat, 'z', false, "__t"),
            Arguments.of(cat, 'c', true, "c_t"),
            Arguments.of(cat, 'a', true, "cat"),
            Arguments.of(cat, 'a', false, "cat"),
            Arguments.of(cat, 'p', false, "cat")
        );
    }

    @SuppressWarnings("DataFlowIssue")
    private static Stream<Supplier<HiddenWord>> ctorsWhichThrowsNullPointerException() {
        return Stream.of(
            () -> new HiddenWord((Words.Unit) null),
            () -> new HiddenWord((String) null),
            () -> new HiddenWord(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("programmingExampleArguments")
    @MethodSource("catExampleArguments")
    void exampleWordProcessing(final HiddenWord hidden, final char letter, final boolean expected, final String word) {
        assertThat(hidden.guess(letter)).isEqualTo(expected);
        assertThat(hidden.toString()).isEqualTo(String.join(" ", word.split("")));
    }

    @Test
    void correctnessBig() {
        var hidden = new HiddenWord("hidden");
        hidden.guess('h');
        assertThat(hidden.isGuessed()).isFalse();
        assertThat(hidden.open()).isEqualTo("hidden");
        assertThat(hidden.isGuessed()).isTrue();
    }

    @Test
    void correctnessSmall() {
        var ct = new HiddenWord("ct");
        ct.guess('c');
        ct.guess('t');
        assertThat(ct.isGuessed()).isTrue();
        assertThat(ct.open()).isEqualTo("ct");
        assertThat(ct.isGuessed()).isTrue();
    }

    @Test
    void correctnessImmediately() {
        var big = new HiddenWord("big");
        assertThat(big.open()).isEqualTo("big");
    }

    @ParameterizedTest
    @MethodSource("ctorsWhichThrowsNullPointerException")
    void equivalentCtors(final Supplier<HiddenWord> ctor) {
        assertThatThrownBy(ctor::get).isInstanceOf(NullPointerException.class);
    }
}

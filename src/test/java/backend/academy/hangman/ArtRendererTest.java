package backend.academy.hangman;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ArtRendererTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -6, 100, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void levelsBadIntsSupport(final int level) {
        levelsIntsSupport(level, _ -> 2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void levelsGoodIntsSupport(final int level) {
        levelsIntsSupport(level, lvl -> lvl + 1);
    }

    @TestFactory
    void levelsIntsSupport(final int level, @NonNull final UnaryOperator<Integer> mapper) {
        final Iterator<String> renderer = new ArtRenderer(level);
        int times = 0;
        while (renderer.hasNext()) {
            renderer.next();
            times++;
        }
        assertThat(times).isEqualTo(mapper.apply(level));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 10, 100, Integer.MAX_VALUE})
    void renderThrowNoSuchElementExceptionIfHasNotNext(final int level) {
        var renderer = new ArtRenderer(6);
        assertThatThrownBy(() -> {
            for (int i = 0; i < level; i++) {
                renderer.next();
            }
        })
            .isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(renderer::current)
            .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 10, Integer.MAX_VALUE, -10, 0, Integer.MIN_VALUE})
    void takeLastEquivalentFullIteratorCycle(final int level) {
        var lhs = new ArtRenderer(level);
        var rhs = new ArtRenderer(level);
        String tmp = null;
        while (lhs.hasNext()) {
            tmp = lhs.next();
        }
        rhs.moveToLast();
        assertThat(tmp)
            .isEqualTo(rhs.current());
    }
}

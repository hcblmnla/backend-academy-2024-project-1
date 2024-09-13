package backend.academy.hangman;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ShortGameTest {

    @Test
    void partialGameWon() {
        partialGameProcessing("""
                a
                b
                m
                d
                o
                n
                o
                i
                """,
            """
                log: _ _ _ _ _ _, 0
                log: _ _ _ _ _ _, 1
                log: _ _ _ _ _ _, 2
                log: m _ _ _ _ _, 2
                log: m _ _ _ _ d, 2
                log: m o _ _ _ d, 2
                log: m o n _ _ d, 2
                log: m o n o _ d, 2
                log: m o n o i d, 2
                """);
    }

    @Test
    void partialGameLose() {
        partialGameProcessing("""
                a
                b
                m
                d
                c
                """,
            """
                log: _ _ _ _ _ _, 0
                log: _ _ _ _ _ _, 1
                log: _ _ _ _ _ _, 2
                log: m _ _ _ _ _, 2
                log: m _ _ _ _ d, 2
                log: m _ _ _ _ d, 3
                """);
    }

    @Test
    void partialGameSurrenderWithHint() {
        partialGameProcessing("""
                a
                b
                2
                m
                d
                ff
                """,
            """
                log: _ _ _ _ _ _, 0
                log: _ _ _ _ _ _, 1
                log: _ _ _ _ _ _, 2
                log: _ _ _ _ _ _, 2
                log: m _ _ _ _ _, 2
                log: m _ _ _ _ d, 2
                log: m _ _ _ _ d, 3
                """);
    }

    @TestFactory
    void partialGameProcessing(@NonNull final String input, @NonNull final String expectedOutput) {
        var sr = new StringReader(input);
        var sw = new StringWriter();
        try (var game = new Game(new PartialIOHandler(sr, sw))) {
            game.run();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        assertThat(sw.toString()).isEqualTo(expectedOutput);
    }

    private static class PartialIOHandler extends IOHandler {

        public PartialIOHandler(@NonNull final Reader in, @NonNull final Writer out) {
            super(in, out);
        }

        @Override
        public Game.Settings getSettings(@NonNull final List<String> categories) {
            return new Game.Settings("fp", Difficulty.MEDIUM, new HiddenWord("monoid", "java can be functional"));
        }

        @Override
        protected void display(
            final State state,
            final int attempt,
            @NonNull final String currentWord,
            final String reason,
            final String info
        ) throws IOException {
            out.write("log: %s, %d%n".formatted(currentWord, attempt));
        }
    }
}

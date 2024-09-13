package backend.academy.hangman;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiFunction;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class LongGameTest {

    public static String readFile(@NonNull final Path path) throws IOException {
        return Files.newBufferedReader(path, StandardCharsets.UTF_8)
            .lines()
            .reduce(
                new StringBuilder(),
                (lhs, rhs) -> lhs.append(rhs.trim()),
                StringBuilder::append
            )
            .toString();
    }

    @Test
    void fullGameWin() {
        fullGameLogsEquivalent("""
                q
                d
                l
                sf

                2
                5
                .
                g
                o
                """,
            DogIOHandler::new,
            Path.of("src/test/resources/hangman/full-game-output.txt"),
            Path.of("src/test/resources/hangman/full-game-expected.txt")
        );
    }

    @SuppressWarnings("SameParameterValue")
    @TestFactory
    private void fullGameLogsEquivalent(
        @NonNull final String input,
        @NonNull final BiFunction<StringReader, BufferedWriter, GameplayHandler> handler,
        @NonNull final Path output,
        @NonNull final Path expected
    ) {
        var sr = new StringReader(input);
        try (
            var fw = Files.newBufferedWriter(output, StandardCharsets.UTF_8);
            var game = new Game(handler.apply(sr, fw))
        ) {
            game.run();
            assertThat(readFile(output)).isEqualTo(readFile(expected));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
        """
            0
            10
            0
            1
            """,
        """
            animals
            very hard
            100

            abc
            hint
            ff
            """,
        """
            animal
            animals
            very medium

            very hard

            1
            """,
        """
            1
            easy
            q
            q
            q
            """,
        "",
        """
            animals
            very hard
            """,
        """
            animal
            """,
        """
            1
            easy
            """
    })
    void ioHandlerGameDoesNotThrowAnyException(@NonNull final String input) {
        Assertions.assertDoesNotThrow(() -> new Game(new IOHandler(
            new StringReader(input),
            new StringWriter()
        )).run());
    }

    private static class DogIOHandler extends IOHandler {

        public DogIOHandler(@NonNull final Reader in, @NonNull final Writer out) {
            super(in, out);
        }

        @Override
        public Game.Settings getSettings(@NonNull final List<String> categories) {
            return new Game.Settings("animals", Difficulty.EASY, new HiddenWord("dog", "bark bark"));
        }
    }
}

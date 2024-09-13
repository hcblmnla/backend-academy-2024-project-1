package backend.academy.hangman;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;

/**
 * Hangman applicator.
 *
 * @author alnmlbch
 */
@Log4j2
public final class Application {

    private Application() {
    }

    public static void main(final String[] args) {
        try (var game = new Game(new IOHandler(
            new InputStreamReader(System.in, StandardCharsets.UTF_8),
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
        ))) {
            game.run();
        } catch (final Exception e) {
            log.error(e.getMessage());
        }
    }
}

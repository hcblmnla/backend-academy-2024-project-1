package backend.academy.hangman;

import java.io.IOException;
import java.security.SecureRandom;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import static backend.academy.hangman.State.HINT;
import static backend.academy.hangman.State.LOSE;
import static backend.academy.hangman.State.PLAYING;
import static backend.academy.hangman.State.SUCCESS;
import static backend.academy.hangman.State.SURRENDER;
import static backend.academy.hangman.State.WRONG;

/**
 * Main Hangman game processor.
 *
 * @author alnmlbch
 */
@Log4j2
public class Game implements AutoCloseable, Runnable {

    private final GameplayHandler handler;
    private Settings settings;

    private State state;
    private int attempt;
    private boolean hinted;

    public Game(@NonNull final GameplayHandler handler) {
        this.handler = handler;
        hinted = false;
    }

    public void setSettings() throws IOException {
        settings = handler.getSettings(Words.CATEGORIES);
        state = PLAYING;
    }

    @Override
    public void run() {
        try {
            setSettings();
        } catch (final Exception e) {
            log.error("Exception was occurred while getting settings {}", e.getMessage());
            return;
        }
        handler.configure(settings, new ArtRenderer(Settings.MAX_ATTEMPTS));
        try {
            while (!state.isOver()) {
                state = step();
            }
        } catch (final Exception e) {
            log.error("Exception was occurred while playing {}", e.getMessage());
        }
    }

    @SuppressWarnings("ReturnCount")
    public State step() throws IOException {
        if (attempt >= Settings.MAX_ATTEMPTS) {
            return handler.lose(LOSE);
        }
        if (settings.word().isGuessed()) {
            return handler.win(attempt);
        }
        var response = handler.step(state, attempt, hinted);
        if (response.state() == SURRENDER) {
            return handler.lose(SURRENDER);
        }
        if (response.state() == HINT) {
            hinted = true;
            return HINT;
        } else if (!settings.word().guess(response.value())) {
            attempt++;
            handler.nextFrame();
            return WRONG;
        }
        return SUCCESS;
    }

    @Override
    public void close() throws Exception {
        handler.close();
    }

    /**
     * Game settings.
     */
    @Getter
    public static class Settings {

        public static final int MAX_ATTEMPTS = 3;
        private static final SecureRandom RANDOM = new SecureRandom();
        private final String category;
        private final Difficulty difficulty;
        private final HiddenWord word;

        public Settings(
            @NonNull final String category,
            @NonNull final Difficulty difficulty,
            @NonNull final HiddenWord word
        ) {
            this.category = category;
            this.difficulty = difficulty;
            this.word = word;
        }

        public Settings(final String category, final String diffStr) {
            this.category = category == null
                ? getRandomCategory()
                : category;
            this.difficulty = Difficulty.of(diffStr);
            this.word = getRandomWord(this.category, this.difficulty);
        }

        public static HiddenWord getRandomWord(
            @NonNull final String category,
            @NonNull final Difficulty difficulty
        ) {
            var units = Words.getUnitsBy(category, difficulty);
            return new HiddenWord(units.get(RANDOM.nextInt(units.size())));
        }

        public static String getRandomCategory() {
            return Words.CATEGORIES.get(RANDOM.nextInt(Words.CATEGORIES.size()));
        }
    }
}

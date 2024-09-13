package backend.academy.hangman;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import static backend.academy.hangman.State.HINT;
import static backend.academy.hangman.State.LOSE;
import static backend.academy.hangman.State.SUCCESS;
import static backend.academy.hangman.State.SURRENDER;
import static backend.academy.hangman.State.WIN;
import static backend.academy.hangman.State.WRONG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
public class GameTest {

    private static Game getCatGame(@NonNull final List<State.Response> responses) {
        var index = new AtomicInteger();
        return new Game(new GameplayHandler() {

            @Override
            public void configure(final Game.@NonNull Settings settings, @NonNull final Renderer<String> renderer) {
            }

            @Override
            public Game.Settings getSettings(@NonNull final List<String> categories) {
                return new Game.Settings("animals", Difficulty.EASY, new HiddenWord("cat", "meow meow"));
            }

            @Override
            public void nextFrame() {
            }

            @Override
            public State.Response step(@NonNull final State state, final int attempt, final boolean hinted) {
                return responses.get(index.getAndIncrement());
            }

            @Override
            public State win(final int attempt) {
                return WIN;
            }

            @Override
            public State lose(@NonNull final State loseState) {
                return loseState;
            }

            @Override
            public void close() {
            }
        });
    }

    private static State.Response st(final char ch) {
        return new State.Response(ch);
    }

    private static State.Response st(@NonNull final State state) {
        return new State.Response(state);
    }

    @TestFactory
    void catGameProcessing(
        @NonNull final List<State.Response> responses,
        @NonNull final List<State> states
    ) {
        var game = getCatGame(responses);
        try {
            game.setSettings();
            for (int i = 0; i < states.size(); i++) {
                var step = game.step();
                assertThat(step)
                    .describedAs("game step is %s, but found %s in %d step", states.get(i), step, i + 1)
                    .isEqualTo(states.get(i));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void correctnessOnCat() {
        catGameProcessing(
            List.of(st('c'), st('a'), st('t')),
            List.of(SUCCESS, SUCCESS, SUCCESS)
        );
    }

    @Test
    void mistakesOnCat() {
        catGameProcessing(
            List.of(st('c'), st(WRONG), st(WRONG), st('t'), st('a')),
            List.of(SUCCESS, WRONG, WRONG, SUCCESS, SUCCESS)
        );
    }

    @Test
    void loseOnCat() {
        catGameProcessing(
            List.of(st('a'), st(WRONG), st(WRONG), st(WRONG)),
            List.of(SUCCESS, WRONG, WRONG, WRONG)
        );
    }

    @Test
    void usageHelpAndNoteOnCat() {
        catGameProcessing(
            List.of(st(HINT), st('t'), st(SURRENDER)),
            List.of(HINT, SUCCESS, SURRENDER)
        );
    }

    @Test
    void cannotUseNotConfigurationIOHandler() {
        assertThatThrownBy(() ->
            new IOHandler(
                new InputStreamReader(System.in, StandardCharsets.UTF_8),
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
            ).lose(LOSE)).isInstanceOf(IllegalStateException.class);
    }
}

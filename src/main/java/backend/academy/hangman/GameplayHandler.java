package backend.academy.hangman;

import java.io.IOException;
import java.util.List;
import org.jspecify.annotations.NonNull;

/**
 * An entity that allows you to communicate with the user.
 *
 * @author alnmlbch
 */
public interface GameplayHandler extends AutoCloseable {

    void configure(Game.@NonNull Settings settings, @NonNull Renderer<String> renderer);

    Game.Settings getSettings(@NonNull List<String> categories) throws IOException;

    void nextFrame();

    State.Response step(@NonNull State state, int attempt, boolean hinted) throws IOException;

    State win(int attempt) throws IOException;

    State lose(@NonNull State loseState) throws IOException;
}

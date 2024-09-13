package backend.academy.hangman;

import lombok.Getter;
import org.jspecify.annotations.NonNull;

/**
 * Game state unit.
 *
 * @author alnmlbch
 */
@Getter
public enum State {

    PLAYING("Let's go! Try to guess the word"),
    WIN("Congratulations! You have won!", true),
    LOSE("Sorry, you have lost.", true),

    SUCCESS("Correct! Continue playing"),
    WRONG("Wrong letter. Try another variant"),

    HINT("Okay, take your hint"),
    SURRENDER("Surrender! Gl next time", true);

    private final String message;

    @Getter
    private final boolean isOver;

    State(@NonNull final String message, final boolean isOver) {
        this.message = message;
        this.isOver = isOver;
    }

    State(@NonNull final String message) {
        this(message, false);
    }

    /**
     * State wrapper for gameplay.
     */
    @Getter
    public static class Response {

        private final State state;
        private char value;

        public Response(final char value) {
            this.value = value;
            this.state = State.SUCCESS;
        }

        public Response(@NonNull final State state) {
            this.state = state;
        }
    }
}

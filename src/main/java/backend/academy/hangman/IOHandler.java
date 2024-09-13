package backend.academy.hangman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import org.jspecify.annotations.NonNull;

/**
 * Input parser and output formatter.
 *
 * @author alnmlbch
 */
public class IOHandler implements GameplayHandler {

    private static final String FF_STR = "ff";
    private static final String HINT_STR = "hint";
    protected final BufferedWriter out;
    private final BufferedReader in;
    private Game.Settings settings;
    private Renderer<String> renderer;
    private boolean configured;

    public IOHandler(@NonNull final Reader in, @NonNull final Writer out) {
        this.in = new BufferedReader(in);
        this.out = new BufferedWriter(out);
    }

    /**
     * Handler configurator. It is needed to separate getting settings and gameplay.
     * And create initial frame.
     */
    @Override
    public void configure(final Game.@NonNull Settings settings, @NonNull final Renderer<String> renderer) {
        this.settings = settings;
        this.renderer = renderer;
        configured = true;
    }

    @Override
    public Game.Settings getSettings(@NonNull final List<String> categories) throws IOException {
        log("""
            Welcome to Hangman! Let's fill some settings
            If you want to skip parameter, just enter '0'
            """);
        return new Game.Settings(
            getParameter("Category", categories),
            getParameter("Difficulty", Arrays.stream(Difficulty.values())
                .map(Difficulty::description)
                .toList()
            )
        );
    }

    private String getParameter(@NonNull final String parameter, @NonNull final List<String> properties)
        throws IOException {
        var sj = new StringJoiner(",\n");
        int maxSize = properties.stream()
            .map(String::length)
            .max(Integer::compareTo)
            .orElse(0);
        for (int i = 0; i < properties.size(); i++) {
            sj.add("  %s%s[%d]".formatted(
                properties.get(i),
                " ".repeat(maxSize - properties.get(i).length() + 1),
                i + 1)
            );
        }
        log("%n> %s (%n%s%n): ", parameter, sj.toString());
        String res;
        while (true) {
            res = in.readLine();
            if ("0".equalsIgnoreCase(res)) {
                res = null;
                break;
            }
            if (res.length() == 1 && Character.isDigit(res.charAt(0))) {
                var pos = Integer.parseInt(res) - 1;
                if (0 <= pos && pos < properties.size()) {
                    res = properties.get(pos);
                    break;
                }
            }
            if (properties.contains(res.toLowerCase())) {
                break;
            }
            log("> %s (you have a mistake): ", parameter);
        }
        return res;
    }

    @Override
    public void nextFrame() {
        assertConfigured();
        renderer.next();
    }

    @Override
    public State.Response step(
        @NonNull final State state,
        final int attempt,
        final boolean hinted
    ) throws IOException {
        assertConfigured();
        var message = "%n%nnote: for surrender enter '%s' [1]%nhint: %s".formatted(FF_STR, settings.word().hint());
        if (!hinted) {
            message = "%n%nnote: for surrender enter '%s' [1]%nhelp: for hint enter '%s'    [2]"
                .formatted(FF_STR, HINT_STR);
        }
        display(state, attempt, settings.word().toString(), "\n\n> Letter: ", message);
        String line;
        while (true) {
            line = in.readLine();
            if (FF_STR.equalsIgnoreCase(line) || "1".equals(line)) {
                return new State.Response(State.SURRENDER);
            }
            if (!hinted && (HINT_STR.equalsIgnoreCase(line) || "2".equals(line))) {
                return new State.Response(State.HINT);
            }
            if (line.length() != 1) {
                log("> Letter (only 1): ");
                continue;
            }
            var ch = line.charAt(0);
            if (Character.isLetter(ch) && 'A' <= ch && ch <= 'z') {
                return new State.Response(Character.toLowerCase(line.charAt(0)));
            }
            log("> Letter (think): ");
        }
    }

    @Override
    public State win(final int attempt) throws IOException {
        assertConfigured();
        display(State.WIN, attempt, settings.word().toString(), "\n", "");
        return State.WIN;
    }

    @Override
    public State lose(@NonNull final State loseState) throws IOException {
        assertConfigured();
        var hidden = settings.word().toString();
        renderer.moveToLast();
        display(loseState, Game.Settings.MAX_ATTEMPTS,
            hidden, "   -> '%s'%n".formatted(settings.word().open()), "");
        return loseState;
    }

    protected void display(
        final State state,
        final int attempt,
        final @NonNull String currentWord,
        final String reason,
        final String info
    ) throws IOException {
        assertConfigured();
        log("%n%s%n%s%nCategory: %s | Difficulty: %s | Attempts: %d/%d%s%n%n  %s%s",
            renderer.current(),
            state.message(),
            settings.category(),
            settings.difficulty().description(),
            attempt,
            Game.Settings.MAX_ATTEMPTS,
            info,
            currentWord,
            reason
        );
    }

    private void log(@NonNull final String message, @NonNull final Object... args) throws IOException {
        out.write(message.formatted(args));
        out.flush();
    }

    private void assertConfigured() {
        if (!configured) {
            throw new IllegalStateException("Console is not configured");
        }
    }

    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}

package backend.academy.hangman;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeSet;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

/**
 * Word wrapper for the Hangman.
 *
 * @author alnmlbch
 */
public final class HiddenWord {

    private static final char NOT_GUESSED = '_';

    private final char[] word;
    private final Map<Character, TreeSet<Integer>> indexes = new HashMap<>();

    @Getter
    private final String hint;

    public HiddenWord(@NonNull final String source, final String hint) {
        word = new char[source.length()];
        for (int i = 0; i < source.length(); i++) {
            word[i] = NOT_GUESSED;
            indexes
                .computeIfAbsent(
                    source.charAt(i),
                    ignore -> new TreeSet<>())
                .add(i);
        }
        this.hint = hint;
    }

    public HiddenWord(final Words.@NonNull Unit cs) {
        this(cs.word(), cs.hint());
    }

    public HiddenWord(@NonNull final String source) {
        this(source, null);
    }

    /**
     * Gaming checker.
     *
     * @return {@code true}, if word contain the letter, else {@code false}
     */
    public boolean guess(final char letter) {
        if (!indexes.containsKey(letter)) {
            return false;
        }
        var index = indexes.get(letter).removeFirst();
        word[index] = letter;
        if (indexes.get(letter).isEmpty()) {
            indexes.remove(letter);
        }
        return true;
    }

    /**
     * Hidden word opener.
     *
     * @return opened word
     */
    public String open() {
        while (!indexes.isEmpty()) {
            guess(indexes.keySet().iterator().next());
        }
        return String.valueOf(word);
    }

    /**
     * Gaming result checker.
     *
     * @return {@code true}, if word was guessed, else {@code false}
     */
    public boolean isGuessed() {
        return indexes.isEmpty();
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(" ");
        for (var ch : word) {
            sj.add(String.valueOf(ch));
        }
        return sj.toString();
    }
}

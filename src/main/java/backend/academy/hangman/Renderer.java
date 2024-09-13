package backend.academy.hangman;

import java.util.Iterator;

/**
 * Iterable frame renderer.
 *
 * @param <T> frames type.
 * @author alnmlbch
 */
public interface Renderer<T> extends Iterator<T> {

    T current();

    void moveToLast();
}

package elements;

import java.util.stream.Stream;

/**
 * Interface representing a collection of {@link WebElement}.
 * <p>It extends the Iterable interface</p>
 * <p>The main reasoning behind this is because most methods provided by List/Collection do not bring value to
 * WebElements</></p>
 * If you want to access the list of those WebElements you can do so by utilizing the stream method provided in this interface.
 */
public interface WebElements extends Iterable<WebElement> {


    /**
     * The size of the internal {@code List<WebElement>}
     * @return the current size of the WebElements found.
     */
    int size();

    boolean isEmpty();

    /**
     * Clears the current fetched elements and enables the research of them.
     * This means the if we have WebElements.size() = 4;
     * Then we remove 1 of those elements from the DOM tree and we do
     * WebElements.clear(); WebElements.size() -> 3
     * Will be the result.
     */
    void clear();

    /**
     * A stream to access the internal {@code List<WebElement>}
     * @return the stream of this.
     */
    Stream<WebElement> stream();

    /**
     * Get the WebElement based on the index specified.
     * @param index we want to fetch.
     * @return the WebElement if it exists or throws List based exceptions.
     */
    WebElement get(int index);
}

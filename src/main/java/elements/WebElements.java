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


    int size();

    boolean isEmpty();

    /**
     * Clears the current fetched elements and enables the research of them.
     */
    void clear();

//    boolean add(WebElement webElement);

    Stream<WebElement> stream();


    WebElement get(int index);

//    WebElement set(int index, WebElement element);
}

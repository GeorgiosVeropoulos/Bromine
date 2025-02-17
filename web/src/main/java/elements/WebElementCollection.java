package elements;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class WebElementCollection implements WebElements {
    protected List<WebElement> elements = new ArrayList<>();
    protected Locator locator;
    protected boolean isFetched = false; // Track if elements have been fetched

    protected WebElementCollection(Locator locator) {
        this.locator = locator;
    }

    protected WebElementCollection(List<WebElement> elements, Locator locator) {
        this.elements = elements;
        this.locator = locator;
        isFetched = true;
    }

    protected void fetchElements() {
        if (!isFetched) {
            List<WebElement> fetchedElements = DriverClient.getElements(locator);
            elements.clear(); // should be removed?
            elements.addAll(fetchedElements);
            isFetched = true;
        }
    }

    public Stream<WebElement> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    @Nonnull
    public Iterator<WebElement> iterator() {
        fetchElements();
        return elements.iterator();
    }

    @Override
    public int size() {
        fetchElements();
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        fetchElements();
        return elements.isEmpty();
    }


    @Override
    public void clear() {
        elements.clear();
        isFetched = false;
    }

    @Override
    public WebElement get(int index) {
        fetchElements();
        return elements.get(index);
    }

}

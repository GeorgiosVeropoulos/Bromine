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

//    @Override
//    public boolean contains(Object o) {
//        fetchElements();
//        return elements.contains(o);
//    }
//
//    @Override
//    @Nonnull
//    public Object[] toArray() {
//        fetchElements();
//        return elements.toArray();
//    }
//
//    @Override
//    @Nonnull
//    public <T> T[] toArray(T[] a) {
//        fetchElements();
//        return elements.toArray(a);
//    }
//
//    @Override
//    public boolean add(WebElement webElement) {
//        fetchElements();
//        return elements.add(webElement);
//    }
//
//    @Override
//    public boolean remove(Object o) {
//        fetchElements();
//        return elements.remove(o);
//    }
//
//    @Override
//    public boolean containsAll(Collection<?> c) {
//        fetchElements();
//        return new HashSet<>(elements).containsAll(c);
//    }
//
//    @Override
//    public boolean addAll(Collection<? extends WebElement> c) {
//        fetchElements();
//        return elements.addAll(c);
//    }
//
//    @Override
//    public boolean removeAll(Collection<?> c) {
//        fetchElements();
//        return elements.removeAll(c);
//    }
//
//    @Override
//    public boolean retainAll(Collection<?> c) {
//        fetchElements();
//        return elements.retainAll(c);
//    }

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

//    @Override
//    public WebElement set(int index, WebElement element) {
//        fetchElements();
//        return elements.set(index, element);
//    }


//    @Override
//    public boolean addAll(int index, Collection<? extends WebElement> c) {
//        return false;
//    }
//
//    @Override
//    public void add(int index, WebElement element) {
//
//    }

//    @Override
//    public WebElement remove(int index) {
//        fetchElements();
//        return elements.remove(index);
//    }

//    @Override
//    public int indexOf(Object o) {
//        fetchElements();
//        return elements.indexOf(o);
//    }

//    @Override
//    public int lastIndexOf(Object o) {
//        fetchElements();
//        return elements.lastIndexOf(o);
//    }

//    @Override
//    @Nonnull
//    public ListIterator<WebElement> listIterator() {
//        fetchElements();
//        return elements.listIterator();
//    }
//
//    @Override
//    @Nonnull
//    public ListIterator<WebElement> listIterator(int index) {
//        fetchElements();
//        return elements.listIterator(index);
//    }
//
//    @Override
//    @Nonnull
//    public List<WebElement> subList(int fromIndex, int toIndex) {
//        fetchElements();
//        return elements.subList(fromIndex, toIndex);
//    }
}

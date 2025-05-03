package exceptions;

public class NoSuchElementException extends ElementNotFoundException {
    public NoSuchElementException(String s) {
        super(s);
    }
}

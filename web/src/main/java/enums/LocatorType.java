package enums;

public enum LocatorType {

    ID("id"),
    XPATH("xpath"),
    NAME("name"),
    CSS("css selector"),
    TAG_NAME("tag name");

    private final String method;

    LocatorType(String method) {
        this.method = method;
    }

    public String get() {
        return method;
    }
}

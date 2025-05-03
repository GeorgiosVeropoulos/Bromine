package elements;

import enums.LocatorType;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

/**
 * The location strategy to be used to locate a WebElement or WebElements
 */
@Getter
public abstract class Locator {

    private final String using;
    private final String value;


    private Locator(LocatorType using, String value) {
        this.using = using.get();
        this.value = value;
    }


    public static Locator id(@NonNull String id) {
        return new withID(id);
    }

    // Static method to locate elements by XPath
    public static Locator xpath(@NonNull String xpath) {
        return new withXpath(xpath);
    }

    // Static method to locate elements by Name
    public static Locator name(@NonNull String name) {
        return new withName(name);
    }

    public static Locator tagName(@NonNull String tagName) {
        return new withTagName(tagName);
    }

    @Override
    public String toString() {
        return using + "=\"" + value + "\"";
    }



    private static class withID extends Locator {
        private final String id;

        public withID(String id) {
            super(LocatorType.CSS, "#" + id);
            this.id = id;
        }

        @Override
        public String toString() {
            return "id : " + id;
        }
    }

    private static class withXpath extends Locator {
        private final String xpath;

        public withXpath(String xpath) {
            super(LocatorType.XPATH, xpath);
            this.xpath = xpath;
        }

        @Override
        public String toString() {
            return "xpath : " + xpath;
        }
    }

    private static class withName extends Locator {
        private final String name;

        public withName(String name) {
            super(LocatorType.NAME, name);
            this.name = name;
        }

        @Override
        public String toString() {
            return "name : " + name;
        }
    }

    private static class withTagName extends Locator {
        private final String tagName;

        public withTagName(String tagName) {
            super(LocatorType.TAG_NAME, tagName);
            this.tagName = tagName;
        }

        @Override
        public String toString() {
            return "TagName : " + tagName;
        }
    }
}

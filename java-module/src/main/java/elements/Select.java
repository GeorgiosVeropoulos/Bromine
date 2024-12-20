package elements;


/**
 *
 */
public class Select {

    WebElement element;

    public Select(WebElement element) {

        if (element == null) {
            throw new IllegalStateException("The provided web element shouldn't be null");
        }
        this.element = element;
        String tagName = this.element.getTagName();
        if (!tagName.equals("select")) {
            throw new SelectTagNotFoundException(tagName);
        }
    }


    public WebElements getOptions() {
        return element.$$(Locator.tagName("option"));
    }

    public WebElement getFirstChild() {
        return getOptions().get(0);
    }

    public WebElement getLastChild() {
        WebElements options = getOptions();
        return options.get(options.size() - 1);
    }

    public WebElement getFirstSelectedOption() {
        return getOptions().stream()
                .filter(WebElement::isSelected)
                .findFirst()
                .orElseThrow(NoOptionSelectedException::new);
    }

    public void click() {
        element.click();
    }



    private static class SelectTagNotFoundException extends RuntimeException{
        public SelectTagNotFoundException(String actualTagName) {
            super("Element should have <select> tag but has <" + actualTagName +">");
        }
    }

    private static class NoOptionSelectedException extends RuntimeException {
        public NoOptionSelectedException() {
            super("The curren't <select> doesn't have any options 'selected'!");
        }
    }


}

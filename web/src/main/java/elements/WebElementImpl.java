package elements;

import assertions.Verify;
import conditions.Condition;
import json.JsonBuilder;
import json.JsonParser;
import org.bromine.utils.net.Response;

import java.util.List;

import static Constants.Constants.VALUE;
import static elements.EndPoints.buildEndpoint;

/**
 * WebElementImpl is the implementation of the WebElement interface.
 * It provides methods to interact with web elements in a browser.
 */
class WebElementImpl implements WebElement {


    private final Locator locator;
    private SearchContext searchContext;


    protected WebElementImpl(SearchContext searchContext, Locator locator) {
        this.searchContext = searchContext;
        this.locator = locator;

    }

    private void checkSearchContext() {
        Verify.nonNull(searchContext, "Search context is null for element: " + locator.toString());
    }

    @Override
    public void click() {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.ELEMENT_CLICK, searchContext.elementId());
        Response response =  HttpMethodExecutor.doPostRequest(endPoint, "{}");
        HandleExceptions.handleResponse(response, "Element was intercepted using: " + getLocator().getUsing() + " value: " + getLocator().getValue());
    }

    @Override
    public void clear() {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.ELEMENT_CLEAR, searchContext.elementId());
        Response response =  HttpMethodExecutor.doPostRequest(endPoint, "{}");
        HandleExceptions.handleResponse(response, "Element wasn't able to be cleared!");
    }

    @Override
    public void sendKeys(String text) {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.ELEMENT_SEND_KEYS, searchContext.elementId());
        Response response =  HttpMethodExecutor.doPostRequest(endPoint, new JsonBuilder().addKeyValue("text", text).build());
        HandleExceptions.handleResponse(response, "Send keys issue");
    }

    @Override
    public String getText() {
        checkSearchContext();
        Response response = HttpMethodExecutor.doGetRequest(EndPoints.buildEndpoint(EndPoints.GET_ELEMENT_TEXT, searchContext.elementId()));
        HandleExceptions.handleResponse(response, "Issue detected trying to fetch getText for WebElement: " + locator.toString());
        return (String) JsonParser.findValueByKey(response, VALUE);
    }

    @Override
    public String getTagName() {
        checkSearchContext();
        Response response = HttpMethodExecutor.doGetRequest(buildEndpoint(EndPoints.GET_ELEMENT_TAG_NAME, searchContext.elementId()));
        HandleExceptions.handleResponse(response,  "Issue detected trying to fetch getTagName for WebElement: " + locator.toString());
        return (String) JsonParser.findValueByKey(response, VALUE);
    }

    @Override
    public String getAttribute(String attributeName) {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.ELEMENT_ATTRIBUTE, searchContext.elementId(), attributeName);
        Response response = HttpMethodExecutor.doGetRequest(endPoint);
        HandleExceptions.handleResponse(response, "Error when trying to fetch getAttribute for WebElement: " + locator.toString());
        return response.getString(VALUE);
    }

    @Override
    public String getProperty(String propertyName) {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.ELEMENT_PROPERTY, searchContext.elementId(), propertyName);
        Object returnValue = HttpMethodExecutor.doGetRequest(endPoint).get(VALUE);
        if (returnValue == null) {
            return null;
        }
        return returnValue.toString();
    }

    @Override
    public boolean isDisplayed() {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.IS_ELEMENT_DISPLAYED, searchContext.elementId());
        Response response = HttpMethodExecutor.doGetRequest(endPoint);
        HandleExceptions.handleResponse(response, "");
        return response.getBoolean(VALUE);
    }

    @Override
    public boolean isEnabled() {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.IS_ELEMENT_ENABLED, searchContext.elementId());
        Response response = HttpMethodExecutor.doGetRequest(endPoint);
        HandleExceptions.handleResponse(response, "");
        return response.getBoolean(VALUE);
    }

    @Override
    public boolean isSelected() {
        checkSearchContext();
        String endPoint = buildEndpoint(EndPoints.IS_ELEMENT_SELECTED, searchContext.elementId());
        Response response = HttpMethodExecutor.doGetRequest(endPoint);
        HandleExceptions.handleResponse(response, "");
        return response.getBoolean(VALUE);
    }

    @Override
    public Locator getLocator() {
        return locator;
    }

    @Override
    public WebElement $(Locator locator) {
        checkSearchContext();
        SearchContext childSearchContext = DriverClient.findElementWithin(searchContext.elementId(), locator);
        return new WebElementImpl(childSearchContext, locator);
    }

    @Override
    public WebElements $$(Locator locator) {
        checkSearchContext();
        List<WebElement> childElements = DriverClient.findElementsWithin(searchContext.elementId(), locator);
        return new WebElementsImpl(childElements, locator);
    }

    @Override
    public SearchContext getSearchContext() {
        return searchContext;
    }

    @Override
    public boolean exists() {
        return searchContext != null;
    }


    @Override
    public WebElement waitTo(Condition conditionToBe) {
        boolean conditionMet = conditionToBe.apply(this.locator);
        if (conditionMet) {
            this.searchContext = ExpectedResult.internalWebElement.get().getSearchContext();
        }
        return this;
    }

}

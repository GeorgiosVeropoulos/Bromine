package elements;

import json.JsonBuilder;
import json.JsonParser;

import java.util.List;

import static Constants.Constants.VALUE;
import static elements.EndPoints.buildEndpoint;

public class WebElementImpl implements WebElement {


    private Locator locator;
    private SearchContext searchContext;


    protected WebElementImpl(SearchContext searchContext, Locator locator) {
        this.searchContext = searchContext;
        this.locator = locator;

    }

    @Override
    public void click() {
        String endPoint = buildEndpoint(EndPoints.ELEMENT_CLICK, searchContext.elementId());
        Response response =  HttpMethodExecutor.doPostRequest(endPoint, "{}");
        HandleExceptions.handleResponse(response, "Element was intercepted using: " + getLocator().getUsing() + " value: " + getLocator().getValue());
    }

    @Override
    public void clear() {
        String endPoint = buildEndpoint(EndPoints.ELEMENT_CLEAR, searchContext.elementId());
        Response response =  HttpMethodExecutor.doPostRequest(endPoint, "{}");
        HandleExceptions.handleResponse(response, "Element wasn't able to be cleared!");
    }

    @Override
    public void sendKeys(String text) {
        String endPoint = buildEndpoint(EndPoints.ELEMENT_SEND_KEYS, searchContext.elementId());
        System.out.println("Will do request on sessionID: " + DriverClient.sessionId());
        Response response =  HttpMethodExecutor.doPostRequest(endPoint, new JsonBuilder().addKeyValue("text", text).build());
        HandleExceptions.handleResponse(response, "Send keys issue");
    }

    @Override
    public String getText() {
        Response response = HttpMethodExecutor.doGetRequest(EndPoints.buildEndpoint(EndPoints.GET_ELEMENT_TEXT, searchContext.elementId()));
        HandleExceptions.handleResponse(response, "Issue detected trying to fetch getText for WebElement: " + locator.toString());
        return (String) JsonParser.findValueByKey(response, VALUE);
    }

    @Override
    public String getTagName() {
        Response response = HttpMethodExecutor.doGetRequest(buildEndpoint(EndPoints.GET_ELEMENT_TAG_NAME, searchContext.elementId()));
        HandleExceptions.handleResponse(response,  "Issue detected trying to fetch getTagName for WebElement: " + locator.toString());
        return (String) JsonParser.findValueByKey(response, VALUE);
    }

    @Override
    public String getAttribute(String attributeName) {
        String endPoint = buildEndpoint(EndPoints.ELEMENT_ATTRIBUTE, searchContext.elementId(), attributeName);
        Response response = HttpMethodExecutor.doGetRequest(endPoint);
        HandleExceptions.handleResponse(response, "Error when trying to fetch getAttribute for WebElement: " + locator.toString());
        return response.getString(VALUE);
    }

    @Override
    public String getProperty(String propertyName) {
        String endPoint = buildEndpoint(EndPoints.ELEMENT_PROPERTY, searchContext.elementId(), propertyName);
        Object returnValue = HttpMethodExecutor.doGetRequest(endPoint).get(VALUE);
        if (returnValue == null) {
            return null;
        }
        return returnValue.toString();
    }

    @Override
    public boolean isDisplayed() {
        String endPoint = buildEndpoint(EndPoints.IS_ELEMENT_DISPLAYED, searchContext.elementId());
        Response response = HttpMethodExecutor.doGetRequest(endPoint);
        HandleExceptions.handleResponse(response, "");
        return response.getBoolean(VALUE);
    }

    @Override
    public boolean isEnabled() {
        String endPoint = buildEndpoint(EndPoints.IS_ELEMENT_ENABLED, searchContext.elementId());
        Response response = HttpMethodExecutor.doGetRequest(endPoint);
        HandleExceptions.handleResponse(response, "");
        return response.getBoolean(VALUE);
    }

    @Override
    public boolean isSelected() {
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
        SearchContext childSearchContext = DriverClient.getElementWithin(searchContext.elementId(), locator);
        return new WebElementImpl(childSearchContext, locator);
    }

    @Override
    public WebElements $$(Locator locator) {
        List<WebElement> childElements = DriverClient.getElementsWithin(searchContext.elementId(), locator);
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






//    The element displayed state is typically exposed as an endpoint for GET requests with a URI Template of
//    /session/{session id}/element/{element id}/displayed.
}

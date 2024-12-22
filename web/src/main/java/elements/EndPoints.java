package elements;


/**
 * All known endpoints available.
 */
public abstract class EndPoints {

    // Base URL
    private static final String SESSION = "/session";

    // === Session Management ===
    protected static final String STATUS = "/status";

    // === Timeouts ===
    protected static final String TIMEOUTS = "/timeouts";

    // === Navigation ===
    protected static final String URL = "/url";
    protected static final String GET_CURRENT_URL = "/url";
    protected static final String BACK = "/back";
    protected static final String FORWARD = "/forward";
    protected static final String REFRESH = "/refresh";

    // === Page Information ===
    protected static final String GET_TITLE = "/title";
    protected static final String GET_PAGE_SOURCE = "/source";

    // === Window Management ===
    protected static final String GET_WINDOW_HANDLE = "/window";
    protected static final String CLOSE_WINDOW = "/window";
    protected static final String SWITCH_TO_WINDOW = "/window";
    protected static final String GET_WINDOW_HANDLES = "/window/handles";
    protected static final String NEW_WINDOW = "/window/new";
    protected static final String GET_WINDOW_RECT = "/window/rect";
    protected static final String SET_WINDOW_RECT = "/window/rect";
    protected static final String MAXIMIZE_WINDOW = "/window/maximize";
    protected static final String MINIMIZE_WINDOW = "/window/minimize";
    protected static final String FULLSCREEN_WINDOW = "/window/fullscreen";

    // === Frame Management ===
    protected static final String SWITCH_TO_FRAME = "/frame";
    protected static final String SWITCH_TO_PARENT_FRAME = "/frame/parent";

    // === Element Management ===
    protected static final String FIND_ELEMENT = "/element";
    protected static final String FIND_ELEMENTS = "/elements";
    protected static final String FIND_ELEMENT_FROM_ELEMENT = "/element/%s/element";
    protected static final String FIND_ELEMENTS_FROM_ELEMENT = "/element/%s/elements";

    protected static final String GET_ELEMENT_TEXT = "/element/%s/text";
    protected static final String GET_ELEMENT_TAG_NAME = "/element/%s/name";
    protected static final String GET_ELEMENT_RECT = "/element/%s/rect";
    protected static final String IS_ELEMENT_DISPLAYED = "/element/%s/displayed";
    protected static final String IS_ELEMENT_ENABLED = "/element/%s/enabled";
    protected static final String IS_ELEMENT_SELECTED = "/element/%s/selected";

    protected static final String ELEMENT_CLICK = "/element/%s/click";
    protected static final String ELEMENT_CLEAR = "/element/%s/clear";
    protected static final String ELEMENT_SEND_KEYS = "/element/%s/value";
    protected static final String ELEMENT_ATTRIBUTE = "/element/%s/attribute/%s";
    protected static final String ELEMENT_PROPERTY = "/element/%s/property/%s";


    // === Alerts ===
    protected static final String DISMISS_ALERT = "/alert/dismiss";
    protected static final String ACCEPT_ALERT = "/alert/accept";
    protected static final String GET_ALERT_TEXT = "/alert/text";
    protected static final String SEND_ALERT_TEXT = "/alert/text";

    // === Cookies ===
    protected static final String GET_ALL_COOKIES = "/cookie";
    protected static final String GET_NAMED_COOKIE = "/cookie/%s";
    protected static final String ADD_COOKIE = "/cookie";
    protected static final String DELETE_COOKIE = "/cookie/%s";
    protected static final String DELETE_ALL_COOKIES = "/%s/cookie";

    // === Screenshots ===
    protected static final String TAKE_SCREENSHOT = "/screenshot";
    protected static final String TAKE_ELEMENT_SCREENSHOT = "/element/%s/screenshot";

    // === Scripting ===
    protected static final String EXECUTE_SCRIPT_SYNC = "/execute/sync";
    protected static final String EXECUTE_SCRIPT_ASYNC = "/execute/async";

    // === Actions ===
    protected static final String PERFORM_ACTIONS = "/actions";
    protected static final String RELEASE_ACTIONS = "/actions";

    // === Printing ===
    protected static final String PRINT_PAGE = "/print";




    // === Utility Method ===

    protected static String buildEndpoint(String endPoint, String... args) {
        return String.format(endPoint, (Object[]) args);
    }

}

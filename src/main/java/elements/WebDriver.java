package elements;


import javax.annotation.WillClose;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Duration;
import java.util.Set;
import java.util.logging.Logger;

// UNDER DESIGN

/**
 * Abstract class all Driver types should extend.
 * Contains all the methods a browser should have based on WebDriverAPI specifications
 */
@ThreadSafe
public abstract class WebDriver {

    protected static final String os = System.getProperty("os.name").toLowerCase();
    protected static final Logger logger = Logger.getLogger(WebDriver.class.getName());
    protected static Process process;



    //ThreadLocal to be used for parallel execution of WebDrivers
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    protected static void runAndShutDownDriver(Runnable createProcess) {
        createProcess.run();
        // Register a shutdown hook to clean up the process when JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process != null && process.isAlive()) {
                process.destroy();
                System.out.println("Driver process destroyed on JVM shutdown");
            }
        }));
    }

    protected WebDriver() {
    }

    public static WebDriver get() {
        return driver.get();
    }


    public WebElement $(Locator locator) {
        return WebElementsFactory.$(locator);
    }

    public WebElements $$(Locator locator) {
        return WebElementsFactory.$$(locator);
    }

    protected void set(WebDriver toSet) {
        driver.set(toSet);
    }


//    @Override
    public void open(String url) {
        DriverClient.openURL(url);
    }

//    @Override
    public String getTitle() {
        return DriverClient.title();
    }

//    @Override
    @WillClose()
    public void close() {
        DriverClient.Contexts.closeWindow();
    }

//    @Override
    @WillClose
    public void quit() {
        DriverClient.closeSession();
        driver.remove();
    }



//    @Override
    public Driver.Navigation navigation() {
        return new navigation();
    }


    private static class navigation implements Driver.Navigation {

        @Override
        public void to(String url) {
            DriverClient.openURL(url);
        }

        @Override
        public String getCurrentUrl() {
            return DriverClient.getUrl();
        }

        @Override
        public void back() {
            DriverClient.back();
        }

        @Override
        public void forward() {
            DriverClient.forward();
        }

        @Override
        public void refresh() {
            DriverClient.refresh();
        }
    }

//    @Override
    public Driver.SwitchTo switchTo() {
        return new SwitchTo();
    }

    private static class SwitchTo implements Driver.SwitchTo {

        @Override
        public WebDriver frame(Number index) {
            DriverClient.Contexts.switchToFrame(index);
            return WebDriver.get();
        }

        @Override
        public WebDriver frame(WebElement element) {
            DriverClient.Contexts.switchToFrame(element);
            return WebDriver.get();
        }

        @Override
        public void window(String handle) {
            DriverClient.Contexts.switchToWindow(handle);
        }

        @Override
        public void defaultContent() {
            DriverClient.Contexts.switchToDefaultContent();
        }

        @Override
        public void newWindow() { DriverClient.Contexts.newWindow(); }

        @Override
        public void newTab() { DriverClient.Contexts.newTab(); }
    }


//    @Override
    public Driver.Contexts window() {
        return new context();
    }

    private static class context implements Driver.Contexts {

        @Override
        public void maximize() {
            DriverClient.maximize();
        }

        @Override
        public void minimize() {
            DriverClient.minimize();
        }

        @Override
        public void fullscreen() {
            DriverClient.fullscreen();
        }

        @Override
        public Set<String> getWindowHandles() {
            return DriverClient.Contexts.getWindowHandles();
        }

        @Override
        public String getWindowHandle() {
            return DriverClient.Contexts.getWindowHandle();
        }
    }

//    @Override
    public Driver.Timeouts timeouts() {
        return new Timeouts();
    }


    private static class Timeouts implements Driver.Timeouts {


        @Override
        public Get get() {
            return new get();
        }

        private static class get implements Driver.Timeouts.Get {

            @Override
            public Duration implicitWait() {
                return DriverClient.getImplicitWait();
            }

            @Override
            public Duration scriptWait() {
                return DriverClient.getScriptWait();
            }

            @Override
            public Duration pageLoad() {
                return DriverClient.getPageLoad();
            }
        }

        @Override
        public Set set() {
            return new set();
        }

        private static class set implements Driver.Timeouts.Set {

            @Override
            public void implicitWait(Duration duration) {
                DriverClient.setImplicitWait(duration);
            }

            @Override
            public void scriptWait(Duration duration) {
                DriverClient.setScriptWait(duration);
            }

            @Override
            public void pageLoad(Duration duration) {
                DriverClient.setPageLoad(duration);
            }
        }
    }


}

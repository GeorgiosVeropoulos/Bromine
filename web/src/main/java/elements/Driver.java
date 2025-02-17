package elements;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Set;

/**
 * Interface for all WebDriverAPI actions
 */
interface Driver {

    Timeouts timeouts();

    interface Timeouts {

        Get get();
        Set set();

         interface Get {
            Duration implicitWait();
            Duration scriptWait();
            Duration pageLoad();
        }

        interface Set {
            void implicitWait(Duration duration);
            void scriptWait(Duration duration);
            void pageLoad(Duration duration);
        }
    }


    Contexts window();

    interface Contexts {
        void maximize();
        void minimize();
        void fullscreen();
        Set<String> getWindowHandles();
        String getWindowHandle();



    }

    SwitchTo switchTo();

    interface SwitchTo {
        WebDriver frame(Number number);
        WebDriver frame(WebElement element);
        void window(String handle);
        void defaultContent();

        void newWindow();

        void newTab();
    }


    Navigation navigation();
    interface Navigation {

        void to(String url);

        @Nullable
        String getCurrentUrl();
        void back();

        void forward();

        void refresh();





    }

    //build the rest...


}

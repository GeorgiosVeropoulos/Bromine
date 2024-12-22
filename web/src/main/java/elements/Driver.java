package elements;

import annotations.Nullable;

import java.time.Duration;
import java.util.Set;

interface Driver {




//    void open(String url);
//
//    @Nullable String getTitle();
//
//    void close();
//    void quit();

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

        @Nullable String getCurrentUrl();
        void back();

        void forward();

        void refresh();





    }

    //build the rest...


}

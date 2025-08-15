package chrome.interfaces;

import chrome.enums.SupportedBinaries;
import chrome.enums.SupportedChannels;

import java.nio.file.Path;

public non-sealed interface LastKnownGoodBuilder extends ChromeForTestingEndPointBuilder<LastKnownGoodBuilder> {

    /**
     * The specific channel we want to fetch.
     * @see SupportedChannels
     */
    LastKnownGoodBuilder withChannel(SupportedChannels channel);
}
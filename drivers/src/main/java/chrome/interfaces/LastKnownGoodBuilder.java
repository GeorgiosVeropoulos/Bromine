package chrome.interfaces;

import chrome.enums.SupportedBinaries;
import chrome.enums.SupportedChannels;

import java.nio.file.Path;

public non-sealed interface LastKnownGoodBuilder extends EndPointBuilder {
    LastKnownGoodBuilder withBinary(SupportedBinaries binary);
    LastKnownGoodBuilder withChannel(SupportedChannels channel);
    LastKnownGoodBuilder downloadTo(Path path);
}
package chrome.jsons.channels;

import lombok.Getter;

@Getter
public class Channels {


    private Stable Stable;
    private Beta Beta;
    private Dev Dev;
    private Canary Canary;

    public Channels(Stable Stable, Beta Beta, Dev Dev, Canary Canary) {
        this.Stable = Stable;
        this.Beta = Beta;
        this.Dev = Dev;
        this.Canary = Canary;
    }

    public Channels() {
        // Needed for reflection
    }
}

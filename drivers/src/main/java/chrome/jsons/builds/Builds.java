package chrome.jsons.builds;

import java.util.LinkedHashMap;

public class Builds extends LinkedHashMap<String, Build> {


    public Builds() {
        super();
    }

    public Build getBuild(String version) {
        return this.get(version);
    }

}

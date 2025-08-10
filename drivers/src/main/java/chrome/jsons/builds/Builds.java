package chrome.jsons.builds;

import lombok.Getter;

import java.util.List;

@Getter
public class Builds {

    List<Build> builds;

    public Builds() {}


    public Builds(List<Build> builds) {
        this.builds = builds;
    }
}

package chrome.jsons.builds;

import chrome.jsons.Downloads;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Build {

    String version;
    String revision;
    Downloads downloads;

    public Build() {}

    public Build(String version, String revision, Downloads downloads) {
        this.version = version;
        this.revision = revision;
        this.downloads = downloads;
    }
}

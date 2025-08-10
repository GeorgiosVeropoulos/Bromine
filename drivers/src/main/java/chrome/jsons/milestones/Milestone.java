package chrome.jsons.milestones;

import chrome.Version;
import chrome.jsons.Downloads;
import lombok.Getter;
import lombok.Setter;



public class Milestone {


    @Getter @Setter
    private String milestone;
            @Setter
    private String version; // we do the getter manually to provide a Version class version of it.
    @Getter @Setter
    private String revision;
    @Getter @Setter
    private Downloads downloads;

    public Milestone() {

    }

    public Version getVersion() {
        return new Version(this.version);
    }

}

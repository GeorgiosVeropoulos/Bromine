package firefox.jsons;

import json.annotations.JsonName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Asset {

    String url;
    Integer id;
    String node_id;
    String name;
    String label;
    Uploader uploader;
    String content_type;
    String state;
    @JsonName("size")
    Integer _size;
    Object digest;
    Integer download_count;
    String created_at;
    String updated_at;
    String browser_download_url;

}

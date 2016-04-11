package misszero.Entities;

public class LinkEntity {

    private String code;
    private int type;
    private String url;
    private boolean downloaded;
    private String path;

    public LinkEntity(String code, int type, String url, boolean downloaded, String path) {

        this.code = code;
        this.type = type;
        this.url = url;
        this.downloaded = downloaded;
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package ir.ac.kntu.Entity;

public class VersionFeature {
    private String text;
    private int versionCode;


    public VersionFeature(String text, int versionCode) {
        this.text = text;
        this.versionCode = versionCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}

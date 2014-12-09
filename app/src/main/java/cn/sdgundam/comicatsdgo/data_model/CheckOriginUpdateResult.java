package cn.sdgundam.comicatsdgo.data_model;

/**
 * Created by xhguo on 12/9/2014.
 */
public class CheckOriginUpdateResult {
    boolean hasUpdate;
    OriginInfo[] origins;

    public CheckOriginUpdateResult() { }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public OriginInfo[] getOrigins() {
        return origins;
    }

    public void setOrigins(OriginInfo[] origins) {
        this.origins = origins;
    }
}

package cn.sdgundam.comicatsdgo.api_model;

import java.io.Serializable;

/**
 * Created by xhguo on 9/25/2014.
 */
public class UnitInfoShort implements Serializable {
    String unitId;
    String modelName;
    String rank;
    String warType;
    int origin;
    String originTitleShort;
    float rating;

    public UnitInfoShort() { }

    public UnitInfoShort(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getWarType() {
        return warType;
    }

    public void setWarType(String warType) {
        this.warType = warType;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getOriginTitleShort() {
        return originTitleShort;
    }

    public void setOriginTitleShort(String originTitleShort) {
        this.originTitleShort = originTitleShort;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

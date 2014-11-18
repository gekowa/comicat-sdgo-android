package cn.sdgundam.comicatsdgo.data_model;

import java.io.Serializable;

/**
 * Created by xhguo on 9/25/2014.
 */
public class UnitMixMaterial implements Serializable {

    String unitId;
    String modelName;
    String level;

    public UnitMixMaterial(String unitId, String modelName, String level) {
        this.unitId = unitId;
        this.modelName = modelName;
        this.level = level;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

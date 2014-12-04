package cn.sdgundam.comicatsdgo.data_model;

import java.util.Date;
import java.util.List;

/**
 * Created by xhguo on 12/4/2014.
 */
public class UnitList {
    Date generated;
    List<UnitInfoShort> units;

    String origin;
    String searchKeyword;

    public Date getGenerated() {
        return generated;
    }

    public void setGenerated(Date generated) {
        this.generated = generated;
    }

    public List<UnitInfoShort> getUnits() {
        return units;
    }

    public void setUnits(List<UnitInfoShort> units) {
        this.units = units;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}

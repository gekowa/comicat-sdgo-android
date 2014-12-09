package cn.sdgundam.comicatsdgo.data_model;

/**
 * Created by xhguo on 12/9/2014.
 */
public class OriginInfo {
    String originIndex;
    String title;
    String shortTitle;
    int numberOfUnits;
    int displayOrder;

    public OriginInfo() {
    }

    public OriginInfo(String originIndex, String title, String shortTitle, int numberOfUnits) {
        this.originIndex = originIndex;
        this.title = title;
        this.shortTitle = shortTitle;
        this.numberOfUnits = numberOfUnits;
    }

    public static final OriginInfo[] builtInOrigins = new OriginInfo[] {
            new OriginInfo("01", "机动战士 高达", "高达", 85),
            new OriginInfo("02", "机动战士 高达 08MS小队", "08MS", 21),
            new OriginInfo("03", "机动战士 高达 0080", "0080", 19),
            new OriginInfo("04", "机动战士 高达 0083", "0083", 31),
            new OriginInfo("05", "机动战士 Z高达", "Z高达", 70),
            new OriginInfo("06", "机动战士 高达ZZ", "ZZ高达", 36),
            new OriginInfo("07", "机动战士 高达 逆袭的夏亚", "逆袭的夏亚", 20),
            new OriginInfo("19", "机动战士 高达 UC", "UC", 51),
            new OriginInfo("08", "机动战士 高达 F91", "F91", 25),
            new OriginInfo("09", "机动战士 V高达", "V高达", 11),
            new OriginInfo("10", "机动武斗传 G高达", "G高达", 26),
            new OriginInfo("11", "新机动战记 高达W", "高达W", 21),
            new OriginInfo("12", "新机动战记 高达W 无尽的华尔兹", "W 华尔兹", 14),
            new OriginInfo("13", "新机动世纪 高达X", "高达X", 14),
            new OriginInfo("14", "倒A 高达", "∀", 7),
            new OriginInfo("15", "机动战士 高达 SEED", "SEED", 84),
            new OriginInfo("16", "机动战士 高达 SEED-DESTINY", "SEED-D", 59),
            new OriginInfo("17", "BB战士 三国传 风云豪杰篇", "三国风云", 26),
            new OriginInfo("18", "机动战士 高达00", "高达00", 99),
            new OriginInfo("20", "机动战士 高达AGE", "高达AGE", 17),
            new OriginInfo("21", "模型战士 高达模型大师 BEGINNING G", "BEGINNING G", 3),
            new OriginInfo("22", "高达创战者", "BF", 19)
    };

    public String getOriginIndex() {
        return originIndex;
    }

    public void setOriginIndex(String originIndex) {
        this.originIndex = originIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}

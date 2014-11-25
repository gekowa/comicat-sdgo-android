package cn.sdgundam.comicatsdgo.data_model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xhguo on 9/25/2014.
 */
public class UnitInfo implements Serializable {

    Date generated;

    // basic
    String unitId;
    String modelName;
    String rank;
    String warType;
    String landform;
    Integer origin;
    String originTitle;
    String story;
    String regdate;
    String driver;
    String feature;
    String howToGet;

    // 4p
    Boolean sniping = false;
    Boolean modification = false;
    Boolean oversize = false;
    Boolean repair = false;

    // stats
    Float attackG;
    Float defenseG;
    Float mobilityG;
    Float controlG;

    // weapons
    Integer weapon1;
    Integer weapon2;
    Integer weapon3;
    Integer weapon4;
    Integer weapon5;
    Integer weapon6;
    String weaponName1;
    String weaponName2;
    String weaponName3;
    String weaponName4;
    String weaponName5;
    String weaponName6;
    String weaponEffect1;
    String weaponEffect2;
    String weaponEffect3;
    String weaponEffect4;
    String weaponEffect5;
    String weaponEffect6;
    String weaponProperty1;
    String weaponProperty2;
    String weaponProperty3;
    String weaponProperty4;
    String weaponProperty5;
    String weaponProperty6;

    String weaponRange1;
    String weaponRange2;
    String weaponRange3;
    String weaponRange4;
    String weaponRange5;
    String weaponRange6;

    String weaponEx1Line1;
    String weaponEx1Line2;
    String weaponEx2Line1;
    String weaponEx2Line2;
    String weaponEx3Line1;
    String weaponEx3Line2;
    String weaponEx4Line1;
    String weaponEx4Line2;
    String weaponEx5Line1;
    String weaponEx5Line2;
    String weaponEx6Line1;
    String weaponEx6Line2;

    // skills
    Integer skill1;
    Integer skill2;
    Integer skill3;
    String skillName1;
    String skillName2;
    String skillName3;
    String skillDesc1;
    String skillDesc2;
    String skillDesc3;
    String skillEx1;
    String skillEx2;
    String skillEx3;

    Integer ratings;
    Float ratingValue;

    String groupName1;
    String groupName2;

    String shopBuy;
    String shopBuyPrice;
    String shopRissCash;
// String shopRissCashPrice;
    String shopRissPoint;
// String shopRissPoIntegerPrice;
    String shopMixBuy;
    String etc;
    String capsule1;
    String capsule2;
    String capsule3;
    String capsule4;
    String quest1;
    String quest2;
    String mission1;
    String mission2;
    String mission3;
    String mission4;
    String mission5;
    String lab1;
    String lab2;

    VideoListItem[] videoList;

    UnitMixMaterial mixingKeyUnit;
    UnitMixMaterial[] mixingMaterialUnits;

    UnitMixMaterial mixingKeyUnitCN;
    UnitMixMaterial[] mixingMaterialUnitsCN; // array of UnitMixMaterial

    UnitMixMaterial[] canMixAsKey; // array of UnitMixMaterial
    UnitMixMaterial[] canMixAsMaterial; // array of UnitMixMaterial

    public UnitInfo() {}
    public UnitInfo(String unitId, String modelName) {
        this.unitId = unitId;
        this.modelName = modelName;
    }

    public Date getGenerated() {
        return generated;
    }

    public void setGenerated(Date generated) {
        this.generated = generated;
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

    public String getLandform() {
        return landform;
    }

    public void setLandform(String landform) {
        this.landform = landform;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public void setOriginTitle(String originTitle) {
        this.originTitle = originTitle;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getHowToGet() {
        return howToGet;
    }

    public void setHowToGet(String howToGet) {
        this.howToGet = howToGet;
    }

    public Boolean isSniping() {
        return sniping;
    }

    public void setSniping(Boolean sniping) {
        this.sniping = sniping;
    }

    public Boolean isModification() {
        return modification;
    }

    public void setModification(Boolean modification) {
        this.modification = modification;
    }

    public Boolean isOversize() {
        return oversize;
    }

    public void setOversize(Boolean oversize) {
        this.oversize = oversize;
    }

    public Boolean isRepair() {
        return repair;
    }

    public void setRepair(Boolean repair) {
        this.repair = repair;
    }

    public Float getAttackG() {
        return attackG;
    }

    public void setAttackG(Float attackG) {
        this.attackG = attackG;
    }

    public Float getDefenseG() {
        return defenseG;
    }

    public void setDefenseG(Float defenseG) {
        this.defenseG = defenseG;
    }

    public Float getMobilityG() {
        return mobilityG;
    }

    public void setMobilityG(Float mobilityG) {
        this.mobilityG = mobilityG;
    }

    public Float getControlG() {
        return controlG;
    }

    public void setControlG(Float controlG) {
        this.controlG = controlG;
    }

    public Float getSum3D() {
        return attackG + defenseG + mobilityG;
    }

    public Float getSum4D() {
        return getSum3D() + controlG;
    }

    public Integer getWeapon1() {
        return weapon1;
    }

    public void setWeapon1(Integer weapon1) {
        this.weapon1 = weapon1;
    }

    public Integer getWeapon2() {
        return weapon2;
    }

    public void setWeapon2(Integer weapon2) {
        this.weapon2 = weapon2;
    }

    public Integer getWeapon3() {
        return weapon3;
    }

    public void setWeapon3(Integer weapon3) {
        this.weapon3 = weapon3;
    }

    public Integer getWeapon4() {
        return weapon4;
    }

    public void setWeapon4(Integer weapon4) {
        this.weapon4 = weapon4;
    }

    public Integer getWeapon5() {
        return weapon5;
    }

    public void setWeapon5(Integer weapon5) {
        this.weapon5 = weapon5;
    }

    public Integer getWeapon6() {
        return weapon6;
    }

    public void setWeapon6(Integer weapon6) {
        this.weapon6 = weapon6;
    }

    public String getWeaponName1() {
        return weaponName1;
    }

    public void setWeaponName1(String weaponName1) {
        this.weaponName1 = weaponName1;
    }

    public String getWeaponName2() {
        return weaponName2;
    }

    public void setWeaponName2(String weaponName2) {
        this.weaponName2 = weaponName2;
    }

    public String getWeaponName3() {
        return weaponName3;
    }

    public void setWeaponName3(String weaponName3) {
        this.weaponName3 = weaponName3;
    }

    public String getWeaponName4() {
        return weaponName4;
    }

    public void setWeaponName4(String weaponName4) {
        this.weaponName4 = weaponName4;
    }

    public String getWeaponName5() {
        return weaponName5;
    }

    public void setWeaponName5(String weaponName5) {
        this.weaponName5 = weaponName5;
    }

    public String getWeaponName6() {
        return weaponName6;
    }

    public void setWeaponName6(String weaponName6) {
        this.weaponName6 = weaponName6;
    }

    public String getWeaponEffect1() {
        return weaponEffect1;
    }

    public void setWeaponEffect1(String weaponEffect1) {
        this.weaponEffect1 = weaponEffect1;
    }

    public String getWeaponEffect2() {
        return weaponEffect2;
    }

    public void setWeaponEffect2(String weaponEffect2) {
        this.weaponEffect2 = weaponEffect2;
    }

    public String getWeaponEffect3() {
        return weaponEffect3;
    }

    public void setWeaponEffect3(String weaponEffect3) {
        this.weaponEffect3 = weaponEffect3;
    }

    public String getWeaponEffect4() {
        return weaponEffect4;
    }

    public void setWeaponEffect4(String weaponEffect4) {
        this.weaponEffect4 = weaponEffect4;
    }

    public String getWeaponEffect5() {
        return weaponEffect5;
    }

    public void setWeaponEffect5(String weaponEffect5) {
        this.weaponEffect5 = weaponEffect5;
    }

    public String getWeaponEffect6() {
        return weaponEffect6;
    }

    public void setWeaponEffect6(String weaponEffect6) {
        this.weaponEffect6 = weaponEffect6;
    }

    public String getWeaponProperty1() {
        return weaponProperty1;
    }

    public void setWeaponProperty1(String weaponProperty1) {
        this.weaponProperty1 = weaponProperty1;
    }

    public String getWeaponProperty2() {
        return weaponProperty2;
    }

    public void setWeaponProperty2(String weaponProperty2) {
        this.weaponProperty2 = weaponProperty2;
    }

    public String getWeaponProperty3() {
        return weaponProperty3;
    }

    public void setWeaponProperty3(String weaponProperty3) {
        this.weaponProperty3 = weaponProperty3;
    }

    public String getWeaponProperty4() {
        return weaponProperty4;
    }

    public void setWeaponProperty4(String weaponProperty4) {
        this.weaponProperty4 = weaponProperty4;
    }

    public String getWeaponProperty5() {
        return weaponProperty5;
    }

    public void setWeaponProperty5(String weaponProperty5) {
        this.weaponProperty5 = weaponProperty5;
    }

    public String getWeaponProperty6() {
        return weaponProperty6;
    }

    public void setWeaponProperty6(String weaponProperty6) {
        this.weaponProperty6 = weaponProperty6;
    }

    public String getWeaponRange1() {
        return weaponRange1;
    }

    public void setWeaponRange1(String weaponRange1) {
        this.weaponRange1 = weaponRange1;
    }

    public String getWeaponRange2() {
        return weaponRange2;
    }

    public void setWeaponRange2(String weaponRange2) {
        this.weaponRange2 = weaponRange2;
    }

    public String getWeaponRange3() {
        return weaponRange3;
    }

    public void setWeaponRange3(String weaponRange3) {
        this.weaponRange3 = weaponRange3;
    }

    public String getWeaponRange4() {
        return weaponRange4;
    }

    public void setWeaponRange4(String weaponRange4) {
        this.weaponRange4 = weaponRange4;
    }

    public String getWeaponRange5() {
        return weaponRange5;
    }

    public void setWeaponRange5(String weaponRange5) {
        this.weaponRange5 = weaponRange5;
    }

    public String getWeaponRange6() {
        return weaponRange6;
    }

    public void setWeaponRange6(String weaponRange6) {
        this.weaponRange6 = weaponRange6;
    }

    public String getWeaponEx1Line1() {
        return weaponEx1Line1;
    }

    public void setWeaponEx1Line1(String weaponEx1Line1) {
        this.weaponEx1Line1 = weaponEx1Line1;
    }

    public String getWeaponEx1Line2() {
        return weaponEx1Line2;
    }

    public void setWeaponEx1Line2(String weaponEx1Line2) {
        this.weaponEx1Line2 = weaponEx1Line2;
    }

    public String getWeaponEx2Line1() {
        return weaponEx2Line1;
    }

    public void setWeaponEx2Line1(String weaponEx2Line1) {
        this.weaponEx2Line1 = weaponEx2Line1;
    }

    public String getWeaponEx2Line2() {
        return weaponEx2Line2;
    }

    public void setWeaponEx2Line2(String weaponEx2Line2) {
        this.weaponEx2Line2 = weaponEx2Line2;
    }

    public String getWeaponEx3Line1() {
        return weaponEx3Line1;
    }

    public void setWeaponEx3Line1(String weaponEx3Line1) {
        this.weaponEx3Line1 = weaponEx3Line1;
    }

    public String getWeaponEx3Line2() {
        return weaponEx3Line2;
    }

    public void setWeaponEx3Line2(String weaponEx3Line2) {
        this.weaponEx3Line2 = weaponEx3Line2;
    }

    public String getWeaponEx4Line1() {
        return weaponEx4Line1;
    }

    public void setWeaponEx4Line1(String weaponEx4Line1) {
        this.weaponEx4Line1 = weaponEx4Line1;
    }

    public String getWeaponEx4Line2() {
        return weaponEx4Line2;
    }

    public void setWeaponEx4Line2(String weaponEx4Line2) {
        this.weaponEx4Line2 = weaponEx4Line2;
    }

    public String getWeaponEx5Line1() {
        return weaponEx5Line1;
    }

    public void setWeaponEx5Line1(String weaponEx5Line1) {
        this.weaponEx5Line1 = weaponEx5Line1;
    }

    public String getWeaponEx5Line2() {
        return weaponEx5Line2;
    }

    public void setWeaponEx5Line2(String weaponEx5Line2) {
        this.weaponEx5Line2 = weaponEx5Line2;
    }

    public String getWeaponEx6Line1() {
        return weaponEx6Line1;
    }

    public void setWeaponEx6Line1(String weaponEx6Line1) {
        this.weaponEx6Line1 = weaponEx6Line1;
    }

    public String getWeaponEx6Line2() {
        return weaponEx6Line2;
    }

    public void setWeaponEx6Line2(String weaponEx6Line2) {
        this.weaponEx6Line2 = weaponEx6Line2;
    }

    public Integer getNumberOfWeapons() {
        if (weapon4 == 0) {
            return 3;
        } else if (weapon5 == 0) {
            return 4;
        } else if (weapon6 == 0) {
            return 5;
        } else {
            return 6;
        }
    }

    public Integer getSkill1() {
        return skill1;
    }

    public void setSkill1(Integer skill1) {
        this.skill1 = skill1;
    }

    public Integer getSkill2() {
        return skill2;
    }

    public void setSkill2(Integer skill2) {
        this.skill2 = skill2;
    }

    public Integer getSkill3() {
        return skill3;
    }

    public void setSkill3(Integer skill3) {
        this.skill3 = skill3;
    }

    public String getSkillName1() {
        return skillName1;
    }

    public void setSkillName1(String skillName1) {
        this.skillName1 = skillName1;
    }

    public String getSkillName2() {
        return skillName2;
    }

    public void setSkillName2(String skillName2) {
        this.skillName2 = skillName2;
    }

    public String getSkillName3() {
        return skillName3;
    }

    public void setSkillName3(String skillName3) {
        this.skillName3 = skillName3;
    }

    public String getSkillDesc1() {
        return skillDesc1;
    }

    public void setSkillDesc1(String skillDesc1) {
        this.skillDesc1 = skillDesc1;
    }

    public String getSkillDesc2() {
        return skillDesc2;
    }

    public void setSkillDesc2(String skillDesc2) {
        this.skillDesc2 = skillDesc2;
    }

    public String getSkillDesc3() {
        return skillDesc3;
    }

    public void setSkillDesc3(String skillDesc3) {
        this.skillDesc3 = skillDesc3;
    }

    public String getSkillEx1() {
        return skillEx1;
    }

    public void setSkillEx1(String skillEx1) {
        this.skillEx1 = skillEx1;
    }

    public String getSkillEx2() {
        return skillEx2;
    }

    public void setSkillEx2(String skillEx2) {
        this.skillEx2 = skillEx2;
    }

    public String getSkillEx3() {
        return skillEx3;
    }

    public void setSkillEx3(String skillEx3) {
        this.skillEx3 = skillEx3;
    }

    public Integer getRatings() {
        return ratings;
    }

    public void setRatings(Integer ratings) {
        this.ratings = ratings;
    }

    public Float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getGroupName1() {
        return groupName1;
    }

    public void setGroupName1(String groupName1) {
        this.groupName1 = groupName1;
    }

    public String getGroupName2() {
        return groupName2;
    }

    public void setGroupName2(String groupName2) {
        this.groupName2 = groupName2;
    }

    public String getShopBuy() {
        return shopBuy;
    }

    public void setShopBuy(String shopBuy) {
        this.shopBuy = shopBuy;
    }

    public String getShopBuyPrice() {
        return shopBuyPrice;
    }

    public void setShopBuyPrice(String shopBuyPrice) {
        this.shopBuyPrice = shopBuyPrice;
    }

    public String getShopRissCash() {
        return shopRissCash;
    }

    public void setShopRissCash(String shopRissCash) {
        this.shopRissCash = shopRissCash;
    }

    public String getShopRissPoint() {
        return shopRissPoint;
    }

    public void setShopRissPoint(String shopRissPoint) {
        this.shopRissPoint = shopRissPoint;
    }

    public String getShopMixBuy() {
        return shopMixBuy;
    }

    public void setShopMixBuy(String shopMixBuy) {
        this.shopMixBuy = shopMixBuy;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getCapsule1() {
        return capsule1;
    }

    public void setCapsule1(String capsule1) {
        this.capsule1 = capsule1;
    }

    public String getCapsule2() {
        return capsule2;
    }

    public void setCapsule2(String capsule2) {
        this.capsule2 = capsule2;
    }

    public String getCapsule3() {
        return capsule3;
    }

    public void setCapsule3(String capsule3) {
        this.capsule3 = capsule3;
    }

    public String getCapsule4() {
        return capsule4;
    }

    public void setCapsule4(String capsule4) {
        this.capsule4 = capsule4;
    }

    public String getQuest1() {
        return quest1;
    }

    public void setQuest1(String quest1) {
        this.quest1 = quest1;
    }

    public String getQuest2() {
        return quest2;
    }

    public void setQuest2(String quest2) {
        this.quest2 = quest2;
    }

    public String getMission1() {
        return mission1;
    }

    public void setMission1(String mission1) {
        this.mission1 = mission1;
    }

    public String getMission2() {
        return mission2;
    }

    public void setMission2(String mission2) {
        this.mission2 = mission2;
    }

    public String getMission3() {
        return mission3;
    }

    public void setMission3(String mission3) {
        this.mission3 = mission3;
    }

    public String getMission4() {
        return mission4;
    }

    public void setMission4(String mission4) {
        this.mission4 = mission4;
    }

    public String getMission5() {
        return mission5;
    }

    public void setMission5(String mission5) {
        this.mission5 = mission5;
    }

    public String getLab1() {
        return lab1;
    }

    public void setLab1(String lab1) {
        this.lab1 = lab1;
    }

    public String getLab2() {
        return lab2;
    }

    public void setLab2(String lab2) {
        this.lab2 = lab2;
    }

    public VideoListItem[] getVideoList() {
        return videoList;
    }

    public void setVideoList(VideoListItem[] videoList) {
        this.videoList = videoList;
    }

    public UnitMixMaterial getMixingKeyUnit() {
        return mixingKeyUnit;
    }

    public void setMixingKeyUnit(UnitMixMaterial mixingKeyUnit) {
        this.mixingKeyUnit = mixingKeyUnit;
    }

    public UnitMixMaterial[] getMixingMaterialUnits() {
        return mixingMaterialUnits;
    }

    public void setMixingMaterialUnits(UnitMixMaterial[] mixingMaterialUnits) {
        this.mixingMaterialUnits = mixingMaterialUnits;
    }

    public UnitMixMaterial getMixingKeyUnitCN() {
        return mixingKeyUnitCN;
    }

    public void setMixingKeyUnitCN(UnitMixMaterial mixingKeyUnitCN) {
        this.mixingKeyUnitCN = mixingKeyUnitCN;
    }

    public UnitMixMaterial[] getMixingMaterialUnitsCN() {
        return mixingMaterialUnitsCN;
    }

    public void setMixingMaterialUnitsCN(UnitMixMaterial[] mixingMaterialUnitsCN) {
        this.mixingMaterialUnitsCN = mixingMaterialUnitsCN;
    }

    public UnitMixMaterial[] getCanMixAsKey() {
        return canMixAsKey;
    }

    public void setCanMixAsKey(UnitMixMaterial[] canMixAsKey) {
        this.canMixAsKey = canMixAsKey;
    }

    public UnitMixMaterial[] getCanMixAsMaterial() {
        return canMixAsMaterial;
    }

    public void setCanMixAsMaterial(UnitMixMaterial[] canMixAsMaterial) {
        this.canMixAsMaterial = canMixAsMaterial;
    }
}

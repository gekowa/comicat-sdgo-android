package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import cn.sdgundam.comicatsdgo.api_model.UnitInfo;
import cn.sdgundam.comicatsdgo.R;

/**
 * Created by xhguo on 11/23/2014.
 */
public class UnitGetwayView extends LinearLayout {
    UnitInfo unitInfo;

    private TextView capsuleTV, buyTV, buyMixTV, questTV, missionTV, labTV, etcTV,
        capsuleCaptionTV, buyCaptionTV, questCaptionTV, missionCaptionTV, labCaptionTV, etcCaptionTV;

    public UnitGetwayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_unit_getway, this, true);
        setOrientation(VERTICAL);

        capsuleTV = (TextView)findViewById(R.id.unit_getway_capsule);
        buyTV = (TextView)findViewById(R.id.unit_getway_buy);
        buyMixTV = (TextView)findViewById(R.id.unit_getway_buy_mix);
        questTV = (TextView)findViewById(R.id.unit_getway_quest);
        missionTV = (TextView)findViewById(R.id.unit_getway_mission);
        labTV = (TextView)findViewById(R.id.unit_getway_lab);
        etcTV = (TextView)findViewById(R.id.unit_getway_etc);

        capsuleCaptionTV = (TextView)findViewById(R.id.unit_getway_capsule_caption);
        buyCaptionTV = (TextView)findViewById(R.id.unit_getway_buy_caption);
        questCaptionTV = (TextView)findViewById(R.id.unit_getway_quest_caption);
        missionCaptionTV = (TextView)findViewById(R.id.unit_getway_mission_caption);
        labCaptionTV = (TextView)findViewById(R.id.unit_getway_lab_caption);
        etcCaptionTV = (TextView)findViewById(R.id.unit_getway_etc_caption);
    }

    public void setUnitInfo(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;

        setupCapsule(unitInfo);
        setupShopBuy(unitInfo);
        setupShopMixBuy(unitInfo);
        setupQuest(unitInfo);
        setupMission(unitInfo);
        setupLab(unitInfo);
        setupEtc(unitInfo);

        requestLayout();
        invalidate();

    }

    private void setupCapsule(UnitInfo unitInfo) {
        boolean showCapsules = false;
        ArrayList<String> capsules = new ArrayList<String>();

        try {
            for (int i = 0; i < 4; i++) {
                String capsule = (String) UnitInfo.class.getMethod("getCapsule" + (i + 1)).invoke(unitInfo);
                showCapsules |= capsule.length() > 0;
                if(capsule.length() > 0) {
                    capsules.add(capsule);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (showCapsules) {
            for (int i = 0; i < capsules.size(); i++) {
                capsuleTV.append(String.format("[%s] ", capsules.get(i)));
            }

            capsuleTV.setVisibility(VISIBLE);
            capsuleCaptionTV.setVisibility(VISIBLE);
        }
    }

    private void setupShopBuy(UnitInfo unitInfo) {
        boolean showShop = (unitInfo.getShopBuy().length() + unitInfo.getShopRissCash().length() + unitInfo.getShopRissPoint().length()) > 0;
        if (showShop) {

            if (unitInfo.getShopBuy().length() > 0) {
                buyTV.append("[可购买] ");
            }

            if (unitInfo.getShopRissCash().length() > 0) {
                buyTV.append("[可以CASH出租] ");
            }

            if (unitInfo.getShopRissPoint().length() > 0) {
                buyTV.append("[可以点数出租] ");
            }

            buyTV.setVisibility(VISIBLE);
            buyCaptionTV.setVisibility(VISIBLE);
        }
    }

    private void setupShopMixBuy(UnitInfo unitInfo) {
        if (unitInfo.getShopMixBuy().length() > 0) {
            buyMixTV.setText("购买合成图");
            buyMixTV.setVisibility(VISIBLE);
        }
    }

    private void setupQuest(UnitInfo unitInfo) {
        boolean showQuest = false;
        ArrayList<String> quests = new ArrayList<String>();

        try {
            for (int i = 0; i < 2; i++) {
                String quest = (String) UnitInfo.class.getMethod("getQuest" + (i + 1)).invoke(unitInfo);
                showQuest |= quest.length() > 0;
                if (quest.length() > 0) {
                    quests.add(quest);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (showQuest) {
            for (int i = 0; i < quests.size(); i++) {
                questTV.append(String.format("[%s]\n", quests.get(i)));
            }
            questTV.setVisibility(VISIBLE);
            questCaptionTV.setVisibility(VISIBLE);
        }
    }

    private void setupMission(UnitInfo unitInfo) {
        boolean showMission = false;
        ArrayList<String> missions = new ArrayList<String>();

        try {
            for (int i = 0; i < 5; i++) {
                String mission = (String) UnitInfo.class.getMethod("getMission" + (i + 1)).invoke(unitInfo);
                showMission |= mission.length() > 0;
                if (mission.length() > 0) {
                    missions.add(mission);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (showMission) {
            for (int i = 0; i < missions.size(); i++) {
                missionTV.append(String.format("[%s]\n", missions.get(i)));
            }
            missionTV.setVisibility(VISIBLE);
            missionCaptionTV.setVisibility(VISIBLE);
        }
    }

    private void setupLab(UnitInfo unitInfo) {
        boolean showLab = false;
        ArrayList<String> labs = new ArrayList<String>();

        try {
            for (int i = 0; i < 2; i++) {
                String lab = (String) UnitInfo.class.getMethod("getLab" + (i + 1)).invoke(unitInfo);
                showLab |= lab.length() > 0;
                if (lab.length() > 0) {
                    labs.add(lab);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (showLab) {
            for (int i = 0; i < labs.size(); i++) {
                labTV.append(String.format("[%s] ", labs.get(i)));
            }
            labTV.setVisibility(VISIBLE);
            labCaptionTV.setVisibility(VISIBLE);
        }
    }


    private void setupEtc(UnitInfo unitInfo) {
        if (unitInfo.getEtc() != null && unitInfo.getEtc().equals("1")) {
            etcTV.setText(unitInfo.getHowToGet());
            etcTV.setVisibility(VISIBLE);
            etcCaptionTV.setVisibility(VISIBLE);
        }
    }

}

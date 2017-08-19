package com.seok.youtome;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Seok on 2015-11-24.
 * 특정 사용자의 특정일 대화 정보
 */
public class DailyTalkData implements Serializable {
    Date talkDate;

    int myTalkCnt = 0;
    int myTalkDelay = 0;
    int myFirstTalkCnt = 0;

    int partnerTalkCnt = 0;
    int partnerTalkDelay = 0;
    int partnerFirstTalkCnt = 0;

    public DailyTalkData(Date talkDate, int myTalkCnt, int myTalkDelay, int myFirstTalkCnt, int partnerTalkCnt, int partnerTalkDelay, int partenrFirstTalkCnt){
        this.talkDate = talkDate;
        this.myTalkCnt = myTalkCnt;
        this.myTalkDelay = myTalkDelay;
        this.myFirstTalkCnt = myFirstTalkCnt;
        this.partnerTalkCnt = partnerTalkCnt;
        this.partnerTalkDelay = partnerTalkDelay;
        this.partnerFirstTalkCnt = partenrFirstTalkCnt;
    }

    public Date getTalkDate() {
        return talkDate;
    }

    public int getMyTalkCnt() {
        return myTalkCnt;
    }

    public int getMyTalkDelay() {
        return myTalkDelay;
    }

    public int getMyFirstTalkCnt() {
        return myFirstTalkCnt;
    }

    public int getPartnerTalkCnt() {
        return partnerTalkCnt;
    }

    public int getPartnerTalkDelay() {
        return partnerTalkDelay;
    }

    public int getPartnerFirstTalkCnt() {
        return partnerFirstTalkCnt;
    }
}
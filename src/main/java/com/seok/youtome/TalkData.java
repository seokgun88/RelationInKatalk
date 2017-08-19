package com.seok.youtome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Seok on 2015-11-24.
 * 각 사용자별 대화 정보
 */
public class TalkData implements Serializable {
    private String partnerName;
    private int totalRank = 0;
    private int sumTalkCntRank = 0;
    private int avrTalkCntRank = 0;
    private int avrTalkDelayRank = 0;
    private int firstTalkRateRank = 0;
    private int talkDays = 0;
    private ArrayList <DailyTalkData> dailyData = new ArrayList<DailyTalkData>();

    private int sumMyTalkCnt=0;
    private int sumMyTalkDelay=0;
    private int sumMyFirstTalkCnt=0;
    private int sumPartnerTalkCnt=0;
    private int sumPartnerTalkDelay=0;
    private int sumPartnerFirstTalkCnt=0;

    public TalkData(String partnerName){
        this.partnerName = partnerName;
    }
    public void addDailyData(Date talkDate, int myTalkCnt, int myTalkDelay, int myFirstTalkCnt, int partnerTalkCnt, int partnerTalkDelay, int partenrFirstTalkCnt){
        dailyData.add(new DailyTalkData(talkDate, myTalkCnt, myTalkDelay, myFirstTalkCnt, partnerTalkCnt, partnerTalkDelay, partenrFirstTalkCnt));
        talkDays++;
    }
    public void setData(){
        sumMyTalkCnt=0;
        sumMyTalkDelay=0;
        sumMyFirstTalkCnt=0;
        sumPartnerTalkCnt=0;
        sumPartnerTalkDelay=0;
        sumPartnerFirstTalkCnt=0;

        for(DailyTalkData temp: dailyData){
            sumMyTalkCnt += temp.myTalkCnt;
            sumMyTalkDelay += temp.myTalkDelay;
            sumMyFirstTalkCnt += temp.myFirstTalkCnt;
            sumPartnerTalkCnt += temp.partnerTalkCnt;
            sumPartnerTalkDelay += temp.partnerTalkDelay;
            sumPartnerFirstTalkCnt += temp.partnerFirstTalkCnt;
        }
    }
    public int getTotalRank() {
        return totalRank;
    }
    public void setTotalRank(int totalRank) {
        this.totalRank = totalRank;
    }
    public int getSumRank() {
        return sumTalkCntRank + avrTalkCntRank + avrTalkDelayRank + firstTalkRateRank;
    }
    public int getSumTalkCntRank() {
        return sumTalkCntRank;
    }
    public void setSumTalkCntRank(int sumTalkCntRank) {
        this.sumTalkCntRank = sumTalkCntRank;
    }
    public int getAvrTalkCntRank() {
        return avrTalkCntRank;
    }
    public void setAvrTalkCntRank(int avrTalkCntRank) {
        this.avrTalkCntRank = avrTalkCntRank;
    }
    public int getAvrTalkDelayRank() {
        return avrTalkDelayRank;
    }
    public void setAvrTalkDelayRank(int avrTalkDelayRank) {
        this.avrTalkDelayRank = avrTalkDelayRank;
    }
    public int getFirstTalkRateRank() {
        return firstTalkRateRank;
    }
    public void setFirstTalkRateRank(int firstTalkRateRank) {
        this.firstTalkRateRank = firstTalkRateRank;
    }
    public String getSummaryData(){
        return partnerName + "\n"
                +getSumTalkCnt() + "개\n"
                +(int)(sumMyTalkCnt*1.0/talkDays) + "개\n"
                +sumMyTalkDelay/sumMyTalkCnt + "분\n"
                +String.format("%.1f", sumMyFirstTalkCnt*1.0/(sumMyFirstTalkCnt + sumPartnerFirstTalkCnt) * 100) + "%\n"
                +(int)(sumPartnerTalkCnt * 1.0/talkDays) + "개\n"
                +sumPartnerTalkDelay/sumPartnerTalkCnt + "분\n"
                +String.format("%.1f", sumPartnerFirstTalkCnt*1.0/(sumMyFirstTalkCnt+sumPartnerFirstTalkCnt)*100) + "%\n";
    }
    public String getPartnerName(){
        return partnerName;
    }
    public int getSumTalkCnt(){
        return sumMyTalkCnt+sumPartnerTalkCnt;
    }
    public int getAvrMyTalkCnt(){ return (int)(sumMyTalkCnt * 1.0/talkDays);
    }
    public int getAvrMyTalkDelay(){ return sumMyTalkDelay/sumMyTalkCnt;
    }
    public double getMyFirstTalkRate(){
        return sumMyFirstTalkCnt*1.0/(sumMyFirstTalkCnt+sumPartnerFirstTalkCnt)*100;
    }
    public int getSumPartnerTalkCnt(){return sumPartnerTalkCnt;
    }
    public int getAvrPartnerTalkCnt(){
        return (int)(sumPartnerTalkCnt * 1.0/talkDays);
    }
    public int getAvrPartnerTalkDelay(){
        return sumPartnerTalkDelay/sumPartnerTalkCnt;
    }
    public double getPartnerFirstTalkRate(){
        return sumPartnerFirstTalkCnt*1.0/(sumMyFirstTalkCnt+sumPartnerFirstTalkCnt)*100;
    }
    public ArrayList<DailyTalkData> getDailyData() {
        return dailyData;
    }
}

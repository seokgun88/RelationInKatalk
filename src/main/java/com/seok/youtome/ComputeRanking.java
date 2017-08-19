package com.seok.youtome;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Seok on 2015-11-24.
 * 종합 순위 계산
 */
public class ComputeRanking {
    public static ArrayList<TalkData> getTotalRanking(ArrayList<TalkData> talkDataList) {
        int rank;
        int preData;
        int same;

        rank=1;
        preData=-1;
        same=0;
        Collections.sort(talkDataList, new SumPartnerTalkCntCompare());
        for (TalkData temp : talkDataList) {
            Log.d("총 상대방 대화수 랭킹", temp.getPartnerName());
            if(preData == temp.getSumPartnerTalkCnt()) {
                same++;
                temp.setSumTalkCntRank(rank++ - same);
                continue;
            }
            same=0;
            preData = temp.getSumPartnerTalkCnt();
            temp.setSumTalkCntRank(rank++);
        }
        rank=1;
        preData=-1;
        same=0;
        Collections.sort(talkDataList, new PartnerTalkCntCompare());
        for (TalkData temp : talkDataList) {
            Log.d("상대방 대화수 랭킹", temp.getPartnerName());
            if(preData == temp.getAvrPartnerTalkCnt()) {
                same++;
                temp.setAvrTalkCntRank(rank++ - same);
                continue;
            }
            same=0;
            preData = temp.getAvrPartnerTalkCnt();
            temp.setAvrTalkCntRank(rank++);
        }
        rank=1;
        preData=-1;
        same=0;
        Collections.sort(talkDataList, new PartnerTalkDelayCompare());
        for (TalkData temp : talkDataList) {
            Log.d("상대방 답장 지연 시간 랭킹", temp.getPartnerName());
            if(preData == temp.getAvrPartnerTalkDelay()){
                same++;
                temp.setAvrTalkDelayRank(rank++ - same);
                continue;
            }
            same=0;
            preData = temp.getAvrPartnerTalkDelay();
            temp.setAvrTalkDelayRank(rank++);
        }
        rank=1;
        preData=-1;
        same=0;
        Collections.sort(talkDataList, new PartnerFirstTalkRateCompare());
        for (TalkData temp : talkDataList) {
            Log.d("상대방 선톡 비율 랭킹", temp.getPartnerName());
            if(preData == temp.getPartnerFirstTalkRate()){
                same++;
                temp.setFirstTalkRateRank(rank++ - same);
                continue;
            }
            same=0;
            preData = (int)temp.getPartnerFirstTalkRate();
            temp.setFirstTalkRateRank(rank++);
        }
        rank=1;
        preData=-1;
        same=0;
        ArrayList <TalkData> rankingList = new ArrayList<TalkData>();
        Collections.sort(talkDataList, new RankingCompare());
        for (TalkData temp : talkDataList) {
            Log.d("종합 랭킹", temp.getPartnerName());
            rankingList.add(temp);

            if(preData == temp.getSumRank()){
                same++;
                temp.setTotalRank(rank++ - same);
                continue;
            }
            same=0;
            preData = (int)temp.getSumRank();
            temp.setTotalRank(rank++);
        }
        return rankingList;
    }
    public static ArrayList<TalkData> getSumTalkRanking(ArrayList<TalkData> talkDataList){
        Collections.sort(talkDataList, new SumPartnerTalkCntCompare());
        return talkDataList;
    }
    public static ArrayList<TalkData> getAvrTalkRanking(ArrayList<TalkData> talkDataList){
        ArrayList <TalkData> rankingList = new ArrayList<TalkData>();
        Collections.sort(talkDataList, new PartnerTalkCntCompare());
        return talkDataList;
    }
    public static ArrayList<TalkData> getAvrDelayRanking(ArrayList<TalkData> talkDataList){
        ArrayList <TalkData> rankingList = new ArrayList<TalkData>();
        Collections.sort(talkDataList, new PartnerTalkDelayCompare());
        return talkDataList;
    }
    public static ArrayList<TalkData> getFirstTalkRanking(ArrayList<TalkData> talkDataList){
        ArrayList <TalkData> rankingList = new ArrayList<TalkData>();
        Collections.sort(talkDataList, new PartnerFirstTalkRateCompare());
        return talkDataList;
    }
    static class SumPartnerTalkCntCompare implements Comparator<TalkData> {
        @Override
        public int compare(TalkData arg0, TalkData arg1) {
            // TODO Auto-generated method stub
            return arg0.getSumPartnerTalkCnt() > arg1.getSumPartnerTalkCnt() ? -1 : arg0.getSumPartnerTalkCnt() < arg1.getSumPartnerTalkCnt() ? 1 : 0;
        }
    }
    static class PartnerTalkCntCompare implements Comparator<TalkData> {
        @Override
        public int compare(TalkData arg0, TalkData arg1) {
            // TODO Auto-generated method stub
            return arg0.getAvrPartnerTalkCnt() > arg1.getAvrPartnerTalkCnt() ? -1 : arg0.getAvrPartnerTalkCnt() < arg1.getAvrPartnerTalkCnt() ? 1 : 0;
        }
    }
    static class PartnerTalkDelayCompare implements Comparator<TalkData> {
        @Override
        public int compare(TalkData arg0, TalkData arg1) {
            // TODO Auto-generated method stub
            return arg0.getAvrPartnerTalkDelay() < arg1.getAvrPartnerTalkDelay() ? -1 : arg0.getAvrPartnerTalkDelay() > arg1.getAvrPartnerTalkDelay() ? 1 : 0;
        }
    }
    static class PartnerFirstTalkRateCompare implements Comparator<TalkData> {
        @Override
        public int compare(TalkData arg0, TalkData arg1) {
            // TODO Auto-generated method stub
            return arg0.getPartnerFirstTalkRate() > arg1.getPartnerFirstTalkRate() ? -1 : arg0.getPartnerFirstTalkRate() < arg1.getPartnerFirstTalkRate() ? 1 : 0;
        }
    }
    static class RankingCompare implements Comparator<TalkData> {
        @Override
        public int compare(TalkData arg0, TalkData arg1) {
            // TODO Auto-generated method stub
            return arg0.getSumRank() < arg1.getSumRank() ? -1 : arg0.getSumRank() > arg1.getSumRank() ? 1 : 0;
        }
    }
}

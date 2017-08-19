package com.seok.youtome;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Seok on 2015-11-24.
 * 카톡 대화 목록 읽기 및 분석
 */
public class TalkReader {
    private final static String CHATSPREPATH = "/sdcard/KakaoTalk/Chats";
    private final static String TALKDATAPATH = "/sdcard/YouToMeInKatalk";
    private final static String TALKDATANAME = "/TalkDataList";
    private final static String MYNAME = "회원님";
    private static ArrayList<TalkData> talkDataList;

    public static ArrayList<TalkData> readFile() {
        talkDataList = new ArrayList<TalkData>();

        String partner = null; //상대방 이름
        String preDay = null; //날짜 바뀌는거 확인
        Date preTalkTime = null; //이전에 대화한 시간
        Date curTalkTime = null; //현재 대화한 시간
        Date curTalkDay = null;

        DateFormat dateAndTimeFormat = new SimpleDateFormat("yyyyMMddaahhmm");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String year, mon, day, ampm, hour, minute, name;

        TalkData mTalkData = null;

        File dir = new File(CHATSPREPATH);
        File[] fileList = dir.listFiles();

        //파일 읽기 실패시 종료
        if (fileList == null) {
            return null;
        } else {
            for (int i = 0; i < fileList.length; i++) {
                Log.d("경로", fileList[i].getAbsolutePath());
                if (fileList[i].isDirectory()) {
                    try {
                        FileReader fr = new FileReader(fileList[i].getAbsolutePath() + "/KakaoTalkChats.txt");
                        BufferedReader br = new BufferedReader(fr);

                        String line = null;
                        preDay = null;
                        int myTalkCnt = 0;
                        int myTalkDelay = 0;
                        int myFirstTalkCnt = 0;
                        int partnerTalkCnt = 0;
                        int partnerTalkDelay = 0;
                        int partnerFirstTalkCnt = 0;

                        Pattern nameP = Pattern.compile("(.+)\\s님과");
                        Pattern dateTimeP = Pattern.compile("([0-9]{4})년\\s([0-9]{1,2})월\\s([0-9]{1,2})일\\s([가-힣]{2})\\s([0-9]{1,2}):([0-9]{1,2}),\\s(\\S+)");
                        Matcher m;

                        m = nameP.matcher(br.readLine());
                        if (m.find()) {
                            partner = m.group(1);
                            mTalkData = new TalkData(partner);
                        }
                        while ((line = br.readLine()) != null) {
                            //Log.d("라인", line);
                            m = dateTimeP.matcher(line);
                            if (m.find()) {
                                /*날짜, 시간, 이름 문자열로 가져오기*/
                                year = m.group(1);
                                mon = m.group(2);
                                if (mon.length() == 1) {
                                    mon = "0" + mon;
                                }
                                day = m.group(3);
                                ampm = m.group(4);
                                hour = m.group(5);
                                if (hour.length() == 1) {
                                    hour = "0" + hour;
                                }
                                minute = m.group(6);
                                if (minute.length() == 1) {
                                    minute = "0" + minute;
                                }
                                name = m.group(7);
                                /*----------------------*/

                                /*현재 대화 시간 Date 형식으로 가져오기*/
                                try {
                                    curTalkTime = dateAndTimeFormat.parse(year + mon + day + ampm + hour + minute);
                                    curTalkDay = dateFormat.parse(year + mon + day);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                /*해당 파일 첫 대화일 경우*/
                                if (preDay == null) {
                                    preDay = day;
                                    preTalkTime = curTalkTime;
                                }
                                /*날짜가 바뀐 경우*/
                                else if (!day.equals(preDay)) {
                                    mTalkData.addDailyData(curTalkDay, myTalkCnt, myTalkDelay, myFirstTalkCnt, partnerTalkCnt, partnerTalkDelay, partnerFirstTalkCnt);
                                    myTalkCnt = 0;
                                    myTalkDelay = 0;
                                    myFirstTalkCnt = 0;
                                    partnerTalkCnt = 0;
                                    partnerTalkDelay = 0;
                                    partnerFirstTalkCnt = 0;
                                    preDay = day;
                                }

                                boolean isFirst = false;
                                /*답장 지연 시간 계산*/
                                long diffTime = (curTalkTime.getTime() - preTalkTime.getTime()) / (60 * 1000);
                                if (diffTime > 360) {
                                    diffTime = 0;
                                    isFirst = true;
                                }

                                if (name.equals(MYNAME)) {
                                    myTalkCnt++; //자신의 그날 대화수 증가
                                    myTalkDelay += diffTime;
                                    if (isFirst) {
                                        myFirstTalkCnt++;
                                    }
                                } else {
                                    partnerTalkCnt++; //상대방 그날 대화수 증가
                                    partnerTalkDelay += diffTime;
                                    if (isFirst) {
                                        partnerFirstTalkCnt++;
                                    }
                                }
                                preTalkTime = curTalkTime;
                            }
                        }
                        talkDataList.add(mTalkData);
                        mTalkData.setData();

                        br.close();
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ArrayList<TalkData> rankingList = ComputeRanking.getTotalRanking(talkDataList);
        /*-분석된 TalkDataList 파일로 저장-*/
        try {
            dir = new File(TALKDATAPATH);
            if(!dir.isDirectory()){
                dir.mkdir();
            }
            ObjectOutputStream oos = new ObjectOutputStream(
              new BufferedOutputStream(new FileOutputStream(TALKDATAPATH + TALKDATANAME))
            );
            oos.writeObject(rankingList);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rankingList;
    }
    public static ArrayList<TalkData> readTalkDataList() {
        File dir = new File(TALKDATAPATH + TALKDATANAME);
        if(dir.isFile()){
            try {
                ObjectInputStream ois = new ObjectInputStream(
                        new BufferedInputStream((new FileInputStream(TALKDATAPATH + TALKDATANAME)))
                );
                talkDataList = (ArrayList<TalkData>)ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            talkDataList = null;
        }
        return talkDataList;
    }
    public static ArrayList<TalkData> getTalkDataList() {
        return talkDataList;
    }
}

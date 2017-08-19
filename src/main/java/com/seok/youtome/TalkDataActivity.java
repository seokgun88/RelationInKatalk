package com.seok.youtome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seok.relationinkatalk.R;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.util.ArrayList;

/**
 * Created by Seok on 2015-11-25.
 */
public class TalkDataActivity extends Activity implements View.OnClickListener {
    private final static String APPDATAPATH = "/sdcard/RealationInKatalk";
    private TalkData talkData = null;
    private String name = "";
    private String analyzedData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talkdata);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        ArrayList<TalkData> talkDataList = TalkReader.getTalkDataList();
        for(TalkData temp: talkDataList){
            /*-해당 파트너 이름과 일치하면 대화 정보 출력-*/
            if(name.equals(temp.getPartnerName())) {
                talkData = temp; //해당 talkData 저장
                /*-대화 분석 내용 말로 설명-*/
                TextView analyzedTalkData = (TextView)findViewById(R.id.analyzedTalkData);
                analyzedData = name + "님은 ";
                /*-각 순위에 대한 정보-*/
                analyzedData += "총 나눈 대화수" +  talkData.getSumTalkCntRank() + "위, \n"
                        + "하루 대화수 " + talkData.getAvrTalkCntRank() + "위, "
                        + "답장 시간 " + talkData.getAvrTalkDelayRank() + "위, \n"
                        + "선톡 " + talkData.getFirstTalkRateRank() + "위로 "
                        + "종합 랭킹 " + talkData.getTotalRank() + "위 입니다.\n\n"
                        + "또한 카톡을 할 때 나";
                /*-평균 대화수, 답장시간, 선톡 비율별 분석 문장-*/
                switch(dataCompare(temp.getAvrMyTalkCnt(), temp.getAvrPartnerTalkCnt())){
                    case 1:
                        analyzedData += "보다 적게 말하고\n";
                        break;
                    case 2:
                        analyzedData += "보다 많이 말하고\n";
                        break;
                    case 3:
                        analyzedData += "와 비슷하게 말하고\n";
                        break;
                }
                switch(dataCompare(temp.getAvrMyTalkDelay(), temp.getAvrPartnerTalkDelay())){
                    case 1:
                        analyzedData += "답장을 빨리 보내며 ";
                        break;
                    case 2:
                        analyzedData += "답장을 느리게 보내며 ";
                        break;
                    case 3:
                        analyzedData += "답장을 비슷하게 보내며 ";
                        break;
                }
                switch(dataCompare((int)temp.getMyFirstTalkRate(), (int)temp.getPartnerFirstTalkRate())){
                    case 1:
                        analyzedData += "선톡을 적게 보냅니다.";
                        break;
                    case 2:
                        analyzedData += "선톡을 자주 보냅니다.";
                        break;
                    case 3:
                        analyzedData += "선톡을 비슷하게 보냅니다.";
                        break;
                }
                analyzedTalkData.setText(analyzedData);
                /*-----------------------------------------*/

                TextView sumTalkCnt = (TextView)findViewById(R.id.sumTalkCnt);
                sumTalkCnt.setText("총 나눈 대화수 : " + temp.getSumTalkCnt() + "번\n");

                LinearLayout.LayoutParams params; //막대 그래프 imageview 비율 조정용

                /*-나눈 대화 막대 그래프-*/
                TextView myTalkCnt = (TextView)findViewById(R.id.myTalkCnt);
                TextView partnerTalkCnt = (TextView)findViewById(R.id.partnerTalkCnt);
                ImageView myTalkCntBar = (ImageView)findViewById(R.id.myTalkCntBar);
                ImageView partnerTalkCntBar = (ImageView)findViewById(R.id.partnerTalkCntBar);

                int mTalkCnt = temp.getAvrMyTalkCnt();
                int pTalkCnt = temp.getAvrPartnerTalkCnt();
                float mTalkRate = mTalkCnt*1.0f/(mTalkCnt+pTalkCnt);
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mTalkRate);
                myTalkCntBar.setLayoutParams(params);
                myTalkCnt.setText(String.format("%d개(%.1f%%)", mTalkCnt, mTalkRate*100));
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1-mTalkRate);
                partnerTalkCnt.setText(String.format("%d개(%.1f%%)", pTalkCnt, (1-mTalkRate)*100));
                partnerTalkCntBar.setLayoutParams(params);

                /*-답장 시간 막대 그래프-*/
                TextView myTalkDelay = (TextView)findViewById(R.id.myTalkDelay);
                TextView partnerTalkDelay = (TextView)findViewById(R.id.partnerTalkDelay);
                ImageView myTalkDelayBar = (ImageView)findViewById(R.id.myTalkDelayBar);
                ImageView partnerTalkDelayBar = (ImageView)findViewById(R.id.partnerTalkDelayBar);

                int mTalkDelay = temp.getAvrMyTalkDelay();
                int pTalkDelay = temp.getAvrPartnerTalkDelay();
                float mTalkDelayRate = mTalkDelay*1.0f/(mTalkDelay+pTalkDelay);
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mTalkDelayRate);
                myTalkDelayBar.setLayoutParams(params);
                myTalkDelay.setText(String.format("%d분(%.1f%%)", mTalkDelay, mTalkDelayRate*100));
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1-mTalkDelayRate);
                partnerTalkDelay.setText(String.format("%d분(%.1f%%)", pTalkDelay, (1-mTalkDelayRate)*100));
                partnerTalkDelayBar.setLayoutParams(params);

                /*-선톡 비율 막대 그래프-*/
                TextView myFirstTalk = (TextView)findViewById(R.id.myFirstTalk);
                TextView partnerFirstTalk = (TextView)findViewById(R.id.partnerFirstTalk);
                ImageView myFirstTalkBar = (ImageView)findViewById(R.id.myFirstTalkBar);
                ImageView partnerFirstTalkBar = (ImageView)findViewById(R.id.partnerFirstTalkBar);

                float mFirstTalkRate = (float)temp.getMyFirstTalkRate();
                float pFirstTalkRate = (float)temp.getPartnerFirstTalkRate();
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mFirstTalkRate);
                myFirstTalkBar.setLayoutParams(params);
                myFirstTalk.setText(String.format("%.1f%%", mFirstTalkRate));
                params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, pFirstTalkRate);
                partnerFirstTalk.setText(String.format("%.1f%%", pFirstTalkRate));
                partnerFirstTalkBar.setLayoutParams(params);

                /*-버튼 리스너 등록-*/
                Button dailyTalkCnt = (Button)findViewById(R.id.dailyTalkCnt);
                Button dailyTalkDelay = (Button)findViewById(R.id.dailyTalkDelay);
                Button dailyFirstTalk = (Button)findViewById(R.id.dailyFirstTalk);
                Button sendToKatalk = (Button)findViewById(R.id.sendToKatalk);
                dailyTalkCnt.setOnClickListener(this);
                dailyTalkDelay.setOnClickListener(this);
                dailyFirstTalk.setOnClickListener(this);
                sendToKatalk.setOnClickListener(this);

                break; //for문 종료
            }
        }
    }
    public int dataCompare(int a, int b){
        if(a > b)
            return 1;
        else if(a < b)
            return 2;
        else
            return 3;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.dailyTalkCnt:
                intent = new Intent(this, DailyTalkCntActivity.class);
                intent.putExtra("dailyData", talkData.getDailyData());
                intent.putExtra("name", name);
                startActivity(intent);
                break;
            case R.id.dailyTalkDelay:
                intent = new Intent(this, DailyTalkDelayActivity.class);
                intent.putExtra("dailyData", talkData.getDailyData());
                intent.putExtra("name", name);
                startActivity(intent);
                break;
            case R.id.dailyFirstTalk:
                intent = new Intent(this, DailyFirstTalkActivity.class);
                intent.putExtra("dailyData", talkData.getDailyData());
                intent.putExtra("name", name);
                startActivity(intent);
                break;
            case R.id.sendToKatalk:
                try {
                    /*-카톡으로 보낼 텍스트 다듬기-*/
                    String analyzedDataStr[] = analyzedData.split("\n");
                    String msg = "";
                    for(int i=0; i<analyzedDataStr.length; i++){
                        msg += analyzedDataStr[i];
                        if(i<2)
                            msg += "\n";
                        if(i==3)
                            msg += "\n\n";
                    }
                    msg += "\n\nFrom \"나에게 넌 in 카톡\"";
                    /*---------------------------*/
                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
                    final KakaoTalkLinkMessageBuilder ktmsgbuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                    ktmsgbuilder.addText(msg);
                    kakaoLink.sendMessage(ktmsgbuilder, this);
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

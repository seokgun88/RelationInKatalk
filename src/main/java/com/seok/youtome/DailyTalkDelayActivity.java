package com.seok.youtome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seok.relationinkatalk.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Seok on 2015-11-26.
 */
public class DailyTalkDelayActivity extends Activity {
    private int sumMyTalkCnt = 0;
    private int sumMyTalkDelay = 0;
    private int sumPartnerTalkCnt = 0;
    private int sumPartnerTalkDelay = 0;
    private int avrMyTalkDelay = 0;
    private int avrPartnerTalkDelay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailytalkdelay);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        ArrayList<DailyTalkData> dailyData = (ArrayList<DailyTalkData>) getIntent().getSerializableExtra("dailyData");
        DateFormat dFormat = new SimpleDateFormat("yy년 MM월 dd일");
        LinearLayout.LayoutParams params; //막대 그래프 imageview 비율 조정용

        LinearLayout dailyTalkDelayLayout = (LinearLayout) findViewById(R.id.dailyTalkDelayLayout);
        TextView dailyTalkDelayName = (TextView) findViewById(R.id.dailyTalkDelayName);
        dailyTalkDelayName.setText(name + "님과의 일자별 답장시간\n(누적 평균 그래프)");

        for (DailyTalkData temp : dailyData) {
            /*-막대 그래프 xml 파일 inflate-*/
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.bargraph, null);

            LinearLayout dailyLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(50, 50, 50, 50);
            dailyLayout.setLayoutParams(layoutParams);
            dailyLayout.setOrientation(LinearLayout.VERTICAL);


            TextView dailyTalkDelayText = new TextView(this);
            dailyTalkDelayText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            int myTalkDelay;
            int partnerTalkDelay;
            if(temp.getMyTalkCnt() == 0){
                myTalkDelay = 0;
            }
            else{
                myTalkDelay = temp.getMyTalkDelay() / temp.getMyTalkCnt();
            }
            if(temp.getPartnerTalkCnt() == 0){
                partnerTalkDelay = 0;
            }
            else{
                partnerTalkDelay = temp.getPartnerTalkDelay() / temp.getPartnerTalkCnt();
            }
            String text = dFormat.format(temp.getTalkDate()) + " : "
                    + myTalkDelay + "분(나), "
                    + partnerTalkDelay + "분(" + name + ")\n";
            dailyTalkDelayText.setText(text);

            /*-막대 그래프 만들기-*/
            sumMyTalkDelay += temp.getMyTalkDelay();
            sumMyTalkCnt += temp.getMyTalkCnt();
            if(sumMyTalkCnt == 0)
                avrMyTalkDelay = 0;
            else
                avrMyTalkDelay = sumMyTalkDelay / sumMyTalkCnt;
            sumPartnerTalkDelay += temp.getPartnerTalkDelay();
            sumPartnerTalkCnt += temp.getPartnerTalkCnt();
            if(sumPartnerTalkCnt == 0)
                avrPartnerTalkDelay = 0;
            else
                avrPartnerTalkDelay = sumPartnerTalkDelay / sumPartnerTalkCnt;

            TextView myData = (TextView) view.findViewById(R.id.myData);
            TextView partnerData = (TextView) view.findViewById(R.id.partnerData);
            ImageView myBar = (ImageView) view.findViewById(R.id.myBar);
            ImageView partnerBar = (ImageView) view.findViewById(R.id.partnerBar);

            float mTalkRate;
            float pTalkRate;
            if(avrMyTalkDelay + avrPartnerTalkDelay == 0) {
                mTalkRate = (float)0.5;
                pTalkRate = (float)0.5;
            }
            else {
                mTalkRate = avrMyTalkDelay * 1.0f / (avrMyTalkDelay + avrPartnerTalkDelay);
                pTalkRate = 1 - mTalkRate;
            }
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mTalkRate);
            myBar.setLayoutParams(params);
            myData.setText(String.format("%d분(%.1f%%)", avrMyTalkDelay, mTalkRate * 100));
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, pTalkRate);
            partnerBar.setLayoutParams(params);
            partnerData.setText(String.format("%d분(%.1f%%)", avrPartnerTalkDelay, pTalkRate * 100));
            /*---------------------*/

            dailyLayout.addView(dailyTalkDelayText);
            dailyLayout.addView(view);
            dailyTalkDelayLayout.addView(dailyLayout);
        }
    }
}

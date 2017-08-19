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
 * 일자별 대화수
 */
public class DailyTalkCntActivity extends Activity{
    private int talkDays = 0;
    private int sumMyTalkCnt = 0;
    private int sumPartnerTalkCnt = 0;
    private int avrMyTalkCnt = 0;
    private int avrPartnerTalkCnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailytalkcnt);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        ArrayList<DailyTalkData> dailyData = (ArrayList<DailyTalkData>)getIntent().getSerializableExtra("dailyData");
        DateFormat dFormat = new SimpleDateFormat("yy년 MM월 dd일");
        LinearLayout.LayoutParams params; //막대 그래프 imageview 비율 조정용

        LinearLayout dailyTalkCntLayout = (LinearLayout)findViewById(R.id.dailyTalkCntLayout);
        TextView dailyTalkCntName = (TextView)findViewById(R.id.dailyTalkCntName);
        dailyTalkCntName.setText(name + "님과의 일자별 대화수\n(누적 평균 그래프)");

        for(DailyTalkData temp: dailyData){
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


            TextView dailyTalkCntText = new TextView(this);
            dailyTalkCntText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            String text = dFormat.format(temp.getTalkDate()) + " : "
                    + temp.getMyTalkCnt() + "개(나), "
                    + temp.getPartnerTalkCnt() + "개(" + name + ")\n";
            dailyTalkCntText.setText(text);

            /*-막대 그래프 만들기-*/
            talkDays++;
            sumMyTalkCnt += temp.getMyTalkCnt();
            avrMyTalkCnt = sumMyTalkCnt/talkDays;
            sumPartnerTalkCnt += temp.getPartnerTalkCnt();
            avrPartnerTalkCnt = sumPartnerTalkCnt/talkDays;

            TextView myData = (TextView)view.findViewById(R.id.myData);
            TextView partnerData = (TextView)view.findViewById(R.id.partnerData);
            ImageView myBar = (ImageView)view.findViewById(R.id.myBar);
            ImageView partnerBar = (ImageView)view.findViewById(R.id.partnerBar);

            float mTalkRate = avrMyTalkCnt*1.0f/(avrMyTalkCnt+avrPartnerTalkCnt);
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mTalkRate);
            myBar.setLayoutParams(params);
            myData.setText(String.format("%d개(%.1f%%)", avrMyTalkCnt, mTalkRate * 100));
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1-mTalkRate);
            partnerBar.setLayoutParams(params);
            partnerData.setText(String.format("%d개(%.1f%%)", avrPartnerTalkCnt, (1 - mTalkRate) * 100));
            /*---------------------*/

            dailyLayout.addView(dailyTalkCntText);
            /*ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null)
                parent.removeView(view);*/
            dailyLayout.addView(view);
            dailyTalkCntLayout.addView(dailyLayout);
        }
    }
}
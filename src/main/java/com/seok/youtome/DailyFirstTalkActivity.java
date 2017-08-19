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
public class DailyFirstTalkActivity extends Activity {
    private int sumMyFirstTalk = 0;
    private int sumPartnerFirstTalk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyfirsttalk);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        ArrayList<DailyTalkData> dailyData = (ArrayList<DailyTalkData>) getIntent().getSerializableExtra("dailyData");
        DateFormat dFormat = new SimpleDateFormat("yy년 MM월 dd일");
        LinearLayout.LayoutParams params; //막대 그래프 imageview 비율 조정용

        LinearLayout dailyFirstTalkLayout = (LinearLayout) findViewById(R.id.dailyFirstTalkLayout);
        TextView dailyFirstTalkName = (TextView) findViewById(R.id.dailyFirstTalkName);
        dailyFirstTalkName.setText(name + "님과의 일자별 선톡횟수\n(누적 평균 그래프)");

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


            TextView dailyFirstTalkText = new TextView(this);
            dailyFirstTalkText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            String text = dFormat.format(temp.getTalkDate()) + " : "
                    + temp.getMyFirstTalkCnt() + "개(나), "
                    + temp.getPartnerFirstTalkCnt() + "개(" + name + ")\n";
            dailyFirstTalkText.setText(text);

            /*-막대 그래프 만들기-*/
            sumMyFirstTalk += temp.getMyFirstTalkCnt();
            sumPartnerFirstTalk += temp.getPartnerFirstTalkCnt();

            TextView myData = (TextView) view.findViewById(R.id.myData);
            TextView partnerData = (TextView) view.findViewById(R.id.partnerData);
            ImageView myBar = (ImageView) view.findViewById(R.id.myBar);
            ImageView partnerBar = (ImageView) view.findViewById(R.id.partnerBar);

            float mTalkRate;
            float pTalkRate;
            if (sumMyFirstTalk + sumPartnerFirstTalk == 0) {
                mTalkRate = (float) 0.5;
                pTalkRate = (float) 0.5;
            }
            else {
                mTalkRate = sumMyFirstTalk * 1.0f / (sumMyFirstTalk + sumPartnerFirstTalk);
                pTalkRate = 1 - mTalkRate;
            }
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mTalkRate);
            myBar.setLayoutParams(params);
            myData.setText(String.format("%.1f%%", mTalkRate * 100));
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, pTalkRate);
            partnerBar.setLayoutParams(params);
            partnerData.setText(String.format("%.1f%%", pTalkRate * 100));
            /*---------------------*/

            dailyLayout.addView(dailyFirstTalkText);
            dailyLayout.addView(view);
            dailyFirstTalkLayout.addView(dailyLayout);
        }
    }
}

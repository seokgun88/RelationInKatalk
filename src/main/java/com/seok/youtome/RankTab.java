package com.seok.youtome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.seok.relationinkatalk.R;

import java.util.ArrayList;

/**
 * Created by Seok on 2015-12-03.
 */
public class RankTab extends Fragment {
    private View view;
    private RadioButton sumTalkOpt;
    private RadioButton avrTalkOpt;
    private RadioButton avrDelayOpt;
    private RadioButton firstTalkRateOpt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_rank, null);

        /*-라디오 버튼 설정-*/
        sumTalkOpt = (RadioButton) view.findViewById(R.id.sumTalkOpt);
        avrTalkOpt = (RadioButton) view.findViewById(R.id.avrTalkOpt);
        avrDelayOpt = (RadioButton) view.findViewById(R.id.avrDelayOpt);
        firstTalkRateOpt = (RadioButton) view.findViewById(R.id.firstTalkRateOpt);
        sumTalkOpt.setOnClickListener(optionOnClickListener);
        avrTalkOpt.setOnClickListener(optionOnClickListener);
        avrDelayOpt.setOnClickListener(optionOnClickListener);
        firstTalkRateOpt.setOnClickListener(optionOnClickListener);
        sumTalkOpt.setChecked(true);

        ArrayList<TalkData> talkDataList = TalkReader.getTalkDataList();
        setRankingList("sumTalk", ComputeRanking.getSumTalkRanking(talkDataList));

        return view;
    }

    /*-선택된 분야에 따른 순위 리스트 정렬-*/
    public void setRankingList(String rankType, ArrayList<TalkData> rankingList) {
        LinearLayout rankLinear = (LinearLayout) view.findViewById(R.id.rankLinear);
        rankLinear.removeAllViews();

        int id = 1;
        int rank = 0;
        for (TalkData temp : rankingList) {
            LinearLayout rankNameLayout = new LinearLayout(view.getContext());
            if (rankType.equals("total")) {
                rank = temp.getTotalRank();
            } else if (rankType.equals("sumTalk")) {
                rank = temp.getSumTalkCntRank();
            } else if (rankType.equals("avrTalk")) {
                rank = temp.getAvrTalkCntRank();
            } else if (rankType.equals("avrDelay")) {
                rank = temp.getAvrTalkDelayRank();
            } else if (rankType.equals("firstTalk")) {
                rank = temp.getFirstTalkRateRank();
            }
            if (rank <= 3) {
                ImageView rankImage = new ImageView(view.getContext());
                rankImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                switch (rank) {
                    case 1:
                        rankImage.setBackgroundResource(R.drawable.first);
                        break;
                    case 2:
                        rankImage.setBackgroundResource(R.drawable.second);
                        break;
                    case 3:
                        rankImage.setBackgroundResource(R.drawable.third);
                        break;
                }
                rankNameLayout.addView(rankImage, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            } else {
                TextView rankNum = new TextView(view.getContext());
                rankNum.setText(" " + rank + " ");
                rankNum.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                rankNum.setTextColor(Color.BLACK);
                rankNameLayout.addView(rankNum, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            TextView rankingName = new TextView(view.getContext());
            rankingName.setText(temp.getPartnerName());
            rankingName.setId(id++);
            rankingName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            rankingName.setOnClickListener(nameOnClickListener);

            rankNameLayout.addView(rankingName, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            rankLinear.addView(rankNameLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    /*-이름 클릭리스너-*/
    OnClickListener nameOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() > 0) {
                Intent intent = new Intent(getContext(), TalkDataActivity.class);
                intent.putExtra("name", ((TextView) v).getText().toString());
                startActivity(intent);
            }
        }
    };

    /*-라디오 버튼 클릭리스너-*/
    RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<TalkData> talkDataList = TalkReader.getTalkDataList();
            switch (v.getId()) {
                case R.id.sumTalkOpt:
                    setRankingList("sumTalk", ComputeRanking.getSumTalkRanking(talkDataList));
                    view.invalidate();
                    break;
                case R.id.avrTalkOpt:
                    setRankingList("avrTalk", ComputeRanking.getAvrTalkRanking(talkDataList));
                    view.invalidate();
                    break;
                case R.id.avrDelayOpt:
                    setRankingList("avrDelay", ComputeRanking.getAvrDelayRanking(talkDataList));
                    view.invalidate();
                    break;
                case R.id.firstTalkRateOpt:
                    setRankingList("firstTalk", ComputeRanking.getFirstTalkRanking(talkDataList));
                    view.invalidate();
                    break;
            }
        }
    };
}

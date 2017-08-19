package com.seok.youtome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seok.relationinkatalk.R;

import java.util.ArrayList;

/**
 * Created by Seok on 2015-11-17.
 * 종합 순위 탭
 */
public class TotalRankTab extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_totalrank, null);

        LinearLayout rankingLinear = (LinearLayout) view.findViewById(R.id.rankingLinear);
        /*-분석된 데이터 있으면 읽어오고 아니면 새로 분석-*/
        ArrayList<TalkData> rankingList = TalkReader.readTalkDataList();
        if (rankingList == null) {
            rankingList = TalkReader.readFile();
        }
        setTotalRankingList(rankingList);

        /*-새로고침 버튼 리스너-*/
        Button renewBtn = (Button) view.findViewById(R.id.renewBtn);
        renewBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*-새로고침 대기창-*/
                final Handler mHandler = new Handler();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ProgressDialog mProgressDialog = ProgressDialog.show(getContext(), "",
                                "잠시만 기다려 주세요.", true);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    setTotalRankingList(TalkReader.readFile());
                                    mProgressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);
                    }
                });
            }
        });

        return view;
    }

    /*-이름 클릭리스너-*/
    OnClickListener nameOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), TalkDataActivity.class);
            intent.putExtra("name", ((TextView) v).getText().toString());
            startActivity(intent);
        }
    };

    /*-종합 순위 리스트 정렬-*/
    public void setTotalRankingList(ArrayList<TalkData> rankingList) {
        LinearLayout rankingLinear = (LinearLayout) view.findViewById(R.id.rankingLinear);
        rankingLinear.removeAllViews();

        int id = 1;
        int rank = 0;

        //저장된 카톡 대화 내역이 없을 경우 종료
        if(rankingList == null){
            return;
        }
        for (TalkData temp : rankingList) {
            LinearLayout rankNameLayout = new LinearLayout(view.getContext());
            rank = temp.getTotalRank();
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
            rankingLinear.addView(rankNameLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
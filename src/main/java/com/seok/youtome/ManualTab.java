package com.seok.youtome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seok.relationinkatalk.R;

/**
 * Created by Seok on 2015-12-03.
 */
public class ManualTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_manual, null);
        return view;
    }
}
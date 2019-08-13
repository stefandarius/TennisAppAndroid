package com.example.stefan.tennis.fragments;

import android.os.Bundle;
import android.view.View;

import com.example.stefan.tennis.R;

public class StatisticiTabFragment extends BaseFragment {

    public static StatisticiTabFragment getInstance(int idSportiv) {
        StatisticiTabFragment statisticiTabFragment = new StatisticiTabFragment();
        Bundle args = new Bundle();
        args.putInt("stId", idSportiv);
        statisticiTabFragment.setArguments(args);
        return statisticiTabFragment;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragmetn_tab_statistici;
    }

    @Override
    public void initUI(View view) {

    }
}
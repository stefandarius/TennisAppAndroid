package com.example.stefan.tennis.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.tennis.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected Toolbar toolbar;
    private Unbinder binder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v("BaseFragment", getClass().getSimpleName() + " onCreateView()");
        View view = inflater.inflate(getFragmentLayoutId(), container, false);
        binder = ButterKnife.bind(this, view);
        initUI(view);
        setupTollbar();
        return view;
    }

    public abstract int getFragmentLayoutId();

    public abstract void initUI(View view);

    @Override
    public void onDestroy() {
        Log.v("BaseFragment", getClass().getSimpleName() + " onDestroy()");
        super.onDestroy();
        binder.unbind();
    }

    public void setupTollbar() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (getActivity().findViewById(R.id.toolbar));
        //  toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        toolbar.setNavigationOnClickListener(v -> {
//            getActivity().getSupportFragmentManager().popBackStack();
//        });
    }
}

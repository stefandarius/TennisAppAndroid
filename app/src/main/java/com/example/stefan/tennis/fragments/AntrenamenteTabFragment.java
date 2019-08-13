package com.example.stefan.tennis.fragments;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.IstoricAntrenamenteSportivAdapter;
import com.example.stefan.tennis.interfaces.OnAntrenamentAdaugat;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AntrenamenteTabFragment extends BaseFragment implements IstoricAntrenamenteSportivAdapter.OnAntrenamentSelected, OnAntrenamentAdaugat {


    public static final String TAG = "AntrenamenteTabFragment";
    @BindView(R.id.listView)
    RecyclerView listView;
    private IstoricAntrenamenteSportivAdapter adapter;
    private List<IstoricAntrenamente> istoricAntrenamenteList;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_tab_antrenamente;
    }

    @Override
    public void initUI(View view) {
        Bundle b = getArguments();
        istoricAntrenamenteList = new ArrayList<>();
        if (b != null) {
            int idSportiv = b.getInt("sId");
            istoricAntrenamenteList.addAll(IstoricAntrenamente.getAntrenamenteBySportiv(idSportiv));
            istoricAntrenamenteList.sort((o1, o2) -> o2.getDataAntrenament().compareTo(o1.getDataAntrenament()));
        }

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Item Decorator:
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));


        adapter = new IstoricAntrenamenteSportivAdapter();
        adapter.updateList(istoricAntrenamenteList);
        listView.setAdapter(adapter);

        adapter.setOnAntrenamentSelected(this);
    }

    public void refresh(IstoricAntrenamente a) {
        if (adapter == null) {
            adapter = new IstoricAntrenamenteSportivAdapter();
            adapter.updateList(istoricAntrenamenteList);
        }
        adapter.add(a);
        istoricAntrenamenteList.sort((o1, o2) -> o2.getDataAntrenament().compareTo(o1.getDataAntrenament()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void antrenamentSelected(IstoricAntrenamente istoricAntrenamente) {
        if (istoricAntrenamente.getRating() == 0) {
            Bundle b = new Bundle();
            b.putInt("sId", istoricAntrenamente.getId());
            b.putBoolean("neev", true);
            AddIstoricAntrenamentFragment addIstoricAntrenamentFragment = new AddIstoricAntrenamentFragment();
            addIstoricAntrenamentFragment.setArguments(b);
            addIstoricAntrenamentFragment.setOnAntrenamentAdaugat(this);
            ProjectUtils.addFragmentToActivity(getActivity(), addIstoricAntrenamentFragment, true, AddIstoricAntrenamentFragment.TAG);
        }
    }

    @Override
    public void onAntrenamentAdaugat(IstoricAntrenamente a) {
        a.save();
        int index = istoricAntrenamenteList.indexOf(a);
        if (index != -1)
            istoricAntrenamenteList.set(index, a);
        adapter.notifyDataSetChanged();
    }
}

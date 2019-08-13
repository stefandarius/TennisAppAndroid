package com.example.stefan.tennis.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.AbonamenteSportiviSwipeAdapter;
import com.example.stefan.tennis.models.AbonamenteSportivi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AbonamenteTabFragment extends BaseFragment {

    public static final String TAG = "AbonamenteTabFragment";
    @BindView(R.id.listView)
    RecyclerView listView;
    private AbonamenteSportiviSwipeAdapter tabAdapter;
    private List<AbonamenteSportivi> abonamentList;

    public static AbonamenteTabFragment newInstance() {
        return new AbonamenteTabFragment();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_tab_abonamente;
    }

    public void initUI(View view) {
        Bundle b = getArguments();
        abonamentList = new ArrayList<>();
        if (b != null) {
            int idSportiv = b.getInt("sId");
            abonamentList.addAll(AbonamenteSportivi.getAbonamenteSportivBySportivId(idSportiv));
            abonamentList.sort((o1, o2) -> o2.getDataInceput().compareTo(o1.getDataInceput()));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listView.setLayoutManager(layoutManager);

        // Item Decorator:

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listView.getContext(),
//                layoutManager.getOrientation());
//        listView.addItemDecoration(dividerItemDecoration);

        tabAdapter = new AbonamenteSportiviSwipeAdapter();
        tabAdapter.updateList(abonamentList);
        listView.setAdapter(tabAdapter);
    }

    public void refresh(AbonamenteSportivi a) {
        if (tabAdapter == null) {
            tabAdapter = new AbonamenteSportiviSwipeAdapter();
            tabAdapter.updateList(abonamentList);
            listView.setAdapter(tabAdapter);
        }
        tabAdapter.add(a);
        abonamentList.sort((o1, o2) -> o2.getDataInceput().compareTo(o1.getDataInceput()));
        tabAdapter.notifyDataSetChanged();
    }

    public AbonamenteSportiviSwipeAdapter getTabAdapter() {
        return tabAdapter;
    }
}

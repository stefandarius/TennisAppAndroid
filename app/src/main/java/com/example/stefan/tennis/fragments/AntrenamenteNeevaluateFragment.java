package com.example.stefan.tennis.fragments;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.IstoricAntrenamenteSportivAdapter;
import com.example.stefan.tennis.interfaces.OnAntrenamentAdaugat;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.List;

import butterknife.BindView;

public class AntrenamenteNeevaluateFragment extends BaseFragment implements IstoricAntrenamenteSportivAdapter.OnAntrenamentSelected, OnAntrenamentAdaugat {

    public static final String TAG = "AntrenamenteNeevaluateF";
    @BindView(R.id.recyclerView)
    RecyclerView listView;
    private IstoricAntrenamenteSportivAdapter adapter;
    private List<IstoricAntrenamente> listaAntrenemante;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.lista_antrenamente_neevaluate;
    }

    @Override
    public void initUI(View view) {

        listaAntrenemante = IstoricAntrenamente.getAntrenamentWithoutRating();

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Item Decorator:
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));


        adapter = new IstoricAntrenamenteSportivAdapter();
        adapter.updateList(listaAntrenemante);
        listView.setAdapter(adapter);

        adapter.setOnAntrenamentSelected(this);
    }

    @Override
    public void setupTollbar() {
        super.setupTollbar();
        toolbar.setTitle("Antrenamente neevaluate");
    }

    @Override
    public void antrenamentSelected(IstoricAntrenamente istoricAntrenamente) {
        Bundle b = new Bundle();
        b.putInt("sId", istoricAntrenamente.getId());
        b.putBoolean("neev", true);
        AddIstoricAntrenamentFragment addIstoricAntrenamentFragment = new AddIstoricAntrenamentFragment();
        addIstoricAntrenamentFragment.setArguments(b);
        addIstoricAntrenamentFragment.setOnAntrenamentAdaugat(this);
        ProjectUtils.addFragmentToActivity(getActivity(), addIstoricAntrenamentFragment, true, AddIstoricAntrenamentFragment.TAG);
    }

    @Override
    public void onAntrenamentAdaugat(IstoricAntrenamente a) {
        a.save();
        int index = listaAntrenemante.indexOf(a);
        if (index != -1)
            listaAntrenemante.set(index, a);
        adapter.notifyDataSetChanged();
        Log.v(TAG, a.toString());
    }
}

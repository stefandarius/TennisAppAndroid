package com.example.stefan.tennis.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.TipAntrenamenteSwipeAdapter;
import com.example.stefan.tennis.models.TipAntrenament;

import java.util.List;

import butterknife.BindView;

public class TipuriAntrenamenteListFragment extends BaseFragment implements AddTipAntrenamentFragment.OnAntrenamentSaved {

    public static final String TAG = "TipuriAntrenamenteListF";
    @BindView(R.id.listView)
    RecyclerView listaAntrenamente;
    private TipAntrenamenteSwipeAdapter tipAntrenamentAdapter;
    private SearchView searchView;
    private View fab;

    public void initUI(View view) {
        List<TipAntrenament> antrenamente = TipAntrenament.getAll();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listaAntrenamente.setLayoutManager(layoutManager);


        //Item Decorator:
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listaAntrenamente.getContext(),
                layoutManager.getOrientation());
        listaAntrenamente.addItemDecoration(dividerItemDecoration);

        tipAntrenamentAdapter = new TipAntrenamenteSwipeAdapter(getContext());
        tipAntrenamentAdapter.updateList(antrenamente);


        searchView = view.findViewById(R.id.searchView);
        fab = getActivity().findViewById(R.id.fab);

        listaAntrenamente.setAdapter(tipAntrenamentAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tipAntrenamentAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onAntrenamentSaved(TipAntrenament ta) {
        tipAntrenamentAdapter.add(ta);
        tipAntrenamentAdapter.notifyDataSetChanged();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_tip_antrenament_list;
    }

    @Override
    public void setupTollbar() {
        fab.setVisibility(View.VISIBLE);
        toolbar.setTitle("Tipuri antrenamente");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTipAntrenamentFragment dialogFragment = new AddTipAntrenamentFragment();
                dialogFragment.setOnAntrenamentSaved(TipuriAntrenamenteListFragment.this);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(AddTipAntrenamentFragment.TAG);
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);
                dialogFragment.setTargetFragment(TipuriAntrenamenteListFragment.this, 1);
                dialogFragment.show(fragmentTransaction, AddTipAntrenamentFragment.TAG);
            }
        });
    }
}

package com.example.stefan.tennis.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.AntrenorSwipeAdapter;
import com.example.stefan.tennis.models.Antrenor;

import java.util.List;

import butterknife.BindView;

/// TODO: 4/28/2019 filter

public class AntrenorListFragment extends BaseFragment implements AddAntrenorFragment.OnAntrenorSaved {

    public static final String TAG = "AntrenorListFragment";

    @BindView(R.id.listaAntrenori)
    RecyclerView listaAntrenori;
    @BindView(R.id.searchView)
    SearchView searchView;
    private AntrenorSwipeAdapter antrenorAdapter;
    private View fab;


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_antenori_list;
    }

    @Override
    public void initUI(View view) {
        List<Antrenor> antrenori = Antrenor.getAll();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listaAntrenori.setLayoutManager(layoutManager);


        //Item Decorator:
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listaAntrenori.getContext(),
                layoutManager.getOrientation());
        listaAntrenori.addItemDecoration(dividerItemDecoration);

        antrenorAdapter = new AntrenorSwipeAdapter(getContext());
        antrenorAdapter.updateList(antrenori);
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAntrenorFragment dialogFragment = new AddAntrenorFragment();
                dialogFragment.setOnAntrenorSaved(AntrenorListFragment.this);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(AddAntrenorFragment.TAG);
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);
                dialogFragment.setTargetFragment(AntrenorListFragment.this, 1);
                dialogFragment.show(transaction, AntrenorSwipeAdapter.TAG);
            }
        });
        listaAntrenori.setAdapter(antrenorAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                antrenorAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                antrenorAdapter.filter(newText);
                return true;
            }
        });

    }

    @Override
    public void onAntrenorSaved(Antrenor a) {
        antrenorAdapter.addToOriginal(a);
        antrenorAdapter.notifyDataSetChanged();
    }

    @Override
    public void setupTollbar() {
        toolbar.setTitle("Lista Antrenori");
    }
}

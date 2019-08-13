package com.example.stefan.tennis.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.SportivSwipeAdapter;
import com.example.stefan.tennis.models.Sportiv;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.List;

import butterknife.BindView;

public class SportivListFragment extends BaseFragment implements AddSportivFragment.OnSportivSaved, SportivSwipeAdapter.OnSportivSelected {
    public static final String TAG = "SportivListFragment";
    @BindView(R.id.listView)
    RecyclerView listaSportivi;
    private SportivSwipeAdapter sportivAdapter;
    private SearchView searchView;
    private View fab;

    public static Fragment newInstance() {
        return new SportivListFragment();
    }


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_sportiv_list;
    }

    public void initUI(View view) {
        List<Sportiv> sportivi = Sportiv.getAll();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listaSportivi.setLayoutManager(layoutManager);


        //Item Decorator:
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listaSportivi.getContext(),
////                layoutManager.getOrientation());
////        listaSportivi.addItemDecoration(dividerItemDecoration);

        sportivAdapter = new SportivSwipeAdapter(getContext());
        sportivAdapter.updateList(sportivi);
        sportivAdapter.setOnSportivSelected(this);
        searchView = view.findViewById(R.id.searchView);
        fab = getActivity().findViewById(R.id.fab);

        listaSportivi.setAdapter(sportivAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sportivAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onSportivSaved(Sportiv s) {
        sportivAdapter.add(s);
        sportivAdapter.notifyDataSetChanged();
    }

    @Override
    public void setupTollbar() {
        Log.v("fab", "s-a apelat!");
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        toolbar.setTitle("Lista sportivi");
        fab.setOnClickListener(view -> {
            AddSportivFragment addSportivFragment = new AddSportivFragment();
            addSportivFragment.setOnSportivSaved(SportivListFragment.this);
            ProjectUtils.addFragmentToActivity(getActivity(), addSportivFragment, true, AddSportivFragment.TAG);
        });
    }

    @Override
    public void sportivSelected(Sportiv sportiv) {
        ProfilSportivFragment profilSportivFragment = ProfilSportivFragment.newInstance(sportiv.getId());
        ProjectUtils.addFragmentToActivity(getActivity(), profilSportivFragment, true, "PROFIL_SPORTIV_FRAGMENT");
    }
}

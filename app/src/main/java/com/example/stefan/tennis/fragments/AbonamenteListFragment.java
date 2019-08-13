package com.example.stefan.tennis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.AbonamentSwipeAdapter;
import com.example.stefan.tennis.models.Abonament;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class AbonamenteListFragment extends BaseFragment implements AddAbonamentFragment.OnAbonamentSaved, AbonamentSwipeAdapter.onAbonamentSelected {

    public static final String TAG = "AbonamenteListFragment";
    @BindView(R.id.listView)
    RecyclerView listaAbonamente;
    @BindView(R.id.searchView)
    SearchView searchView;
    private AbonamentSwipeAdapter abonamenteAdapter;
    private View fab;
    private OnAdaugareAbonamentLaSportiv onAdaugareAbonamentLaSportiv;
    private AbonamentSwipeAdapter.onAbonamentSelected onAbonamentSelected;

    public static Fragment newInstance() {
        return new AbonamenteListFragment();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_abonament_list;
    }

    public void setOnAbonamentSelected(AbonamentSwipeAdapter.onAbonamentSelected onAbonamentSelected) {
        this.onAbonamentSelected = onAbonamentSelected;
    }

    @Override
    public void onResume() {
        super.onResume();
        // fab.setVisibility(View.VISIBLE);
    }

    public void initUI(View view) {

        Bundle b = getArguments();

        List<Abonament> abonamente = Abonament.getAll();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listaAbonamente.setLayoutManager(layoutManager);

        abonamenteAdapter = new AbonamentSwipeAdapter(getContext());
        abonamenteAdapter.updateList(abonamente);
        abonamenteAdapter.setOnAbonamentSelected(this);

        listaAbonamente.setAdapter(abonamenteAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                abonamenteAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void setupTollbar() {
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        toolbar.setTitle("Abonamente");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAbonamentFragment dialogFragment = new AddAbonamentFragment();
                dialogFragment.setOnAbonamentSaved(AbonamenteListFragment.this);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(AddAbonamentFragment.TAG);
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);
                dialogFragment.setTargetFragment(AbonamenteListFragment.this, 1);
                dialogFragment.show(transaction, AddAbonamentFragment.TAG);
            }
        });
    }

    @Override
    public void onAbonamentSaved(Abonament a) {
        abonamenteAdapter.add(a);
        abonamenteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAbonamentSelected(final Abonament a) {
        if (onAdaugareAbonamentLaSportiv != null) {
            DatePickerFragment dpf = new DatePickerFragment();
            dpf.setOnSetDate(new DatePickerFragment.OnSetDate() {
                @Override
                public void setDate(int day, int month, int year) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_MONTH, day);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.YEAR, year);
                    onAdaugareAbonamentLaSportiv.adaugaAbonamentSportiv(a, c.getTime());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            dpf.show(getActivity().getFragmentManager(), "DATE_PICKER_ABONAMENT");
        }
    }

    public void setOnAdaugareAbonamentLaSportiv(OnAdaugareAbonamentLaSportiv onAdaugareAbonamentLaSportiv) {
        this.onAdaugareAbonamentLaSportiv = onAdaugareAbonamentLaSportiv;
    }

    public interface OnAdaugareAbonamentLaSportiv {
        void adaugaAbonamentSportiv(Abonament a, Date dataInceput);
    }
}

package com.example.stefan.tennis.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.TabAdapter;
import com.example.stefan.tennis.interfaces.OnAntrenamentAdaugat;
import com.example.stefan.tennis.models.Abonament;
import com.example.stefan.tennis.models.AbonamenteSportivi;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.models.Sportiv;
import com.example.stefan.tennis.utils.ProjectUtils;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Date;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class ProfilSportivFragment extends BaseFragment implements AbonamenteListFragment.OnAdaugareAbonamentLaSportiv, OnAntrenamentAdaugat {
    @BindView(R.id.imgProfil)
    ImageView imgProfil;
    @BindView(R.id.txtNume)
    TextView txtNume;
    @BindView(R.id.txtData)
    TextView txtData;
    @BindView(R.id.txtSex)
    TextView txtSex;
    @BindView(R.id.txtNivel)
    TextView txtNivel;
    @BindView(R.id.txtSanatate)
    TextView txtSanatate;

    SpeedDialView speedDialView;

    FloatingActionButton fab;
    private Sportiv s;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter adapter;
    private AbonamenteTabFragment abonamenteTabFragment;
    private AntrenamenteTabFragment antrenamenteTabFragment;
    private StatisticiTabFragment statisticiTabFragment;
    private boolean initialized;

    public static ProfilSportivFragment newInstance(int idSportiv) {
        ProfilSportivFragment profilSportivFragment = new ProfilSportivFragment();
        Bundle args = new Bundle();
        args.putInt("sId", idSportiv);
        profilSportivFragment.setArguments(args);
        return profilSportivFragment;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_profil_sportiv;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onDestroy() {
        super.onDestroy();
        fab.setVisibility(View.INVISIBLE);
    }

    public void initUI(View view) {
        Bundle b = getArguments();
        s = Sportiv.getSportivById(b.getInt("sId"));
        txtNume.setText(s.getNumeComplet().toString());
        txtData.setText(ProjectUtils.getFormattedDate(s.getDataNastere()));
        txtSex.setText(s.isSex() ? "Male" : "Female");
        String[] niveluri = getResources().getStringArray(R.array.niveluri);
        txtNivel.setText(niveluri[s.getNivel()]);
        String[] stareSanatate = getResources().getStringArray(R.array.stareSanatate);
        txtSanatate.setText(stareSanatate[s.getStareSanatate()]);
        setupTabs(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupTollbar() {

        fab = getActivity().findViewById(R.id.fab);
        speedDialView = getActivity().findViewById(R.id.speedDial);
        toolbar.setTitle("Profil sportiv");
        if (tabLayout.getSelectedTabPosition() == 0) {
            fab.setVisibility(View.GONE);
            speedDialView.setVisibility(View.VISIBLE);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    fab.setVisibility(View.INVISIBLE);
                    speedDialView.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                    speedDialView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab.setOnClickListener(v -> {
            switch (tabLayout.getSelectedTabPosition()) {
                case 1:
                    AbonamenteListFragment alf = new AbonamenteListFragment();
                    alf.setOnAdaugareAbonamentLaSportiv(ProfilSportivFragment.this);
                    alf.setArguments(getArguments());
                    ProjectUtils.addFragmentToActivity(getActivity(), alf, true, AbonamenteListFragment.TAG);
                    break;
                case 0:
                    if (AbonamenteSportivi.getValabilNrSedinteBySportiv(s.getId()) != null) {
                        AddIstoricAntrenamentFragment addIstoricAntrenamentFragment = new AddIstoricAntrenamentFragment();
                        addIstoricAntrenamentFragment.setArguments(getArguments());
                        addIstoricAntrenamentFragment.setOnAntrenamentAdaugat(ProfilSportivFragment.this);
                        ProjectUtils.addFragmentToActivity(getActivity(), addIstoricAntrenamentFragment, true, "ADD_ISTORIC_ANTRENAMENT_FRAGMENT");
                    } else {
                        Toasty.error(getActivity(), "Sportivul nu are niciun abonament valabil").show();
                    }
                    break;
                default:
                    Toasty.info(getActivity(), "Statistici").show();
                    break;
            }
        });

        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_programari, R.drawable.ic_date_range_white_24dp)
                .setLabel("Programare sedinta")
                .setLabelClickable(true)
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_efectuat, R.drawable.ic_check_white_24dp)
                .setLabel("Inregistrare sedinta")
                .setLabelClickable(true)
                .create());
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_efectuat:
                        if (AbonamenteSportivi.getValabilNrSedinteBySportiv(s.getId()) == null) {
                            Toasty.error(getActivity(), "Sportivul nu are niciun abonament valabil").show();
                            return false;
                        }
                        AddIstoricAntrenamentFragment addIstoricAntrenamentFragment = new AddIstoricAntrenamentFragment();
                        addIstoricAntrenamentFragment.setArguments(getArguments());
                        addIstoricAntrenamentFragment.setOnAntrenamentAdaugat((OnAntrenamentAdaugat) ProfilSportivFragment.this);
                        ProjectUtils.addFragmentToActivity(getActivity(), addIstoricAntrenamentFragment, true, "ADD_ISTORIC_ANTRENAMENT_FRAGMENT");
                        return false;
                    case R.id.fab_programari:
                        if (AbonamenteSportivi.getValabilNrSedinteBySportiv(s.getId()) == null) {
                            Toasty.error(getActivity(), "Sportivul nu are niciun abonament valabil").show();
                            return false;
                        }
                        AddProgramareAntrenamentFragment addProgramareAntrenamentFragment = new AddProgramareAntrenamentFragment();
                        addProgramareAntrenamentFragment.setArguments(getArguments());
                        addProgramareAntrenamentFragment.setOnAntrenamentAdaugat((OnAntrenamentAdaugat) ProfilSportivFragment.this);
                        ProjectUtils.addFragmentToActivity(getActivity(), addProgramareAntrenamentFragment, true, "ADD_PROGRAMARE_ANTRENAMENT_FRAGMENT");
                        return false;
                    default:
                        return false;
                }
            }
        });

    }

    private void setupTabs(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        abonamenteTabFragment = AbonamenteTabFragment.newInstance();
        abonamenteTabFragment.setArguments(getArguments());
        antrenamenteTabFragment = new AntrenamenteTabFragment();
        antrenamenteTabFragment.setArguments(getArguments());
        statisticiTabFragment = StatisticiTabFragment.getInstance(getArguments().getInt("stId"));
        adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(antrenamenteTabFragment, "Antrenamente");
        adapter.addFragment(abonamenteTabFragment, "Abonamente");
        adapter.addFragment(statisticiTabFragment, "Statistici");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void adaugaAbonamentSportiv(Abonament a, Date dataInceput) {
        AbonamenteSportivi abonamenteSportivi = new AbonamenteSportivi();
        abonamenteSportivi.setAbonament(a);
        abonamenteSportivi.setSportiv(s);
        abonamenteSportivi.setDataInceput(dataInceput);
        if (abonamenteSportivi.save()) {
            abonamenteTabFragment.refresh(abonamenteSportivi);
            Toasty.info(getActivity(), a.getDenumire()).show();
        }

    }

    @Override
    public void onAntrenamentAdaugat(IstoricAntrenamente ia) {
        if (ia.save()) {
            antrenamenteTabFragment.refresh(ia);
            abonamenteTabFragment.getTabAdapter().notifyDataSetChanged();
            Toasty.info(getActivity(), "Antrenamentul a fost salvat cu succes!").show();
        }
    }
}


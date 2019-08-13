package com.example.stefan.tennis.fragments;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.NotificariAdapter;
import com.example.stefan.tennis.models.Notificare;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.List;

import butterknife.BindView;

public class NotificariFragment extends BaseFragment implements NotificariAdapter.OnItemPressed {

    public static final String TAG = "NotificariFragment";
    @BindView(R.id.recyclerView)
    RecyclerView listaNotificari;
    private View fab;

    public static NotificariFragment newInstance() {
        return new NotificariFragment();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_notificari_list;
    }

    @Override
    public void initUI(View view) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        listaNotificari.setLayoutManager(layoutManager);


        //Item Decorator:
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listaNotificari.getContext(),
                layoutManager.getOrientation());
        listaNotificari.addItemDecoration(dividerItemDecoration);

        List<Notificare> notificari = Notificare.getNotificariNesterse();
        Log.v(TAG, notificari.size() + " notificari");
        NotificariAdapter adapter = new NotificariAdapter();
        adapter.setOnItemPressed(this);
        adapter.updateList(notificari);
        listaNotificari.setAdapter(adapter);
    }

    @Override
    public void pressedItem(Notificare n) {
        ProjectUtils.addFragmentToActivity(getActivity(), new AntrenamenteNeevaluateFragment(), true, AntrenamenteNeevaluateFragment.TAG);
    }

    @Override
    public void setupTollbar() {
        super.setupTollbar();
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }
}

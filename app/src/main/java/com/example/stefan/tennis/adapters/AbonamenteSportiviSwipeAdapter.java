package com.example.stefan.tennis.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.fragments.AddAbonamentFragment;
import com.example.stefan.tennis.models.AbonamenteSportivi;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AbonamenteSportiviSwipeAdapter extends RecyclerSwipeAdapter<AbonamenteSportiviSwipeAdapter.NViewHolder> implements AddAbonamentFragment.OnAbonamentSportSaved {

    private List<AbonamenteSportivi> mList;

    @NonNull
    @Override
    public NViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_abonament_tab, viewGroup, false);
        return new NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NViewHolder iaViewHolder, int i) {
        AbonamenteSportivi ia = mList.get(i);

        iaViewHolder.delete.setOnClickListener(v -> {
            if (ia.save()) {
                mList.remove(i);
                ia.delete();
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mList.size());
            }
        });

        iaViewHolder.txtDenumire.setText(ia.getAbonament().getDenumire());
        iaViewHolder.txtDataInceput.setText(String.format("Data inceput: %s", ProjectUtils.getFormattedDate(ia.getDataInceput())));
        iaViewHolder.txtDataSfarsit.setText(String.format("Data sfarist: %s", ProjectUtils.getFormattedDate(ia.getDataSfarsit())));
        iaViewHolder.txtValabil.setText(String.format("Valabil: %s", ia.isValabil() ? "Da" : "Nu"));
        iaViewHolder.txtSedinteRamase.setText(String.format("Sedinte ramase: %s", ia.getSedinteRamase()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public void onAbonamenteSportivSaved(AbonamenteSportivi a) {
        int index = mList.indexOf(a);
        mList.set(index, a);
        notifyDataSetChanged();
    }

    public void updateList(List<AbonamenteSportivi> list) {
        this.mList = list;
    }

    public void add(AbonamenteSportivi as) {
        mList.add(as);
    }

    public static class NViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtDenumire)
        TextView txtDenumire;
        @BindView(R.id.txtValabil)
        TextView txtValabil;
        @BindView(R.id.txtDataInceput)
        TextView txtDataInceput;
        @BindView(R.id.txtDataSfarsit)
        TextView txtDataSfarsit;
        @BindView(R.id.txtSedinteRamase)
        TextView txtSedinteRamase;
        @BindView(R.id.swipe)
        SwipeLayout swipeLayout;
        @BindView(R.id.delete)
        ImageView delete;


        public NViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

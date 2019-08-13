package com.example.stefan.tennis.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
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
import com.example.stefan.tennis.fragments.AddAntrenorFragment;
import com.example.stefan.tennis.models.Abonament;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AbonamentSwipeAdapter extends RecyclerSwipeAdapter<AbonamentSwipeAdapter.NViewHolder> implements AddAbonamentFragment.OnAbonamentSaved {

    private List<Abonament> mList;
    private List<Abonament> itemsCopy = new ArrayList<>();
    private Context context;
    private AbonamentSwipeAdapter.onAbonamentSelected onAbonamentSelected;

    public AbonamentSwipeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_abonament, viewGroup, false);
        return new NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NViewHolder iaViewHolder, int i) {
        Abonament ia = mList.get(i);

        // iaViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        iaViewHolder.delete.setOnClickListener(v -> {
            if (ia.save()) {
                mList.remove(i);
                itemsCopy.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mList.size());
            }
        });

        iaViewHolder.edit.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt("aId", ia.getId());
            FragmentActivity activity = (FragmentActivity) (context);
            FragmentManager fm = activity.getSupportFragmentManager();
            AddAbonamentFragment alertDialog = new AddAbonamentFragment();
            alertDialog.setArguments(b);
            alertDialog.setOnAbonamentSaved(AbonamentSwipeAdapter.this);
            alertDialog.show(fm, AddAntrenorFragment.TAG);
            alertDialog.setBool(true);
        });

        iaViewHolder.cardView.setOnClickListener(v -> {
            onAbonamentSelected.onAbonamentSelected(ia);
        });

        iaViewHolder.txtNume.setText(ia.getDenumire());

        iaViewHolder.txtNrSedinte.setText("Numar sedinte: " + ia.getNrSedinte());

        iaViewHolder.txtPret.setText("Pret: " + ia.getPret());
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
    public void onAbonamentSaved(Abonament a) {
        int index = mList.indexOf(a);
        mList.set(index, a);
        index = itemsCopy.indexOf(a);
        itemsCopy.set(index, a);
        notifyDataSetChanged();
    }

    public void updateList(List<Abonament> list) {
        this.mList = list;
        this.itemsCopy.addAll(mList);
    }

    public void add(Abonament a) {
        mList.add(a);
        itemsCopy.add(a);
    }

    public void filter(String text) {
        mList.clear();
        if (text.isEmpty()) {
            mList.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (Abonament item : itemsCopy) {
                if (item.getDenumire().toLowerCase().startsWith(text.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnAbonamentSelected(AbonamentSwipeAdapter.onAbonamentSelected onAbonamentSelected) {
        this.onAbonamentSelected = onAbonamentSelected;
    }

    public interface onAbonamentSelected {
        void onAbonamentSelected(Abonament a);
    }

    public static class NViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtDenumire)
        TextView txtNume;
        @BindView(R.id.txtNrSedinte)
        TextView txtNrSedinte;
        @BindView(R.id.txtPret)
        TextView txtPret;
        @BindView(R.id.swipe)
        SwipeLayout swipeLayout;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.edit)
        ImageView edit;
        @BindView(R.id.card_view)
        CardView cardView;

        public NViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

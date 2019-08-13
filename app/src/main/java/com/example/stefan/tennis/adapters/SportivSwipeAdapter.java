package com.example.stefan.tennis.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.stefan.tennis.fragments.AddSportivFragment;
import com.example.stefan.tennis.models.Sportiv;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SportivSwipeAdapter extends RecyclerSwipeAdapter<SportivSwipeAdapter.NViewHolder> implements AddSportivFragment.OnSportivSaved {

    private List<Sportiv> mList;
    private List<Sportiv> itemsCopy = new ArrayList<>();
    private Context context;
    private OnSportivSelected onSportivSelected;

    public SportivSwipeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_sportiv, viewGroup, false);
        return new NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NViewHolder iaViewHolder, int i) {
        Sportiv ia = mList.get(i);

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
            b.putInt("sId", ia.getId());
            AddSportivFragment alertDialog = new AddSportivFragment();
            alertDialog.setArguments(b);
            alertDialog.setOnSportivSaved(SportivSwipeAdapter.this);
            ProjectUtils.addFragmentToActivity((Activity) context, alertDialog, true, AddSportivFragment.TAG);
            alertDialog.setBool(true);
        });

        iaViewHolder.cardView.setOnClickListener(v -> {
            onSportivSelected.sportivSelected(ia);
        });

        iaViewHolder.txtNume.setText(ia.getNumeComplet());

        String input;
        switch (ia.getNivel()) {
            case 1:
                input = "Incepator";
                break;
            case 2:
                input = "Intermediar";
                break;
            case 3:
                input = "Avansat";
                break;
            case 4:
                input = "Profesionist";
                break;
            default:
                input = "-";
                break;
        }
        iaViewHolder.txtNivel.setText("Nivel: " + input);

        switch (ia.getStareSanatate()) {
            case 1:
                input = "Sanatos";
                break;
            case 2:
                input = "Bolnav";
                break;
            case 3:
                input = "Accidentat";
                break;
            default:
                input = "-";
                break;
        }
        iaViewHolder.txtStareSanatate.setText("Stare sanatate: " + input);
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
    public void onSportivSaved(Sportiv s) {
        int index = mList.indexOf(s);
        mList.set(index, s);
        index = itemsCopy.indexOf(s);
        itemsCopy.set(index, s);
        notifyDataSetChanged();
    }

    public void updateList(List<Sportiv> list) {
        this.mList = list;
        this.itemsCopy.addAll(mList);
    }

    public void add(Sportiv a) {
        mList.add(a);
        itemsCopy.add(a);
    }

    public void filter(String text) {
        mList.clear();
        if (text.isEmpty()) {
            mList.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (Sportiv item : itemsCopy) {
                if (item.getNume().toLowerCase().startsWith(text.toLowerCase())
                        || item.getPrenume().toLowerCase().startsWith(text.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnSportivSelected(OnSportivSelected onSportivSelected) {
        this.onSportivSelected = onSportivSelected;
    }

    public interface OnSportivSelected {
        void sportivSelected(Sportiv sportiv);
    }

    public static class NViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNume)
        TextView txtNume;
        @BindView(R.id.txtNivel)
        TextView txtNivel;
        @BindView(R.id.txtStareSanatate)
        TextView txtStareSanatate;
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

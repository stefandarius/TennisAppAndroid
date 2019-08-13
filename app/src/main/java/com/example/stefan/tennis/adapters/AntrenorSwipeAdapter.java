package com.example.stefan.tennis.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.fragments.AddAntrenorFragment;
import com.example.stefan.tennis.models.Antrenor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AntrenorSwipeAdapter extends RecyclerSwipeAdapter<AntrenorSwipeAdapter.NViewHolder> implements AddAntrenorFragment.OnAntrenorSaved {

    public static final String TAG = "AntrenorSwipeAdapter";
    private List<Antrenor> mList;
    private List<Antrenor> itemsCopy = new ArrayList<>();
    private Context context;

    public AntrenorSwipeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_antrenor, viewGroup, false);
        return new NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NViewHolder iaViewHolder, int i) {
        Antrenor ia = mList.get(i);

        iaViewHolder.delete.setOnClickListener(v -> {
            if (ia.save()) {
                mList.remove(i);
                itemsCopy.remove(i);
                ia.delete();
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mList.size());
            }
        });

        iaViewHolder.edit.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt("aId", ia.getId());
            FragmentActivity activity = (FragmentActivity) (context);
            FragmentManager fm = activity.getSupportFragmentManager();
            AddAntrenorFragment alertDialog = new AddAntrenorFragment();
            alertDialog.setArguments(b);
            alertDialog.setOnAntrenorSaved(AntrenorSwipeAdapter.this);
            alertDialog.show(fm, AddAntrenorFragment.TAG);
            alertDialog.setBool(true);
        });

        iaViewHolder.txtContinut.setText(ia.getNumeComplet());
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
    public void onAntrenorSaved(Antrenor a) {
        int index = mList.indexOf(a);
        mList.set(index, a);
        index = itemsCopy.indexOf(a);
        itemsCopy.set(index, a);
        notifyDataSetChanged();
    }

    public void updateList(List<Antrenor> list) {
        this.mList = list;
        this.itemsCopy.addAll(mList);
    }

    public void addToOriginal(Antrenor a) {
        mList.add(a);
        itemsCopy.add(a);
    }

    public void filter(String text) {
        mList.clear();
        if (text.isEmpty()) {
            mList.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (Antrenor item : itemsCopy) {
                if (item.getNumeComplet().toLowerCase().startsWith(text.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class NViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNume)
        TextView txtContinut;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.edit)
        ImageView edit;

        public NViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

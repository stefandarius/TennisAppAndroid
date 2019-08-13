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

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.fragments.AddTipAntrenamentFragment;
import com.example.stefan.tennis.models.TipAntrenament;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TipAntrenamenteSwipeAdapter extends RecyclerSwipeAdapter<TipAntrenamenteSwipeAdapter.NViewHolder> implements AddTipAntrenamentFragment.OnAntrenamentSaved {

    private List<TipAntrenament> mList;
    private List<TipAntrenament> itemsCopy = new ArrayList<>();
    private Context context;

    public TipAntrenamenteSwipeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_tip_anatrenament, viewGroup, false);
        return new NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NViewHolder iaViewHolder, int i) {
        TipAntrenament ia = mList.get(i);

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
            b.putInt("taId", ia.getId());
            FragmentActivity activity = (FragmentActivity) (context);
            FragmentManager fm = activity.getSupportFragmentManager();
            AddTipAntrenamentFragment alertDialog = new AddTipAntrenamentFragment();
            alertDialog.setArguments(b);
            alertDialog.setOnAntrenamentSaved(TipAntrenamenteSwipeAdapter.this);
            alertDialog.show(fm, AddTipAntrenamentFragment.TAG);
            alertDialog.setBool(true);
        });

        iaViewHolder.txtDenumire.setText(ia.getDenumirea());
        iaViewHolder.txtStatus.setText(ia.isActiv() ? "Activ" : "Inactiv");
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
    public void onAntrenamentSaved(TipAntrenament ta) {
        int index = mList.indexOf(ta);
        mList.set(index, ta);
        index = itemsCopy.indexOf(ta);
        itemsCopy.set(index, ta);
        notifyDataSetChanged();
    }

    public void updateList(List<TipAntrenament> list) {
        this.mList = list;
        this.itemsCopy.addAll(mList);
    }

    public void add(TipAntrenament a) {
        mList.add(a);
        itemsCopy.add(a);
    }

    public void filter(String text) {
        mList.clear();
        if (text.isEmpty()) {
            mList.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for (TipAntrenament item : itemsCopy) {
                if (item.getDenumirea().toLowerCase().startsWith(text.toLowerCase())) {
                    mList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class NViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtDenumire)
        TextView txtDenumire;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.swipe)
        SwipeLayout swipeLayout;
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

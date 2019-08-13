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
import com.example.stefan.tennis.models.Notificare;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificariAdapter extends RecyclerSwipeAdapter<NotificariAdapter.NViewHolder> {

    private List<Notificare> mList;
    private OnItemPressed onItemPressed;

    @NonNull
    @Override
    public NViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_notificari, viewGroup, false);
        return new NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NViewHolder iaViewHolder, int i) {
        Notificare ia = mList.get(i);

        // iaViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        iaViewHolder.delete.setOnClickListener(v -> {
            ia.setDataStergere(new Date());
            if (ia.save()) {
                mList.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mList.size());
            }
        });

        iaViewHolder.swipeLayout.setOnClickListener(v -> {
            onItemPressed.pressedItem(ia);
        });

        iaViewHolder.txtContinut.setText(ia.getContinut());
        iaViewHolder.txtDataOra.setText(ProjectUtils.getFormattedDate(ia.getDataPrimire()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void updateList(List<Notificare> list) {
        this.mList = list;
    }

    public void setOnItemPressed(OnItemPressed onItemPressed) {
        this.onItemPressed = onItemPressed;
    }

    public interface OnItemPressed {
        void pressedItem(Notificare n);
    }

    public static class NViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtContinut)
        TextView txtContinut;
        @BindView(R.id.txtDataOra)
        TextView txtDataOra;
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

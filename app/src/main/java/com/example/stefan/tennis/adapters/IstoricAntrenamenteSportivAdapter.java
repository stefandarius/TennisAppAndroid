package com.example.stefan.tennis.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class IstoricAntrenamenteSportivAdapter extends RecyclerView.Adapter<IstoricAntrenamenteSportivAdapter.IAViewHolder> {

    private List<IstoricAntrenamente> mList;
    private OnAntrenamentSelected onAntrenamentSelected;

    @NonNull
    @Override
    public IAViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_antrenament, viewGroup, false);
        return new IAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IAViewHolder iaViewHolder, int i) {
        IstoricAntrenamente ia = mList.get(i);
        iaViewHolder.txtTipAntrenament.setText(ia.getTipAntrenament().getDenumirea());
        iaViewHolder.txtdataOra.setText(ProjectUtils.getFormattedDate(ia.getDataAntrenament(), ProjectUtils.BASE_DATE_FORMAT_TIME));
        iaViewHolder.txtNume.setText(ia.getAntrenor().getNumeComplet());

        String[] dificultate = getContext().getResources().getStringArray(R.array.gradDificultate);
        iaViewHolder.txtGradDificultate.setText(dificultate[ia.getGradDificultate()]);

        iaViewHolder.ratingBar.setRating(ia.getRating());

        iaViewHolder.cardView.setOnClickListener(v -> {
            onAntrenamentSelected.antrenamentSelected(ia);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<IstoricAntrenamente> list) {
        this.mList = list;
    }

    public void add(IstoricAntrenamente ia) {
        mList.add(ia);
    }

    public void setOnAntrenamentSelected(OnAntrenamentSelected onAntrenamentSelected) {
        this.onAntrenamentSelected = onAntrenamentSelected;
    }

    public interface OnAntrenamentSelected {
        void antrenamentSelected(IstoricAntrenamente istoricAntrenamente);
    }

    public static class IAViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtTipAntrenament)
        TextView txtTipAntrenament;
        @BindView(R.id.txtDataOra)
        TextView txtdataOra;
        @BindView(R.id.txtNumeAntrenor)
        TextView txtNume;
        @BindView(R.id.txtGradDificultate)
        TextView txtGradDificultate;
        @BindView(R.id.rating)
        RatingBar ratingBar;
        @BindView(R.id.card_view)
        CardView cardView;

        public IAViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

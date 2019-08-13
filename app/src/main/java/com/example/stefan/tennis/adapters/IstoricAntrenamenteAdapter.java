package com.example.stefan.tennis.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IstoricAntrenamenteAdapter extends RecyclerView.Adapter<IstoricAntrenamenteAdapter.IAViewHolder> {

    private List<IstoricAntrenamente> mList;

    @NonNull
    @Override
    public IAViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_antrenament_viitor, viewGroup, false);
        return new IAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IAViewHolder iaViewHolder, int i) {
        IstoricAntrenamente ia = mList.get(i);
        iaViewHolder.txtTipAntrenament.setText(ia.getTipAntrenament().getDenumirea());
        iaViewHolder.txtdataOra.setText(ProjectUtils.getFormattedDate(ia.getDataAntrenament(), ProjectUtils.BASE_DATE_FORMAT_TIME));
        if (ia.getAbonamanteSportiv() != null)
            iaViewHolder.txtNume.setText(ia.getAbonamanteSportiv().getSportiv().getNumeComplet());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<IstoricAntrenamente> list) {
        this.mList = list;
    }

    public static class IAViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtTipAntrenament)
        TextView txtTipAntrenament;
        @BindView(R.id.txtDataOra)
        TextView txtdataOra;
        @BindView(R.id.txtNume)
        TextView txtNume;

        public IAViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.example.stefan.tennis.fragments;


import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.models.TipAntrenament;
import com.example.stefan.tennis.utils.ProjectUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddTipAntrenamentFragment extends android.support.v4.app.DialogFragment {
    public static final String TAG = "AddTipAntrenamentFragme";
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.etxtNume)
    EditText etxtDenumire;
    @BindView(R.id.ckbStatus)
    CheckBox ckbStatus;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnDiscard)
    Button btnDiscard;
    private OnAntrenamentSaved onAntrenamentSaved;
    private TipAntrenament tp;
    private View fab;
    private Unbinder binder;
    private boolean bool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_tip_antrenament, container, false);
        ProjectUtils.setupUI(view, getActivity());
        binder = ButterKnife.bind(this, view);
        initFragmentUI(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        binder.unbind();
    }

    private void initFragmentUI(View view) {
        txtTitle.setText(bool ? "Edit" : "Create");
        ckbStatus.setChecked(true);

        loadTipAntrenamentData();

        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btnSave)
    public void btnSaveClicked() {
        if (tp == null) {
            tp = new TipAntrenament();
        }
        tp.setDenumirea(etxtDenumire.getText().toString().trim());
        tp.setActiv(ckbStatus.isChecked());
        try {
            if (tp.save()) {
                Toast.makeText(getActivity(), "Tip antrenament salvat", Toast.LENGTH_SHORT).show();
                onAntrenamentSaved.onAntrenamentSaved(tp);
                ProjectUtils.hideSoftKeyboard(getActivity());
                getDialog().dismiss();
            } else {
                Toast.makeText(getActivity(), "Eroare", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLiteConstraintException ex) {
            if (ex.getMessage().contains("denumire")) {
                etxtDenumire.selectAll();
                etxtDenumire.requestFocus();
                Toast.makeText(getActivity(), "Acest antrenament exista deja", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btnDiscard)
    public void btnDiscardClicked() {
        getDialog().dismiss();
    }

    public void setTitle(String input) {
        txtTitle.setText(input);
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    private void loadTipAntrenamentData() {
        Bundle arg = getArguments();
        if (arg != null && arg.containsKey("taId")) {
            int id = arg.getInt("taId");
            tp = TipAntrenament.getTipAntrenamentById(id);
            etxtDenumire.setText(tp.getDenumirea());
            ckbStatus.setChecked(tp.isActiv());
        }
    }

    public void setOnAntrenamentSaved(OnAntrenamentSaved onAntrenamentSaved) {
        this.onAntrenamentSaved = onAntrenamentSaved;
    }


    public interface OnAntrenamentSaved {
        void onAntrenamentSaved(TipAntrenament ta);
    }
}

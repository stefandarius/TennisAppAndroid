package com.example.stefan.tennis.fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.models.Abonament;
import com.example.stefan.tennis.models.AbonamenteSportivi;
import com.example.stefan.tennis.utils.ProjectUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

public class AddAbonamentFragment extends android.support.v4.app.DialogFragment {

    public static final String TAG = "AddAbonamentFragment";
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.etxtNume)
    EditText etxtDenumire;
    @BindView(R.id.etxtNrSedinte)
    EditText etxtNrSedinte;
    @BindView(R.id.etxtPret)
    EditText etxtPret;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnDiscard)
    Button btnDiscard;
    @BindView(R.id.etxtDurata)
    EditText etxtDurata;
    private OnAbonamentSaved onAbonamentSaved;
    private OnAbonamentSportSaved onAbonamentSportSaved;
    private Abonament a;
    private View fab;
    private Unbinder binder;
    private boolean bool;
    private AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_abonament, container, false);
        ProjectUtils.setupUI(view, getActivity());
        binder = ButterKnife.bind(this, view);
        initFragmentUI(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fab.setVisibility(View.VISIBLE);
        binder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtNume, RegexTemplate.NOT_EMPTY, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtDurata, RegexTemplate.NOT_EMPTY, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtPret, RegexTemplate.NOT_EMPTY, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtNrSedinte, RegexTemplate.NOT_EMPTY, R.string.eroare);
    }

    private void initFragmentUI(View view) {
        txtTitle.setText(bool ? "Edit" : "Create");
        loadAbonamentData();
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    public void setTitle(String input) {
        txtTitle.setText(input);
    }

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    private void loadAbonamentData() {
        Bundle arg = getArguments();
        if (arg != null && arg.containsKey("aId")) {
            int id = arg.getInt("aId");
            a = Abonament.getAbonamentById(id);
            etxtDenumire.setText(a.getDenumire());
            etxtNrSedinte.setText(String.valueOf(a.getNrSedinte()));
            etxtPret.setText(String.valueOf(a.getPret()));
            etxtDurata.setText(String.valueOf(a.getDurata()));
        }
    }

    public void setOnAbonamentSaved(OnAbonamentSaved onAbonamentSaved) {
        this.onAbonamentSaved = onAbonamentSaved;
    }

    @OnClick(R.id.btnSave)
    public void btnSaveClicked() {
        boolean validate = mAwesomeValidation.validate();
        if (validate) {
            if (a == null) {
                a = new Abonament();
            }
            a.setDenumire(etxtDenumire.getText().toString().trim());
            a.setNrSedinte(Integer.parseInt(etxtNrSedinte.getText().toString()));
            a.setPret(Integer.parseInt(etxtPret.getText().toString()));
            a.setDurata(Integer.parseInt(etxtDurata.getText().toString().trim()));
            try {
                if (a.save()) {
                    Toasty.info(getActivity(), "Tip abonament adaugat").show();
                    onAbonamentSaved.onAbonamentSaved(a);
                    ProjectUtils.hideSoftKeyboard(getActivity());
                    getDialog().dismiss();
                } else {
                    Toasty.error(getActivity(), "Eroare").show();
                }

            } catch (SQLiteConstraintException ex) {
                if (ex.getMessage().contains("denumire")) {
                    etxtDenumire.selectAll();
                    etxtDenumire.requestFocus();
                    Toasty.error(getActivity(), "Acesta denumire exista deja").show();
                }
            }
        }
    }

    @OnClick(R.id.btnDiscard)
    public void btnDiscardClicked() {
        getDialog().dismiss();
    }

    public void setOnAbonamentSportSaved(OnAbonamentSportSaved onAbonamentSportSaved) {
        this.onAbonamentSportSaved = onAbonamentSportSaved;
    }

    public interface OnAbonamentSaved {
        void onAbonamentSaved(Abonament a);
    }

    public interface OnAbonamentSportSaved {
        void onAbonamenteSportivSaved(AbonamenteSportivi a);
    }
}

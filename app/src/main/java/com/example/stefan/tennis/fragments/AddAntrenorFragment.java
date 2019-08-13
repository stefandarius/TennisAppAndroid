package com.example.stefan.tennis.fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.models.Antrenor;
import com.example.stefan.tennis.utils.ProjectUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;


public class AddAntrenorFragment extends DialogFragment {

    public static final String TAG = "AddAntrenorFragment";
    @BindView(R.id.etxtNume)
    EditText etxtNume;
    @BindView(R.id.etxtPrenume)
    EditText etxtPrenume;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnDiscard)
    Button btnDiscard;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    private Unbinder binder;
    private View fab;
    private Antrenor a;
    private OnAntrenorSaved onAntrenorSaved;
    private boolean bool;
    private AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_antrenor, container, false);
        ProjectUtils.setupUI(view, getActivity());
        binder = ButterKnife.bind(this, view);
        initUI(view);
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
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtNume, Patterns.EMAIL_ADDRESS, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtPrenume, RegexTemplate.NOT_EMPTY, R.string.eroare);
    }

    public void initUI(View view) {
        txtTitle.setText(bool ? "Edit" : "Create");
        loadAntrenortData();
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    private void loadAntrenortData() {
        Bundle arg = getArguments();
        if (arg != null && arg.containsKey("aId")) {
            int id = arg.getInt("aId");
            a = Antrenor.getAntrenorById(id);
            etxtNume.setText(a.getNume());
            etxtPrenume.setText(a.getPrenume());
        }
    }

    public void setOnAntrenorSaved(OnAntrenorSaved onAntrenorSaved) {
        this.onAntrenorSaved = onAntrenorSaved;
    }

    @OnClick(R.id.btnSave)
    public void btnSaveClicked() {
        boolean validate = mAwesomeValidation.validate();
        if (validate) {
            if (a == null) {
                a = new Antrenor();
            }
            a.setNume(etxtNume.getText().toString().trim());
            a.setPrenume(etxtPrenume.getText().toString().trim());
            try {
                if (a.save()) {
                    Toasty.info(getActivity(), "Antrenor adaugat").show();
                    onAntrenorSaved.onAntrenorSaved(a);
                    ProjectUtils.hideSoftKeyboard(getActivity());
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "Eroare", Toast.LENGTH_SHORT).show();
                }

            } catch (SQLiteConstraintException ex) {
                if (ex.getMessage().contains("denumire")) {
                    etxtNume.selectAll();
                    etxtNume.requestFocus();
                    Toasty.error(getActivity(), "Acest antrenor exista deja").show();
                }
            }
        }
    }

    @OnClick(R.id.btnDiscard)
    public void btnDiscardClicked() {
        getDialog().dismiss();
    }

    public interface OnAntrenorSaved {
        void onAntrenorSaved(Antrenor a);
    }
}

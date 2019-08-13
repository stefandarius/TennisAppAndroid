package com.example.stefan.tennis.fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.models.Sportiv;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class AddSportivFragment extends BaseFragment implements DatePickerFragment.OnSetDate {
    public static final String TAG = "AddSportivFragment";
    @BindView(R.id.txtTitle)
    TextView txtAction;
    @BindView(R.id.etxtNume)
    EditText etxtNume;
    @BindView(R.id.etxtPrenume)
    EditText etxtPrenume;
    @BindView(R.id.etxtEmail)
    EditText etxtEmail;
    @BindView(R.id.etxtNumarTelefon)
    EditText etxtNumarTelefon;
    @BindView(R.id.etxtDataNastere)
    TextView etxtDataNastere;
    @BindView(R.id.etxtInaltime)
    EditText etxtInaltime;
    @BindView(R.id.etxtGreutate)
    EditText etxtGreutate;
    @BindView(R.id.ckbMasculin)
    RadioButton ckbMasculin;
    @BindView(R.id.ckbFeminin)
    RadioButton ckbMFeminin;
    @BindView(R.id.spnNivel)
    Spinner spnNivel;
    @BindView(R.id.spnStareSanatate)
    Spinner spnStareSanatate;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnDiscard)
    Button btnDiscard;
    private OnSportivSaved onSportivSaved;
    private View fab;
    private Sportiv s;
    private boolean bool;
    private AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_add_sportiv;
    }

    public void initUI(View view) {
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        txtAction.setText(bool ? "Edit" : "Create");

        ArrayAdapter<CharSequence> adapterNiveluri = ArrayAdapter.createFromResource(getActivity(),
                R.array.niveluri, android.R.layout.simple_spinner_item);
        adapterNiveluri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNivel.setAdapter(adapterNiveluri);

        ArrayAdapter<CharSequence> adapterStari = ArrayAdapter.createFromResource(getActivity(),
                R.array.stareSanatate, android.R.layout.simple_spinner_item);
        spnStareSanatate.setAdapter(adapterStari);
        adapterStari.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        loadSportivData();
    }

    private void addValidationSpinner(int idSpinner, final String mesaj) {
        mAwesomeValidation.addValidation(getActivity(), idSpinner, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equals(mesaj)) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.app_name);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addValidationSpinner(R.id.spnNivel, "--Selectati nivel--");
        addValidationSpinner(R.id.spnStareSanatate, "--Selectati stare--");
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtNume, RegexTemplate.NOT_EMPTY, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtPrenume, RegexTemplate.NOT_EMPTY, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtEmail, Patterns.EMAIL_ADDRESS, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtNumarTelefon, RegexTemplate.TELEPHONE, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtInaltime, RegexTemplate.NOT_EMPTY, R.string.eroare);
        mAwesomeValidation.addValidation(getActivity(), R.id.etxtGreutate, RegexTemplate.NOT_EMPTY, R.string.eroare);
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    private void loadSportivData() {
        Bundle arg = getArguments();
        if (arg != null && arg.containsKey("sId")) {
            int id = arg.getInt("sId");
            s = Sportiv.getSportivById(id);
            etxtNume.setText(s.getNume());
            etxtPrenume.setText(s.getPrenume());
            etxtDataNastere.setText(ProjectUtils.getFormattedDate(s.getDataNastere()));
            etxtEmail.setText(s.getEmail());
            etxtNumarTelefon.setText(s.getNumarTelefon());
            etxtGreutate.setText(String.valueOf(s.getGreutate()));
            etxtInaltime.setText(String.valueOf(s.getInaltime()));
            spnNivel.setSelection(s.getNivel());
            spnStareSanatate.setSelection(s.getStareSanatate());
            if (s.isSex()) ckbMasculin.setChecked(true);
            else ckbMFeminin.setChecked(false);
        }
    }

    @OnClick(R.id.etxtDataNastere)
    public void etxtDataNastereClicked() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setOnSetDate(AddSportivFragment.this);
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.btnSave)
    public void btnSaveClicked() {
        if (s == null) {
            s = new Sportiv();
        }

        if (!valid()) return;

        s.setNume(etxtNume.getText().toString());
        s.setPrenume(etxtPrenume.getText().toString());
        s.setEmail(etxtEmail.getText().toString());
        s.setNumarTelefon(etxtNumarTelefon.getText().toString());
        s.setGreutate(Integer.parseInt(etxtGreutate.getText().toString()));
        s.setInaltime(Integer.parseInt(etxtInaltime.getText().toString()));
        if (ckbMasculin.isChecked()) s.setSex(true);
        if (ckbMFeminin.isChecked()) s.setSex(false);
        s.setNivel(spnNivel.getSelectedItemPosition());
        s.setStareSanatate(spnStareSanatate.getSelectedItemPosition());
        s.setDataNastere(ProjectUtils.getDateFromString(etxtDataNastere.getText().toString()));

        try {
            if (s.save()) {
                Toasty.info(getActivity(), "Sportiv adaugat").show();
                onSportivSaved.onSportivSaved(s);
                ProjectUtils.hideSoftKeyboard(getActivity());
                getActivity().onBackPressed();
            } else {
                Toasty.error(getActivity(), "Eroare").show();
            }

        } catch (SQLiteConstraintException ex) {
//                    if (ex.getMessage().contains("denumire")) {
//                        etxtDenumire.selectAll();
//                        etxtDenumire.requestFocus();
//                        Toast.makeText(getActivity(), "Acesta denumire exista deja", Toast.LENGTH_SHORT).show();
//                    }
        }
    }

    @OnClick(R.id.btnDiscard)
    public void btnDiscardClicked() {
        getActivity().onBackPressed();
    }

    private boolean valid() {
        boolean validate = mAwesomeValidation.validate();
        try {
            int greutate = Integer.parseInt(etxtGreutate.getText().toString());
        } catch (NumberFormatException ex) {
            etxtGreutate.requestFocus();
            Toasty.info(getActivity(), "Greutatea trebuie exprimata printr-un numar natural").show();
            return false;
        }
        try {
            int inaltime = Integer.parseInt(etxtInaltime.getText().toString());
        } catch (NumberFormatException ex) {
            etxtInaltime.requestFocus();
            Toasty.info(getActivity(), "Inaltimea trebuie exprimata printr-un numar natural").show();
            return false;
        }
        return validate;
    }

    public void setOnSportivSaved(OnSportivSaved onSportivSaved) {
        this.onSportivSaved = onSportivSaved;
    }

    @Override
    public void setDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        etxtDataNastere.setText(ProjectUtils.getFormattedDate(c.getTime()));
    }

    public interface OnSportivSaved {
        void onSportivSaved(Sportiv s);
    }

}

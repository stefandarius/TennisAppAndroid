package com.example.stefan.tennis.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.example.stefan.tennis.R;
import com.example.stefan.tennis.interfaces.OnAntrenamentAdaugat;
import com.example.stefan.tennis.models.AbonamenteSportivi;
import com.example.stefan.tennis.models.Antrenor;
import com.example.stefan.tennis.models.IstoricAntrenamente;
import com.example.stefan.tennis.models.TipAntrenament;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class AddIstoricAntrenamentFragment extends BaseFragment implements DatePickerFragment.OnSetDate, TimePickerFragment.OnSetTime, RatingBar.OnRatingBarChangeListener {

    public static final String TAG = "AddIstoricAntrenamentFr";
    @BindView(R.id.spnAntrenament)
    Spinner spnAntrenament;
    @BindView(R.id.spnAntrenor)
    Spinner spnAntrenor;
    @BindView(R.id.spnGradDificultate)
    Spinner spnGradDificultate;
    @BindView(R.id.txtNume)
    TextView txtNume;
    @BindView(R.id.etxtDataOra)
    TextView etxtDataOra;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    private View fab;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private Date dataOraAntrenament;
    private IstoricAntrenamente istoricAntrenamente;
    private OnAntrenamentAdaugat onAntrenamentAdaugat;
    private AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    private ArrayAdapter<CharSequence> adapterDificultate;
    private ArrayAdapter<TipAntrenament> adapterTipAntrenament;
    private ArrayAdapter<Antrenor> adapterAntrenor;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_add_antrenament_tab;
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
        addValidationSpinner(R.id.spnAntrenament, "--Selectati antrenamentul--");
        addValidationSpinner(R.id.spnGradDificultate, "--Selectati dificultatea--");
        addValidationSpinner(R.id.spnAntrenor, "--Selectati antrenorul--");

    }

    @Override
    public void initUI(View view) {
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        ratingBar.setOnRatingBarChangeListener(this);
        ratingBar.setStepSize((float) 1);

        adapterDificultate = ArrayAdapter.createFromResource(getActivity(),
                R.array.gradDificultate, android.R.layout.simple_spinner_item);
        adapterDificultate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGradDificultate.setAdapter(adapterDificultate);

        List<TipAntrenament> tipuriAntrenamente = TipAntrenament.getAll();
        tipuriAntrenamente.add(0, new TipAntrenament("--Selectati antrenamentul--"));
        adapterTipAntrenament = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, tipuriAntrenamente);
        adapterTipAntrenament.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAntrenament.setAdapter(adapterTipAntrenament);

        List<Antrenor> antrenori = Antrenor.getAll();
        antrenori.add(0, new Antrenor("--Selectati antrenorul--"));
        adapterAntrenor = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, antrenori);
        adapterAntrenor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAntrenor.setAdapter(adapterAntrenor);

        loadSportivData();
    }

    @Override
    public void setDate(int day, int month, int year) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnSetTime(AddIstoricAntrenamentFragment.this);
        timePickerFragment.show(getFragmentManager(), "timePicker");

        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, this.hour);
        c.set(Calendar.MINUTE, this.minute);

        this.dataOraAntrenament = c.getTime();

        etxtDataOra.setText(ProjectUtils.getFormattedDate(c.getTime(), "dd/MM/yyyy HH:mm"));
    }

    @OnClick(R.id.etxtDataOra)
    public void etxtDataOraClicked() {
        DatePickerFragment newFragment = new DatePickerFragment();
        long maxDate = System.currentTimeMillis();
        Bundle b = new Bundle();
        b.putLong("maxDate", maxDate);
        newFragment.setArguments(b);
        newFragment.setOnSetDate(AddIstoricAntrenamentFragment.this);
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.btnDiscard)
    public void btnDiscardClicked() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.btnSave)
    public void btnSaveClicked() {
        Bundle args = getArguments();
        boolean validate = mAwesomeValidation.validate();
        if (etxtDataOra.getText().toString().trim().isEmpty()) {
            Toasty.error(getActivity(), "Selectati data si ora!").show();
            return;
        }
        if (validate) {
            int sId = getArguments().getInt("sId", 0);
            if (istoricAntrenamente == null) {
                istoricAntrenamente = new IstoricAntrenamente();
            }
            istoricAntrenamente.setRating((int) ratingBar.getRating());
            istoricAntrenamente.setDataAntrenament(dataOraAntrenament);
            istoricAntrenamente.setAntrenor((Antrenor) spnAntrenor.getSelectedItem());
            istoricAntrenamente.setTipAntrenament((TipAntrenament) spnAntrenament.getSelectedItem());
            istoricAntrenamente.setGradDificultate(spnGradDificultate.getSelectedItemPosition());
            AbonamenteSportivi abonamenteSportivi = AbonamenteSportivi.getAbonamentValabiByDate(sId, dataOraAntrenament);
            if (!args.containsKey("neev") && abonamenteSportivi == null)
                Toasty.info(getActivity(), "Sportivul nu are abonamente active!").show();
            else {
                if (sId == 0)
                    istoricAntrenamente.setAbonamanteSportiv(abonamenteSportivi);
                onAntrenamentAdaugat.onAntrenamentAdaugat(istoricAntrenamente);
                getActivity().getSupportFragmentManager().popBackStack();
            }

        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        ratingBar.setRating(ratingBar.getRating());
    }

    public void setOnAntrenamentAdaugat(OnAntrenamentAdaugat onAntrenamentAdaugat) {
        this.onAntrenamentAdaugat = onAntrenamentAdaugat;
    }

    private void loadSportivData() {
        Bundle arg = getArguments();
        if (arg != null && arg.containsKey("sId")) {
            int id = arg.getInt("sId");
            istoricAntrenamente = IstoricAntrenamente.getIstoricAntrenamentById(id);
            Log.v(TAG, istoricAntrenamente.toString());
            int indexAntrenor = adapterAntrenor.getPosition(istoricAntrenamente.getAntrenor());
            spnAntrenor.setSelection(indexAntrenor);
            dataOraAntrenament = istoricAntrenamente.getDataAntrenament();
            int indexAntrenament = adapterTipAntrenament.getPosition(istoricAntrenamente.getTipAntrenament());
            spnAntrenament.setSelection(indexAntrenament);
            spnGradDificultate.setSelection(istoricAntrenamente.getGradDificultate());
            etxtDataOra.setText(ProjectUtils.getFormattedDate(istoricAntrenamente.getDataAntrenament(), "dd.MM.yyyy HH:mm"));
            ratingBar.setRating(istoricAntrenamente.getRating());

        }
    }

}

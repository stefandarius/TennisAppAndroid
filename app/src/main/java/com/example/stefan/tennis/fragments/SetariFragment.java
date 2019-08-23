package com.example.stefan.tennis.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.utils.ProjectUtils;
import com.example.stefan.tennis.utils.Setari;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class SetariFragment extends BaseFragment implements TimePickerFragment.OnSetTime {

    public static final String TAG = "SetariFragment";
    @BindView(R.id.txtOra)
    EditText textView;
    @BindView(R.id.btnSave)
    Button button;
    private int hour;
    private int minute;
    private Date dataOraAntrenament;
    private View fab;
    private Setari setari;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent alarmIntent;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.setare_notificari;
    }

    @Override
    public void initUI(View view) {
        setari = Setari.getInstance(getContext());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, setari.getOra());
        c.set(Calendar.MINUTE, setari.getMinut());
        textView.setText(ProjectUtils.getFormattedDate(c.getTime(), "HH:mm"));
    }

    @OnClick(R.id.txtOra)
    public void txtOraClicked() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnSetTime(SetariFragment.this);
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, this.hour);
        c.set(Calendar.MINUTE, this.minute);

        this.dataOraAntrenament = c.getTime();

        textView.setText(ProjectUtils.getFormattedDate(c.getTime(), "HH:mm"));
    }

    @Override
    public void setupTollbar() {
        super.setupTollbar();
        toolbar.setTitle("Setari");
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btnSave)
    public void buttonClicked() {
        String[] ora = textView.getText().toString().split(":");
        setari.setOra(Integer.parseInt(ora[0]));
        setari.setMinut(Integer.parseInt(ora[1]));
        getActivity().getSupportFragmentManager().popBackStack();
        ProjectUtils.hideSoftKeyboard(getActivity());

        Calendar alarmStartTime = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, setari.getOra());
        alarmStartTime.set(Calendar.MINUTE, setari.getMinut());
        alarmStartTime.set(Calendar.SECOND, 0);
        ProjectUtils.rescheduele(getContext(), alarmStartTime);
    }
}

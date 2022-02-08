package com.example.kalkulatorrealizacjiprodukcji;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    EditText cycleTime, quantity, multiply, grams;
    RadioGroup cavityNumber;
    TextView currentCycleTime, hEfficiency, weightNeeded, goalTime, hour;
    Button button, resetButton;
    String selectedCavityNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        buttonEffect(button);
    }

    public void onClick(View view) {

        cycleTime = findViewById(R.id.cycleTime);
        cavityNumber = findViewById(R.id.cavityNumber);
        selectedCavityNumber = getSelectedRadio(cavityNumber);
        quantity = findViewById(R.id.quantity);
        multiply = findViewById(R.id.multiply);
        grams = findViewById(R.id.grams);

        Efficiency eff = createEfficiency(cycleTime, quantity, multiply, selectedCavityNumber, grams);

        currentCycleTime = findViewById(R.id.currentCycleTime);
        currentCycleTime.setText(cycleTime.getText());

        hEfficiency = findViewById(R.id.hEfficiency);
        hEfficiency.setText(calcEfficiency(eff));

        weightNeeded = findViewById(R.id.weightNeeded);
        weightNeeded.setText(calcWeightOfMaterial(eff));

        goalTime = findViewById(R.id.goalTime);
        goalTime.setText(calcGoalTime(eff));

        hour = findViewById(R.id.hour);
        hour.setText(calcHour(eff));

        resetButton = findViewById(R.id.resetButton);
        buttonEffect(resetButton);
        hideKeyboard(view);
    }

    public Efficiency createEfficiency (EditText cycleTime, EditText quantity, EditText multiply,
                                        String selectedCavityNumber, EditText grams) {

        String cycleT = cycleTime.getText().toString();
        if(cycleT.isEmpty()) cycleT = "-1";
        String quant = quantity.getText().toString();
        if(quant.isEmpty()) quant = "-1";
        String multi = multiply.getText().toString();
        if(multi.isEmpty()) multi = "-1";
        String selectedCavity = selectedCavityNumber;
        if(selectedCavity.isEmpty()) selectedCavity = "-1";
        String gr = grams.getText().toString();
        if(gr.isEmpty()) gr = "-1";

        Efficiency eff = new Efficiency(Double.parseDouble(cycleT), Integer.parseInt(quant),
                Double.parseDouble(multi), Integer.parseInt(selectedCavity), Double.parseDouble(gr));
        return eff;
    }

    public String calcEfficiency(Efficiency eff) {
        if (eff.getCycleTime() == -1 || eff.getSelectedCavityNumber() == -1) return "nieznana";
        int hourEfficiency = (int) Math.round(3600 / eff.getCycleTime() * eff.getSelectedCavityNumber());
        String effInfo = String.valueOf(hourEfficiency);
        return effInfo;
    }

    public String calcWeightOfMaterial(Efficiency eff) {
        if (eff.getGrams() == -1 || eff.getQuantity() == -1 || eff.getMultiply() == -1)
            return "nieznane";
        double weight = eff.getGrams() * eff.getQuantity() * eff.getMultiply() / 1000;
        DecimalFormat weightFormat = new DecimalFormat("0.00");
        String weightInfo = weightFormat.format(weight);
        return weightInfo;
    }

    public String calcGoalTime(Efficiency eff) {
        if(eff.getCycleTime() == -1 || eff.getQuantity() == -1 || eff.getMultiply() == -1 ||
                eff.getSelectedCavityNumber() == -1) return "niewystarczające dane";

        int goalInSec = calcGoalSec(eff.getCycleTime(), eff.getMultiply(), eff.getQuantity(),
                eff.getSelectedCavityNumber());
        int goalInMin = calcGoalMin(goalInSec);
        int goalInHours = calcGoalHour(goalInMin);

        if (goalInMin < 1) return String.valueOf(goalInSec).concat(" sekund");
        if (goalInHours < 1) return String.valueOf(goalInMin).concat(" minut i ")
                .concat(String.valueOf(goalInSec - goalInMin * 60)).concat(" sekund");
        return String.valueOf(goalInHours).concat(" godzin, ")
                .concat(String.valueOf(goalInMin - (goalInHours * 60))).concat(" minut i ")
                .concat(String.valueOf(goalInSec - (goalInMin * 60)).concat(" sekund"));
    }

    public int calcGoalSec(double cycle, double multiply, int quantity, int cavities) {
        return (int) Math.round(cycle * quantity * multiply / cavities);
    }

    public int calcGoalMin(int goalInSec) {
        return goalInSec / 60;
    }

    public int calcGoalHour(int goalInMin) {
        return goalInMin / 60;
    }

    public String calcHour(Efficiency eff) {
        if (eff.getCycleTime() == -1 || eff.getQuantity() == -1 || eff.getMultiply() == -1 ||
                eff.getSelectedCavityNumber() == -1) return "niewystarczające dane";

        int goalInSec = calcGoalSec(eff.getCycleTime(), eff.getMultiply(), eff.getQuantity(),
                eff.getSelectedCavityNumber());

        Calendar present = Calendar.getInstance();
        present.add(Calendar.SECOND, goalInSec);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss (dd-MM-yyyy)");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Poland"));

        return dateFormat.format(present.getTime());
    }

    public String getSelectedRadio(RadioGroup cavityNumber) {
        if (cavityNumber.getCheckedRadioButtonId() == -1) return "-1";
        RadioButton radioButton = findViewById(cavityNumber.getCheckedRadioButtonId());
        return radioButton.getText().toString();
    }

    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xFF707070, PorterDuff.Mode.SRC);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }

    public void reset(View view) {
        finish();
        startActivity(getIntent());
    }

    public void quit(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

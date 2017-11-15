package nguyen.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BurnedCaloriesCalculatorActivity extends AppCompatActivity
    implements OnEditorActionListener, OnKeyListener {

    private EditText mWeightET;
    private TextView mMilesRanTV;
    private SeekBar mMilesRanSeekBar;
    private TextView mCaloriesBurnedTV;
    private Spinner mFeetSpinner;
    private Spinner mInchesSpinner;
    private TextView mBMITV;
    private EditText mNameET;

    private String weightString;
    private String nameString;
    private float caloriesBurned = 0;
    private float bmi = 0;
    private int weight;
    private int milesRan;
    private int feet;
    private int inches;

    private int milesRanProgress;
    private int feetSplit = 1;
    private int inchesSplit = 1;

    DecimalFormat decimalFormat = new DecimalFormat("#0.0");

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        mWeightET = (EditText) findViewById(R.id.weightET);
        mMilesRanTV = (TextView) findViewById(R.id.milesRanTV);
        mMilesRanSeekBar = (SeekBar) findViewById(R.id.milesRanSeekBar);
        mCaloriesBurnedTV = (TextView) findViewById(R.id.caloriesBurnedTV);
        mFeetSpinner = (Spinner) findViewById(R.id.feetSpinner);
        mInchesSpinner = (Spinner) findViewById(R.id.inchesSpinner);
        mBMITV = (TextView) findViewById(R.id.bmiTV);
        mNameET = (EditText) findViewById(R.id.nameET);

        mWeightET.setOnKeyListener(this);

        mWeightET.setOnEditorActionListener(checkedChangeListener);

        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(this, R.array.feet_array, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFeetSpinner.setAdapter(feetAdapter);

        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource(this, R.array.inches_array, android.R.layout.simple_spinner_item);
        inchesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInchesSpinner.setAdapter(inchesAdapter);

        mFeetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                feetSplit = position + 1;
                calculateAndDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mInchesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inchesSplit = position + 1;
                calculateAndDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mMilesRanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                milesRanProgress = progress;
                milesRan = milesRanProgress;
                calculateAndDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);
    }



    private OnEditorActionListener checkedChangeListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            calculateAndDisplay();
            return false;
        }
    };

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        calculateAndDisplay();
        return false;
    }

    public void calculateAndDisplay() {
        weightString = mWeightET.getEditableText().toString();
        milesRan = mMilesRanSeekBar.getProgress();

        if(weightString.equals("")) {
            weight = 0;
        } else {
            weight = Integer.parseInt(weightString);
        }

        feet = 3;
        if(feetSplit == 1) {
            feet = 3;
        } else if(feetSplit == 2) {
            feet = 4;
        } else if(feetSplit == 3) {
            feet = 5;
        } else if(feetSplit == 4) {
            feet = 6;
        } else {
            feet = 7;
        }

        inches = 1;
        if(inchesSplit == 1) {
            inches = 1;
        } else if(inchesSplit == 2) {
            inches = 2;
        } else if(inchesSplit == 3) {
            inches = 3;
        } else if(inchesSplit == 4) {
            inches = 4;
        } else if(inchesSplit == 5) {
            inches = 5;
        } else if(inchesSplit == 6) {
            inches = 6;
        } else if(inchesSplit == 7) {
            inches = 7;
        } else if(inchesSplit == 8) {
            inches = 8;
        } else if(inchesSplit == 9) {
            inches = 9;
        } else if(inchesSplit == 10) {
            inches = 10;
        } else {
            inches = 11;
        }

        caloriesBurned = (float) (0.75 * weight * milesRan);
        bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));

        mMilesRanTV.setText(milesRanProgress + "mi");
        mCaloriesBurnedTV.setText(decimalFormat.format(caloriesBurned));
        mBMITV.setText(decimalFormat.format(bmi));
    }

    @Override
    protected void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("weightString", weightString);
        editor.putInt("milesRan", milesRan);
        editor.putInt("milesRanProgress", milesRanProgress);
        editor.putFloat("caloriesBurned", caloriesBurned);
        editor.putInt("feetSplit", feetSplit);
        editor.putInt("inchesSplit", inchesSplit);
        editor.putFloat("bmi", bmi);

        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        weightString = savedValues.getString("weightString", "");
        milesRan = savedValues.getInt("milesRan", 1);
        milesRanProgress = savedValues.getInt("milesRanProgress", 1);
        caloriesBurned = savedValues.getFloat("caloriesBurned", 0);
        feetSplit = savedValues.getInt("feetSplit", 1);
        inchesSplit = savedValues.getInt("inchesSplit", 1);
        bmi = savedValues.getFloat("bmi", 0);

        mWeightET.setText(weightString);

        mMilesRanTV.setText(milesRanProgress + "mi");
        mMilesRanSeekBar.setProgress(milesRanProgress);

        int feetPosition = feetSplit - 1;
        mFeetSpinner.setSelection(feetPosition);

        int inchesPosition = inchesSplit - 1;
        mInchesSpinner.setSelection(inchesPosition);

    }
}

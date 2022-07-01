package com.matrix_maeny.decisionmaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class RandomNumberActivity extends AppCompatActivity {

    EditText rangeStartText, rangeEndText;
    TextView randomNumberText;
    AppCompatButton startBtn;
    final Random random = new Random();
    final Handler handler = new Handler();

    int x = 0;
    int rangeStart = 0, rangeEnd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Number generator");

        rangeStartText = findViewById(R.id.rangeStart);
        rangeEndText = findViewById(R.id.rangeEnd);
        randomNumberText = findViewById(R.id.randomNumberText);
        startBtn = findViewById(R.id.startBtn1);

    }

    public void startBtn1(View view) {
        startAnimating();
    }

    private void startAnimating() {

        if (!getTheValues()) {
            return;
        }
        x = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (x < 15) {
                    x++;
                    int y = returnRandomRangeNumber(rangeStart, rangeEnd);
                    randomNumberText.setText(String.valueOf(y));
                    handler.postDelayed(this, 50);
                } else {
                    showRandomNumber();
                }
            }
        }, 50);
    }


    private boolean getTheValues() {
        int rangeStart = -1;
        int rangeEnd = -1;
        String rs = "", re = "";

        try {
            rs = rangeStartText.getText().toString();
        } catch (Exception ignored) {
        }

        try {
            re = rangeEndText.getText().toString();
        } catch (Exception ignored) {
        }

        rs = rs.trim();
        re = re.trim();

        if (rs.equals("") || re.equals("")) {
            rs = re = "0";
        }

        try {
            rangeStart = Integer.parseInt(rs);
            rangeEnd = Integer.parseInt(re);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing range", Toast.LENGTH_SHORT).show();
        }

        if (rangeStart >= rangeEnd || rangeStart < 0) {
            Toast.makeText(this, "Please enter a valid range", Toast.LENGTH_SHORT).show();
            return false;
        }

        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        return true;

    }


    private int returnRandomRangeNumber(int rangeStart, int rangeEnd) {

        return random.nextInt((rangeEnd - rangeStart)) + rangeStart;
    }


    private void showRandomNumber() {

        int randomNumber = returnRandomRangeNumber(rangeStart, rangeEnd);

        randomNumberText.setText(String.valueOf(randomNumber));


    }
}
package com.matrix_maeny.decisionmaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class RandomDecisionActivity extends AppCompatActivity implements DecisionDialog.DecisionListener {

    TextView randomDecisionView;
    AppCompatButton startBtn, addDecisionBtn;

    ArrayList<String> list = null;
    int listLength = 0;
    int count = 0;
    final Handler handler = new Handler();
    final Random random = new Random();

    DecisionDataBase dataBase = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_decision);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Decision generator");

        initialize();

    }

    private void initialize() {
        randomDecisionView = findViewById(R.id.randomDecisionView);
        startBtn = findViewById(R.id.startBtn2);
        addDecisionBtn = findViewById(R.id.addDecisionBtn);

        list = new ArrayList<>();
        loadList();
    }

    public void AddDecisionBtn(View view) {
        DecisionDialog decisionDialog = new DecisionDialog();
        decisionDialog.show(getSupportFragmentManager(), "Decision dialog");

    }

    private void loadList() {

        dataBase = new DecisionDataBase(RandomDecisionActivity.this);
        Cursor cursor = dataBase.getData();

        if (cursor.getCount() > 1) {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
        }

        dataBase.close();

    }


    public void setStartBtn(View view) {

        if (list != null) {
            listLength = list.size();
            startAnimating();

        } else {
            Toast.makeText(this, "Please add decisions and press OK", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAnimating() {
        if (listLength <= 1) {
            Toast.makeText(this, "Please add Decisions and Press OK", Toast.LENGTH_SHORT).show();
            randomDecisionView.setText(getString(R.string.decision_appears_here));

            return;
        }

        count = 0;


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count < 15) {
                    count++;
                    int x = random.nextInt(listLength);

                    randomDecisionView.setText(list.get(x));
                    handler.postDelayed(this, 50);
                } else {
                    showDecision();
                }

            }


        }, 50);


    }

    private void showDecision() {

        int randomDecision = random.nextInt(listLength);

        randomDecisionView.setText(list.get(randomDecision));
    }

    @Override
    public void getList(ArrayList<String> list) {
        this.list = list;

    }
}
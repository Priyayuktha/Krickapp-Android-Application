package com.example.krickapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class scoredesk extends AppCompatActivity {

    private TextView tvScore, tvOvers, tvBatsman1, tvBatsman1Stat, tvBatsman2, tvBatsman2Stat, tvBowler, tvBowlerStat;

    // Match State
    private int totalRuns = 0, totalWickets = 0, totalBalls = 0, maxOvers = 10;
    private int batsman1Runs = 0, batsman1Balls = 0;
    private int batsman2Runs = 0, batsman2Balls = 0;
    private int bowlerBalls = 0, bowlerRuns = 0, bowlerWickets = 0;

    // To manage undo
    private Stack<Action> actionStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoredesk);

        // Bind views
        tvScore = findViewById(R.id.tvScore);
        tvOvers = findViewById(R.id.tvOvers);
        tvBatsman1 = findViewById(R.id.tvBatsman1);
        tvBatsman1Stat = findViewById(R.id.tvBatsman1Stat);
        tvBatsman2 = findViewById(R.id.tvBatsman2);
        tvBatsman2Stat = findViewById(R.id.tvBatsman2Stat);
        tvBowler = findViewById(R.id.tvBowler);
        tvBowlerStat = findViewById(R.id.tvBowlerStat);

        // Keypad buttons
        setRunButton(R.id.btn0, 0);
        setRunButton(R.id.btn1, 1);
        setRunButton(R.id.btn2, 2);
        setRunButton(R.id.btn3, 3);
        setRunButton(R.id.btn4, 4);
        setRunButton(R.id.btn6, 6);

        findViewById(R.id.btn57).setOnClickListener(v -> addRuns(5, true)); // treat 5,7 same as 5
        findViewById(R.id.btnWD).setOnClickListener(v -> addExtra("WD"));
        findViewById(R.id.btnNB).setOnClickListener(v -> addExtra("NB"));
        findViewById(R.id.btnLB).setOnClickListener(v -> addExtra("LB"));
        findViewById(R.id.btnOUT).setOnClickListener(v -> addWicket());
        findViewById(R.id.btnUndo).setOnClickListener(v -> undoLastAction());

        findViewById(R.id.btnEndMatch).setOnClickListener(v ->
                Toast.makeText(this, "Match Ended!", Toast.LENGTH_SHORT).show()
        );

        updateUI();
    }

    // Handle normal runs
    private void setRunButton(int id, int runs) {
        Button btn = findViewById(id);
        btn.setOnClickListener(v -> addRuns(runs, true));
    }

    private void addRuns(int runs, boolean legal) {
        totalRuns += runs;
        batsman1Runs += runs;
        batsman1Balls++;
        bowlerRuns += runs;
        if (legal) {
            totalBalls++;
            bowlerBalls++;
        }
        actionStack.push(new Action(runs, false, legal));
        updateUI();
    }

    // Handle extras (wide, no-ball, leg-bye)
    private void addExtra(String type) {
        totalRuns++;
        bowlerRuns++;
        actionStack.push(new Action(1, false, false));
        Toast.makeText(this, type + " given", Toast.LENGTH_SHORT).show();
        updateUI();
    }

    // Handle wicket
    private void addWicket() {
        totalWickets++;
        bowlerWickets++;
        batsman1Balls++;
        totalBalls++;
        bowlerBalls++;
        actionStack.push(new Action(0, true, true));
        Toast.makeText(this, "Wicket!", Toast.LENGTH_SHORT).show();
        updateUI();
    }

    // Undo last action
    private void undoLastAction() {
        if (!actionStack.isEmpty()) {
            Action last = actionStack.pop();
            totalRuns -= last.runs;
            if (last.wicket) {
                totalWickets--;
                bowlerWickets--;
                batsman1Balls--;
            }
            if (last.legalBall) {
                totalBalls--;
                bowlerBalls--;
            }
            bowlerRuns -= last.runs;
            batsman1Runs -= last.runs;
            if (last.legalBall) batsman1Balls--;
            updateUI();
        } else {
            Toast.makeText(this, "Nothing to undo", Toast.LENGTH_SHORT).show();
        }
    }

    // Refresh display
    private void updateUI() {
        tvScore.setText(totalRuns + "/" + totalWickets);
        tvOvers.setText("(" + (totalBalls / 6) + "." + (totalBalls % 6) + "/" + maxOvers + ")");
        tvBatsman1Stat.setText(batsman1Runs + "(" + batsman1Balls + ")");
        tvBatsman2Stat.setText(batsman2Runs + "(" + batsman2Balls + ")");
        tvBowlerStat.setText(bowlerBalls + " " + bowlerWickets + " " + bowlerRuns);
    }

    // Action model for undo
    private static class Action {
        int runs;
        boolean wicket;
        boolean legalBall;

        Action(int runs, boolean wicket, boolean legalBall) {
            this.runs = runs;
            this.wicket = wicket;
            this.legalBall = legalBall;
        }
    }
}

package com.example.ereztictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

public class GameScreen extends AppCompatActivity {

    RadioGroup playerSelector;
    RadioButton x_button, o_button;
    TextView curTurn;
    ImageButton restart;
    GridLayout board;
    User user;
    ImageButton[][] cells;
    int[][] state;
    int turn;
    int playerSign;
    DBhelper dbh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_screen);
        startUI();
    }

    private void startUI() {
        user = getIntent().getParcelableExtra("user");
        String msg = user==null?"no user selected":user.toString();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        turn = -1;
        playerSelector = findViewById(R.id.playerSelector);
        playerSelector.setOnCheckedChangeListener((playerSelector , id)-> changePlayer(id));
        o_button = findViewById(R.id.o_button);
        x_button = findViewById(R.id.x_button);
        curTurn = findViewById(R.id.turn);
        restart = findViewById(R.id.restart);
        restart.setOnClickListener(restart -> createGame());
        board = findViewById(R.id.tttBoard);
        cells = new ImageButton[3][3];
        state = new int[3][3];
        playerSign = -1;
        dbh = new DBhelper(GameScreen.this);
        createGame();
    }

    private void changePlayer(int id) {
        if (id == R.id.x_button) playerSign = -1;
        else playerSign = 1;
        createGame();
    }

    private void playTurn(int i, int j) {
        if (!cells[i][j].isEnabled()) return;
        state[i][j] = turn;
        cells[i][j].setImageDrawable(AppCompatResources.getDrawable(this, turn>0?R.drawable.ic_o:R.drawable.ic_x));
        cells[i][j].setEnabled(false);

        int res = checkGrid(state);
        if (res != 0 || isFull(state)) {
            for (int k = 0; k < state.length; k++) {
                for (int h = 0; h < state.length; h++) {
                    cells[k][h].setEnabled(false);
                }
            }
            dbh.updateStats(user.getId(), res);
        } else {
            turn = -turn;
            curTurn.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(this, turn>0?R.drawable.ic_o:R.drawable.ic_x), null,null,null);
            if (turn != playerSign) cpuTurn();
        }
    }

    private void cpuTurn() {
        Log.d("hola", "---------------------------------------------------");
        int[] nextTurn = new int[2];
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                if (state[i][j]==0) {
                    state[i][j] = -playerSign;
                    int score = minimax(state, playerSign, 1);
                    state[i][j] = 0;
                    if ((-playerSign)*score>bestScore) {
                        Log.d("hola", "minimax: " + i + "," + j + " : " + bestScore + " -> " + (-playerSign)*score);
                        bestScore = (-playerSign)*score;
                        nextTurn = new int[]{i, j};
                    }
                }
            }
        }

        int[] finalNextTurn = nextTurn;
        new Handler().postDelayed(() -> playTurn(finalNextTurn[0], finalNextTurn[1]), 500);
    }

    private int minimax(int[][] cur, int player, int depth) {
        if (checkGrid(cur) != 0) return checkGrid(cur);
        if (isFull(cur)) return 0;

        int bestScore = player==1?Integer.MIN_VALUE:Integer.MAX_VALUE;
        for (int i = 0; i < cur.length; i++) {
            for (int j = 0; j < cur.length; j++) {
                if (cur[i][j]==0) {
                    cur[i][j] = player;
                    bestScore = player==1?Math.max(minimax(cur, -player, depth+1), bestScore):Math.min(minimax(cur, -player,depth+1), bestScore);
                    cur[i][j] = 0;
                }
            }
        }
        return bestScore;
    }

    private boolean isFull(int[][] cur) {
        for (int[] ints : cur) {
            for (int j = 0; j < cur.length; j++) {
                if (ints[j] == 0) return false;
            }
        }
        return true;
    }

    private int checkGrid(int[][] cur) {

        for (int i = 0; i < cur.length; i++) {
            if (cur[i][1]==cur[i][2]&&cur[i][1]==cur[i][0]) return cur[i][1];
            if (cur[1][i]==cur[2][i]&&cur[1][i]==cur[0][i]) return cur[1][i];
        }

        if ((cur[1][1]==cur[2][2]&&cur[1][1]==cur[0][0]) || (cur[1][1]==cur[2][0]&&cur[1][1]==cur[0][2])) return cur[1][1];
        return 0;
    }

    private void createGame() {
        wins.setText(dbh.getWins(user.getId()));
        losses.setText(dbh.getLosses(user.getId()));
        draws.setText(dbh.getDraws(user.getId()));
        board.removeAllViews();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                ImageButton cell = new ImageButton(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)
                );
                params.setMargins(30, 30, 30, 30);
                cell.setLayoutParams(params);
                cell.setBackground(AppCompatResources.getDrawable(this, R.drawable.box_bg));
                cell.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_e));
                int finalI = i;
                int finalJ = j;
                cell.setOnClickListener(v -> playTurn(finalI, finalJ));
                cell.setScaleType(ImageView.ScaleType.FIT_CENTER);
                cells[i][j] = cell;
                state[i][j] = 0;
                board.addView(cells[i][j]);
            }
        }
        turn = -1;
        curTurn.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_x), null,null,null);
        if (turn != playerSign) cpuTurn();
    }
}

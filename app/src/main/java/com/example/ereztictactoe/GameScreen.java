package com.example.ereztictactoe;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

public class GameScreen extends AppCompatActivity {

    RadioGroup playerSelector;
    TextView curTurn;
    ImageButton restart;
    GridLayout board;
    User user;
    ImageButton[] cells;
    boolean turn;

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
        turn = false;
        playerSelector = findViewById(R.id.playerSelector);
        curTurn = findViewById(R.id.turn);
        restart = findViewById(R.id.restart);
        restart.setOnClickListener(restart -> createGame());
        board = findViewById(R.id.tttBoard);
        cells = new ImageButton[9];
        for (int i = 0; i < cells.length; i++) {
            ImageButton cell = new ImageButton(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
            );
            params.setMargins(30,30,30,30);
            cell.setLayoutParams(params);
            cell.setBackground(AppCompatResources.getDrawable(this, R.drawable.box_bg));
            cell.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_e));
            int finalI = i;
            cell.setOnClickListener(v -> playTurn(finalI));
            cell.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cells[i] = cell;
            board.addView(cells[i]);
        }
    }

    private void playTurn(int i) {
        if (!cells[i].isEnabled()) return;
        cells[i].setImageDrawable(AppCompatResources.getDrawable(this, turn?R.drawable.ic_o:R.drawable.ic_x));
        cells[i].setEnabled(false);

        //checkGrid();
        //board.setEnabled(false);
        //win/lose dialog
        //update database


        turn = !turn;
        curTurn.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(this, turn?R.drawable.ic_o:R.drawable.ic_x), null,null,null);
    }

    private void checkGrid() {
    }

    private void createGame() {
        board.removeAllViews();
        for (int i = 0; i < cells.length; i++) {
            ImageButton cell = new ImageButton(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
            );
            params.setMargins(30,30,30,30);
            cell.setLayoutParams(params);
            cell.setBackground(AppCompatResources.getDrawable(this, R.drawable.box_bg));
            cell.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_e));
            int finalI = i;
            cell.setOnClickListener(v -> playTurn(finalI));
            cell.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cells[i] = cell;
            board.addView(cells[i]);
        }
    }
}

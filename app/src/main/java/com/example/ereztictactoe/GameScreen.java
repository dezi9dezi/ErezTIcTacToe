package com.example.ereztictactoe;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_screen);
        startUI();
    }

    private void startUI() {
        User user = getIntent().getParcelableExtra("user");
        String msg = user==null?"no user selected":user.toString();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

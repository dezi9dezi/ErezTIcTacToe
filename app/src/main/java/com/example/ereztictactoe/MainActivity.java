package com.example.ereztictactoe;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText un, pw;
    Button login;
    TextView forgotPw;
    DBhelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        startUI();
    }

    public void startUI() {

        un = findViewById(R.id.username);
        pw = findViewById(R.id.password);
        login = findViewById(R.id.loginBTN);
        forgotPw = findViewById(R.id.forgotPassword);
        dbh = new DBhelper(MainActivity.this);

        login.setOnClickListener(login -> login());
        forgotPw.setOnClickListener(forgotPw -> forgotPassword());
        forgotPw.setOnClickListener(forgotPw -> forgotPassword());
    }

    private void forgotPassword() {
        Dialog fpDialog = new changePassword(MainActivity.this, dbh);
        fpDialog.show();
    }

    private void login() {
        String username = un.getText().toString();
        String password = pw.getText().toString();
        if (!username.isEmpty()&&!password.isEmpty()) {
            User u = dbh.addNewUser(new User(username, password));
            Intent i = new Intent(MainActivity.this, GameScreen.class);
            i.putExtra("user", u);
            startActivity(i);
        }
    }
}
package com.example.ereztictactoe;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class customDialog extends Dialog {

    Context ma;
    Button update;
    EditText userId, userUsername, userNewPassword;
    DBhelper dbh;
    public customDialog(@NonNull Context context, DBhelper dbh) {
        super(context);
        ma = context;
        this.dbh = dbh;
        setContentView(R.layout.dialog_layout);
        setCancelable(true);
        update = findViewById(R.id.update_update_button);
        userId = findViewById(R.id.update_user_ID);
        userUsername = findViewById(R.id.update_user_username);
        userNewPassword = findViewById(R.id.update_user_password);
        update.setOnClickListener(update -> updatePassword());
    }

    private void updatePassword() {
        long id = Integer.parseInt(userId.getText().toString());
        String username = userUsername.getText().toString();
        String newPassword = userNewPassword.getText().toString();
        User u = dbh.getUser(id);
        if ((u.getId() == id && u.getUsername().equals(username) && !newPassword.isEmpty()) && dbh.updateUser(u, newPassword)) {
            Toast.makeText(ma, "Password updated successfully", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else Toast.makeText(ma, "error", Toast.LENGTH_SHORT).show();
    }
}

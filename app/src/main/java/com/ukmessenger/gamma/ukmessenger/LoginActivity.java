package com.ukmessenger.gamma.ukmessenger;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ukmessenger.gamma.ukmessenger.Main.MainActivity;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference rootReference;

    private CircularProgressButton btnLogin, btnRegister;
    private TextInputEditText txtUser, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();

        initialize();
        login();
        register();
    }

    void initialize(){
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtUser = findViewById(R.id.txt_user);
        txtPassword = findViewById(R.id.txt_password);
    }

    void login(){
        btnLogin.setOnClickListener(v -> {
            closeKeyboard();
            txtUser.setEnabled(false);
            txtPassword.setEnabled(false);

            final String user = txtUser.getText().toString();
            final String pass = txtPassword.getText().toString();

            if (user.trim().length() == 0) txtUser.setError(getResources().getString(R.string.e_user_required));
            if (pass.trim().length() == 0) txtPassword.setError(getResources().getString(R.string.e_pass_required));

            btnLogin.startAnimation();

            if (user.trim().length() > 0 && pass.trim().length() > 0) {
                fAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = task.getException().getMessage();
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("Ok", ((dialog, which) -> dialog.dismiss()))
                                .show();
                    }

                    btnLogin.revertAnimation();
                    txtUser.setEnabled(true);
                    txtPassword.setEnabled(true);
                });
            }
            else {
                btnLogin.revertAnimation();
                txtUser.setEnabled(true);
                txtPassword.setEnabled(true);
            }
        });
    }

    void register(){
        btnRegister.setOnClickListener(v -> {
            closeKeyboard();
            txtUser.setEnabled(false);
            txtPassword.setEnabled(false);

            final String user = txtUser.getText().toString();
            final String pass = txtPassword.getText().toString();

            if (user.trim().length() == 0) txtUser.setError(getResources().getString(R.string.e_user_required));
            if (pass.trim().length() == 0) txtPassword.setError(getResources().getString(R.string.e_pass_required));

            btnRegister.startAnimation();

            if (user.trim().length() > 0 && pass.trim().length() > 0) {
                fAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String currentUserID = fAuth.getCurrentUser().getUid();
                        rootReference.child("Users").child(currentUserID).setValue("");

                        Snackbar.make(v,
                                getResources().getString(R.string.m_register_success),
                                Snackbar.LENGTH_LONG).show();
                    } else {
                        String error = task.getException().getMessage();
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage(error)
                                .setPositiveButton("Ok", ((dialog, which) -> dialog.dismiss()))
                                .show();
                    }

                    btnRegister.revertAnimation();
                    txtUser.setEnabled(true);
                    txtPassword.setEnabled(true);
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        btnLogin.dispose();
        super.onDestroy();
    }

    void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
    }
}

package id.indibi.dev.mydiaryapps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
FirebaseAuth mAuth;
private EditText et_email, et_password;
private ProgressBar progressBar;
private CheckBox ingat;
private SharedPreferences Ipref;
private static final String PREFS_NAME="PrefData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_SignUp).setOnClickListener(this);
        findViewById(R.id.b_sign).setOnClickListener(this);
        et_email = (EditText)findViewById(R.id.email);
        et_password = (EditText)findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.P_login);
        ingat = (CheckBox)findViewById(R.id.CRemember);

        Ipref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        getPrefData();



    }
    //method sharedpreferences
    private void getPrefData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email")){
            String e = sp.getString("pref_email", "not found");
            et_email.setText(e.toString());
        }
        if (sp.contains("pref_password")){
            String p = sp.getString("pref_password","not found");
            et_password.setText(p.toString());
        }
        if (sp.contains("pref_cek")){
            Boolean b = sp.getBoolean("pref_cek", false);
                ingat.setChecked(b);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_SignUp:
                startActivity(new Intent(this, ActivitySignUp.class));
                break;

            case R.id.b_sign:
                login();
                break;
        }

    }

    private void login() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        //logic validasi form
        if (email.isEmpty()) {
            et_email.setError("Uppss.. ");
            et_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Masukan email yang valid");
            et_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            et_password.setError("Upps...");
            et_password.requestFocus();
            return;
        }
        if (password.length() < 8) {
            et_password.setError("Password minimal 8 character");
            et_password.requestFocus();
            return;
        }
        //logic ceklis checkbox remember
        if (ingat.isChecked()){
            Boolean Bingat = ingat.isChecked();
            SharedPreferences.Editor editor = Ipref.edit();
            editor.putString("pref_email", et_email.getText().toString());
            editor.putString("pref_password", et_password.getText().toString());
            editor.putBoolean("pref_cek", Bingat);
            editor.apply();

        } else {
            Ipref.edit().clear().apply();
        }
        //logic clear
        et_email.getText().clear();
        et_password.getText().clear();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(home);

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

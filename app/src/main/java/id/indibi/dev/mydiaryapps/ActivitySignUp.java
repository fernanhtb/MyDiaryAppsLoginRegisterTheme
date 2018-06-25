package id.indibi.dev.mydiaryapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText Reg_email, Reg_password;
    private FirebaseAuth mAuth;
    private Button btn_daftar;
    private TextView tv_Blogin;
    private CheckBox cek;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Reg_email = findViewById(R.id.et_RegEmail);
        Reg_password = findViewById(R.id.et_RegPassword);
        mAuth = FirebaseAuth.getInstance();
        btn_daftar = (Button) findViewById(R.id.b_signUp);
        btn_daftar.setOnClickListener(this);
        //disable button default using checkbox for activated button
        btn_daftar.setEnabled(false);
        tv_Blogin = (TextView) findViewById(R.id.tv_Blogin);
        tv_Blogin.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.P_reg);

        //logic for active or disable button
        final CheckBox cek = (CheckBox) findViewById(R.id.check_agree);
        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cek.isChecked()) {
                    btn_daftar.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Terima kasih sudah menyetujui layanan", Toast.LENGTH_LONG).show();
                } else {
                    btn_daftar.setEnabled(false);
                }
            }
        });

    }

    //logic validasi signup
    private void Registrasi() {

        String email = Reg_email.getText().toString().trim();
        String password = Reg_password.getText().toString().trim();

        if (email.isEmpty()) {
            Reg_email.setError("Uppss.. ");
            Reg_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Reg_email.setError("Masukan email yang valid");
            Reg_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Reg_password.setError("Upps...");
            Reg_password.requestFocus();
            return;
        }
        if (password.length() < 8) {
            Reg_password.setError("Password minimal 8 character");
            Reg_password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent ihome = new Intent(getApplicationContext(), HomeActivity.class);
                    ihome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ihome);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Horeyy.. kamu telah terdaftar", Toast.LENGTH_LONG).show();
                } else {
                    //method to check if user alredy register
                    progressBar.setVisibility(View.GONE);
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Uppss.. Kamu sudah terdaftar sebelumnya", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Uppss.. Terjadi kesalahan", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });
    }

    //method onclick button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_signUp:
                Registrasi();
                break;

            case R.id.tv_Blogin:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }

    }


}

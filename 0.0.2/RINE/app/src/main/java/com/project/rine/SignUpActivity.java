package com.project.rine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    EditText signUpName;
    EditText signUpPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);
        signUpName = findViewById(R.id.sign_up_name);
        signUpPassword = findViewById(R.id.sign_up_password);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_up_button) {
            doSignUp();
        }
    }

    public void doSignUp() {
        genkey();
        SharedPreferences pref = getSharedPreferences("keys", MODE_PRIVATE);
        String nString = pref.getString("n", "");
        String eString = pref.getString("e", "");
        if (nString.equals("")) {
            Log.e("RINE_error", "no n keys!");
        }
        post("signUp", StringByte.parsePostSignUp(signUpName.getText().toString(),
                signUpPassword.getText().toString(), nString, eString));
    }

    @Override
    public void postCallback(String res) {
        super.postCallback(res);
        String idString = parseJSONString(res, JSON_MODE_DIRECT, "id");
        gV.xLog("RINE_debug", "Result:"+res+
                "IdString:"+idString);
        int id = Integer.parseInt(idString);
        if (id > 0) {
            gV.xLog("RINE_debug", "SignUp success.");
            gV.userId = id;
            gV.xLog("RINE_debug", "Id="+gV.userId);
            Intent intent = new Intent(SignUpActivity.this, MainMenuActivity.class);
            startActivity(intent);
        }
    }
}

package com.project.rine;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends BaseActivity implements View.OnClickListener {

    EditText login_name;
    EditText login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button button = findViewById(R.id.login_button);
        button.setOnClickListener(this);
        login_name = findViewById(R.id.login_name);
        login_password = findViewById(R.id.login_password);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            LogIn();
        }
    }

    public void LogIn() {
        post("login", StringByte.parsePostLogIn(login_name.getText().toString(),
                login_password.getText().toString()));
    }

    @Override
    public void postCallback(String res) {
        super.postCallback(res);
        String idString = parseJSONString(res, JSON_MODE_DIRECT, "id");
        if (idString.equals("")) {
            Log.e("RINE_error", "Log in failed.");
        }
        int id = Integer.parseInt(idString);
        gV.xLog("RINE_debug", "Changing id:"+id);
        gV.userId = id;
        Intent intent = new Intent(LogInActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }
}

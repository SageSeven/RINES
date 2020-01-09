package com.project.rine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TalkActivity extends BaseActivity implements View.OnClickListener {

    EditText talkText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        gV.xLog("RINE_debug", "talking with:"+gV.dstId);
        Button sendButton = findViewById(R.id.talk_send);
        sendButton.setOnClickListener(this);
        talkText = findViewById(R.id.talk_text);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.talk_send) {
            sendMessage(talkText.getText().toString());
            talkText.setText("");
        }
    }

    public void sendMessage(String msg) {
        post("sendMsg", StringByte.parsePostSendMsg(gV.userId, gV.dstId, msg));
    }
}

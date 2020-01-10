package com.project.rine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class TalkActivity extends BaseActivity implements View.OnClickListener {

    EditText talkText;
    TextView talk_text_view;

    private int postStatus;
    private static final int ON_SEND_MSG = 0;
    private static final int ON_GET_MSG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        gV.xLog("RINE_debug", "talking with:"+gV.dstId);
        Button sendButton = findViewById(R.id.talk_send);
        sendButton.setOnClickListener(this);
        Button getMsgButton = findViewById(R.id.talk_get_msg);
        getMsgButton.setOnClickListener(this);
        talkText = findViewById(R.id.talk_text);
        talk_text_view = findViewById(R.id.talk_text_view);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.talk_send) {
            postStatus = ON_SEND_MSG;
            sendMessage(talkText.getText().toString());
            talkText.setText("");
        }
        else if (v.getId() == R.id.talk_get_msg) {
            postStatus = ON_GET_MSG;
            getMessage();
        }
    }

    public void sendMessage(String msg) {
        String encryptedMsg = encrypt(msg, gV.dstKeyN, gV.dstKeyE);
        post("sendMsg", StringByte.parsePostSendMsg(gV.userId, gV.dstId, encryptedMsg));
    }

    public void getMessage() {
        post("getMsg", StringByte.parsePostGetMsg(gV.userId));
    }

    @Override
    public void postCallback(String res) {
        super.postCallback(res);
        if (postStatus == ON_GET_MSG) {
            final ArrayList<String> answer =
                    parseMultiJSONString(res, JSON_MODE_ALL, "text");
            gV.xLog("RINE_debug", "Answer is "+answer.get(0));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int size = answer.size();
                    StringBuilder builder = new StringBuilder();
                    String s;
                    for (int i = 0; i < size; i++) {
                        s = decrypt(answer.get(i));
                        builder.append(s);
                    }
                    talk_text_view.setText(builder.toString());
                }
            });
        }
    }
}

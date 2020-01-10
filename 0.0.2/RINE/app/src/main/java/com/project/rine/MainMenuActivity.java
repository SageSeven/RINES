package com.project.rine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.math.BigInteger;

public class MainMenuActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public String[] friends = {"testUser01","123","7","8","9"};
    private MsgDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainMenuActivity.this,
                android.R.layout.simple_list_item_1, friends);
        ListView mainMenuList = findViewById(R.id.main_menu_list);
        mainMenuList.setAdapter(adapter);
        mainMenuList.setOnItemClickListener(this);
        dbHelper = new MsgDatabaseHelper(this, "msg.db", null, 1);
        dbHelper.getWritableDatabase();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String friend = friends[position];
        setTalk(friend);
        Intent intent = new Intent(MainMenuActivity.this, TalkActivity.class);
        startActivity(intent);
    }

    public void setTalk(String friend) {
        post("getFriend", StringByte.parsePostGetFriend(friend));
    }

    @Override
    public void postCallback(String res) {
        super.postCallback(res);
        String dstIdString = parseJSONString(res, JSON_MODE_DIRECT, "id");
        int dstId = Integer.parseInt(dstIdString);
        gV.xLog("RINE_debug", "Changing dstId:"+dstId);
        gV.dstId = dstId;
        String keyNString = parseJSONString(res, JSON_MODE_DIRECT, "key_n");
        String keyEString = parseJSONString(res, JSON_MODE_DIRECT, "key_e");
        gV.dstKeyN = new BigInteger(keyNString);
        gV.dstKeyE = new BigInteger(keyEString);
    }
}

package com.project.rine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonConnect = (Button)findViewById(R.id.button_connect);
        responseText = (TextView) findViewById(R.id.response_text);
        buttonConnect.setOnClickListener(this);
        Button buttonTest = findViewById(R.id.testButton);
        buttonTest.setOnClickListener(this);
        Button jumpSignIn = findViewById(R.id.jumpSignUp);
        jumpSignIn.setOnClickListener(this);
        Button jumpLogIn = findViewById(R.id.jumpLogIn);
        jumpLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_connect) {
            get("msg");
            //post("login","{\"username\":\"123\",\"pwd\":\"456\"}");
            post("sendMsg", StringByte.parsePostSendMsg(2,1,"1"));
            post("getMsg", StringByte.parsePostGetMsg(1));
        }
        else if (v.getId() == R.id.testButton) {
            /*String testString = "12345678910111213141516171819202122232425262728293031323334353"
                    +"63738394041424344454647484950515253545556575859606162636465666768697071727"
                    +"37475767778798081828384858687888990919293949596979899100101102103104105";*/
            //String testString = "12312383219053818074017031758057310875083174087318047803175807"
                    //+"98127382037812075803718047108392018905138787508173490790417490731709731509"
                    //+"21984783175857198274875913789379817189578314703175813750718074307801357";
            //String testString = "乘法使用快速幂，如果底数位数是x，指数位数是e，底数足够大的话，复杂度取决于" +
                    //"模数，模数是m位的话，复杂度是O(m*m*e)。程序里，大数的存储是2的32次方进制的，" +
                    //"这里的m*m要除以（32*32）。";
            genkey();
            //String s1 = encrypt(testString);
            //String s2 = decrypt(s1);
            //gV.xLog("RINE_debug", "decrypted String ="+s2);
            BigInteger n1 = new BigInteger("3233");
            BigInteger e1 = new BigInteger("17");
            BigInteger m = new BigInteger("65");
            gV.xLog("RINE_debug", "65**17 mod 3233 ="+
                    Encrypter.encryptBigInteger(m,n1,e1).toString());
            gV.xLog("RINE_debug", "(byte)234 ="+
                    (byte)234);
        }
        else if (v.getId() == R.id.jumpSignUp) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.jumpLogIn) {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void getCallback(final String response) {
        super.getCallback(response);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}

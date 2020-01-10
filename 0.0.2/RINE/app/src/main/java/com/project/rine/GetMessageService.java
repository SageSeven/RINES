package com.project.rine;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GetMessageService extends Service {

    private GetMessageBinder mBinder = new GetMessageBinder();

    class GetMessageBinder extends Binder {
        public String getMessage() {
            gV.xLog("RINE_debug", "Getting message.");
            return "";
        }
    }


    public GetMessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

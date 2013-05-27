package com.zhuang.sheen.requestinterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RequestReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, intent.getStringExtra("result"), Toast.LENGTH_SHORT).show();
	}

}

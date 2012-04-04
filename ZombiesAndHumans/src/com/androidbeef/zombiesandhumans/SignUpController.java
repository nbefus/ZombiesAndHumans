package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SignUpController extends Activity implements OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onClick(View v) 
	{
		if(v.getId() == R.id.Button_Cancel)
		{
			this.finish();
		}
		else if(v.getId() == R.id.Button_SignUpNow)
		{
			
		}
	}
	
}

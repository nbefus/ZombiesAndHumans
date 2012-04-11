package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SignUpController extends Activity implements OnClickListener
{
	private final String debugClass = "SIGN_UP_CONTROLLER";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
	}

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

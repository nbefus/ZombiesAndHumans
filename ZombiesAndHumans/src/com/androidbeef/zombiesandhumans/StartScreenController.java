package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class StartScreenController extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
    }

	@Override
	public void onClick(View v) 
	{
		if(v.getId() == R.id.Button_LogIn)
		{
			// Go to server and check username and password and (in)validate 
		}
		else if(v.getId() == R.id.Button_SignUp)
		{
			// Go to sign up screen and form
		}
	}
}
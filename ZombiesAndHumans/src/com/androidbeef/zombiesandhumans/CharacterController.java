package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CharacterController extends Activity implements OnClickListener
{
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character_layout);
		
		((Button) findViewById(R.id.healthMinus)).setOnClickListener(this);
	}
	public void onClick(View v)
	{
		if(v.getId()==R.id.healthMinus)
		{
			
		}
	}

}

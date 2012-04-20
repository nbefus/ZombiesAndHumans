package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class battleController extends Activity
{
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);
		
		((Button) findViewById(R.id.attackButton)).setOnClickListener(this);
	}
	public void onClick(View v)
	{
		if(v.getId()==R.id.attackButton)
		{
			
		}
	}
}

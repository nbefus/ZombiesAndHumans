package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;

public class HomeScreenController extends Activity
{
	private final String debugClass = "HOME_SCREEN_CONTROLLER";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
	}
	
}
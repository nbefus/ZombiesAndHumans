package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;

public class BattleController extends Activity
{
	private final String debugClass = "BATTLE_CONTROLLER";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);
	}
	
}
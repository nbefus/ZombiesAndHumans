package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreenController extends Activity implements OnClickListener
{
	private final String debugClass = "HOME_SCREEN_CONTROLLER";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		((Button) findViewById(R.id.character_button)).setOnClickListener(this);
		((Button) findViewById(R.id.items_button)).setOnClickListener(this);
		((Button) findViewById(R.id.battle_button)).setOnClickListener(this);
		((Button) findViewById(R.id.logout_button)).setOnClickListener(this);
		
	}

	public void onClick(View v)
	{
		if(v.getId() == R.id.character_button)
		{
			Intent c = new Intent(HomeScreenController.this, CharacterController.class);
			startActivity(c);
		}
		else if(v.getId() == R.id.items_button)
		{
			Intent b = new Intent(HomeScreenController.this, ItemController.class);
			startActivity(b);
		}
		else if(v.getId() == R.id.battle_button)
		{
			Intent a = new Intent(HomeScreenController.this, BattleController.class);
			startActivity(a);
		}
		else if(v.getId() == R.id.logout_button)
		{
			//I'm not sure how to code this so I'll leave it to you Nate.  Presumably
			//there is a way to do this...but I'm not sure how to terminate the access
			//to the PHP.
		}
	}
}
package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreenController extends Activity implements OnClickListener
{
	private Player p;
	private Character c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		p = (Player) getIntent().getExtras().getSerializable(
				StartScreenController.SELF);
		c = (Character) getIntent().getExtras().getSerializable(
				"char");
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
			c.putExtra("self", p);
			c.putExtra("char", c);
			startActivity(c);
		}
		else if(v.getId() == R.id.items_button)
		{
			Intent b = new Intent(HomeScreenController.this, ItemController.class);
			b.putExtra("self", p);
			b.putExtra("char", c);
			startActivity(b);
		}
		else if(v.getId() == R.id.battle_button)
		{
			Intent a = new Intent(HomeScreenController.this, PreBattleController.class);
			a.putExtra("self", p);
			a.putExtra("char", c);
			startActivity(a);
		}
		else if(v.getId() == R.id.logout_button)
		{
			Intent d = new Intent(HomeScreenController.this, StartScreenController.class);
			startActivity(d);
		}
	}
}
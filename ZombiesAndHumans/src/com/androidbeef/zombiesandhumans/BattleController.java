package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BattleController extends Activity implements OnClickListener
{
	private TextView eHealthDisplay;
	private TextView eNameDisplay;
	private TextView eLevelDisplay;
	private TextView eCurrentIDisplay;
	private TextView uHealthDisplay;
	private TextView uNameDisplay;
	private TextView uLevelDisplay;
	private TextView uCurrentIDisplay;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);
		
		((Button) findViewById(R.id.attackButton)).setOnClickListener(this);
		((Button) findViewById(R.id.retreatButton)).setOnClickListener(this);
		((Button) findViewById(R.id.itemsButton)).setOnClickListener(this);
		
		
	}
	public void onClick(View v)
	{
		if(v.getId()==R.id.attackButton)
		{
			//get user's attack info and then deal damage to the enemy's health
			
		}
		else if(v.getId()==R.id.retreatButton)
		{
			//flee
		}
		else if(v.getId()==R.id.itemsButton)
		{
			
		}
	}
	public TextView getEHealthDisplay()
	{
		if(eHealthDisplay==null)
		{
			eHealthDisplay=(TextView) findViewById(R.id.healthDisplay);
		}
		return eHealthDisplay;
	}
	public TextView getELevelDisplay()
	{
		if(eLevelDisplay==null)
		{
			eLevelDisplay=(TextView) findViewById(R.id.levelDisplay);
		}
		return eLevelDisplay;
	}
	public TextView getECurrentIDisplay()
	{
		if(eCurrentIDisplay==null)
		{
			eCurrentIDisplay=(TextView) findViewById(R.id.textView4);
		}
		return eCurrentIDisplay;
	}
	public TextView getENameDisplay()
	{
		if(eNameDisplay==null)
		{
			eNameDisplay=(TextView) findViewById(R.id.textView2);
		}
		return eNameDisplay;
	}
	public TextView getUNameDisplay()
	{
		if(uNameDisplay==null)
		{
			uNameDisplay=(TextView) findViewById(R.id.userTitle);
		}
		return uNameDisplay;
	}
	public TextView getULevelDisplay()
	{
		if(uLevelDisplay==null)
		{
			uLevelDisplay=(TextView) findViewById(R.id.userLvlDisplay);
		}
		return uLevelDisplay;
	}
	public TextView getUHealthDisplay()
	{
		if(uHealthDisplay==null)
		{
			uHealthDisplay=(TextView) findViewById(R.id.userHpDisplay);
		}
		return uHealthDisplay;
	}
	public TextView getUCurrentIDisplay()
	{
		if(uCurrentIDisplay==null)
		{
			uCurrentIDisplay=(TextView) findViewById(R.id.userCIDisplay);
		}
		return uCurrentIDisplay;
	}
}

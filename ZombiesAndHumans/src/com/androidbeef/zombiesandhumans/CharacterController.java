package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CharacterController extends Activity implements OnClickListener
{
	private int health=100;
	private int strength=1;
	private int defense=1;
	private int accuracy=30;
	private int evasion=30;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.character_layout);
		
		
		
		((Button) findViewById(R.id.healthMinus)).setOnClickListener(this);
		((Button) findViewById(R.id.healthPlus)).setOnClickListener(this);
		((Button) findViewById(R.id.strengthMinus)).setOnClickListener(this);
		((Button) findViewById(R.id.strengthPlus)).setOnClickListener(this);
		((Button) findViewById(R.id.defensePlus)).setOnClickListener(this);
		((Button) findViewById(R.id.defenseMinus)).setOnClickListener(this);
		((Button) findViewById(R.id.accuracyMinus)).setOnClickListener(this);
		((Button) findViewById(R.id.accuracyPlus)).setOnClickListener(this);
		((Button) findViewById(R.id.evasionMinus)).setOnClickListener(this);
		((Button) findViewById(R.id.evasionPlus)).setOnClickListener(this);
	}
	public void onClick(View v)
	{
		if(v.getId()==R.id.healthMinus)
		{
			if(!(health-1==0))
			{
				health--;
			}
		}
		else if(v.getId()==R.id.healthPlus)
		{
			health++;
		}
		else if(v.getId()==R.id.strengthMinus)
		{
			if(!(strength-1==0))
			{
				strength--;
			}
		}
		else if(v.getId()==R.id.strengthPlus)
		{
			strength++;
		}
		else if(v.getId()==R.id.defenseMinus)
		{
			if(!(defense-1==0))
			{
				defense--;
			}
		}
		else if(v.getId()==R.id.defensePlus)
		{
			defense++;
		}
		else if(v.getId()==R.id.accuracyMinus)
		{
			if(!(accuracy-1==0))
			{
				accuracy--;
			}
		}
		else if(v.getId()==R.id.accuracyPlus)
		{
			accuracy++;
		}
		else if(v.getId()==R.id.evasionMinus)
		{
			if(!(evasion-1==0))
			{
				evasion--;
			}
		}
		else if(v.getId()==R.id.evasionPlus)
		{
			evasion++;
		}
	}

}

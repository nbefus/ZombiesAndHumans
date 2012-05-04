package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CharacterController extends Activity implements OnClickListener
{
	private ZombiesAndHumansBrain	brain		= new ZombiesAndHumansBrain(this);
	private int health;
	private int strength;
	private int defense;
	private int accuracy;
	private int evasion;
	
	private TextView nameDisplay;
	private TextView levelDisplay;
	private TextView healthDisplay;
	private TextView strengthDisplay;
	private TextView defenseDisplay;
	private TextView accuracyDisplay;
	private TextView evasionDisplay;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		System.out.println("BEGIN");
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.character_layout);
		
		brain.setSelf((Player) getIntent().getExtras().getSerializable(
				StartScreenController.SELF));
		brain.setCharacter((Character) getIntent().getExtras().getSerializable(
				"char"));
		
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
		
		health = brain.getCharacter().getHealth();
		strength = brain.getCharacter().getStrength();
		defense = brain.getCharacter().getDefense();
		accuracy = brain.getCharacter().getAccuracy();
		evasion = brain.getCharacter().getEvasion();
		
		
		this.getHealthDisplay().setText(""+health);
		this.getStrengthDisplay().setText(""+strength);
		this.getDefenseDisplay().setText(""+defense);
		this.getAccuracyDisplay().setText(""+accuracy);
		this.getEvasionDisplay().setText(""+evasion);
		
	}
	public TextView getNameDisplay()
	{
		if(nameDisplay==null)
		{
			nameDisplay=(TextView) findViewById(R.id.textView1);
		}
		return nameDisplay;
	}
	public TextView getLevelDisplay()
	{
		if(levelDisplay==null)
		{
			levelDisplay=(TextView) findViewById(R.id.levelDisplay);
		}
		return levelDisplay;
	}
	public TextView getHealthDisplay()
	{
		if(healthDisplay==null)
		{
			healthDisplay=(TextView) findViewById(R.id.healthDisplay);
		}
		return healthDisplay;
	}
	public TextView getStrengthDisplay()
	{
		if(strengthDisplay==null)
		{
			strengthDisplay=(TextView) findViewById(R.id.strengthDisplay);
		}
		return strengthDisplay;
	}
	public TextView getDefenseDisplay()
	{
		if(defenseDisplay==null)
		{
			defenseDisplay=(TextView) findViewById(R.id.defenseDisplay);
		}
		return defenseDisplay;
	}
	public TextView getAccuracyDisplay()
	{
		if(accuracyDisplay==null)
		{
			accuracyDisplay=(TextView) findViewById(R.id.accuracyDisplay);
		}
		return accuracyDisplay;
	}
	public TextView getEvasionDisplay()
	{
		if(evasionDisplay==null)
		{
			evasionDisplay=(TextView) findViewById(R.id.evasionDisplay);
		}
		return evasionDisplay;
	}
	public void onClick(View v)
	{
		if(v.getId()==R.id.healthMinus)
		{
			if(!(health-1==0))
			{
				health--;
			}
			this.getHealthDisplay().setText(""+health);
		}
		else if(v.getId()==R.id.healthPlus)
		{
			health++;
			this.getHealthDisplay().setText(""+health);
		}
		else if(v.getId()==R.id.strengthMinus)
		{
			if(!(strength-1==0))
			{
				strength--;
			}
			this.getStrengthDisplay().setText(""+strength);
		}
		else if(v.getId()==R.id.strengthPlus)
		{
			strength++;
			this.getStrengthDisplay().setText(""+strength);
		}
		else if(v.getId()==R.id.defenseMinus)
		{
			if(!(defense-1==0))
			{
				defense--;
			}
			this.getDefenseDisplay().setText(""+defense);
		}
		else if(v.getId()==R.id.defensePlus)
		{
			defense++;
			this.getDefenseDisplay().setText(""+defense);
		}
		else if(v.getId()==R.id.accuracyMinus)
		{
			if(!(accuracy-1==0))
			{
				accuracy--;
			}
			this.getAccuracyDisplay().setText(""+accuracy);
		}
		else if(v.getId()==R.id.accuracyPlus)
		{
			accuracy++;
			this.getAccuracyDisplay().setText(""+accuracy);
		}
		else if(v.getId()==R.id.evasionMinus)
		{
			if(!(evasion-1==0))
			{
				evasion--;
			}
			this.getEvasionDisplay().setText(""+evasion);
		}
		else if(v.getId()==R.id.evasionPlus)
		{
			evasion++;
			this.getEvasionDisplay().setText(""+evasion);
		}
	}

}

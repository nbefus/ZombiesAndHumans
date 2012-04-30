package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	private Button attack;
	private Button retreat;
	private Button items;
	private ListView itemsView;
	
	private int userLevel=1;
	private int enemyLevel=1;
	private int userHealth=100;
	private int enemyHealth=100;
	private int userBP=5;
	private int enemyBP=5;
	private String userItem="bat";
	private String enemyItem="none";
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);
		
		//setListAdapter(new ArrayAdapter<String>(this,R.layout.battle,))
		
		((Button) findViewById(R.id.attackButton)).setOnClickListener(this);
		((Button) findViewById(R.id.retreatButton)).setOnClickListener(this);
		((Button) findViewById(R.id.itemsButton)).setOnClickListener(this);
		this.getULevelDisplay().setText(""+userLevel);
		this.getUHealthDisplay().setText(""+userHealth);
		this.getUCurrentIDisplay().setText(userItem);
		this.getELevelDisplay().setText(""+enemyLevel);
		this.getEHealthDisplay().setText(""+enemyHealth);
		this.getECurrentIDisplay().setText(enemyItem);
	}
	public void onClick(View v)
	{
		int enemyHealth=0;
		if(v.getId()==R.id.attackButton)
		{
			//get user's attack info and then deal damage to the enemy's health
			enemyHealth=Integer.parseInt((String) getECurrentIDisplay().getText());
			this.getEHealthDisplay().setText(""+(enemyHealth-userBP));
		}
		else if(v.getId()==R.id.retreatButton)
		{
			//Intent a = new Intent(PreBattleController.this, CharacterController.class);
			//startActivity(a);
			//create a toast that the user will take automatic damage for leaving the battle
			Context context=getApplicationContext();
			CharSequence fleeText="You are now leaving the battle and will take "+enemyBP;
			int duration=Toast.LENGTH_SHORT;
			Toast fleeToast=Toast.makeText(context, fleeText, duration);
		}
		else if(v.getId()==R.id.itemsButton)
		{
			//I'm not real sure how to implement this but we have a few options that we could explore here
			//we could either implement a list like we had done before for real-time use of items and maybe
			//the items button can be used to activate the items from a stack
		}
	}
	public Button getAttackButton()
	{
		if(attack==null)
		{
			attack=(Button) findViewById(R.id.attackButton);
		}
		return attack;
	}
	public Button getItemsButton()
	{
		if(items==null)
		{
			items=(Button) findViewById(R.id.itemsButton);
		}
		return items;
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

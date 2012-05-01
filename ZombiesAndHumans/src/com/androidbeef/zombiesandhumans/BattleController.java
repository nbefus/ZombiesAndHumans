package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;

import com.androidbeef.zombiesandhumans.PreBattleController.FindEnemiesInBackground;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
	private Button reload;
	private ListView itemsView;
	
	private int userLevel=1;
	private int enemyLevel=1;
	private int userHealth=100;
	private int enemyHealth=100;
	private int userBP=5;
	private int enemyBP=5;
	private String userItem="bat";
	private String enemyItem="none";
	private String[]				itemNames = {"medkit","waterbottle","canned food"};
	private int[]					numOfItem = {1,3,4};
	private HashMap<String, Number>	itemNameAndNum;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);
		
		((Button) findViewById(R.id.attackButton)).setOnClickListener(this);
		((Button) findViewById(R.id.retreatButton)).setOnClickListener(this);
		((Button) findViewById(R.id.reloadButton)).setOnClickListener(this);
		this.getULevelDisplay().setText(""+userLevel);
		this.getUHealthDisplay().setText(""+userHealth);
		this.getUCurrentIDisplay().setText(userItem);
		this.getELevelDisplay().setText(""+enemyLevel);
		this.getEHealthDisplay().setText(""+enemyHealth);
		this.getECurrentIDisplay().setText(enemyItem);
		
		itemNameAndNum = new HashMap<String,Number>();
		for (int i = 0; i < itemNames.length; i++)
		{
			itemNameAndNum.put(itemNames[i], numOfItem[i]);
		}
		createListView(itemsView, itemNameAndNum);
	}
	private void createListView(final ListView v, final HashMap<String,Number> map)
	{
		ArrayAdapter<String> adapter;

		adapter = new ArrayAdapter<String>(this, R.layout.itemrow,
				new ArrayList<String>());
		
		v.setAdapter(adapter);

		v.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Object []keys = map.keySet().toArray();
				
				itemDialog(v, (String)keys[position], map.get((String)keys[position]).intValue());
			}
		});

		if (map.size() > 0)
		{
			Object []keys = map.keySet().toArray();
			Toast.makeText(this, keys.length + " "+map.size(),
					Toast.LENGTH_LONG).show();
			for (int i = 0; i < map.size(); i++)
			{
				adapter.add("[" + map.get((String)keys[i]) + "]   " + (String)keys[i]);
			}

			v.invalidate();
		}
	}
	private void itemDialog(final ListView v, final String name, final int level)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to equip "+name)
				.setTitle("Confirmation")
				.setCancelable(true)
				.setPositiveButton("Use " + name,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								//right now there are only medkit type items so they can only add
								//to health
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dialog.cancel();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
	public void onClick(View v)
	{
		int enemyHealth=0;
		if(v.getId()==R.id.attackButton)
		{
			//get user's attack info and then deal damage to the enemy's health
			enemyHealth=Integer.parseInt((String) getECurrentIDisplay().getText());
			this.getEHealthDisplay().setText(""+(enemyHealth-userBP));
			//I want to make a toast here telling the user how much damage has been done to the enemy.
			
			//I also need to use the acync class here to make the button unpressable
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
		else if(v.getId()==R.id.reloadButton)
		{
			//this will eventually be used to reload the selected weapon
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
	public Button getReloadButton()
	{
		if(reload==null)
		{
			reload=(Button) findViewById(R.id.reloadButton);
		}
		return reload;
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

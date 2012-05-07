package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;

import com.androidbeef.zombiesandhumans.ItemController.performQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
	private double[]					numOfItem = {0.2,0.1,0.1};
	private HashMap<String, Number>	itemNameAndNum;
	private String attackCooldown="5000"; //in miliseconds
	private String enemyAttackTime="1000";
	private ZombiesAndHumansBrain brain = new ZombiesAndHumansBrain(this);
	private ProgressDialog	pd;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);
		
		brain.setSelf((Player) getIntent().getExtras().getSerializable(
				StartScreenController.SELF));
		brain.setCharacter((Character) getIntent().getExtras().getSerializable(
				"char"));
		brain.setEnemyCharacter((Character) getIntent().getExtras().getSerializable(
				"enemy char"));
		brain.setEnemy((Player) getIntent().getExtras().getSerializable(
				"enemy"));
		
		((Button) findViewById(R.id.attackButton)).setOnClickListener(this);
		((Button) findViewById(R.id.retreatButton)).setOnClickListener(this);
		((Button) findViewById(R.id.reloadButton)).setOnClickListener(this);
		((Button) findViewById(R.id.reloadButton)).setVisibility(View.INVISIBLE); //Until we get it implemented
		this.getULevelDisplay().setText(""+userLevel);
		this.getUHealthDisplay().setText(""+userHealth);
		this.getUCurrentIDisplay().setText(userItem);
		this.getELevelDisplay().setText(""+enemyLevel);
		this.getEHealthDisplay().setText(""+enemyHealth);
		this.getECurrentIDisplay().setText(enemyItem);
		itemsView=(ListView) findViewById(R.id.itemsView);
		
		getAllItems();
	}
	
	private void setItems()
	{
		itemNameAndNum = new HashMap<String,Number>();
		System.out.println("ITEM SIZE: "+brain.getItems().size());
		for (int i = 0; i < brain.getItems().size(); i++)
		{
			String[] abil = brain.getItems().get(i).getAbility().split(" ");
			if((abil[0].equals("+") && abil[2].equals("hp")) && brain.getItems().get(i).getInbackpackcount()>0)
				itemNameAndNum.put(brain.getItems().get(i).getIname(),Double.parseDouble(abil[1]));/// brain.getItems().get(i).getItemcount());
		}
		
		createListView(itemsView, itemNameAndNum);
	}
	
	private void getAllItems()
	{
		String[] entities = {"itemid","iname","ability","inbackpackcount","instoragecount"};
		String filename = "testing";
		String[] dataTypes = {"int","string","string","int","int"};
		String query = "SELECT i.itemid, iname, ability, inbackpackcount, instoragecount FROM backpack b JOIN backpackitems p ON b.backpackid = p.backpackid JOIN item i ON p.itemid = i.itemid WHERE b.backpackid="+brain.getSelf().getBackpackid();
				
		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...", "Checking with database", true, true);
		new performQuery().execute("Get All Items");
	}
	
	private void updateItem(String iname)
	{
		String filename = "testing";
		String query6;
		int posOfItem = brain.findItemByName(iname);
		
		query6 = "UPDATE backpackitems SET inbackpackcount="+(brain.getItems().get(posOfItem).getInbackpackcount()-1)+" WHERE backpackid="+brain.getSelf().getBackpackid()+" AND itemid="+brain.getItems().get(brain.findItemByName(iname)).getItemid();

		brain.getItems().get(posOfItem).setInbackpackcount(brain.getItems().get(posOfItem).getInbackpackcount()-1);
		brain.prepareForQuery(null, filename, null, query6);
		pd = ProgressDialog.show(this, "Processing...", "Inserting into database", true, true);
		new performQuery().execute("Update Item");
	}
	
	private void updateHealth(boolean myHealth)
	{
		String filename = "testing";
		String query6;
		
		if(myHealth)
			query6 = "UPDATE `character` SET health="+Integer.parseInt((String) getUHealthDisplay().getText().toString())+" WHERE characterid="+brain.getCharacter().getCharacterid();
		else
			query6 = "UPDATE `character` SET health="+Integer.parseInt((String) getUHealthDisplay().getText().toString())+" WHERE characterid="+brain.getEnemyCharacter().getCharacterid();

		brain.prepareForQuery(null, filename, null, query6);
		pd = ProgressDialog.show(this, "Processing...", "Inserting into database", true, true);
		if(myHealth)
			new performQuery().execute("Update Self Health");
		else
			new performQuery().execute("Update Enemy Health");
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
				itemDialog(v, (String)keys[position], map.get((String)keys[position]).doubleValue());
			}
		});

		if (map.size() > 0)
		{
			Object []keys = map.keySet().toArray();
			Toast.makeText(this, keys.length + " "+map.size(),
					Toast.LENGTH_LONG).show();
			for (int i = 0; i < map.size(); i++)
			{
				adapter.add("["+brain.getItems().get(brain.findItemByName(((String)keys[i]).trim())).getInbackpackcount()+"] "+ (String)keys[i]);
			}

			v.invalidate();
		}
	}
	private void itemDialog(final ListView v, final String name, final double abilityAmt)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("The "+name+" adds "+abilityAmt+" of health. " +
				"Are you sure you want to equip "+name+"?")
				.setTitle("Confirmation")
				.setCancelable(true)
				.setPositiveButton("Use " + name,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								int currentUHealth=0;
								currentUHealth=Integer.parseInt((String) getUHealthDisplay().getText());
								currentUHealth=(int) (currentUHealth*(1+abilityAmt));
								if(currentUHealth<150)
								{
									//update itemcount and health
									itemNameAndNum.put(name, (itemNameAndNum.get(name).intValue()-1));
									v.invalidate();
									updateItem(name.trim());
									setItems();
									getUHealthDisplay().setText(""+currentUHealth);
								}
								else
								{
									Context context=getApplicationContext();
									CharSequence fleeText="Sorry, but you can't use anymore health" +
											" boosts beyond 150 HP.";
									int duration=Toast.LENGTH_SHORT;
									Toast.makeText(context, fleeText, duration).show();
								}
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
			enemyHealth=Integer.parseInt((String) getEHealthDisplay().getText());
			if((enemyHealth-userBP)>=0)
			{
				getEHealthDisplay().setText(""+(enemyHealth-userBP));
				getAttackButton().setEnabled(false);
				new ButtonDisabled().execute(attackCooldown);
			}
		}
		else if(v.getId()==R.id.retreatButton)
		{
			//Intent a = new Intent(PreBattleController.this, CharacterController.class);
			//startActivity(a);
			Context context=getApplicationContext();
			CharSequence fleeText="You are now leaving the battle and will take "+enemyBP+" of damage.";
			int duration=Toast.LENGTH_SHORT;
			Toast.makeText(context, fleeText, duration).show();
			
			Intent a = new Intent(BattleController.this,
					HomeScreenController.class);
			a.putExtra("self", brain.getSelf());
			startActivity(a);
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
			eCurrentIDisplay=(TextView) findViewById(R.id.userCIDisplay);
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
			uCurrentIDisplay=(TextView) findViewById(R.id.textView4);
		}
		return uCurrentIDisplay;
	}
	//to disable the attackButton
	private class ButtonDisabled extends AsyncTask<String,Integer,String>
	{
		@Override
		protected String doInBackground(final String... toDo)
		{
			try
			{
				Thread.sleep(Integer.parseInt(toDo[0]));
			}
			catch (NumberFormatException e)
			{
			}
			catch (InterruptedException e)
			{
			}

			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			getAttackButton().setEnabled(true);
		}
	}
	
	
	public class computerPlayer extends AsyncTask<String,Integer,String>
	{
		//The enemy will attack 3 second after the user has attacked.
		@Override
		protected String doInBackground(final String... cmd)
		{
			try
			{
				Thread.sleep(Integer.parseInt(cmd[0]));
			}
			catch (NumberFormatException e)
			{
			}
			catch (InterruptedException e1)
			{
			}
			// TODO Auto-generated method stub
			

			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			int currentUHealth=0;
			currentUHealth=Integer.parseInt((String) getEHealthDisplay().getText());
			if((currentUHealth-enemyBP)>=0)
			{
				getEHealthDisplay().setText(""+(currentUHealth-enemyBP));
			}
		}
	}
	
	
	class performQuery extends AsyncTask<String, Integer, String>
	{
		boolean updated;
		@Override
		protected String doInBackground(String... parameters)
		{
			if(parameters[0].equals("Get All Items"))
				updated = brain.performQuery(true);
			else
				updated = brain.performQuery(false);
			
			return parameters[0];
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result.equals("Update Item"))
			{
				if(updated)
					Toast.makeText(BattleController.this, "Updating worked",
						Toast.LENGTH_LONG).show();
				else
					Toast.makeText(BattleController.this, "Updating failed",
							Toast.LENGTH_LONG).show();
				
				pd.dismiss();
			}
			else if(result.equals("Update Enemy Health")||result.equals("Update Self Health"))
			{
				if(updated)
					Toast.makeText(BattleController.this, "Updating health worked",
						Toast.LENGTH_LONG).show();
				else
					Toast.makeText(BattleController.this, "Updating health failed",
							Toast.LENGTH_LONG).show();
				
				if(result.equals("Update Enemy Health"))
				{
					pd.dismiss();
				}
				else
					updateHealth(false);
			}
			else if(result.equals("Get All Items"))
			{
				if(updated)
				{
					if(brain.getSearchResults().length > 0 && !(brain.getSearchResults()[0][0] instanceof String && ((String)brain.getSearchResults()[0][0]).equals("NO RESULTS")))
					{
						Object[][] dbItems = brain.getSearchResults();
						ArrayList<Item> items = new ArrayList<Item>();
						
						for(int i=0; i<dbItems.length; i++)
						{
							items.add(new Item(((Integer)dbItems[i][0]).intValue(), (String)dbItems[i][1], (String)dbItems[i][2], ((Integer)dbItems[i][3]).intValue(),((Integer)dbItems[i][4]).intValue()));
						}
						
						brain.setItems(items);
						setItems();
					}
				}
				pd.dismiss();
			}		
		}
	}
	
}

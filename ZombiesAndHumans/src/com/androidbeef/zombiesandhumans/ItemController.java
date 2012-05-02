package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.androidbeef.zombiesandhumans.SignUpController.performQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ItemController extends Activity implements OnClickListener
{
	private ListView				backpackListView;
	private ListView				storageListView;
	private HashMap<String, Number>	alitems;
	private HashMap<String, Number>	albackpackItems;
	private static final int 		INSTORAGE_COLUMN = 6;

	//private String[]				items				= { "Smoke grenade",
	//		"AK-47", "Frag", "Knife", "Armor", "Health pack" };
	//private String[]				backpackItems		= { "AK-47", "Frag",
		//	"Knife", "Armor", "Health pack"			};
	//private int[]					itemscount			= { 1, 2, 3, 4, 5, 6 };
	//private int[]					backpackitemscount	= { 1, 2, 3, 4, 5 };
	public ProgressDialog	pd;
	public ZombiesAndHumansBrain	brain = new ZombiesAndHumansBrain(this,null);

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);

		alitems = new HashMap<String,Number>();
		albackpackItems = new HashMap<String,Number>();
		
		brain.setSelf((Player) getIntent().getExtras().getSerializable(
				StartScreenController.SELF));
		/*
		for (int i = 0; i < items.length; i++)
		{
			alitems.put(items[i], itemscount[i]);
		}
		for (int i = 0; i < backpackItems.length; i++)
		{
			albackpackItems.put(backpackItems[i], backpackitemscount[i]);
		}
		*/

		backpackListView = (ListView) findViewById(R.id.ListView_backpack);
		storageListView = (ListView) findViewById(R.id.ListView_itemStorage);
		
		doGetItems("Get Backpack Items", 'n');
	}
	
	private void getBackpackItems(Object[][] bpItems)
	{
		if(bpItems.length > 0 && !(bpItems[0][0] instanceof String && ((String)bpItems[0][0]).equals("NO RESULTS")))
		for (int i = 0; i < bpItems.length; i++)
		{
			albackpackItems.put((String)bpItems[i][0], bpItems.length);
		}
		setUpListView(backpackListView, albackpackItems,"storage");
		doGetItems("Get Storage Items", 'y');
	}
	
	private void getStorageItems(Object[][] sItems)
	{
		if(sItems.length > 0 && !(sItems[0][0] instanceof String && ((String)sItems[0][0]).equals("NO RESULTS")))
		for (int i = 0; i < sItems.length; i++)
		{
			alitems.put((String)sItems[i][0], sItems.length);
		}
		setUpListView(storageListView,alitems, "backpack");
		
	}
	
	private void doGetItems(String what, char instorage)
	{
		String[] entities = {"iname","itemcount"};
		String filename = "testing";
		String[] dataTypes = {"string","int"};
		String query = "SELECT iname, itemcount FROM backpack b JOIN backpackitems p ON b.backpackid = p.backpackid JOIN item i ON p.itemid = i.itemid WHERE b.backpackid="+brain.getSelf().getBackpackid()+" AND p.instorage = '"+instorage+"'";
				
		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...", "Checking with database", true, true);
		new performQuery().execute(what);
		
		//if(what.equals("Get Backpack Items"))
			
	}
	
	
	private void doInsert()
	{
		String filename = "testing";
		//String query2 = "INSERT INTO `Players` (`COMPUTER_PLAYER`, `USERNAME`, `PASSWORD`) VALUES ('y', 'comp', 'player')";
		//String query = "INSERT INTO `Players`(`COMPUTER_PLAYER`, `USERNAME`, `PASSWORD`) VALUES ('n', 'test', 'testpassword')";
		
		//String query3 = "DELETE FROM `Players` WHERE USERNAME = 'user'";
		
		//String query4 = "UPDATE `Players` SET `COMPUTER_PLAYER`='y' WHERE `USERNAME`='Nathaniel'";
		
		//String query6 = "INSERT INTO `Players`(`COMPUTER_PLAYER`, `USERNAME`, `PASSWORD`) VALUES ('n', '"+username.getText().toString().trim()+"', '"+password.getText().toString().trim()+"')";
				
		//brain.prepareForQuery(null, filename, null, query6);
		pd = ProgressDialog.show(this, "Processing...", "Inserting into database", true, true);
		new performQuery().execute("Insert User");
	}

	private void setUpListView(final ListView v, final HashMap<String,Number> map, final String what)
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
				//Toast.makeText(ItemController.this, "PRESSED",
				//		Toast.LENGTH_LONG).show();
				Object []keys = map.keySet().toArray();
				itemDialog(v, (String)keys[position], what);
			}
		});

		if (map.size() > 0)
		{
			Object []keys = map.keySet().toArray();
			//Toast.makeText(this, keys.length + " "+map.size(),
			//		Toast.LENGTH_LONG).show();
			for (int i = 0; i < map.size(); i++)
			{
				adapter.add("[" + map.get((String)keys[i]) + "]   " + (String)keys[i]);
			}

			v.invalidate();

		}
	}
	
	private void deleteItem(String item, String what)
	{
		String filename = "testing";
		String query6 = "DELETE FROM `backpackitems` WHERE " +
				" ("+(1)+",50,100)";
				
		brain.prepareForQuery(null, filename, null, query6);
		pd = ProgressDialog.show(this, "Processing...", "Inserting into database", true, true);
		new performQuery().execute("Insert Backpack");
	}
	

	private void itemDialog(final ListView v, final String item, final String what)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to switch " + item + " into " + what)
				.setTitle("Switching an Item")
				.setCancelable(true)
				.setPositiveButton("Switch into " + what,
						new DialogInterface.OnClickListener()
						{
					
							public void onClick(DialogInterface dialog, int id)
							{
								if(what.contains("backpack"))
								{
										if(alitems.containsKey(item) && alitems.get(item).intValue() > 1)
										{
											alitems.put(item, alitems.remove(item).intValue()-1);
										}
										else
										{
											alitems.remove(item);
										}
										if(albackpackItems.containsKey(item))
											albackpackItems.put(item, albackpackItems.get(item).intValue()+1);
										else
											albackpackItems.put(item, 1);
										
									
									v.invalidate();
									setUpListView(backpackListView, albackpackItems,"storage");
									setUpListView(storageListView,alitems, "backpack");
								}
								else
								{
									if(albackpackItems.containsKey(item) && albackpackItems.get(item).intValue() > 1)
									{
										albackpackItems.put(item, albackpackItems.remove(item).intValue()-1);
									}
									else
									{
										albackpackItems.remove(item);
									}
									if(alitems.containsKey(item))
										alitems.put(item, alitems.get(item).intValue()+1);
									else
										alitems.put(item, 1);
										
									setUpListView(backpackListView, albackpackItems,"storage");
									setUpListView(storageListView,alitems, "backpack");	
								}
								
								
							}
						})
				.setNegativeButton("Cancel",
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

	}
	
	
	class performQuery extends AsyncTask<String, Integer, String>
	{
		boolean updated;
		@Override
		protected String doInBackground(String... parameters)
		{
			if(parameters[0].equals("Get Backpack Items") || parameters[0].equals("Get Storage Items"))
				updated = brain .performQuery(true);
			else
				updated = brain.performQuery(false);
			
			return parameters[0];
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result.equals("Update Item"))
			{
				if(updated)
					Toast.makeText(ItemController.this, "Updating worked",
						Toast.LENGTH_LONG).show();
				else
					Toast.makeText(ItemController.this, "Updating failed",
							Toast.LENGTH_LONG).show();
				pd.dismiss();
				//doCheck("2");
			}
			else if(result.equals("Get Backpack Items") || result.equals("Get Storage Items"))
			{
				if(updated)
				{
					Object[][] results = brain.getSearchResults();
					if(brain.getSearchResults().length > 0 && !(brain.getSearchResults()[0][0] instanceof String && ((String)brain.getSearchResults()[0][0]).equals("NO RESULTS")))
					{
						
						if(result.equals("Get Backpack Items"))
						{
							Toast.makeText(ItemController.this, "Getting backpack items",
								Toast.LENGTH_LONG).show();
							pd.dismiss();
							getBackpackItems(results);
						}	
						else
						{
							Toast.makeText(ItemController.this, "Getting storage items",
									Toast.LENGTH_LONG).show();
							pd.dismiss();
							getStorageItems(results);
						}
						pd.dismiss();
						
					}
					else
					{
						if(result.equals("Get Backpack Items"))
						{
							Toast.makeText(ItemController.this, "No Backpack Items",
								Toast.LENGTH_SHORT).show();
							pd.dismiss();
							getBackpackItems(results);
						}
							
						else
						{
							Toast.makeText(ItemController.this, "No Storage Items",
									Toast.LENGTH_SHORT).show();
							pd.dismiss();
							getStorageItems(results);
						}
							
						
						pd.dismiss();
						//doInsert();
					}
				}
				else
				{
					Toast.makeText(ItemController.this, "Error when trying to get data",
							Toast.LENGTH_LONG).show();
				}
				pd.dismiss();
				
			}			
		}
	}
	
}

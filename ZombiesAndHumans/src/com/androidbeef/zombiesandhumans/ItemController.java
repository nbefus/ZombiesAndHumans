package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ItemController extends Activity
{
	private ListView				backpackListView;
	private ListView				storageListView;
	private HashMap<String, Number>	alitems;
	private HashMap<String, Number>	albackpackItems;
	public ProgressDialog	pd;
	public ZombiesAndHumansBrain	brain = new ZombiesAndHumansBrain(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);

		alitems = new HashMap<String,Number>();
		albackpackItems = new HashMap<String,Number>();
		
		brain.setSelf((Player) getIntent().getExtras().getSerializable(
				StartScreenController.SELF));

		backpackListView = (ListView) findViewById(R.id.ListView_backpack);
		storageListView = (ListView) findViewById(R.id.ListView_itemStorage);
		
		getAllItems();
	}

	private void setItems()
	{
		for(int i=0; i<brain.getItems().size(); i++)
		{
			if(brain.getItems().get(i).getInstoragecount() > 0)
				alitems.put(brain.getItems().get(i).getIname(), brain.getItems().get(i).getInstoragecount());
			if(brain.getItems().get(i).getInbackpackcount() > 0)
				albackpackItems.put(brain.getItems().get(i).getIname(), brain.getItems().get(i).getInbackpackcount());
		}
		
		setUpListView(storageListView,alitems, "backpack");
		setUpListView(backpackListView, albackpackItems,"storage");
	}
	
	private void getAllItems()
	{
		String[] entities = {"itemid","iname","ability","cooldown","inbackpackcount","instoragecount"};
		String filename = "testing";
		String[] dataTypes = {"int","string","string","int","int","int"};
		String query = "SELECT i.itemid, iname, ability, cooldown, inbackpackcount, instoragecount FROM backpack b JOIN backpackitems p ON b.backpackid = p.backpackid JOIN item i ON p.itemid = i.itemid WHERE b.backpackid="+brain.getSelf().getBackpackid();
				
		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...", "Checking with database", true, true);
		new performQuery().execute("Get All Items");
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
				Object []keys = map.keySet().toArray();
				itemDialog(v, (String)keys[position], what);
			}
		});

		if (map.size() > 0)
		{
			Object []keys = map.keySet().toArray();
			for (int i = 0; i < map.size(); i++)
			{
				adapter.add("[" + map.get((String)keys[i]) + "]   " + (String)keys[i]);
			}

			v.invalidate();
		}
	}
	private void updateItem(String iname, boolean goinginstorage)
	{
		String filename = "testing";
		String query6;
		int posOfItem = brain.findItemByName(iname);
		if(goinginstorage)
			query6 = "UPDATE backpackitems SET inbackpackcount="+(brain.getItems().get(posOfItem).getInbackpackcount()-1)+",instoragecount="+(brain.getItems().get(posOfItem).getInstoragecount()+1)+" WHERE backpackid="+brain.getSelf().getBackpackid()+" AND itemid="+brain.getItems().get(brain.findItemByName(iname)).getItemid();
		else
			query6 = "UPDATE backpackitems SET inbackpackcount="+(brain.getItems().get(posOfItem).getInbackpackcount()+1)+",instoragecount="+(brain.getItems().get(posOfItem).getInstoragecount()-1)+" WHERE backpackid="+brain.getSelf().getBackpackid()+" AND itemid="+brain.getItems().get(brain.findItemByName(iname)).getItemid();

		brain.prepareForQuery(null, filename, null, query6);
		pd = ProgressDialog.show(this, "Processing...", "Inserting into database", true, true);
		new performQuery().execute("Update Item");
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
									updateItem(item, false);
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
									updateItem(item, true);
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
	
	class performQuery extends AsyncTask<String, Integer, String>
	{
		boolean updated;
		@Override
		protected String doInBackground(String... parameters)
		{
			if(parameters[0].equals("Get All Items"))
				updated = brain .performQuery(true);
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
					Toast.makeText(ItemController.this, "Updating item worked",
						Toast.LENGTH_LONG).show();
				else
					Toast.makeText(ItemController.this, "Updating item failed",
							Toast.LENGTH_LONG).show();
				pd.dismiss();
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
							items.add(new Item(((Integer)dbItems[i][0]).intValue(), (String)dbItems[i][1], (String)dbItems[i][2], ((Integer)dbItems[i][3]).intValue(),((Integer)dbItems[i][4]).intValue(),((Integer)dbItems[i][5]).intValue()));
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

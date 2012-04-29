package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

	private String[]				items				= { "Smoke grenade",
			"AK-47", "Frag", "Knife", "Armor", "Health pack" };
	private String[]				backpackItems		= { "AK-47", "Frag",
			"Knife", "Armor", "Health pack"			};
	private int[]					itemscount			= { 1, 2, 3, 4, 5, 6 };
	private int[]					backpackitemscount	= { 1, 2, 3, 4, 5 };

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);

		alitems = new HashMap<String,Number>();
		albackpackItems = new HashMap<String,Number>();
		
		for (int i = 0; i < items.length; i++)
		{
			alitems.put(items[i], itemscount[i]);
		}
		for (int i = 0; i < backpackItems.length; i++)
		{
			albackpackItems.put(backpackItems[i], backpackitemscount[i]);
		}

		backpackListView = (ListView) findViewById(R.id.ListView_backpack);
		storageListView = (ListView) findViewById(R.id.ListView_itemStorage);
		setUpListView(backpackListView, albackpackItems,"storage");
		setUpListView(storageListView,alitems, "backpack");
	}

	private void setUpListView(final ListView v, final HashMap<String,Number> map, final String what)
	{
		
		ArrayAdapter<String> adapter;

		adapter = new ArrayAdapter<String>(this, R.layout.itemrow,
				new ArrayList<String>());
		
		v.setAdapter(adapter);
		//registerForContextMenu(backpackListView);
		//backpackListView.setOnCreateContextMenuListener(this);

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
		
		/*
		if(what.equals("backpack"))
		{
			backpackListView.setAdapter(adapter);
			//registerForContextMenu(backpackListView);
			//backpackListView.setOnCreateContextMenuListener(this);
	
			backpackListView.setOnItemClickListener(new OnItemClickListener()
			{
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					Toast.makeText(ItemController.this, "PRESSED",
							Toast.LENGTH_LONG).show();
					Object []keys = map.keySet().toArray();
					itemDialog(backpackListView, (String)keys[position], what);
				}
			});
		}
		else
		{
			storageListView.setAdapter(adapter);
			//registerForContextMenu(storageListView);
			//storageListView.setOnCreateContextMenuListener(this);
	
			storageListView.setOnItemClickListener(new OnItemClickListener()
			{
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id)
				{
					Toast.makeText(ItemController.this, "PRESSED",
							Toast.LENGTH_LONG).show();
					Object []keys = map.keySet().toArray();
					itemDialog(storageListView, (String)keys[position], what);
				}
			});
		}
		*/
		

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
									/*
									int num;
									if(alitems.containsKey(item) && alitems.get(item).intValue() > 1)
										num = alitems.get(item).intValue();
									else
										num = alitems.remove(item).intValue();

*/
									
									
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
									/*
									int num;
									if(albackpackItems.containsKey(item) && albackpackItems.get(item).intValue() > 1)
										num = albackpackItems.get(item).intValue();
									else
										num = albackpackItems.remove(item).intValue();
*/
									
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
}

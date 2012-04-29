package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PreBattleController extends MapActivity
{
	private ZombiesAndHumansBrain	brain		= new ZombiesAndHumansBrain(this);
	private MapView					mapView;
	private MapController			mc;
	private GeoPoint				myGeoPoint;
	private String[]				enemyNames = {"diablo20","nevaLrndNEthing","gamer","Coolio","maya","doomsday2012"};
	private int[]					level = {1,2,3,4,5,6};
	private ListView				lv;
	private HashMap<String, Number>	nameandlevel;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prebattle);
		mapView = (MapView) findViewById(R.id.mapView);
		lv = (ListView) findViewById(R.id.listView2);
		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		nameandlevel = new HashMap<String,Number>();
		for (int i = 0; i < level.length; i++)
		{
			nameandlevel.put(enemyNames[i], level[i]);
		}
		new FindEnemiesInBackground().execute("here");
		setUpListView(lv, nameandlevel);

	}

	private void setUpListView(final ListView v, final HashMap<String,Number> map)
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
				Object []keys = map.keySet().toArray();
				
				battleDialog(v, (String)keys[position], map.get((String)keys[position]).intValue());
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
	
	private void battleDialog(final ListView v, final String name, final int level)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to battle "+name + " at level "+level)
				.setTitle("Battle Confirmation")
				.setCancelable(true)
				.setPositiveButton("Battle " + name,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								Intent a = new Intent(PreBattleController.this, BattleController.class);
								startActivity(a);
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
	
	private void putMeAndEnemiesOnMap()
	{
		myGeoPoint = new GeoPoint((int) (brain.getMyLat() * 1E6),
				(int) (brain.getMyLon() * 1E6));

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable enemyDrawable = this.getResources().getDrawable(
				R.drawable.enemy);
		Drawable me = this.getResources().getDrawable(R.drawable.you);
		CustomMapOverlay itemizedoverlay2 = new CustomMapOverlay(me,
				PreBattleController.this);
		CustomMapOverlay itemizedoverlay = new CustomMapOverlay(enemyDrawable,
				PreBattleController.this);

		OverlayItem[] ois = new OverlayItem[brain.getEnemies().size()];
		for (int i = 0; i < brain.getEnemies().size(); i++)
		{
			ois[i] = new OverlayItem(brain.getEnemies().get(i), "Enemy",
					"Not sure what to say here");
			itemizedoverlay.addOverlay(ois[i]);
		}

		OverlayItem overlayitem = new OverlayItem(myGeoPoint, "You are here",
				"Not sure what to say here either");

		itemizedoverlay2.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mapOverlays.add(itemizedoverlay2);

		Toast.makeText(PreBattleController.this, "Done Loading Enemies",
				Toast.LENGTH_SHORT).show();

	}

	class FindEnemiesInBackground extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... stopInfo)
		{
			brain.findEnemies();
			return "";
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result)
		{
			putMeAndEnemiesOnMap();
			mc.animateTo(myGeoPoint);
			mc.setZoom(15);
			mapView.invalidate();
		}
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

}

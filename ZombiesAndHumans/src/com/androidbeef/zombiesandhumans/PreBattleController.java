package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
	private ZombiesAndHumansBrain	brain	= new ZombiesAndHumansBrain(this);
	private MapView					mapView;
	private MapController			mc;
	private GeoPoint				myGeoPoint;
	private ListView				lv;
	private HashMap<String, Number>	nameandlevel;
	private ProgressDialog			pd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prebattle);
		brain.setSelf((Player) getIntent().getExtras().getSerializable(
				StartScreenController.SELF));
		brain.setCharacter((Character) getIntent().getExtras().getSerializable(
				"char"));
		mapView = (MapView) findViewById(R.id.mapView);
		lv = (ListView) findViewById(R.id.listView2);
		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		nameandlevel = new HashMap<String, Number>();
		getEnemies();
	}

	private void setNameAndLevel()
	{
		for (int i = 0; i < brain.getEnemies().size(); i++)
		{
			nameandlevel.put(brain.getEnemies().get(i).getUsername(), brain
					.getEnemiesCharacters().get(i).getClevel());
		}
		setUpListView(lv, nameandlevel);
		putMeAndEnemiesOnMap();
		mc.animateTo(myGeoPoint);
		mc.setZoom(15);
		mapView.invalidate();
	}

	private void setUpListView(final ListView v,
			final HashMap<String, Number> map)
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
				Object[] keys = map.keySet().toArray();
				int enemyPos = brain.findEnemyPosByUsername((String) keys[position]);
				int enemyCharPos = brain.findEnemyCharPosByUsername((String) keys[position]);
				if(enemyPos == -1)
					Toast.makeText(PreBattleController.this, "ERROR: Enemy Not Found!",
						Toast.LENGTH_SHORT).show();
				else if(enemyCharPos == -1)
					Toast.makeText(PreBattleController.this, "ERROR: Enemy Character Not Found!",
							Toast.LENGTH_SHORT).show();
				else
					battleDialog(v, (String) keys[position],
						map.get((String) keys[position]).intValue(),enemyPos,enemyCharPos);
				
					
			}
		});

		if (map.size() > 0)
		{
			Object[] keys = map.keySet().toArray();

			for (int i = 0; i < map.size(); i++)
			{
				adapter.add("[" + map.get((String) keys[i]) + "]   "
						+ (String) keys[i]);
			}

			v.invalidate();
		}
	}

	private void battleDialog(final ListView v, final String name,
			final int level, final int enemyPos, final int enemyCharPos)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Are you sure you want to battle " + name + " at level "
						+ level)
				.setTitle("Battle Confirmation")
				.setCancelable(true)
				.setPositiveButton("Battle " + name,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								brain.setEnemy(brain.getEnemies().get(enemyPos));
								brain.setEnemyCharacter(brain.getEnemiesCharacters().get(enemyCharPos));
								Intent a = new Intent(PreBattleController.this,
										BattleController.class);
								a.putExtra("self", brain.getSelf());
								a.putExtra("char", brain.getCharacter());
								a.putExtra("enemy", brain.getEnemy());
								a.putExtra("enemy char", brain.getEnemyCharacter());
								startActivity(a);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener()
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
		myGeoPoint = new GeoPoint((int) (brain.getSelf().getLocationx() * 1E6),
				(int) (brain.getSelf().getLocationy() * 1E6));

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
			ois[i] = new OverlayItem(brain.getEnemyLoc().get(i), "Enemy",
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

	private void getEnemies()
	{
		String[] entities = { "playerid", "computerplayer", "username",
				"password", "locationx", "locationy", "safehousex",
				"safehousey", "backpackid", "characterid", "characterid",
				"cname", "clevel", "health", "strength", "defense", "accuracy",
				"evasion" };
		String filename = "testing";
		String[] dataTypes = { "int", "string", "string", "string", "double",
				"double", "double", "double", "int", "int", "int", "string",
				"int", "int", "int", "int", "int", "int" };
		String query = "select * from player p join `character` c on p.characterid = c.characterid where (locationx BETWEEN "
				+ (brain.getSelf().getLocationx() - 5)
				+ " and "
				+ (brain.getSelf().getLocationx() + 5)
				+ " AND locationy BETWEEN "
				+ (brain.getSelf().getLocationy() - 5)
				+ " and "
				+ (brain.getSelf().getLocationy() + 5 + ") AND locationx <> "
						+ brain.getSelf().getLocationx() + " AND locationy <> " + brain
						.getSelf().getLocationy());

		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...", "Finding Enemies",
				true, true);
		new FindEnemiesInBackground().execute("Find Enemies");

	}

	class FindEnemiesInBackground extends AsyncTask<String, Integer, String>
	{
		boolean	updated;

		@Override
		protected String doInBackground(String... parameters)
		{
			updated = brain.performQuery(true);
			return "";
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (updated)
			{
				if (brain.getSearchResults().length > 0
						&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
								.getSearchResults()[0][0]).equals("NO RESULTS")))
				{
					Object[][] results = brain.getSearchResults();
					ArrayList<Player> enemies = new ArrayList<Player>();
					ArrayList<Character> enemiesCharacters = new ArrayList<Character>();
					for (int i = 0; i < results.length; i++)
					{
						enemies.add(new Player(((Integer) results[i][0])
								.intValue(),
								((String) results[i][1]).charAt(0),
								(String) results[i][2], (String) results[i][3],
								((Double) results[i][4]).doubleValue(),
								((Double) results[i][5]).doubleValue(),
								((Double) results[i][6]).doubleValue(),
								((Double) results[i][7]).doubleValue(),
								((Integer) results[i][8]).intValue(),
								((Integer) results[i][9]).intValue()));
						enemiesCharacters.add(new Character(
								((Integer) results[i][10]).intValue(),
								((String) results[i][11]),
								((Integer) results[i][12]).intValue(),
								((Integer) results[i][13]).intValue(),
								((Integer) results[i][14]).intValue(),
								((Integer) results[i][15]).intValue(),
								((Integer) results[i][16]).intValue(),
								((Integer) results[i][17])));
					}
					pd.dismiss();
					brain.setEnemies(enemies);
					brain.setEnemiesCharacters(enemiesCharacters);
					setNameAndLevel();
				}
			}

		}
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

}

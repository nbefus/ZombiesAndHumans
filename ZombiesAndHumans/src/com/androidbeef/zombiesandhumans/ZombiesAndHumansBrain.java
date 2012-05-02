package com.androidbeef.zombiesandhumans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.Toast;


// Sorry for all the commented out Toasts. Apparently an Async Task which will calls these methods cannot have anything which
// runs on the main UI thread. Will have to handle/detect/make known errors a little differently.

//Decided that for an error put "error" into the first result. Although for a Query with no variables, result will say invalid
//even if it is successful. So will not know if the sql failed due to an invalid argument so we must check our sql statement well
//before using it.

/*
 * Here are a list of example database commands
 * 
 * String query = "INSERT INTO `Players`(`COMPUTER_PLAYER`, `USERNAME`, `PASSWORD`) VALUES ('n', 'test', 'testpassword')";
 * String query1 = "DELETE FROM `Players` WHERE USERNAME = 'user'";
 * String query2 = "UPDATE `Players` SET `COMPUTER_PLAYER`='y' WHERE `USERNAME`='Nathaniel'";
 * String query3 = "SELECT * from Players where USERNAME = 'Nathaniel'";
 * 
 */
public class ZombiesAndHumansBrain
{
	private final String	debugClass	= "BRAIN";

	private Player self;
	private ArrayList<Item> items;
	private ArrayList<Player> enemies;
	private Context			c;
	private ArrayList<GeoPoint>	enemyLoc;
	private Query search;
	private Object[][] searchResults;
	
	//private double[]			latitude	= { 21.2613, 12, 21.2636, 21.2663,
	//		21.2679, 21.2702, 21.2723, 21.2731, 21.2721, 21.2738, 21.2758,
	//		21.277, 21.2783, 21.2793		};
	//private double[]			longitude	= { -157.8181, -157.8185,
	//		-157.8168, -157.816, -157.816, -157.8164, -157.8172, -157.8199,
	//		-157.8211, -157.8226, -157.8237, -157.8248, -157.826 };
	
	//private double				myLat;
	//private double				myLon;

	public ZombiesAndHumansBrain(Context c)
	{
		this.c = c;
	}
	
	public void prepareForQuery(String[] entities,
			String filename, String[] dataTypes, String query)
	{
		search = new Query(entities, filename, dataTypes, query);
	}
	
	public boolean performQuery(boolean withResults)
	{
		if(search != null)
		{
			if(withResults)
				searchResults = search.performQueryWithResult();
			else
				return search.performQueryWithNoResults();
			
			if(searchResults[0][0] instanceof String && !((String)searchResults[0][0]).contains("error"))
				return true;
			else if(!(searchResults[0][0] instanceof String))
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	/*
	public void findEnemies()
	{
		myLat = 21.2500;
		myLon = -157.8100;
		//double latPrec = .005, lonPrec = .005;
		enemyLoc = new ArrayList<GeoPoint>();

		for (int i = 0; i < longitude.length; i++)
			//if (Math.abs(latitude[i] - myLat) <= latPrec
				//	&& Math.abs(longitude[i] - myLon) <= lonPrec)
				enemyLoc.add(new GeoPoint((int) (latitude[i] * 1E6),
						(int) (longitude[i] * 1E6)));
	}
	*/
	
	public void setEnemies(ArrayList<Player> enemies)
	{
		this.enemies = enemies;
		System.out.println("ENEMY SIZE : "+enemies.size());
		enemyLoc = new ArrayList<GeoPoint>();
		for(int i=0; i<enemies.size(); i++)
			enemyLoc.add(new GeoPoint((int) (enemies.get(i).getLocationx() * 1E6),
					(int) (enemies.get(i).getLocationy() * 1E6)));
	}
	
	public ArrayList<Player> getEnemies()
	{
		return enemies;
	}
	
	public void setSelf(Player self)
	{
		this.self = self;
	}
	
	public void setItems(ArrayList<Item> items)
	{
		this.items = items;
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
	public Player getSelf()
	{
		return self;
	}
	
	public ArrayList<GeoPoint> getEnemyLoc()
	{
		return enemyLoc;
	}
	
	//public double getMyLat()
	//{
	//	return myLat;
	//}
	
	//public double getMyLon()
	//{
	//	return myLon;
	//}
	
	public Object[][] getSearchResults()
	{
		return searchResults;
	}
	
}

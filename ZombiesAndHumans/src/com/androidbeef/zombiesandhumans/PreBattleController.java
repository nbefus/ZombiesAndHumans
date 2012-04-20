package com.androidbeef.zombiesandhumans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PreBattleController extends Activity
{
	private final String		debugClass	= "BATTLE_CONTROLLER";

	private double[]			latitude	= { 21.2613, 12, 21.2636, 21.2663,
			21.2679, 21.2702, 21.2723, 21.2731, 21.2721, 21.2738, 21.2758,
			21.277, 21.2783, 21.2793		};
	private double[]			longitude	= { -157.8181, -157.8185,
			-157.8168, -157.816, -157.816, -157.8164, -157.8172, -157.8199,
			-157.8211, -157.8226, -157.8237, -157.8248, -157.826 };

	private MapView				mapView;
	private MapController		mc;
	private double				myLat;
	private double				myLon;
	private GeoPoint			myGeoPoint;
	private ArrayList<GeoPoint>	enemies;

	private int					numOfEnemies;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prebattle);
	}

	private void setUpMap()
	{
		mapView = (MapView) findViewById(R.id.mapView);

		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		myLat = 21.2500;
		myLon = -157.8100;
		// GeoPoint hawaii = new GeoPoint((int)(21.469684 * 1E6), (int)
		// (-157.975159 * 1E6));

		// processNearBusStops();
		new BusStop().execute("here");

	}

	private void putMeAndNearestBusStopsOnMap()
	{
		myGeoPoint = new GeoPoint((int) (myLat * 1E6), (int) (myLon * 1E6));

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.stop2);
		Drawable drawable2 = this.getResources().getDrawable(R.drawable.you);
		enemyOverlay itemizedoverlay = new enemyOverlay(drawable,
				PreBattleController.this);
		enemyOverlay itemizedoverlay2 = new enemyOverlay(drawable2,
				PreBattleController.this);

		OverlayItem[] ois = new OverlayItem[enemies.size()];
		for (int i = 0; i < enemies.size(); i++)
		{
			ois[i] = new OverlayItem(stops[i], name[i], stopIDs[i]);
			itemizedoverlay.addOverlay(ois[i]);
		}

		OverlayItem overlayitem = new OverlayItem(
				myGeoPoint,
				"You are (around) here",
				"Remember: This could be inaccurate depending on which location provider you used");

		itemizedoverlay2.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mapOverlays.add(itemizedoverlay2);

		mc.animateTo(myGeoPoint);
		mc.setZoom(15);
		mapView.invalidate();
		Toast.makeText(PreBattleController.this, "Done Loading Enemies",
				Toast.LENGTH_SHORT).show();

	}

	private String findEnemies()
	{
		double latPrec = .005, lonPrec = .005;
		enemies = new ArrayList<GeoPoint>();

		for (int i = 0; i < longitude.length; i++)
			if (Math.abs(latitude[i] - myLat) <= latPrec
					&& Math.abs(longitude[i] - myLon) <= lonPrec)
				enemies.add(new GeoPoint((int) (latitude[i] * 1E6),
						(int) (longitude[i] * 1E6)));
		return "";
	}

	private class enemyOverlay extends ItemizedOverlay
	{

		private boolean					isPinch		= false;
		private Context					mContext;

		private ArrayList<OverlayItem>	mOverlays	= new ArrayList<OverlayItem>();

		public enemyOverlay(Drawable defaultMarker, Context context)
		{
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		@Override
		protected OverlayItem createItem(int i)
		{
			return mOverlays.get(i);
		}

		public void addOverlay(OverlayItem overlay)
		{
			mOverlays.add(overlay);
			populate();
		}

		@Override
		public boolean onTouchEvent(MotionEvent e, MapView mapView)
		{

			if (!(Build.VERSION.SDK_INT == Build.VERSION_CODES.DONUT))
			{
				if (e.getAction() == MotionEvent.ACTION_DOWN)
				{
					isPinch = false; // Touch DOWN don't know if it's a pinch
										// yet
				}
				if (e.getAction() == MotionEvent.ACTION_MOVE)
				{
					isPinch = true; // Two fingers a pinch
				}
			}
			else
			{
				// Is donut device
			}
			return super.onTouchEvent(e, mapView);
		}

		@Override
		protected boolean onTap(int index)
		{
			if (!isPinch)
			{
				OverlayItem item = mOverlays.get(index);

				/*
				 * AlertDialog.Builder dialog = new
				 * AlertDialog.Builder(mContext);
				 * 
				 * dialog.setTitle(item.getTitle());
				 * dialog.setMessage(item.getSnippet());
				 * 
				 * if (!item.getTitle().equals("You are (around) here")) {
				 * i.putExtra(MapsActivity.STOP_ID, item.getSnippet());
				 * System.out.println(MapsActivity.STOP_ID + " " +
				 * item.getSnippet());
				 * 
				 * if (!event.equals("bus")) {
				 * dialog.setPositiveButton(R.string.loadStop, new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { startActivity(i);
				 * 
				 * } }); } else { dialog.setPositiveButton(R.string.pos_button,
				 * new OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { dialog.cancel(); } }); }
				 * 
				 * if (!event.equals("bus")) {
				 * dialog.setNegativeButton(R.string.neg_button, new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { dialog.cancel();
				 * 
				 * } }); } } else {
				 * dialog.setPositiveButton(R.string.pos_button, new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { // TODO Auto-generated method stub dialog.cancel();
				 * } }); }
				 * 
				 * dialog.show(); return true;
				 */
			}

			return false;

		}

		@Override
		public int size()
		{
			return mOverlays.size();
		}

		public void clear()
		{
			mOverlays.clear();
			setLastFocusedIndex(-1);
			populate();
		}

	}

	class BusStop extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... stopInfo)
		{
			return findEnemies();
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (result.length() > 0)
				putMeAndNearestBusStopsOnMap();
		}
	}

}

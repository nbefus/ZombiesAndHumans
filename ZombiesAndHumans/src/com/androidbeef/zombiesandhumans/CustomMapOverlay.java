package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CustomMapOverlay extends ItemizedOverlay
{
	private boolean					isPinch		= false;
	private Context					c;

	private ArrayList<OverlayItem>	mOverlays	= new ArrayList<OverlayItem>();

	public CustomMapOverlay(Drawable defaultMarker, Context context)
	{
		super(boundCenterBottom(defaultMarker));
		c = context;
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

			Toast.makeText(c, item.getSnippet() + ":"+item.getTitle() +":"+ item.getPoint(),
					Toast.LENGTH_SHORT).show();
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
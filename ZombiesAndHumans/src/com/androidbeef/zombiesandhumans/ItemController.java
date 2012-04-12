package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ItemController extends Activity implements OnClickListener
{
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);
		
		//need a listview for the items
		//have one for the backpack
		//when you click a button in the item's list it will give a pop-up of an option
		//to add the item to your backpack
	}

	public void onClick(View v)
	{

	}
}

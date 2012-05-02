package com.androidbeef.zombiesandhumans;

import android.os.AsyncTask;

public class computerPlayer extends AsyncTask<String,Integer,String>
{
	@Override
	protected String doInBackground(String... arg0)
	{
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
		    public void run()
		    {
		    	try {
					Thread.sleep(10000);
				}
		    	catch (InterruptedException e) {
				}
		    }
		  }).start();

		return null;
	}
	@Override
	protected void onPostExecute(String result)
	{
		//attack the player
		
	}
	
}

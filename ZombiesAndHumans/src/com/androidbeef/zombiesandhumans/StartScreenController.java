package com.androidbeef.zombiesandhumans;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartScreenController extends Activity implements OnClickListener {
	
	private final String debugClass = "START_SCREEN_CONTROLLER";

	private ZombiesAndHumansBrain brain = new ZombiesAndHumansBrain(this);
	private EditText username;
	private EditText password;
	private ProgressDialog pd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button) findViewById(R.id.Button_LogIn)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_SignUp)).setOnClickListener(this);

        username = (EditText) findViewById(R.id.EditText_Username);
        password = (EditText) findViewById(R.id.EditText_Password);
    }

	@Override
	public void onClick(View v)
	{		
		if(v.getId() == R.id.Button_LogIn)
		{
			String[] entities = {"USERNAME","PASSWORD"};
			String filename = "testing";
			String[] dataTypes = {"string","string"};
			String query = "select * from Players where USERNAME = '"+username.getText().toString().trim()+"' AND PASSWORD = '"+password.getText().toString().trim()+"'";
					
			brain.setQueryVariables(entities, filename, dataTypes, query);
			pd = ProgressDialog.show(this, "Processing...", "Trying to log in", true, true);
			new performQuery().execute();
		}	
		else if(v.getId() == R.id.Button_SignUp)
		{
			Intent j = new Intent(StartScreenController.this, SignUpController.class);
			startActivity(j);
		}
			
	}	
	
	/*
	 * A class which will do the actual searching of the database in the background. This
	 * means that there will be no lag on the UI.
	 * 
	 * ****Must call ZombiesAndHumansBrain method setQueryVariables before calling this.****
	 */
	class performQuery extends AsyncTask<String, Integer, String>
	{
		private Object[][] results;
		
		@Override
		protected String doInBackground(String... parameters)
		{
			results = brain.performSearchWithResult();
			
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
			if(!(results == null))
			{
				if(results.length > 0)
				{
					Toast.makeText(StartScreenController.this, "User known",
							Toast.LENGTH_LONG).show();
					
					Intent i = new Intent(StartScreenController.this, HomeScreenController.class);
					startActivity(i);
				}
				else
				{
					Toast.makeText(StartScreenController.this, "User unknown!",
							Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(StartScreenController.this, "User unknown!",
						Toast.LENGTH_LONG).show();
			}
			pd.dismiss();
			
		}
	}
}
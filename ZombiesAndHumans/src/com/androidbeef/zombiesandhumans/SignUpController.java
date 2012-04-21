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

public class SignUpController extends Activity implements OnClickListener
{
	private final String debugRandom = "SIGN_UP_CONTROLLER";
	private ZombiesAndHumansBrain brain = new ZombiesAndHumansBrain(this);
	private EditText	username;
	private EditText	password;
	private ProgressDialog	pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		((Button) findViewById(R.id.Button_Cancel)).setOnClickListener(this);
        ((Button) findViewById(R.id.Button_SignUpNow)).setOnClickListener(this);

        username = (EditText) findViewById(R.id.EditText_DesiredUsername);
        password = (EditText) findViewById(R.id.EditText_DesiredPassword);
	}

	public void onClick(View v) 
	{
		if(v.getId() == R.id.Button_Cancel)
		{
			this.finish();
		}
		else if(v.getId() == R.id.Button_SignUpNow)
		{
			doCheck("0");
			//doInsert();
		}
	}
	
	private void doInsert()
	{
		String filename = "testing";
		//String query2 = "INSERT INTO `Players` (`COMPUTER_PLAYER`, `USERNAME`, `PASSWORD`) VALUES ('y', 'comp', 'player')";
		//String query = "INSERT INTO `Players`(`COMPUTER_PLAYER`, `USERNAME`, `PASSWORD`) VALUES ('n', 'test', 'testpassword')";
		
		//String query3 = "DELETE FROM `Players` WHERE USERNAME = 'user'";
		
		//String query4 = "UPDATE `Players` SET `COMPUTER_PLAYER`='y' WHERE `USERNAME`='Nathaniel'";
		
		String query6 = "INSERT INTO `Players`(`COMPUTER_PLAYER`, `USERNAME`, `PASSWORD`) VALUES ('n', '"+username.getText().toString().trim()+"', '"+password.getText().toString().trim()+"')";
				
		brain.prepareForQuery(null, filename, null, query6);
		pd = ProgressDialog.show(this, "Processing...", "Inserting into database", true, true);
		new performQuery().execute("1");
	}
	
	private void doCheck(String num)
	{
		String[] entities = {"USERNAME"};
		String filename = "testing";
		String[] dataTypes = {"string"};
		String query = "select * from Players where USERNAME = '"+username.getText().toString().trim()+"'";
				
		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...", "Checking with database", true, true);
		new performQuery().execute(num);
	}
	
	/*
	 * A class which will do the actual searching of the database in the background. This
	 * means that there will be no lag on the UI.
	 * 
	 * ****Must call ZombiesAndHumansBrain method setQueryVariables before calling this.****
	 */
	class performQuery extends AsyncTask<String, Integer, String>
	{
		boolean updated;
		@Override
		protected String doInBackground(String... parameters)
		{
			if(parameters[0].equals("0") || parameters[0].equals("2"))
				updated = brain.performQuery(true);
			else
				updated = brain.performQuery(false);
			
			return parameters[0];
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result.equals("1"))
			{
				if(updated)
					Toast.makeText(SignUpController.this, "Updating worked",
						Toast.LENGTH_LONG).show();
				else
					Toast.makeText(SignUpController.this, "Updating failed",
							Toast.LENGTH_LONG).show();
				pd.dismiss();
				doCheck("2");
			}
			else if(result.equals("0"))
			{
				if(updated)
				{
					if(brain.getSearchResults().length > 0 && brain.getSearchResults()[0][0] instanceof String && !((String)brain.getSearchResults()[0][0]).equals("NO RESULTS"))
					{
						Toast.makeText(SignUpController.this, "User is already in database",
								Toast.LENGTH_LONG).show();

						//pd.dismiss();
					}
					else
					{
						Toast.makeText(SignUpController.this, "Username not in database.",
								Toast.LENGTH_LONG).show();
						
						pd.dismiss();
						doInsert();
						//Intent i = new Intent(SignUpController.this, HomeScreenController.class);
						//startActivity(i);
					}
				}
				else
				{
					Toast.makeText(SignUpController.this, "Error when trying to get data",
							Toast.LENGTH_LONG).show();
				}
				pd.dismiss();
				
				/*
				if(!(brain.getSearchResults() == null) && !brain.getSearchResults()[0][0].equals("error"))
				{
					if(brain.getSearchResults().length > 0)
					{
						Toast.makeText(SignUpController.this, "User is now successfully in database",
								Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(SignUpController.this, "Username not in database. Error happened",
								Toast.LENGTH_LONG).show();
						
						//Intent i = new Intent(SignUpController.this, HomeScreenController.class);
						//startActivity(i);
					}
				}
				else if(brain.getSearchResults() == null)
				{
					Toast.makeText(SignUpController.this, "Username not in database.",
							Toast.LENGTH_LONG).show();
					
					//Intent i = new Intent(SignUpController.this, HomeScreenController.class);
					//startActivity(i);
				}
				else
				{
					Toast.makeText(SignUpController.this, "Error when trying to get data from database",
							Toast.LENGTH_LONG).show();
				}
				pd.dismiss();
				*/
			}
			else if(result.equals("2"))
			{
				if(updated)
				{
					if(brain.getSearchResults().length > 0)
					{
						Toast.makeText(SignUpController.this, "User is now successfully in database",
								Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(SignUpController.this, "Username not in database. Error happened",
								Toast.LENGTH_LONG).show();
						
						//Intent i = new Intent(SignUpController.this, HomeScreenController.class);
						//startActivity(i);
					}
				}
				else
				{
					Toast.makeText(SignUpController.this, "Error when trying to get data from database",
							Toast.LENGTH_LONG).show();
				}
				pd.dismiss();
			}				
			
		}
	}
}

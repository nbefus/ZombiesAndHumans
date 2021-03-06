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

public class StartScreenController extends Activity implements OnClickListener
{

	public static final String		SELF	= "self";
	private ZombiesAndHumansBrain	brain	= new ZombiesAndHumansBrain(this);
	private EditText				username;
	private EditText				password;
	private ProgressDialog			pd;

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

	public void onClick(View v)
	{
		if (v.getId() == R.id.Button_LogIn)
		{
			String[] entities = { "playerid", "computerplayer", "username",
					"password", "locationx", "locationy", "safehousex",
					"safehousey", "backpackid", "characterid", "characterid",
					"cname", "clevel", "health", "strength", "defense",
					"accuracy", "evasion", "tokens" };
			String filename = "testing";
			String[] dataTypes = { "int", "string", "string", "string",
					"double", "double", "double", "double", "int", "int",
					"int", "string", "int", "int", "int", "int", "int", "int","int" };
			String query = "select * from player p join `character` c on p.characterid = c.characterid where username = '"
					+ username.getText().toString().trim()
					+ "' AND password = '"
					+ password.getText().toString().trim() + "'";

			brain.prepareForQuery(entities, filename, dataTypes, query);
			pd = ProgressDialog.show(this, "Processing...", "Trying to log in",
					true, true);
			new performQuery().execute();
		}
		else if (v.getId() == R.id.Button_SignUp)
		{
			Intent j = new Intent(StartScreenController.this,
					SignUpController.class);
			startActivity(j);
		}
	}

	class performQuery extends AsyncTask<String, Integer, String>
	{
		private boolean	errorFree;

		@Override
		protected String doInBackground(String... parameters)
		{
			errorFree = brain.performQuery(true);

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
			if (errorFree)
			{
				if (brain.getSearchResults().length > 0
						&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
								.getSearchResults()[0][0]).equals("NO RESULTS")))
				{
					Toast.makeText(StartScreenController.this,
							"User known" + brain.getSearchResults()[0][0],
							Toast.LENGTH_LONG).show();

					if (((String) brain.getSearchResults()[0][2])
							.equals("DBAdmin")
							&& ((String) brain.getSearchResults()[0][3])
									.equals("DBAdminPassword"))
					{
						Intent i = new Intent(StartScreenController.this,
								DBAdminController.class);
						startActivity(i);
					}
					else
					{
						Object[][] results = brain.getSearchResults();
						Player self = new Player(
								((Integer) results[0][0]).intValue(),
								((String) results[0][1]).charAt(0),
								(String) results[0][2], (String) results[0][3],
								((Double) results[0][4]).doubleValue(),
								((Double) results[0][5]).doubleValue(),
								((Double) results[0][6]).doubleValue(),
								((Double) results[0][7]).doubleValue(),
								((Integer) results[0][8]).intValue(),
								((Integer) results[0][9]).intValue());
						brain.setSelf(self);
						Character character = new Character(
								((Integer) results[0][10]).intValue(),
								((String) results[0][11]),
								((Integer) results[0][12]).intValue(),
								((Integer) results[0][13]).intValue(),
								((Integer) results[0][14]).intValue(),
								((Integer) results[0][15]).intValue(),
								((Integer) results[0][16]).intValue(),
								((Integer) results[0][17]).intValue(),
								((Integer) results[0][18]).intValue()
								);
						brain.setCharacter(character);

						Intent i = new Intent(StartScreenController.this,
								HomeScreenController.class);
						i.putExtra("self", brain.getSelf());
						i.putExtra("char", brain.getCharacter());

						startActivity(i);
					}
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
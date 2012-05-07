package com.androidbeef.zombiesandhumans;

import java.util.Calendar;

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
	public static final String		SELF	= "self";

	private ZombiesAndHumansBrain	brain	= new ZombiesAndHumansBrain(this);

	private EditText				username;
	private EditText				password;
	private ProgressDialog			pd;
	private int						numOfPlayers, numOfBackpacks,numOfCharacters;

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
		if (v.getId() == R.id.Button_Cancel)
		{
			this.finish();
		}
		else if (v.getId() == R.id.Button_SignUpNow)
		{
			doCheck("Unique User Check");
		}
	}

	private void databaseCount()
	{
		String[] entities = { "COUNT(playerid)", "COUNT(b.backpackid)",
				"COUNT(c.characterid)" };
		String filename = "testing";
		String[] dataTypes = { "string", "string", "string" };
		String query6 = "SELECT COUNT(playerid), COUNT(b.backpackid), COUNT(c.characterid) FROM `backpack` b RIGHT JOIN player p ON b.backpackid = p.backpackid LEFT JOIN `character` c ON c.characterid = p.characterid";
		brain.prepareForQuery(entities, filename, dataTypes, query6);
		new performQuery().execute("Count Users");
	}

	private void doInsert()
	{
		String filename = "testing";

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String date = "";

		date += year;

		if (month < 10)
			date += "-0" + (month + 1);
		else
			date += "-" + (month + 1);

		if (day < 10)
			date += "-0" + day;
		else
			date += "-" + day;

		int randomx = (int) Math.random()*5;
		int randomy = (int) Math.random()*5;
		String query6 = "INSERT INTO `player`(`playerid`,`computerplayer`,`username`,`password`,`datejoined`,`locationx`,`locationy`,`safehousex`,`safehousey`,`backpackid`,`characterid`) VALUES"
				+ " ("
				+ (numOfPlayers + 1)
				+ ",'n', '"
				+ username.getText().toString().trim()
				+ "', '"
				+ password.getText().toString().trim()
				+ "','"
				+ date
				+ "',21.5"+randomx+"00,-157.8"+randomy+"00,21.5"+randomx+"00,-157.8"+randomy+"00,"
				+ (numOfBackpacks + 1) + "," + (numOfCharacters + 1) + ")";

		brain.prepareForQuery(null, filename, null, query6);
		new performQuery().execute("Insert User");
	}

	private void doCheck(String what)
	{
		String[] entities = { "playerid", "computerplayer", "username",
				"password", "locationx", "locationy", "safehousex",
				"safehousey", "backpackid", "characterid" };
		String filename = "testing";
		String[] dataTypes = { "int", "string", "string", "string", "double",
				"double", "double", "double", "int", "int" };
		String query = "select * from player where username = '"
				+ username.getText().toString().trim() + "'";

		brain.prepareForQuery(entities, filename, dataTypes, query);
		if (what.equals("Unique User Check"))
			pd = ProgressDialog.show(this, "Processing...",
					"Checking with database", true, true);
		new performQuery().execute(what);
	}

	private void newBackpack()
	{
		String filename = "testing";
		String query6 = "INSERT INTO `backpack`(`backpackid`,`itemweightcount`,`capacity`) VALUES"
				+ " (" + (numOfBackpacks + 1) + ",50,100)";

		brain.prepareForQuery(null, filename, null, query6);
		new performQuery().execute("Insert Backpack");
	}

	private void newBackpackItems(int itemid, int inbackpackcount, int instoragecount)
	{
		String filename = "testing";

		String query6 = "INSERT INTO `backpackitems`(`backpackid`,`itemid`,`inbackpackcount`,`instoragecount`) VALUES"
				+ " ("
				+ (numOfBackpacks + 1)
				+ ","
				+ itemid
				+ ","
				+ inbackpackcount
				+ ","+instoragecount+")";

		brain.prepareForQuery(null, filename, null, query6);
		new performQuery().execute("Insert Item " + itemid + " Into Backpack");
	}

	private void newCharacter()
	{
		String filename = "testing";
		String query6 = "INSERT INTO `character`(`characterid`,`cname`,`clevel`,`health`,`strength`,`defense`,`accuracy`,`evasion`) VALUES"
				+ " (" + (numOfCharacters + 1) + ",'Unamed',1,1,1,1,1,1)";

		Character character = new Character((numOfCharacters + 1), "Unamed", 1,
				1, 1, 1, 1, 1);
		brain.setCharacter(character);

		brain.prepareForQuery(null, filename, null, query6);
		new performQuery().execute("Insert Character");
	}

	class performQuery extends AsyncTask<String, Integer, String>
	{
		boolean	updated;

		@Override
		protected String doInBackground(String... parameters)
		{
			if (parameters[0].equals("Unique User Check")
					|| parameters[0].equals("Insert Check")
					|| parameters[0].equals("Count Users"))
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
			if (result.equals("Insert User"))
			{
				if (updated)
				{
					Toast.makeText(SignUpController.this, "Updating worked",
							Toast.LENGTH_LONG).show();

					newBackpack();
				}

				else
					Toast.makeText(SignUpController.this, "Updating failed",
							Toast.LENGTH_LONG).show();

			}
			else if (result.equals("Insert Item 1 Into Backpack")
					|| result.equals("Insert Item 2 Into Backpack")
					|| result.equals("Insert Item 5 Into Backpack"))
			{
				if (result.equals("Insert Item 1 Into Backpack"))
					newBackpackItems(2, 2,2);
				else if (result.equals("Insert Item 2 Into Backpack"))
					newBackpackItems(5, 1,0);
				else
					newCharacter();
			}
			else if (result.equals("Insert Character"))
			{
				if (updated)
				{
					doCheck("Insert Check");
				}
			}
			else if (result.equals("Insert Backpack"))
			{
				if (updated)
				{
					newBackpackItems(1, 6, 2);
				}
			}
			else if (result.equals("Count Users"))
			{
				if (updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{
						numOfPlayers = Integer.parseInt((String) brain
								.getSearchResults()[0][0]);
						numOfCharacters = Integer.parseInt((String) brain
								.getSearchResults()[0][2]);
						numOfBackpacks = Integer.parseInt((String) brain
								.getSearchResults()[0][1]);

						doInsert();
					}
					else
					{
						numOfPlayers = 0;
						Toast.makeText(SignUpController.this,
								"Error with number of users", Toast.LENGTH_LONG)
								.show();
					}
				}
				else
					Toast.makeText(SignUpController.this, "Updated is false",
							Toast.LENGTH_LONG).show();
			}
			else if (result.equals("Unique User Check")
					|| result.equals("Insert Check"))
			{
				if (updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{
						if (result.equals("Unique User Check"))
							Toast.makeText(SignUpController.this,
									"User is already in database",
									Toast.LENGTH_LONG).show();
						else
						{
							Object[][] results = brain.getSearchResults();
							Player self = new Player(
									((Integer) results[0][8]).intValue(),
									((String) results[0][1]).charAt(0),
									(String) results[0][2],
									(String) results[0][3],
									((Double) results[0][4]).doubleValue(),
									((Double) results[0][5]).doubleValue(),
									((Double) results[0][6]).doubleValue(),
									((Double) results[0][7]).doubleValue(),
									((Integer) results[0][8]).intValue(),
									((Integer) results[0][9]).intValue());
							brain.setSelf(self);

							Toast.makeText(SignUpController.this,
									"User is now successfully in database",
									Toast.LENGTH_LONG).show();
							pd.dismiss();
							Intent i = new Intent(SignUpController.this,
									HomeScreenController.class);
							i.putExtra("self", brain.getSelf());
							i.putExtra("char", brain.getCharacter());
							startActivity(i);
						}
					}
					else
					{
						if (result.equals("Unique User Check"))
						{
							Toast.makeText(SignUpController.this,
									"Username not in database.",
									Toast.LENGTH_LONG).show();
							databaseCount();
						}
						else
							Toast.makeText(
									SignUpController.this,
									"Username not in database. Error happened "
											+ result, Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(SignUpController.this,
							"Error when trying to get data", Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}
}

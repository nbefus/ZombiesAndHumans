package com.androidbeef.zombiesandhumans;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DBAdminController extends Activity implements OnClickListener
{
	private ZombiesAndHumansBrain	brain		= new ZombiesAndHumansBrain(
														this);
	private ProgressDialog			pd;
	private TableLayout				tl;
	private EditText				query;
	private static final String[]	KEYWORDS	= new String[] { "select",
			"from", "datatypes", "string", "int", "players", "characters",
			"abilities", "characterabilities", "item", "backpack",
			"backpackitems", "abilityid", "aname", "effect",
			"alevelrestriction", "alevel", "multiplierperlevel", "backpackid",
			"itemweightcount", "capacity", "itemid", "characterid", "cname",
			"clevel", "health", "strength", "defense", "accuracy", "evasion",
			"ability", "weight", "cooldown", "capturetime", "numofuses",
			"ilevelrestriction", "playerid", "computerplayer", "username",
			"password", "datejoined", "locationx", "locationy", "safehousex",
			"safehousey"						};

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbadmin);
		query = (EditText) findViewById(R.id.edit);
		tl = (TableLayout) findViewById(R.id.tableLayoutwork);
		((Button) findViewById(R.id.Button_DBAdminSearch))
				.setOnClickListener(this);
		tl.setBackgroundColor(Color.RED);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, KEYWORDS);
		MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.edit);
		textView.setAdapter(adapter);
		textView.setTokenizer(new SpaceTokenizer());
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.Button_DBAdminSearch)
		{
			String search = query.getText().toString().trim();
			String Esubstring = search.substring(7, search.indexOf("from"));
			Esubstring = Esubstring.replace(" ", "");
			String[] entities = Esubstring.split(",");
			String Dsubstring = search.substring(
					search.indexOf("datatypes") + 9, search.length());
			Dsubstring = Dsubstring.replace(" ", "");
			String[] dataTypes = Dsubstring.split(",");

			for (int i = 0; i < entities.length; i++)
			{
				System.out.println(":" + entities[i] + ":");
			}
			for (int i = 0; i < dataTypes.length; i++)
			{
				System.out.println(":" + dataTypes[i] + ":");
			}

			// String[] entities = {"USERNAME","PASSWORD"};
			String filename = "testing";
			// String[] dataTypes = {"string","string"};
			String query = (search.substring(0, search.indexOf("datatypes"))
					.trim());
			System.out.println(":" + query + ":");
			brain.setQueryVariables(entities, filename, dataTypes, query);
			pd = ProgressDialog.show(this, "Processing...",
					"Getting data from database", true, true);
			new performQuery().execute();
		}
	}

	private void addRowToTable(String[] info)
	{
		TableRow tableRow = new TableRow(this);
		tableRow.setBackgroundColor(Color.BLACK);
		tableRow.setPadding(0, 0, 0, 2);

		TableRow.LayoutParams llp = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(2, 2, 2, 2);
		// llp.gravity = Gravity.CENTER;
		// TableLayout.LayoutParams tlParams = new TableLayout.LayoutParams();

		for (int i = 0; i < info.length; i++)
		{
			// TableRow.LayoutParams trParams = new TableRow.LayoutParams();
			// trParams.span = 5;
			LinearLayout cell = new LinearLayout(this);

			TextView title = new TextView(this);
			title.setText(info[i]);
			title.setPadding(0, 0, 0, 3);
			title.setTextColor(Color.BLACK);
			// title.setGravity(Gravity.CENTER);

			cell.setLayoutParams(llp);
			cell.setBackgroundColor(Color.WHITE);
			// title.setBackgroundColor(Color.BLUE);
			cell.addView(title);
			// title.setTextSize(60);
			// title.setLayoutParams(trParams);
			tableRow.addView(cell);
		}
		tl.addView(tableRow);
		tl.invalidate();
	}

	class performQuery extends AsyncTask<String, Integer, String>
	{
		private Object[][]	results;

		@Override
		protected String doInBackground(String... parameters)
		{
			results = brain.performQueryWithResult();

			if(results == null)
				System.out.println("NULLL");
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
			/*
			for(int i=0; i<results.length; i++)
				for(int j=0; j<results[i].length; j++)
					System.out.println((String)results[i][j]);
					*/
			if(results == null)
				System.out.println("NULLL bad");
			if (!(results == null))
			{
				if (results.length > 0)
				{
					String[] theStuff;
					Toast.makeText(DBAdminController.this, "User known",
							Toast.LENGTH_LONG).show();

					for (int i = 0; i < results.length; i++)
					{
						theStuff = new String[results.length];

						for (int j = 0; j < results[i].length; j++)
						{
							theStuff[j] = (String) results[i][j];
							System.out.println(":" + theStuff[j] + ":");
						}

						addRowToTable(theStuff);
					}
				}
				else
				{
					Toast.makeText(DBAdminController.this, "User unknown!",
							Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				Toast.makeText(DBAdminController.this, "User unknown!! null",
						Toast.LENGTH_LONG).show();
			}
			pd.dismiss();

		}
	}

	class SpaceTokenizer implements Tokenizer
	{

		public int findTokenStart(CharSequence text, int cursor)
		{
			int i = cursor;

			while (i > 0 && text.charAt(i - 1) != ' ')
			{
				i--;
			}
			while (i < cursor && text.charAt(i) == ' ')
			{
				i++;
			}

			return i;
		}

		public int findTokenEnd(CharSequence text, int cursor)
		{
			int i = cursor;
			int len = text.length();

			while (i < len)
			{
				if (text.charAt(i) == ' ')
				{
					return i;
				}
				else
				{
					i++;
				}
			}

			return len;
		}

		public CharSequence terminateToken(CharSequence text)
		{
			int i = text.length();

			while (i > 0 && text.charAt(i - 1) == ' ')
			{
				i--;
			}

			if (i > 0 && text.charAt(i - 1) == ' ')
			{
				return text;
			}
			else
			{
				if (text instanceof Spanned)
				{
					SpannableString sp = new SpannableString(text + " ");
					TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
							Object.class, sp, 0);
					return sp;
				}
				else
				{
					return text;
				}
			}
		}
	}

}
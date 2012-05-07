package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.Spinner;
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
	private int[] 					tablesToBeDescribed;

	/*
	 * private static final String[] KEYWORDS = new String[] { "select", "from",
	 * "datatypes", "string", "int", "player", "character", "abilitie",
	 * "characterabilities", "item", "backpack", "backpackitems", "abilityid",
	 * "aname", "effect", "alevelrestriction", "alevel", "multiplierperlevel",
	 * "backpackid", "itemweightcount", "capacity", "itemid", "characterid",
	 * "cname", "clevel", "health", "strength", "defense", "accuracy",
	 * "evasion", "ability", "weight", "cooldown", "capturetime", "numofuses",
	 * "ilevelrestriction", "playerid", "computerplayer", "username",
	 * "password", "datejoined", "locationx", "locationy", "safehousex",
	 * "safehousey" };
	 */

	private boolean					noChange;
	private String					table;
	private HashMap<String, String>	dtoa;
	private Set<String>[]			ta;
	private Spinner					s;
	private String[][]				tableStructure;
	private ArrayList<String[][]>   finalTableStructure;
	private String					html;
	private String					mime		= "text/html";
	private String					encoding	= "utf-8";
	private WebView					myWebView;
	private JSInterface				myJSInterface;
	private int						deleteOrEdit;
	private String[]				relations;
	private boolean[]	checkedList = {true,false,false,false,false,false,false};

	public class JSInterface
	{

		private WebView	mAppView;
		private String	selId;
		private int		selPos;

		public JSInterface(WebView appView)
		{
			this.mAppView = appView;
		}

		public void showDialog(String id, int pos)
		{
			selPos = pos;
			selId = id;
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					DBAdminController.this);

			dialog.setTitle("What do you want to do?");

			dialog.setSingleChoiceItems(R.array.Options, 0,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							deleteOrEdit = which;
							Toast.makeText(DBAdminController.this,
									"Selected" + which, Toast.LENGTH_LONG)
									.show();
						}
					});

			dialog.setCancelable(false);

			dialog.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							Toast.makeText(DBAdminController.this,
									"Selected" + deleteOrEdit,
									Toast.LENGTH_LONG).show();

							if (deleteOrEdit == 0)
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(
										DBAdminController.this);

								alert.setTitle("Title");
								alert.setMessage("Message");

								final EditText input = new EditText(
										DBAdminController.this);

								InputFilter[] FilterArray = new InputFilter[1];
								FilterArray[0] = new InputFilter.LengthFilter(
										Integer.parseInt(tableStructure[selPos][3]
												.trim()));
								input.setFilters(FilterArray);

								alert.setView(input);

								alert.setPositiveButton("Ok",
										new DialogInterface.OnClickListener()
										{
											public void onClick(
													DialogInterface dialog,
													int whichButton)
											{
												String value = input.getText()
														.toString();
											}
										});

								alert.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener()
										{
											public void onClick(
													DialogInterface dialog,
													int whichButton)
											{

											}
										});

								alert.show();
							}

						}

					});
			dialog.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{

							dialog.cancel();
						}
					});

			dialog.show();
		}

		public void showEditTextDialog()
		{

		}

		public void editDialog(String echo)
		{

			String filename = "testing";

			// String query6 =
			// "UPDATE `"+table+"` SET "+tableStructure[selPos][0]+"="

			// brain.prepareForQuery(null, filename, null, query6);
			// new performQuery().execute("Insert Item " + itemid +
			// " Into Backpack");

		}
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbadmin);
		relations = new String[7];
		relations[0] = "ability";
		relations[1] = "characterabilities";
		relations[2] = "character";
		relations[3] = "player";
		relations[4] = "backpack";
		relations[5] = "backpackitems";
		relations[6] = "item";
		dtoa = new HashMap<String, String>();
		ta = new Set[5];
		for (int i = 0; i < ta.length; i++)
			ta[i] = new HashSet<String>();

		// query = (EditText) findViewById(R.id.edit);

		// ///tl = (TableLayout) findViewById(R.id.tableLayoutwork);
		((Button) findViewById(R.id.Button_DBAdminTableSelection))
		 .setOnClickListener(this);
		//s = (Spinner) findViewById(R.id.spinner1);
		// ////tl.setBackgroundColor(Color.RED);
		//setUpSpinner();

		// String temp = "<html><body>Hello, World!</body></html>";

		myWebView = (WebView) this.findViewById(R.id.webView1);
		myJSInterface = new JSInterface(myWebView);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.addJavascriptInterface(myJSInterface, "Android");

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_dropdown_item_1line, KEYWORDS);
		// MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView)
		// findViewById(R.id.edit);
		// textView.setAdapter(adapter);
		// textView.setTokenizer(new SpaceTokenizer());
	}
	
	private String findTableFromInt(int pos)
	{
		String table;
		if(pos == 0)
			table = "ability";
		else if(pos == 1)
			table = "characterabilities";
		else if(pos == 2)
			table = "character";
		else if(pos == 3)
			table = "player";
		else if(pos == 4)
			table = "backpack";
		else if(pos == 5)
			table = "backpackitems";
		else
			table = "item";
		
		return table;
	}

	private void describeTable(String table)
	{
		String[] entities = { "Field", "Type", "Null", "Key", "Default",
				"Extra" };
		String filename = "testing";
		String[] dataTypes = { "string", "string", "string", "string",
				"string", "string" };
		String query = "describe `" + table.trim() + "`";

		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...",
				"Checking with database", true, true);
		new performQuery().execute("Describe", table);
	}

	private void describeMultipleTables(String table, int num, int pos)
	{
		String[] entities = { "Field", "Type", "Null", "Key", "Default",
				"Extra" };
		String filename = "testing";
		String[] dataTypes = { "string", "string", "string", "string",
				"string", "string" };
		String query = "describe `" + table.trim() + "`";

		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...",
				"Checking with database", true, true);
		new performQuery().execute("Describe Table", num+"",pos+"");
	}
	
	private void getStarFromTable(String table)
	{
		String[] entities = new String[tableStructure.length];
		String filename = "testing";
		String[] dataTypes = new String[tableStructure.length];

		for (int i = 0; i < entities.length; i++)
		{
			entities[i] = tableStructure[i][0];
			dataTypes[i] = tableStructure[i][1];
		}

		String query = "select * from `" + table + "`";

		// tl.removeAllViews();
		brain.prepareForQuery(entities, filename, dataTypes, query);
		// pd = ProgressDialog.show(this, "Processing...",
		// "Checking with database", true, true);
		new performQuery().execute("Select Star", table);
	}

	public void tableDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		noChange = true;
		dialog.setTitle("Table Selection");
		// dialog.setMessage(R.string.npMessage);

		dialog.setMultiChoiceItems(R.array.TableOptions, checkedList,
				new DialogInterface.OnMultiChoiceClickListener()
				{
					@Override
					public void onClick(DialogInterface d, int which,
							boolean checked)
					{
						noChange = false;
						Toast.makeText(DBAdminController.this,
								"Selected" + which + " is now "+checked, Toast.LENGTH_SHORT)
								.show();
						checkedList[which] = checked;
					}
					
				});

		dialog.setCancelable(false);

		dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(noChange)
					dialog.cancel();
				else
				{
					int count=0;
					for(int i=0; i<checkedList.length; i++)
					{
						if(checkedList[i])
						{
							count++;
						}
					}
					tablesToBeDescribed = new int[count];
					count =0;
					for(int i=0; i<checkedList.length; i++)
					{
						if(checkedList[i])
						{
							tablesToBeDescribed[count] = i;
							count++;
						}
					}
					if(tablesToBeDescribed.length > 0)
						describeMultipleTables(findTableFromInt(tablesToBeDescribed[0]),tablesToBeDescribed[0], 0);
				}
			}
		});
		
		/*
		dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

					dialog.cancel();
			}
		});
*/
		dialog.show();
	}

	/*
	public void setUpSpinner()
	{
		String[] tables = { "character", "ability", "backpackitems",
				"backpack", "player", "item", "characterabilities" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, tables); // new
																// ArrayList<String>());
		// ArrayAdapter<String> adapter = ArrayAdapter.createFromResource(
		// this,new ArrayList<String>(), android.R.layout.simple_spinner_item);
		// new ArrayAdapter<String>(this, R.layout.itemrow,
		// );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		s.setAdapter(adapter);

		s.setSelected(false);
		s.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				describeTable(((String) s.getAdapter().getItem(position))
						.trim());
				table = (String) s.getAdapter().getItem(position);
				System.out.println(position + " " + table);

			}

			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}
	*/

	private void setTA()
	{
		// 0 = ability
		// 1 = backpack
		// 2 = character
		// 3 = item
		// 4 = player
		// 5 = backpackitems
		// 6 = characterabilities
		ta[0].add("abilityid");
		ta[0].add("aname");
		ta[0].add("effect");
		ta[0].add("alevelrestriction");
		ta[0].add("alevel");
		ta[0].add("multiplierperlevel");
		ta[1].add("backpackid");
		ta[1].add("itemweightcount");
		ta[1].add("capacity");
		ta[2].add("characterid");
		ta[2].add("cname");
		ta[2].add("clevel");
		ta[2].add("health");
		ta[2].add("strength");
		ta[2].add("defense");
		ta[2].add("accuracy");
		ta[2].add("evasion");
		ta[3].add("itemid");
		ta[3].add("ability");
		ta[3].add("weight");
		ta[3].add("cooldown");
		ta[3].add("capturetime");
		ta[3].add("numofuses");
		ta[3].add("ilevelrestriction");
		ta[3].add("iname");
		ta[4].add("playerid");
		ta[4].add("computerplayer");
		ta[4].add("username");
		ta[4].add("password");
		ta[4].add("datejoined");
		ta[4].add("locationx");
		ta[4].add("locationy");
		ta[4].add("safehousex");
		ta[4].add("safehousey");
		ta[4].add("backpackid");
		ta[4].add("characterid");
		ta[5].add("backpackid");
		ta[5].add("itemid");
		ta[5].add("itemcount");
		ta[5].add("instorage");
		ta[6].add("characterid");
		ta[6].add("abilityid");
	}

	private void setDTOA()
	{
		dtoa.put("itemid", "int");
		dtoa.put("ability", "string");
		dtoa.put("weight", "int");
		dtoa.put("cooldown", "int");
		dtoa.put("capturetime", "int");
		dtoa.put("numofuses", "int");
		dtoa.put("ilevelrestiriction", "int");
		dtoa.put("iname", "string");
		dtoa.put("playerid", "int");
		dtoa.put("computerplayer", "string");
		dtoa.put("username", "string");
		dtoa.put("password", "string");
		dtoa.put("datejoined", "string");
		dtoa.put("locationx", "double");
		dtoa.put("locationy", "double");
		dtoa.put("safehousex", "double");
		dtoa.put("safehousey", "double");
		dtoa.put("backpackid", "int");
		dtoa.put("characterid", "int");
		dtoa.put("abilityid", "int");
		dtoa.put("cname", "string");
		dtoa.put("clevel", "int");
		dtoa.put("health", "int");
		dtoa.put("strength", "int");
		dtoa.put("defense", "int");
		dtoa.put("accuracy", "int");
		dtoa.put("evasion", "int");
		dtoa.put("itemcount", "int");
		dtoa.put("instorage", "string");
		dtoa.put("itemweightcount", "int");
		dtoa.put("capacity", "int");
		dtoa.put("aname", "string");
		dtoa.put("effect", "string");
		dtoa.put("alevelrestriction", "int");
		dtoa.put("alevel", "int");
		dtoa.put("multiplierperlevel", "int");
	}

	public void onClick(View v)
	{
		if(v.getId() == R.id.Button_DBAdminTableSelection)
		{
			tableDialog();
		}
		if (v.getId() == -1)
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
			brain.prepareForQuery(entities, filename, dataTypes, query);
			pd = ProgressDialog.show(this, "Processing...",
					"Getting data from database", true, true);
			new performQuery().execute();
		}
	}

	private void createHTMLTableRow(String[] info, boolean areTitles)
	{
		String tag, endTag;
		html += "<tr>";

		for (int i = 0; i < info.length; i++)
		{
			if (i != 0)
			{
				if (areTitles)
				{
					tag = "<th>";
					endTag = "</th>";
				}
				else
				{
					tag = "<td><a href=\"javascript:void(0)\" onclick=\"showDialog('"
							+ info[0] + "," + i + "')\"/>";
					endTag = "</a></td>";
				}
			}
			else
			{
				if (areTitles)
				{
					tag = "<th>";
					endTag = "</th>";
				}
				else
				{
					tag = "<td><a href=\"javascript:void(0)\" onclick=\"showDialog('"
							+ info[0] + "," + i + "')\"/>";
					endTag = "</a></td>";
				}
			}

			html += tag + info[i] + endTag;
		}
		html += "</tr>";
	}

	private void addRowToTable(String[] info, int textSize)
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
			title.setTextSize(textSize);
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
		boolean	updated;
		String	table;
		int count;

		@Override
		protected String doInBackground(String... parameters)
		{
			if (parameters[0].equals("Describe")
					|| parameters[0].equals("Select Star") || parameters[0].equals("Describe Tables"))
				updated = brain.performQuery(true);
			else
				updated = brain.performQuery(false);

			System.out.println(parameters[0] + " on " + parameters[1] + updated);
			if(parameters[0].equals("Describe Tables"))
				return parameters[0]+" "+parameters[1] + " "+parameters[2];
			table = parameters[1];
			return parameters[0];
		}
		
		private String findTableByInt(int pos)
		{
			String table;
			if(pos == 0)
				table = "ability";
			else if(pos == 1)
				table = "characterabilities";
			else if(pos == 2)
				table = "character";
			else if(pos == 3)
				table = "player";
			else if(pos == 4)
				table = "backpack";
			else if(pos == 5)
				table = "backpackitems";
			else
				table = "item";
			return table;
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result.contains("Describe Tables"))
			{
				if(updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{
						int pos = Integer.parseInt(result.split(" ")[2].trim());
						
						int describePos = Integer.parseInt(result.split(" ")[3].trim());
						
						String table = findTableByInt(pos);
						
						doTableStructure();
						
						finalTableStructure.add(tableStructure);
						
						if(describePos < tablesToBeDescribed.length-1)
							describeMultipleTables(table, tablesToBeDescribed[describePos+1],describePos+1);
					}
				}
			}
			else if (result.equals("Describe"))
			{
				if (updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{

						doTableStructure();

						getStarFromTable(table);
					}
				}

			}
			else if (result.equals("Select Star"))
			{
				if (updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{
						html = "<html><head><script type=\"text/javascript\">function showDialog(id,pos) {Android.showDialog(id,pos);}</script><body><table border=\"1\">";

						String[] theStuff;
						theStuff = new String[tableStructure.length];
						for (int i = 0; i < theStuff.length; i++)
						{
							theStuff[i] = tableStructure[i][0];
						}
						createHTMLTableRow(theStuff, true);
						for (int i = 0; i < brain.getSearchResults().length; i++)
						{
							theStuff = new String[brain.getSearchResults()[i].length];

							for (int j = 0; j < brain.getSearchResults()[i].length; j++)
							{
								if (brain.getSearchResults()[i][j] instanceof String)
									theStuff[j] = (String) brain
											.getSearchResults()[i][j];
								else
									theStuff[j] = ""
											+ brain.getSearchResults()[i][j];
								System.out.println(":" + theStuff[j] + ":");
							}

							createHTMLTableRow(theStuff, false);

							/*
							 * String[] theStuff; theStuff = new
							 * String[tableStructure.length]; for(int i=0; i<
							 * theStuff.length; i++) { theStuff[i] =
							 * tableStructure[i][0]; } addRowToTable(theStuff,
							 * 45); for(int i=0;
							 * i<brain.getSearchResults().length; i++) {
							 * theStuff = new
							 * String[brain.getSearchResults()[i].length];
							 * 
							 * for (int j = 0; j <
							 * brain.getSearchResults()[i].length; j++) {
							 * if(brain.getSearchResults()[i][j] instanceof
							 * String) theStuff[j] = (String)
							 * brain.getSearchResults()[i][j]; else theStuff[j]
							 * = ""+brain.getSearchResults()[i][j];
							 * System.out.println(":" + theStuff[j] + ":"); }
							 * 
							 * addRowToTable(theStuff,30);
							 */
						}
						html += "</table></body></html>";
						myWebView.loadDataWithBaseURL(null, html, mime,
								encoding, null);
					}
				}
				pd.dismiss();
			}

		}
		
		public void doTableStructure()
		{
			Object[][] dbResults = brain.getSearchResults();
			tableStructure = new String[dbResults.length][4];
			for (int i = 0; i < dbResults.length; i++)
			{
				String field = (String) dbResults[i][0];
				String type = (String) dbResults[i][1];

				if (type.contains("int"))
				{
					String substring = type.substring(
							type.indexOf("(") + 1,
							type.indexOf(")"));
					tableStructure[i][1] = "int";
					tableStructure[i][2] = "int";
					tableStructure[i][3] = substring.trim();
				}
				else if (type.contains("varchar"))
				{
					String substring = type.substring(
							type.indexOf("(") + 1,
							type.indexOf(")"));
					tableStructure[i][1] = "string";
					tableStructure[i][2] = "varchar";
					tableStructure[i][3] = substring.trim();

				}
				else if (type.contains("char"))
				{
					String substring = type.substring(
							type.indexOf("(") + 1,
							type.indexOf(")"));
					tableStructure[i][1] = "string";
					tableStructure[i][2] = "char";
					tableStructure[i][3] = substring.trim();
				}
				else if (type.contains("date"))
				{
					tableStructure[i][1] = "string";
					tableStructure[i][2] = "date";
					tableStructure[i][3] = "0";
				}
				else if (type.contains("decimal"))
				{
					String substring = type.substring(
							type.indexOf("(") + 1,
							type.indexOf(")"));
					tableStructure[i][1] = "double";
					tableStructure[i][2] = "decimal";
					tableStructure[i][3] = substring.trim();
				}
				tableStructure[i][0] = field;
			}
		}
	}

	class performQueryOld extends AsyncTask<String, Integer, String>
	{
		private boolean	freeOfErrors;

		@Override
		protected String doInBackground(String... parameters)
		{
			freeOfErrors = brain.performQuery(true);

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
			if (freeOfErrors)
			{
				String[] theStuff;
				Toast.makeText(DBAdminController.this, "User known",
						Toast.LENGTH_LONG).show();

				for (int i = 0; i < brain.getSearchResults().length; i++)
				{
					theStuff = new String[brain.getSearchResults().length];

					for (int j = 0; j < brain.getSearchResults()[i].length; j++)
					{
						theStuff[j] = (String) brain.getSearchResults()[i][j];
						System.out.println(":" + theStuff[j] + ":");
					}

					addRowToTable(theStuff, 30);
				}
			}
			else
			{
				Toast.makeText(DBAdminController.this, "User unknown!",
						Toast.LENGTH_LONG).show();
			}

			/*
			 * if(brain.getSearchResults() == null)
			 * System.out.println("NULLL bad"); if (!(brain.getSearchResults()
			 * == null)) { if (brain.getSearchResults().length > 0) { String[]
			 * theStuff; Toast.makeText(DBAdminController.this, "User known",
			 * Toast.LENGTH_LONG).show();
			 * 
			 * for (int i = 0; i < brain.getSearchResults().length; i++) {
			 * theStuff = new String[brain.getSearchResults().length];
			 * 
			 * for (int j = 0; j < brain.getSearchResults()[i].length; j++) {
			 * theStuff[j] = (String) brain.getSearchResults()[i][j];
			 * System.out.println(":" + theStuff[j] + ":"); }
			 * 
			 * addRowToTable(theStuff); } } else {
			 * Toast.makeText(DBAdminController.this, "User unknown!",
			 * Toast.LENGTH_LONG).show(); } } else {
			 * Toast.makeText(DBAdminController.this, "User unknown!! null",
			 * Toast.LENGTH_LONG).show(); }
			 */
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
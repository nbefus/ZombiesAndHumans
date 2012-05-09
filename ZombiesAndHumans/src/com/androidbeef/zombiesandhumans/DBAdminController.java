package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DBAdminController extends Activity implements OnClickListener
{

	private ZombiesAndHumansBrain	brain						= new ZombiesAndHumansBrain(
																		this);
	private ProgressDialog			pd;
	private EditText				query;
	private int[]					tablesToBeDescribed;
	private Dialog					dial;
	private Dialog					newRestriction;
	private ListView				restrictionListView;
	private String					operation;
	private String					selectedAttribute;
	private int						ascendOrDescend;
	private String					orderByString;
	private boolean					delete;
	private String[]				newOptions;
	private ArrayList<String>		restrictions				= new ArrayList<String>();
	private boolean					isEditable;
	private String[]				options;
	private boolean					noChange;
	private String					table;
	private String[][]				tableStructure;
	private ArrayList<String[][]>	finalTableStructure			= new ArrayList<String[][]>();
	private String					html;
	private String					mime						= "text/html";
	private String					encoding					= "utf-8";
	private WebView					myWebView;
	private JSInterface				myJSInterface;
	private int						deleteOrEdit;
	private boolean[]				checkedTableList			= { false,
			false, false, false, false, false, false			};
	private ArrayList<String>		checkedAttributeArrayList	= new ArrayList<String>();
	private boolean[]				checkedAttributeList;
	private String[][]				attributesAndDataTypes;
	private Button	tableButton;
	private Button	attributeButton;
	private Button	restrictionButton;

	public class JSInterface
	{
		private WebView	mAppView;

		public JSInterface(WebView appView)
		{
			this.mAppView = appView;
		}

		//When a player clicks on a cell
		public void showDialog(final String id, final int pos,
				final String pk1, final String pk2)
		{

			AlertDialog.Builder dialog = new AlertDialog.Builder(
					DBAdminController.this);

			dialog.setTitle("What do you want to do?");

			dialog.setSingleChoiceItems(R.array.Options, 0,
					new DialogInterface.OnClickListener()
					{
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
						public void onClick(DialogInterface dialog, int which)
						{
							Toast.makeText(DBAdminController.this,
									"Selected" + deleteOrEdit,
									Toast.LENGTH_LONG).show();

							//User chose to edit
							if (deleteOrEdit == 1)
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(
										DBAdminController.this);

								alert.setTitle("Edit");
								alert.setMessage("Enter the new value for this cell");

								final EditText input = new EditText(
										DBAdminController.this);

								input.setText(id);

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
												String parPk1 = pk1.trim();
												String parPk2 = pk2.trim();
												String parVal = value.trim();
												if (tableStructure[0][1].trim()
														.equals("string"))
													parPk1 = "'" + pk1.trim()
															+ "'";
												if (tableStructure[1][1].trim()
														.equals("string"))
													parPk2 = "'" + pk2.trim()
															+ "'";
												if (tableStructure[pos][1]
														.trim()
														.equals("string"))
													parVal = "'" + value + "'";

												update(tableStructure[pos][0]
														.trim(), parVal,
														tableStructure[0][0]
																.trim(),
														tableStructure[1][0]
																.trim(),
														parPk1, parPk2);
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
							//User chose to delete
							else if (deleteOrEdit == 2)
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(
										DBAdminController.this);

								alert.setTitle("Delete");

								alert.setMessage("Are you sure you want to delete this entire row?");

								alert.setPositiveButton("Yes",
										new DialogInterface.OnClickListener()
										{
											public void onClick(
													DialogInterface dialog,
													int whichButton)
											{
												String parPk1 = pk1.trim();
												String parPk2 = pk2.trim();
												if (tableStructure[0][1].trim()
														.equals("string"))
													parPk1 = "'" + pk1.trim()
															+ "'";
												if (tableStructure[1][1].trim()
														.equals("string"))
													parPk2 = "'" + pk2.trim()
															+ "'";
												delete(tableStructure[0][0]
														.trim(),
														tableStructure[1][0]
																.trim(),
														parPk1, parPk2);
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
							else
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(
										DBAdminController.this);

								alert.setTitle("Create");
								// alert.setMessage("Enter the new value for this cell");

								ScrollView sv = new ScrollView(
										DBAdminController.this);
								LinearLayout layout = new LinearLayout(
										DBAdminController.this);
								layout.setOrientation(LinearLayout.VERTICAL);
								int dates = 0;
								for (int i = 0; i < tableStructure.length; i++)
								{
									if (tableStructure[i][2].equals("date"))
										dates++;
								}

								final EditText[] inputs = new EditText[tableStructure.length
										- dates];
								final DatePicker[] dps = new DatePicker[dates];
								int dpCount = 0;
								int inputCount = 0;
								for (int i = 0; i < inputs.length + dps.length; i++)
								{

									if (tableStructure[i][1].equals("string"))
									{

										if (tableStructure[i][2].equals("date"))
										{
											dps[dpCount] = new DatePicker(
													DBAdminController.this);
											dpCount++;
										}
										else
										{
											inputs[inputCount] = new EditText(
													DBAdminController.this);
											inputs[inputCount]
													.setHint(tableStructure[i][0]);
											InputFilter[] FilterArray = new InputFilter[1];
											FilterArray[0] = new InputFilter.LengthFilter(
													Integer.parseInt(tableStructure[i][3]
															.trim()));
											inputCount++;
										}

									}
									else if (tableStructure[i][1].equals("int"))
									{
										System.out.println("INT");
										inputs[inputCount] = new EditText(
												DBAdminController.this);
										inputs[inputCount]
												.setHint(tableStructure[i][0]);
										InputFilter[] FilterArray = new InputFilter[1];
										inputs[inputCount]
												.setInputType(InputType.TYPE_CLASS_NUMBER);
										FilterArray[0] = new InputFilter.LengthFilter(
												Integer.parseInt(tableStructure[i][3]
														.trim()));
										inputCount++;
									}
									else if (tableStructure[i][1]
											.equals("double"))
									{
										inputs[inputCount] = new EditText(
												DBAdminController.this);
										inputs[inputCount]
												.setHint(tableStructure[i][0]);
										String[] split = tableStructure[i][3]
												.split(",");
										System.out.println("Error: "
												+ tableStructure[i][3] + " "
												+ tableStructure[i][2] + " "
												+ tableStructure[i][1] + " "
												+ tableStructure[i][0]);
										inputs[inputCount].setFilters(new InputFilter[] { new DecimalDigitsInputFilter(
												Integer.parseInt(split[0]
														.trim())
														- Integer
																.parseInt(split[1]
																		.trim())
														+ 2, Integer
														.parseInt(split[1]
																.trim())) });;
										inputCount++;
									}

									if (!tableStructure[i][2].equals("date"))
									{
										layout.addView(inputs[inputCount - 1]);
									}
									else
									{
										layout.addView(dps[dpCount - 1]);
									}

								}
								sv.addView(layout);
								alert.setView(sv);

								alert.setPositiveButton("Ok",
										new DialogInterface.OnClickListener()
										{
											public void onClick(
													DialogInterface dialog,
													int whichButton)
											{
												int dpCount = 0;
												int inputCount = 0;
												boolean isOk = true;
												String[] values = new String[tableStructure.length];
												for (int i = 0; i < tableStructure.length; i++)
												{
													String lengthOfType = tableStructure[i][3];
													if (tableStructure[i][2]
															.equalsIgnoreCase("date"))
													{
														int month = dps[dpCount]
																.getMonth();
														int year = dps[dpCount]
																.getYear();
														int day = dps[dpCount]
																.getDayOfMonth();

														String date = "";

														date += year;

														if (month < 10)
															date += "-0"
																	+ (month + 1);
														else
															date += "-"
																	+ (month + 1);

														if (day < 10)
															date += "-0" + day;
														else
															date += "-" + day;
														dpCount++;
														values[i] = "'" + date
																+ "'";
													}
													else if (tableStructure[i][1]
															.equalsIgnoreCase("double"))
													{
														String one = inputs[inputCount]
																.getText()
																.toString();
														String[] split = one
																.split("[.]");

														if (split[0].charAt(0) == '-')
															split[0] = split[0]
																	.substring(
																			1,
																			split[0].length());
														if (split.length > 1)
														{
															String[] split2 = lengthOfType
																	.split(",");
													
															if (split[0]
																	.length() <= Integer
																	.parseInt(split2[0])
																	&& split[1]
																			.length() <= Integer
																			.parseInt(split2[1]))
															{
																values[i] = one;
															}
															else
																isOk = false;
														}
														else
														{
															isOk = false;
														}
														inputCount++;
													
													}
													else if (tableStructure[i][1]
															.equalsIgnoreCase("int"))
													{
														String one = null;
													
														one = inputs[inputCount]
																.getText()
																.toString();
														inputCount++;
														values[i] = one;
													}
													else if (tableStructure[i][2]
															.equalsIgnoreCase("char")
															|| tableStructure[i][2]
																	.equalsIgnoreCase("varchar"))
													{
														String one = inputs[inputCount]
																.getText()
																.toString();
														if (tableStructure[i][2]
																.equalsIgnoreCase("char"))
														{
															if (lengthOfType.equals(one
																	.length()
																	+ ""))
															{
																values[i] = "'"
																		+ one
																		+ "'";
															}
															else
																isOk = false;
														}
														else
														{
															values[i] = "'"
																	+ one + "'";
														}
														inputCount++;
													}
												}
												if (isOk)
												{
													insert(values);
												}
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

						public void onClick(DialogInterface dialog, int which)
						{

							dialog.cancel();
						}
					});

			dialog.show();
		}
	}

	private void insert(String[] values)
	{
		String filename = "testing";
		String query6;
		String statement = "";
		for (int i = 0; i < values.length; i++)
		{
			statement += values[i] + ",";
		}

		statement = statement.substring(0, statement.length() - 1);

		query6 = "INSERT INTO `" + findTableFromInt(tablesToBeDescribed[0])
				+ "` VALUES (" + statement + ")";
		brain.prepareForQuery(null, filename, null, query6);
		System.out.println("INSERT QUERY: "+query6);
		pd = ProgressDialog.show(this, "Processing...",
				"Inserting into database", true, true);
		new performQuery().execute("Update");
	}

	private void update(String attribute, String newValue, String pk1,
			String pk2, String pk1Val, String pk2Val)
	{
		String filename = "testing";
		String query6;
		query6 = "UPDATE `" + findTableFromInt(tablesToBeDescribed[0])
				+ "` SET " + attribute + "=" + newValue + " WHERE " + pk1 + "="
				+ pk1Val + " AND " + pk2 + "=" + pk2Val;
		brain.prepareForQuery(null, filename, null, query6);
		System.out.println("UPDATE QUERY: "+query6);
		pd = ProgressDialog.show(this, "Processing...", "Updating database",
				true, true);
		new performQuery().execute("Update");
	}

	private void delete(String pk1, String pk2, String pk1Val, String pk2Val)
	{
		String filename = "testing";
		String query6;
		query6 = "DELETE FROM `" + findTableFromInt(tablesToBeDescribed[0])
				+ "` WHERE " + pk1 + "=" + pk1Val + " AND " + pk2 + "="
				+ pk2Val;
		brain.prepareForQuery(null, filename, null, query6);
		System.out.println("DELETE QUERY: "+query6);
		pd = ProgressDialog.show(this, "Processing...",
				"Deleting row from database", true, true);
		new performQuery().execute("Update");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbadmin);

		tableButton = ((Button) findViewById(R.id.Button_DBAdminTableSelection));
		tableButton.setOnClickListener(this);
		attributeButton = ((Button) findViewById(R.id.Button_DBAdminAttributeSelection));
		attributeButton.setOnClickListener(this);
		restrictionButton = ((Button) findViewById(R.id.Button_DBAdminRestrictionSelection));
		restrictionButton.setOnClickListener(this);

		attributeButton.setEnabled(false);
		restrictionButton.setEnabled(false);
		
		myWebView = (WebView) this.findViewById(R.id.webView1);
		myJSInterface = new JSInterface(myWebView);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.addJavascriptInterface(myJSInterface, "Android");
	}

	private String findTableFromInt(int pos)
	{
		String table;
		if (pos == 0)
			table = "ability";
		else if (pos == 1)
			table = "characterabilities";
		else if (pos == 2)
			table = "character";
		else if (pos == 3)
			table = "player";
		else if (pos == 4)
			table = "backpack";
		else if (pos == 5)
			table = "backpackitems";
		else
			table = "item";

		return table;
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
		// System.out.println("Describe " + table + " with table num: " + num
		// + " and pos in array: " + pos);
		new performQuery().execute("Describe Tables", num + "", pos + "");
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

	private void performTheBigQuery()
	{
		int numOfEntities = 0;

		for (int i = 0; i < finalTableStructure.size(); i++)
		{
			for (int j = 0; j < finalTableStructure.get(i).length; j++)
			{
				numOfEntities++;
			}
		}
		checkedAttributeList = new boolean[numOfEntities];
		String[] entities = new String[numOfEntities];
		String filename = "testing";
		String[] dataTypes = new String[numOfEntities];
		attributesAndDataTypes = new String[numOfEntities][7];
		int position = 0;
		String entitystring = "";

		for (int i = 0; i < finalTableStructure.size(); i++)
		{
			for (int j = 0; j < finalTableStructure.get(i).length; j++)
			{
				String[] line = finalTableStructure.get(i)[j];
				entities[position] = line[0];
				dataTypes[position] = line[1];

				entitystring += findTableFromInt(tablesToBeDescribed[i]) + "."
						+ line[0] + ",";
				attributesAndDataTypes[position][0] = findTableFromInt(tablesToBeDescribed[i])
						+ "." + line[0];
				attributesAndDataTypes[position][1] = line[1];
				attributesAndDataTypes[position][2] = line[2] + "(" + line[3]
						+ ")";
				attributesAndDataTypes[position][3] = "false";
				attributesAndDataTypes[position][4] = line[0];
				attributesAndDataTypes[position][5] = line[3];
				attributesAndDataTypes[position][6] = line[2];
				position++;
			}
		}

		if (entitystring.length() > 1)
			entitystring = entitystring.substring(0, entitystring.length() - 1);

		String tablestring = getTableString(entitystring);

		// String query = "select "+entitystring+" from `" + table + "`";

		String query = tablestring;
		 System.out.println("The big query is: " + query);

		// tl.removeAllViews();
		brain.prepareForQuery(entities, filename, dataTypes, query);
		// pd = ProgressDialog.show(this, "Processing...",
		// "Checking with database", true, true);
		new performQuery().execute("Big Query", table);
	}

	private void performTheBigQuery2(boolean useRestrictions)
	{
		int numOfEntities = 0;

		for (int i = 0; i < attributesAndDataTypes.length; i++)
		{
			if (attributesAndDataTypes[i][3].equals("true"))
				numOfEntities++;
		}
		String[] entities = new String[numOfEntities];
		String filename = "testing";
		String[] dataTypes = new String[numOfEntities];
		int position = 0;
		String entitystring = "";
		for (int i = 0; i < attributesAndDataTypes.length; i++)
		{
			if (attributesAndDataTypes[i][3].equals("true"))
			{
				entities[position] = attributesAndDataTypes[i][4].trim();
				dataTypes[position] = attributesAndDataTypes[i][1].trim();
				// System.out.println(entities[position]
				// + " enti and data ttype: " + dataTypes[position]);
				entitystring += attributesAndDataTypes[i][0].trim() + ",";
				position++;
			}
		}

		entitystring = entitystring.substring(0, entitystring.length() - 1);

		String tablestring = getTableString(entitystring);

		String query;
		if (!useRestrictions)
			query = tablestring;
		else
			query = tablestring + " " + makeRestrictionString();
	 System.out.println("The big query 2 is: " + query);

		brain.prepareForQuery(entities, filename, dataTypes, query);
		pd = ProgressDialog.show(this, "Processing...",
				"Checking with database", true, true);
		new performQuery().execute("Big Query 2", table);
	}

	private String getTableString(String attributestring)
	{
		int firstTable = tablesToBeDescribed[0];
		int lastTable = tablesToBeDescribed[tablesToBeDescribed.length - 1];
		String tablestring = "";

		for (int i = 0; i < lastTable - firstTable + 1; i++)
		{
			if (i == 0)
			{
				tablestring += "select " + attributestring + " from `"
						+ findTableFromInt(firstTable) + "`";
			}
			else
			{
				int currentTable = firstTable + i;
				if (currentTable == 1)
					tablestring += " join `characterabilities` on ability.abilityid = characterabilities.abilityid";
				else if (currentTable == 2)
					tablestring += " join `character` on character.characterid = characterabilities.characterid";
				else if (currentTable == 3)
					tablestring += " join `player` on player.characterid = character.characterid";
				else if (currentTable == 4)
					tablestring += " join `backpack` on backpack.backpackid = player.backpackid";
				else if (currentTable == 5)
					tablestring += " join `backpackitems` on backpackitems.backpackid = backpack.backpackid";
				else if (currentTable == 6)
					tablestring += " join `item` on item.itemid = backpackitems.itemid";
			}
		}

		return tablestring;
	}

	private String makeRestrictionString()
	{
		int countWhere = 0;
		String restrictionString = "";
		for (int i = 0; i < restrictions.size(); i++)
		{
			if (restrictions.get(i).contains("where"))
			{
				countWhere++;
				if (countWhere > 1)
				{
					String restrict = restrictions.get(i);
					restrictionString += " and "
							+ restrict.substring(6, restrict.length());
				}
				else
				{
					restrictionString += restrictions.get(i);
				}
			}
		}

		int countOrderBy = 0;
		boolean ascend = true;
		for (int i = 0; i < restrictions.size(); i++)
		{
			if (restrictions.get(i).contains("order by"))
			{
				String restrict = restrictions.get(i);
				countOrderBy++;
				if (countOrderBy > 1)
				{

					if (restrict.contains("desc"))
						ascend = false;

					restrict = restrict.split(" ")[2];

					restrictionString += ", " + restrict;
				}
				else
				{
					if (restrict.contains("desc"))
					{
						restrict = restrict.split(" ")[2];
						restrict = " order by " + restrict;
						restrictionString += restrict;
						ascend = false;
					}
					else
						restrictionString += restrictions.get(i);
				}
			}
		}
		if (!ascend)
		{
			restrictionString += " desc";
		}

		int countGroupBy = 0;
		for (int i = 0; i < restrictions.size(); i++)
		{
			if (restrictions.get(i).contains("group by"))
			{
				String restrict = restrictions.get(i);
				countGroupBy++;
				if (countGroupBy > 1)
				{

					restrict = restrict.split(" ")[2];

					restrictionString += ", " + restrict;
				}
				else
				{
					restrictionString += restrictions.get(i);
				}
			}
		}

		return restrictionString;
	}

	private void viewRestrictions()
	{
		dial = new Dialog(this);
		dial.setContentView(R.layout.customdbview);
		Button addButton = (Button) dial.findViewById(R.id.restrictAdd);
		restrictionListView = (ListView) dial
				.findViewById(R.id.restrictionList);
		Button doneButton = (Button) dial.findViewById(R.id.restrictDone);

		doneButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				dial.dismiss();
				performTheBigQuery2(true);
			}
		});

		addButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				addingRestriction();
			}
		});

		dial.setTitle("View Restrictions");

		ArrayAdapter<String> adapter;

		adapter = new ArrayAdapter<String>(this, R.layout.itemrow, restrictions);
		restrictionListView.setAdapter(adapter);
		registerForContextMenu(restrictionListView);
		restrictionListView.setOnCreateContextMenuListener(this);

		restrictionListView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				listViewDialog(position);
			}
		});

		dial.show();
	}

	public void addingRestriction()
	{
		newRestriction = new Dialog(this);
		newRestriction.setContentView(R.layout.addnewrestrict);
		Button nextButton = (Button) newRestriction
				.findViewById(R.id.restrictionNext);
		Button whereButton = (Button) newRestriction
				.findViewById(R.id.Button_Where);
		Button orderbyButton = (Button) newRestriction
				.findViewById(R.id.Button_OrderBy);
		Button groupbyButton = (Button) newRestriction
				.findViewById(R.id.Button_GroupBy);

		groupbyButton.setVisibility(View.INVISIBLE);
		
		whereButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				whereRestrictionDialog();
			}
		});
		groupbyButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				groupByDialog();
			}
		});
		orderbyButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				orderByDialog();
			}
		});
		nextButton.setOnClickListener(new OnClickListener()
		{
			// @Override
			public void onClick(View v)
			{
				newRestriction.dismiss();
			}
		});

		int count = 0;
		for (int i = 0; i < attributesAndDataTypes.length; i++)
		{
			if (attributesAndDataTypes[i][3].equals("true"))
			{
				count++;
			}
		}
		newOptions = new String[count];
		int position = 0;
		for (int i = 0; i < attributesAndDataTypes.length; i++)
		{
			if (attributesAndDataTypes[i][3].equals("true"))
			{
				newOptions[position] = attributesAndDataTypes[i][0];
				position++;
			}
		}

		newRestriction.setTitle("Adding A New Restriction");
		// String[] items = new String[] { "One", "Two", "Three" };
		Spinner restrictionSpinner = (Spinner) newRestriction
				.findViewById(R.id.restrictionSpinner);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, newOptions);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		restrictionSpinner.setAdapter(adapter);

		restrictionSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id)
					{
						Toast.makeText(getApplicationContext(),
								options[position], Toast.LENGTH_SHORT).show();
						selectedAttribute = newOptions[position];
					}

					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});

		newRestriction.show();

	}

	public void addRestriction(Dialog restriction)
	{
		dial.dismiss();
		if (restriction != null)
			restriction.dismiss();
		newRestriction.dismiss();
		viewRestrictions();
	}

	public void whereRestrictionDialog()
	{
		final Dialog restriction = new Dialog(this);
		restriction.setContentView(R.layout.wherelayout);
		final EditText value1 = (EditText) restriction
				.findViewById(R.id.editText1);

		final EditText value2 = (EditText) restriction
				.findViewById(R.id.editText2);
		Button okButton = (Button) restriction.findViewById(R.id.button_ok);
		final DatePicker dp = (DatePicker) restriction
				.findViewById(R.id.datePicker1);
		final DatePicker dp2 = (DatePicker) restriction
				.findViewById(R.id.datePicker2);
		value2.setVisibility(View.INVISIBLE);
		value1.setVisibility(View.INVISIBLE);
		dp.setVisibility(View.INVISIBLE);
		dp2.setVisibility(View.INVISIBLE);
		
		String[] s = selectedAttribute.split("[.]");
		final String dataType = findDataTypeByAttribute(s[1]);
		final String lengthOfType = findDataLengthByAttribute(s[1]);
System.out.println(s[0]+" DT" +dataType + " LOT "+lengthOfType);
		final Spinner restrictionSpinner = (Spinner) restriction
				.findViewById(R.id.spinner1);

		okButton.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				if (dataType.equalsIgnoreCase("date"))
				{
					int month = dp.getMonth();
					int year = dp.getYear();
					int day = dp.getDayOfMonth();

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

					if (operation.equals("between")
							|| operation.equals("not between"))
					{
						int month2 = dp2.getMonth();
						int year2 = dp2.getYear();
						int day2 = dp2.getDayOfMonth();

						String date2 = "";

						date2 += year2;

						if (month2 < 10)
							date2 += "-0" + (month2 + 1);
						else
							date2 += "-" + (month2 + 1);

						if (day2 < 10)
							date2 += "-0" + day2;
						else
							date2 += "-" + day2;
						restrictions.add("where " + selectedAttribute + " "
								+ operation + " '" + date + "' and '" + date2
								+ "'");

						// System.out.println("where "+selectedAttribute+" "+operation+" '"+date+"' and '"+date2+"'");
						addRestriction(restriction);
					}
					else
					{
						restrictions.add("where " + selectedAttribute + " "
								+ operation + " '" + date + "'");
						// System.out.println("where "+selectedAttribute+" "+operation+" '"+date+"'");
						addRestriction(restriction);
					}
				}
				else if (dataType.equalsIgnoreCase("decimal"))
				{
					if (operation.equals("between")
							|| operation.equals("not between"))
					{
						String one = value1.getText().toString();
						String two = value2.getText().toString();
						String[] split = one.split("[.]");
						if (split[0].charAt(0) == '-')
							split[0] = split[0].substring(1, split[0].length());
						if (split.length > 1)
						{
							String[] split2 = lengthOfType.split(",");
							if (split[0].length() <= Integer
									.parseInt(split2[0])
									&& split[1].length() <= Integer
											.parseInt(split2[1]))
							{
								String[] split3 = one.split("[.]");
								if (split3[0].charAt(0) == '-')
									split3[0] = split2[0].substring(1,
											split3[0].length());
								if (split3.length > 1)
								{
									String[] split4 = lengthOfType.split(",");
									if (split3[0].length() <= Integer
											.parseInt(split4[0])
											&& split3[1].length() <= Integer
													.parseInt(split4[1]))
									{
										restrictions.add("where "
												+ selectedAttribute + " "
												+ operation + " " + one
												+ " and " + two);
										// System.out.println("where "+selectedAttribute+" "+operation+" "+one+
										// " and "+two);
										addRestriction(restriction);
									}
								}
							}
						}
					}
					else
					{
						String one = value1.getText().toString();
						String[] split = one.split("[.]");
						// System.out.println("ONE is: "+one+"  "+split.length);

						if (split[0].charAt(0) == '-')
							split[0] = split[0].substring(1, split[0].length());
						if (split.length > 1)
						{
							String[] split2 = lengthOfType.split(",");
							// System.out.println(split2[0]+" "+split2[1]);
							// System.out.println(Integer.parseInt(split[0])
							// +" "+ Integer.parseInt(split2[0])+
							// " "+Integer.parseInt(split[1])+" "+
							// Integer.parseInt(split2[1]));
							if (split[0].length() <= Integer
									.parseInt(split2[0])
									&& split[1].length() <= Integer
											.parseInt(split2[1]))
							{
								restrictions.add("where " + selectedAttribute
										+ " " + operation + " " + one);
								// System.out.println("where "+selectedAttribute+" "+operation+" "+one);
								addRestriction(restriction);
							}
						}
					}
				}
				else if (dataType.equalsIgnoreCase("int"))
				{
					if (operation.equals("between")
							|| operation.equals("not between"))
					{
						String one = value1.getText().toString();
						String two = value2.getText().toString();
						restrictions.add("where " + selectedAttribute + " "
								+ operation + " " + one + " and " + two);
						// System.out.println("where "+selectedAttribute+" "+operation+" "+one+" and "+two);
						addRestriction(restriction);
					}
					else
					{
						String one = value1.getText().toString();
						restrictions.add("where " + selectedAttribute + " "
								+ operation + " " + one);
						// System.out.println("where "+selectedAttribute+" "+operation+" "+one);
						addRestriction(restriction);
					}

				}
				else if (dataType.equalsIgnoreCase("char")
						|| dataType.equalsIgnoreCase("varchar"))
				{
					if (operation.equals("between")
							|| operation.equals("not between"))
					{
						String one = value1.getText().toString();
						String two = value2.getText().toString();
						if (dataType.equalsIgnoreCase("char"))
						{
							if (lengthOfType.equals(one.length() + "")
									&& lengthOfType.equals(two.length() + ""))
							{
								restrictions.add("where " + selectedAttribute
										+ " " + operation + " '" + one
										+ "' and '" + two + "'");
								// System.out.println("where "+selectedAttribute+" "+operation+" '"+one+"' and '"+two+"'");
								addRestriction(restriction);
							}
						}
						else
						{
							restrictions.add("where " + selectedAttribute + " "
									+ operation + " '" + one + "' and '" + two
									+ "'");
							// System.out.println("where "+selectedAttribute+" "+operation+" '"+one+"' and '"+two+"'");
							addRestriction(restriction);
						}
					}
					else
					{
						String one = value1.getText().toString();
						if (dataType.equalsIgnoreCase("char"))
						{
							// System.out.println(lengthOfType +
							// " len of type and one "+one.length());
							if (lengthOfType.equals(one.length() + ""))
							{
								restrictions.add("where " + selectedAttribute
										+ " " + operation + " '" + one + "'");
								// System.out.println("where "+selectedAttribute+" "+operation+" '"+one+"'");
								addRestriction(restriction);
							}
						}
						else
						{
							restrictions.add("where " + selectedAttribute + " "
									+ operation + " '" + one + "'");
							// System.out.println("where "+selectedAttribute+" "+operation+" '"+one+"'");
							addRestriction(restriction);
						}
					}
				}

			}
		});

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.DialogOptions,
				android.R.layout.simple_spinner_item);

		// System.out.println("SELECTED ATTRIBUTE DATA TYPE: "+dataType+" "+lengthOfType);

		if (dataType.equalsIgnoreCase("int"))
		{
			value2.setVisibility(View.VISIBLE);
			value1.setVisibility(View.VISIBLE);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(
					Integer.parseInt(lengthOfType));
			value1.setInputType(InputType.TYPE_CLASS_NUMBER);
			value1.setFilters(FilterArray);
		}
		else if (dataType.equalsIgnoreCase("varchar")
				|| dataType.equalsIgnoreCase("char"))
		{
			value2.setVisibility(View.VISIBLE);
			value1.setVisibility(View.VISIBLE);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(
					Integer.parseInt(lengthOfType));
			// value1.setInputType(InputType.TYPE_CLASS_NUMBER);
			value1.setFilters(FilterArray);
		}
		else if (dataType.equalsIgnoreCase("decimal"))
		{
			value2.setVisibility(View.VISIBLE);
			value1.setVisibility(View.VISIBLE);
			// InputFilter[] FilterArray = new InputFilter[1];
			// FilterArray[0] = new
			// InputFilter.LengthFilter(Integer.parseInt(lengthOfType));
			String[] split = lengthOfType.split(",");
			// value1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
			// value1.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
			value1.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(
					Integer.parseInt(split[0].trim())
							- Integer.parseInt(split[1].trim()) + 2, Integer
							.parseInt(split[1].trim())) });;
		}
		else if (dataType.trim().equalsIgnoreCase("date"))
		{
			dp.setVisibility(View.VISIBLE);
			// dp2.setVisibility(View.VISIBLE);
			// InputFilter[] FilterArray = new InputFilter[1];
			// FilterArray[0] = new
			// InputFilter.LengthFilter(Integer.parseInt(lengthOfType));
			// String[] split = lengthOfType.split(",");
			// value1.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
			// value1.setFilters(new InputFilter[] {new
			// DecimalDigitsInputFilter(Integer.parseInt(split[0].trim()),Integer.parseInt(split[1].trim()))});;
		}

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		restriction.setTitle("Where Restriction");

		restrictionSpinner.setAdapter(adapter);

		restrictionSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id)
					{
						if (position == 0)
							operation = ">";
						else if (position == 1)
							operation = "<";
						else if (position == 2)
							operation = "=";
						else if (position == 3)
							operation = ">=";
						else if (position == 4)
							operation = "<=";
						else if (position == 5)
							operation = "<>";
						else if (position == 6)
							operation = "between";
						else if (position == 7)
							operation = "not between";

						if (!dataType.trim().equalsIgnoreCase("date"))
						{
							if (position == 6 || position == 7)
								value2.setVisibility(View.VISIBLE);
							else
								value2.setVisibility(View.INVISIBLE);
						}
						else
						{
							if (position == 6 || position == 7)
							{
								// System.out.println("LET THE DATES BE VISIBLE");
								// dp.setVisibility(View.VISIBLE);
								dp2.setVisibility(View.VISIBLE);
							}
							else
							{
								// dp.setVisibility(View.INVISIBLE);
								dp2.setVisibility(View.INVISIBLE);
							}

						}
					}

					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});

		restriction.show();

	}

	public void listViewDialog(final int position)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle("Options");

		boolean[] choice = { false };
		dialog.setMultiChoiceItems(R.array.ListViewOptions, choice,
				new OnMultiChoiceClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1,
							boolean arg2)
					{
						delete = arg2;
					}
				});

		dialog.setCancelable(false);

		dialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						if (delete)
						{
							restrictions.remove(position);
							dialog.cancel();
							dial.dismiss();
							viewRestrictions();
						}
						else
						{
							dialog.cancel();
						}
					}

				});
		dialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				});

		dialog.show();
	}

	public void orderByDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle("Order By");

		ascendOrDescend = 0;
		dialog.setSingleChoiceItems(R.array.OrderBy, 0,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						ascendOrDescend = which;
					}
				});

		dialog.setCancelable(false);

		dialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						if (ascendOrDescend == 0)
						{
							orderByString = "order by " + selectedAttribute;
						}
						else
						{
							orderByString = "order by " + selectedAttribute
									+ " desc";
						}
						restrictions.add(orderByString);
						// System.out.println(orderByString);
						dialog.cancel();
						addRestriction(null);
					}

				});
		dialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				});

		dialog.show();
	}

	public void groupByDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setTitle("Group By");

		dialog.setMessage("Do you want to group by this attribute?");

		dialog.setCancelable(false);

		dialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						restrictions.add("group by " + selectedAttribute);
						// System.out.println("group by "+selectedAttribute);
						dialog.cancel();
						addRestriction(null);
					}

				});
		dialog.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				});

		dialog.show();
	}

	public String findDataTypeByAttribute(String attribute)
	{
		for (int i = 0; i < attributesAndDataTypes.length; i++)
		{
			if (attributesAndDataTypes[i][4].equals(attribute))
			{
				return attributesAndDataTypes[i][6];
			}
		}
		return "Not Found";
	}

	public String findDataLengthByAttribute(String attribute)
	{
		for (int i = 0; i < attributesAndDataTypes.length; i++)
		{
			if (attributesAndDataTypes[i][4].equals(attribute))
			{
				return attributesAndDataTypes[i][5];
			}
		}
		return "Not Found";
	}

	public class DecimalDigitsInputFilter implements InputFilter
	{

		Pattern	mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero,
				int digitsAfterZero)
		{
			mPattern = Pattern.compile("[0-9,-]{0," + (digitsBeforeZero - 1)
					+ "}+((\\.[0-9]{0," + (digitsAfterZero - 1)
					+ "})?)||(\\.)?");
		}

		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend)
		{

			Matcher matcher = mPattern.matcher(dest);
			if (!matcher.matches())
				return "";
			return null;
		}
	}

	public void tableDialog(final String title)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		noChange = true;
		dialog.setTitle("Table Selection");
		// dialog.setMessage(R.string.npMessage);

		if (title.equals("Attribute Selection"))
		{
			options = new String[attributesAndDataTypes.length];
			for (int i = 0; i < options.length; i++)
			{
				options[i] = attributesAndDataTypes[i][0] + "\n\t-"
						+ attributesAndDataTypes[i][2];
			}
			// for(int i=0; i<checkedAttributeList.length; i++)
			// checkedAttributeList[i]=true;

			dialog.setMultiChoiceItems(options, checkedAttributeList,
					new DialogInterface.OnMultiChoiceClickListener()
					{
						public void onClick(DialogInterface d, int which,
								boolean checked)
						{
							noChange = false;
							if (checked)
							{
								checkedAttributeArrayList.add(options[which]
										.toString());
								attributesAndDataTypes[which][3] = "true";
							}
							else
							{
								checkedAttributeArrayList.remove(which);
								attributesAndDataTypes[which][3] = "false";
							}

							checkedAttributeList[which] = checked;
						}

					});
		}

		else if (title.equals("Table Selection"))
		{
			dialog.setMultiChoiceItems(R.array.TableOptions, checkedTableList,
					new DialogInterface.OnMultiChoiceClickListener()
					{
						public void onClick(DialogInterface d, int which,
								boolean checked)
						{

							noChange = false;
							checkedTableList[which] = checked;
						}

					});
		}
		dialog.setCancelable(false);

		dialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						if (title.equals("Table Selection"))
						{
							finalTableStructure.clear();
							if (noChange)
								dialog.cancel();
							else
							{
								isEditable = true;
								attributeButton.setEnabled(true);
								restrictionButton.setEnabled(false);
								restrictions.clear();
								multiTableSelection();
							}
						}
						else if (title.equals("Attribute Selection"))
						{

							if (noChange)
								dialog.cancel();
							else
							{
								restrictionButton.setEnabled(true);
								isEditable = false;
								restrictions.clear();
								performTheBigQuery2(false);
							}

						}

					}
				});

		dialog.show();
	}

	private void multiTableSelection()
	{
		int count = 0;
		for (int i = 0; i < checkedTableList.length; i++)
		{
			if (checkedTableList[i])
			{
				count++;
			}
		}
		tablesToBeDescribed = new int[count];
		// System.out.println("NEW SIZE for ttbd: " +
		// tablesToBeDescribed.length);
		count = 0;
		for (int i = 0; i < checkedTableList.length; i++)
		{
			if (checkedTableList[i])
			{
				tablesToBeDescribed[count] = i;
				count++;
			}
		}
		if (tablesToBeDescribed.length > 0)
		{
			// System.out.println("Describe " + tablesToBeDescribed.length
			// + " tables");
			describeMultipleTables(findTableFromInt(tablesToBeDescribed[0]),
					tablesToBeDescribed[0], 0);
		}
	}

	public void onClick(View v)
	{
		if (v.getId() == R.id.Button_DBAdminTableSelection)
		{
			tableDialog("Table Selection");
		}
		else if (v.getId() == R.id.Button_DBAdminAttributeSelection)
		{
			tableDialog("Attribute Selection");
		}
		else if (v.getId() == R.id.Button_DBAdminRestrictionSelection)
		{
			viewRestrictions();
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
				// System.out.println(":" + entities[i] + ":");
			}
			for (int i = 0; i < dataTypes.length; i++)
			{
				// System.out.println(":" + dataTypes[i] + ":");
			}

			// String[] entities = {"USERNAME","PASSWORD"};
			String filename = "testing";
			// String[] dataTypes = {"string","string"};
			String query = (search.substring(0, search.indexOf("datatypes"))
					.trim());
			// System.out.println(":" + query + ":");
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
		boolean oneTableSelected = (tablesToBeDescribed.length == 1);

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
					if (oneTableSelected && isEditable)
					{
						tag = "<td><a href=\"javascript:void(0)\" onclick=\"showDialog('"
								+ info[i]
								+ "',"
								+ i
								+ ",'"
								+ info[0]
								+ "','"
								+ info[1] + "')\"/>";
						endTag = "</a></td>";
					}
					else
					{
						tag = "<td>";
						endTag = "</td>";
					}

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
					if (oneTableSelected && isEditable)
					{
						tag = "<td><a href=\"javascript:void(0)\" onclick=\"showDialog('"
								+ info[i]
								+ "',"
								+ i
								+ ",'"
								+ info[0]
								+ "','"
								+ info[1] + "')\"/>";
						endTag = "</a></td>";
					}
					else
					{
						tag = "<td>";
						endTag = "</td>";
					}
				}
			}

			html += tag + info[i] + endTag;
		}
		html += "</tr>";
	}

	class performQuery extends AsyncTask<String, Integer, String>
	{
		boolean	updated;
		// String table;
		int		count;

		@Override
		protected String doInBackground(String... parameters)
		{
			if (parameters[0].equals("Describe")
					|| parameters[0].equals("Select Star")
					|| parameters[0].equals("Describe Tables")
					|| parameters[0].equals("Big Query")
					|| parameters[0].equals("Big Query 2"))
				updated = brain.performQuery(true);
			else
				updated = brain.performQuery(false);

			// System.out
			// .println(parameters[0] + " on " + parameters[1] + updated);
			if (parameters[0].equals("Describe Tables"))
				return parameters[0] + " " + parameters[1] + " "
						+ parameters[2];
			// table = parameters[1];
			return parameters[0];
		}

		private String findTableByInt(int pos)
		{
			String table;
			if (pos == 0)
				table = "ability";
			else if (pos == 1)
				table = "characterabilities";
			else if (pos == 2)
				table = "character";
			else if (pos == 3)
				table = "player";
			else if (pos == 4)
				table = "backpack";
			else if (pos == 5)
				table = "backpackitems";
			else
				table = "item";

			return table;
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (result.contains("Update"))
			{
				pd.dismiss();
				if (updated)
				{
					Toast.makeText(DBAdminController.this, "Table Updated",
							Toast.LENGTH_LONG).show();
					performTheBigQuery();
				}
				else
					Toast.makeText(DBAdminController.this,
							"Table Was Not Updated", Toast.LENGTH_LONG).show();
			}
			if (result.contains("Describe Tables"))
			{
				if (updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{
						int describePos = Integer.parseInt(result.split(" ")[3]
								.trim());

						// finalTableStructure = new
						// String[dbResults.length][4];
						doTableStructure();

						finalTableStructure.add(tableStructure);
						pd.dismiss();
						if (describePos < tablesToBeDescribed.length - 1)
						{
							String table = findTableByInt(tablesToBeDescribed[describePos + 1]);
							System.out
									.println("Calling describe multiple tables again with "
											+ table
											+ " with table pos: "
											+ tablesToBeDescribed[describePos + 1]
											+ " at array pos: "
											+ describePos
											+ 1);
							describeMultipleTables(table,
									tablesToBeDescribed[describePos + 1],
									describePos + 1);
						}

						else
						{
							// System.out.println("PERFORM BIG QUERY");
							performTheBigQuery();
						}

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
								// System.out.println(":" + theStuff[j] + ":");
							}

							createHTMLTableRow(theStuff, false);

						}
						html += "</table></body></html>";
						myWebView.loadDataWithBaseURL(null, html, mime,
								encoding, null);
					}
				}
				pd.dismiss();
			}

			else if (result.equals("Big Query"))
			{
				if (updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{
						System.out
								.println("Showing the big query on the table");
						html = "<html><head><script type=\"text/javascript\">function showDialog(id,pos,pk1,pk2) {Android.showDialog(id,pos,pk1,pk2);}</script><body><table border=\"1\">";

						int count = 0;
						for (int i = 0; i < finalTableStructure.size(); i++)
						{
							for (int j = 0; j < finalTableStructure.get(i).length; j++)
							{
								count++;
							}
						}

						String[] theStuff;
						theStuff = new String[count];

						int index = 0;
						for (int i = 0; i < finalTableStructure.size(); i++)
						{
							for (int j = 0; j < finalTableStructure.get(i).length; j++)
							{
								theStuff[index] = finalTableStructure.get(i)[j][0];
								// System.out.println("BAH " + theStuff[index]);
								index++;
							}
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
								// //System.out.println(":" + theStuff[j] +
								// ":");
							}

							createHTMLTableRow(theStuff, false);

						}

						html += "</table></body></html>";
						myWebView.loadDataWithBaseURL(null, html, mime,
								encoding, null);
					}
				}
				pd.dismiss();
			}
			else if (result.equals("Big Query 2"))
			{
				if (updated)
				{
					if (brain.getSearchResults().length > 0
							&& !(brain.getSearchResults()[0][0] instanceof String && ((String) brain
									.getSearchResults()[0][0])
									.equals("NO RESULTS")))
					{
						System.out
								.println("Showing the big query 2 on the table");
						html = "<html><head><script type=\"text/javascript\">function showDialog(id,pos,pk1,pk2) {Android.showDialog(id,pos,pk1,pk2);}</script><body><table border=\"1\">";

						int count = 0;
						for (int i = 0; i < attributesAndDataTypes.length; i++)
						{
							if (attributesAndDataTypes[i][3].equals("true"))
							{
								count++;
							}
						}

						String[] theStuff;
						theStuff = new String[count];

						int index = 0;
						for (int i = 0; i < attributesAndDataTypes.length; i++)
						{
							if (attributesAndDataTypes[i][3].equals("true"))
							{
								theStuff[index] = attributesAndDataTypes[i][4];
								// System.out.println("BAH " + theStuff[index]);
								index++;
							}
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
								// //System.out.println(":" + theStuff[j] +
								// ":");
							}

							createHTMLTableRow(theStuff, false);

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
					String substring = type.substring(type.indexOf("(") + 1,
							type.indexOf(")"));
					tableStructure[i][1] = "int";
					tableStructure[i][2] = "int";
					tableStructure[i][3] = substring.trim();
				}
				else if (type.contains("varchar"))
				{
					String substring = type.substring(type.indexOf("(") + 1,
							type.indexOf(")"));
					tableStructure[i][1] = "string";
					tableStructure[i][2] = "varchar";
					tableStructure[i][3] = substring.trim();

				}
				else if (type.contains("char"))
				{
					String substring = type.substring(type.indexOf("(") + 1,
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
					String substring = type.substring(type.indexOf("(") + 1,
							type.indexOf(")"));
					tableStructure[i][1] = "double";
					tableStructure[i][2] = "decimal";
					tableStructure[i][3] = substring.trim();
				}
				tableStructure[i][0] = field;
			}
		}
	}
}
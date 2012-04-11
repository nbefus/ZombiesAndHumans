package com.androidbeef.zombiesandhumans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;


// Sorry for all the commented out Toasts. Apparently an Async Task which will calls these methods cannot have anything which
// runs on the main UI thread. Will have to handle/detect/make known errors a little differently.
public class ZombiesAndHumansBrain
{
	private final String	debugClass	= "BRAIN";

	private Context			c;
	private JSONArray		jArray;
	private String[]		entities, dataTypes;
	private String			filename, query;

	public ZombiesAndHumansBrain(Context c)
	{
		this.c = c;
	}
	
	//Must call this method before performing a search
	public void setQueryVariables(String[] entities,
			String filename, String[] dataTypes, String query)
	{
		this.entities = new String[entities.length];
		this.dataTypes = new String[dataTypes.length];
		System.arraycopy(entities, 0, this.entities, 0, entities.length);
		this.filename = filename;
		System.arraycopy(dataTypes, 0, this.dataTypes, 0, dataTypes.length);
		this.query = query;
	}
	
	public Object[][] performSearchWithResult()
	{		
		if(entities != null && dataTypes != null && query != null && filename != null)
		{
			if (dataTypes.length != entities.length)
			{
				/*
				Toast.makeText(
						c,
						"ERROR: "
								+ debugClass
								+ " in PerformSearchWithResults() with error message: "
								+ "dataTypes and entities lengths are different!",
						Toast.LENGTH_LONG).show();
						*/
				return null;
			}
	
			for (int i = 0; i < dataTypes.length; i++)
				if (!(dataTypes[i].equalsIgnoreCase("int")
						|| dataTypes[i].equalsIgnoreCase("double")
						|| dataTypes[i].equalsIgnoreCase("long")
						|| dataTypes[i].equalsIgnoreCase("boolean") || dataTypes[i]
							.equalsIgnoreCase("string")))
				{
					/*
					Toast.makeText(
							c,
							"ERROR: "
									+ debugClass
									+ " in PerformSearchWithResults() with error message: "
									+ "there are some data types in dataTypes which are not allowed!",
							Toast.LENGTH_LONG).show();
							*/
					return null;
				}
	
			Object[][] results = null;
	
			try
			{
	
				String result = getPHPPage("http://androidbeef.com/" + filename
						+ ".php", query);
				if (!result.contains("null"))
				{
					jArray = new JSONArray(result);
	
					JSONObject json_data = null;
	
					//Toast.makeText(c, "There are " + jArray.length() + "results",
					//		Toast.LENGTH_LONG).show();
	
					results = new Object[jArray.length()][entities.length];
	
					for (int i = 0; i < jArray.length(); i++)
					{
						json_data = jArray.getJSONObject(i);
						for (int j = 0; j < entities.length; j++)
						{
							if (dataTypes[j].equalsIgnoreCase("int"))
								results[i][j] = json_data.getInt(entities[j]);
							else if (dataTypes[j].equalsIgnoreCase("string"))
								results[i][j] = json_data.getString(entities[j]);
							else if (dataTypes[j].equalsIgnoreCase("boolean"))
								results[i][j] = json_data.getBoolean(entities[j]);
							else if (dataTypes[j].equalsIgnoreCase("double"))
								results[i][j] = json_data.getDouble(entities[j]);
							else if (dataTypes[j].equalsIgnoreCase("long"))
								results[i][j] = json_data.getLong(entities[j]);
						}
					}
	
					/*
					Toast.makeText(c, "Done with loading results from query",
							Toast.LENGTH_LONG).show();
							*/
				}
				else
				{
					return results;
				}
	
			}
			catch (JSONException e1)
			{
				/*
				Toast.makeText(
						c,
						"JSON ERROR: "
								+ debugClass
								+ " in PerformSearchWithResults() with error message: "
								+ e1.getMessage(), Toast.LENGTH_LONG).show();
								*/
			}
			catch (ParseException e1)
			{
				/*
				Toast.makeText(
						c,
						"PARSE ERROR: "
								+ debugClass
								+ " in PerformSearchWithResults() with error message: "
								+ e1.getMessage(), Toast.LENGTH_LONG).show();
								*/
			}
	
			return results;
		}
		else
			return null;
	}

	private String getPHPPage(String page, String query)
	{
		InputStream is = null;
		StringBuilder sb = null;
		String result = null;

		try
		{
			System.out.println("Making client");
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(page);
			List nameValuePairs = new ArrayList(1);
			nameValuePairs.add(new BasicNameValuePair("query", query));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}
		catch (Exception e)
		{
			/*
			Toast.makeText(
					c,
					"ERROR 1: " + debugClass
							+ " in getPHPPage() with error message: "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
							*/
		}

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";

			System.out.println("Making the string");
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
			System.out.println("String is : " + result);
			//Toast.makeText(c, ":" + result + ":", Toast.LENGTH_LONG).show();

		}
		catch (Exception e)
		{
			/*
			Toast.makeText(
					c,
					"ERROR 2: " + debugClass
							+ " in getPHPPage() with error message: "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
							*/
		}

		return result;
	}
}

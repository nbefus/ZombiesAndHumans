package com.androidbeef.zombiesandhumans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.widget.Toast;

public class InternetTools 
{
	private Context c;
	
	public InternetTools(Context c)
	{
		this.c = c;
	}

	public String getPHPPage(String page)
	{
		System.out.println("PRESSSED");
        InputStream is = null;
        StringBuilder sb = null;
        String result1=null;
 
        try{
        	System.out.println("Making client");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(page);
           // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
        	System.out.println("Response: " + response.toString());
            is = entity.getContent();
        }catch(Exception e){
            Toast.makeText(c, "ERROR", Toast.LENGTH_LONG).show();
        }
     
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
          
            System.out.println("Making the string");
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
             
            is.close();
            result1=sb.toString();
            System.out.println("String is : "+result1);
            Toast.makeText(c, result1, Toast.LENGTH_LONG).show();

             
        }catch(Exception e){
            Toast.makeText(c, "ERROR", Toast.LENGTH_LONG).show();

        }
 
        return result1;
	}
}

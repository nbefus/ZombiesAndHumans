package com.androidbeef.zombiesandhumans;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
//I made this.

// Nathaniel test
public class StartScreenController extends Activity implements OnClickListener {
    private JSONArray jArray;
	//private List<? extends NameValuePair> nameValuePairs;
    private ZombiesAndHumansBrain it = new ZombiesAndHumansBrain(this);
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button) findViewById(R.id.Button_LogIn)).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) 
	{
		System.out.println(v.getId());
		System.out.println(R.id.Button_LogIn);
		if(v.getId() == R.id.Button_LogIn)
		{
			//TESTING FOR DATABASE CONNECTION
			getPhoneAndID("testing");
			//it.postData();
			// Go to server and check username and password and (in)validate 
			
		}
		else if(v.getId() == R.id.Button_SignUp)
		{
			// Go to sign up screen and form
		}
	}
	
	//TEST METHOD TO SEE HOW GETTING THINGS FROM MYSQL DATABASE WORKS
	private void getPhoneAndID(String filename)
	{
		int id = -99;

        String phone = "Error";  
        
        try{
        	
        jArray = new JSONArray(it.getPHPPage("http://androidbeef.com/"+filename+".php"));
        JSONObject json_data = null;
        Toast.makeText(this, "Length is "+jArray.length(), Toast.LENGTH_LONG).show();

        for(int i=0;i<jArray.length();i++){
                json_data = jArray.getJSONObject(i);
                id=json_data.getInt("id");
                phone=json_data.getString("phone");
        }
        
        Toast.makeText(this, "phone: " + phone + "  id: "+id, Toast.LENGTH_LONG).show();

         
        }catch(JSONException e1){
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
        }catch (ParseException e1){
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
        }
        
	}
}
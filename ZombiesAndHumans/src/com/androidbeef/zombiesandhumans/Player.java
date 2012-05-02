package com.androidbeef.zombiesandhumans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Player implements Serializable
{
	private int playerid,backpackid,characterid;
	private double locationx,locationy,safehousex,safehousey;
	private char computerplayer;
	private String username,password;
	
	public Player(int playerid, char computerplayer, String username,
			String password, double locationx, double locationy,
			double safehousex, double safehousey, int backpackid,
			int characterid)
	{
		this.playerid = playerid;
		this.locationx = locationx;
		this.locationy = locationy;
		this.safehousex = safehousex;
		this.safehousey = safehousey;
		this.backpackid = backpackid;
		this.characterid = characterid;
		this.computerplayer = computerplayer;
		this.username = username;
		this.password = password;
	}

	public int getPlayerid()
	{
		return playerid;
	}

	public double getLocationx()
	{
		return locationx;
	}

	public double getLocationy()
	{
		return locationy;
	}

	public double getSafehousex()
	{
		return safehousex;
	}

	public double getSafehousey()
	{
		return safehousey;
	}

	public double getBackpackid()
	{
		return backpackid;
	}

	public int getCharacterid()
	{
		return characterid;
	}

	public char getComputerplayer()
	{
		return computerplayer;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}
}

package com.androidbeef.zombiesandhumans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Character implements Serializable
{
	private int characterid,clevel,health,strength,defense,accuracy,evasion;
	private String cname;
	public Character(int characterid, String cname, int clevel, int health, int strength,
			int defense, int accuracy, int evasion)
	{
		this.characterid = characterid;
		this.clevel = clevel;
		this.health = health;
		this.strength = strength;
		this.defense = defense;
		this.accuracy = accuracy;
		this.evasion = evasion;
		this.cname = cname;
	}
	
	public int getCharacterid()
	{
		return characterid;
	}
	public int getClevel()
	{
		return clevel;
	}
	public int getHealth()
	{
		return health;
	}
	public int getStrength()
	{
		return strength;
	}
	public int getDefense()
	{
		return defense;
	}
	public int getAccuracy()
	{
		return accuracy;
	}
	public int getEvasion()
	{
		return evasion;
	}
	public String getCname()
	{
		return cname;
	}

	public void setCharacterid(int characterid)
	{
		this.characterid = characterid;
	}

	public void setClevel(int clevel)
	{
		this.clevel = clevel;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	public void setStrength(int strength)
	{
		this.strength = strength;
	}

	public void setDefense(int defense)
	{
		this.defense = defense;
	}

	public void setAccuracy(int accuracy)
	{
		this.accuracy = accuracy;
	}

	public void setEvasion(int evasion)
	{
		this.evasion = evasion;
	}

	public void setCname(String cname)
	{
		this.cname = cname;
	}
	
	
}

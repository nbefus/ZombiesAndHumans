package com.androidbeef.zombiesandhumans;

public class Item
{
	private int itemid,itemcount;
	private String iname;
	private char instorage;
	
	public Item(int itemid, int itemcount, String iname, char instorage)
	{
		this.itemid = itemid;
		this.itemcount = itemcount;
		this.iname = iname;
		this.instorage = instorage;
	}

	public int getItemid()
	{
		return itemid;
	}

	public int getItemcount()
	{
		return itemcount;
	}

	public String getIname()
	{
		return iname;
	}

	public char getInstorage()
	{
		return instorage;
	}

	public void setItemid(int itemid)
	{
		this.itemid = itemid;
	}

	public void setItemcount(int itemcount)
	{
		this.itemcount = itemcount;
	}

	public void setIname(String iname)
	{
		this.iname = iname;
	}

	public void setInstorage(char instorage)
	{
		this.instorage = instorage;
	}
}

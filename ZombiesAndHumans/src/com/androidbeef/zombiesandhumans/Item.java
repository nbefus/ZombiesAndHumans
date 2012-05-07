package com.androidbeef.zombiesandhumans;

public class Item
{
	private int itemid,inbackpackcount,instoragecount;
	private String iname, ability;
	
	public Item(int itemid, String iname,String ability,int inbackpackcount, int instoragecount)
	{
		this.ability = ability;
		this.itemid = itemid;
		this.inbackpackcount = inbackpackcount;
		this.iname = iname;
		this.instoragecount = instoragecount;
	}

	public int getItemid()
	{
		return itemid;
	}

	public int getInbackpackcount()
	{
		return inbackpackcount;
	}

	public String getIname()
	{
		return iname;
	}
	
	public String getAbility()
	{
		return ability;
	}

	public int getInstoragecount()
	{
		return instoragecount;
	}

	public void setItemid(int itemid)
	{
		this.itemid = itemid;
	}

	public void setItemcount(int itemcount)
	{
		this.inbackpackcount = itemcount;
	}

	public void setIname(String iname)
	{
		this.iname = iname;
	}
	
	public void setAbility(String ability)
	{
		this.ability = ability;
	}

	public void setInstorage(int instorage)
	{
		this.instoragecount = instorage;
	}

	public void setInbackpackcount(int inbackpackcount)
	{
		this.inbackpackcount = inbackpackcount;
	}

	public void setInstoragecount(int instoragecount)
	{
		this.instoragecount = instoragecount;
	}
}

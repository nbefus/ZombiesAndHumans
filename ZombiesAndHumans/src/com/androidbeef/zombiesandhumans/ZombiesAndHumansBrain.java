package com.androidbeef.zombiesandhumans;

import java.util.ArrayList;


import com.google.android.maps.GeoPoint;

import android.content.Context;


public class ZombiesAndHumansBrain
{
	private Character character;
	private Character enemyCharacter;
	private Player self;
	private Player enemy;
	private ArrayList<Item> items;
	private ArrayList<Player> enemies;
	private ArrayList<Character> enemiesCharacters;
	private Context			c;
	private ArrayList<GeoPoint>	enemyLoc;
	private Query search;
	private Object[][] searchResults;
	
	public ZombiesAndHumansBrain(Context c)
	{
		this.c = c;
	}
	
	public void prepareForQuery(String[] entities,
			String filename, String[] dataTypes, String query)
	{
		search = new Query(entities, filename, dataTypes, query);
	}
	
	public boolean performQuery(boolean withResults)
	{
		if(search != null)
		{
			if(withResults)
				searchResults = search.performQueryWithResult();
			else
				return search.performQueryWithNoResults();
			
			if(searchResults[0][0] instanceof String && !((String)searchResults[0][0]).contains("error"))
				return true;
			else if(!(searchResults[0][0] instanceof String))
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	public int findEnemyPosByUsername(String username)
	{
		for(int i=0; i<enemies.size(); i++)
			if(enemies.get(i).getUsername().trim().equals(username.trim()))
				return i;
		return -1;
	}
	
	public int findEnemyCharPosByUsername(String username)
	{
		for(int i=0; i<enemies.size(); i++)
			if(enemies.get(i).getUsername().trim().equals(username.trim()))
				for(int j=0; j<enemiesCharacters.size(); j++)
					if(enemies.get(i).getCharacterid() == enemiesCharacters.get(j).getCharacterid())
						return j;
		return -1;
	}
	
	public int findItemByName(String iname)
	{
		for(int i=0; i<items.size(); i++)
			if(items.get(i).getIname().trim().equals(iname.trim()))
				return i;
		return -1;
	}

	public void setEnemies(ArrayList<Player> enemies)
	{
		this.enemies = enemies;
		System.out.println("ENEMY SIZE : "+enemies.size());
		enemyLoc = new ArrayList<GeoPoint>();
		for(int i=0; i<enemies.size(); i++)
			enemyLoc.add(new GeoPoint((int) (enemies.get(i).getLocationx() * 1E6),
					(int) (enemies.get(i).getLocationy() * 1E6)));
	}
	
	public ArrayList<Player> getEnemies()
	{
		return enemies;
	}
	
	public void setEnemiesCharacters(ArrayList<Character> enemiesCharacters)
	{
		this.enemiesCharacters = enemiesCharacters;
	}
	
	public ArrayList<Character> getEnemiesCharacters()
	{
		return enemiesCharacters;
	}
	
	public void setEnemy(Player enemy)
	{
		this.enemy = enemy;
	}
	
	public Player getEnemy()
	{
		return enemy;
	}
	
	public void setSelf(Player self)
	{
		this.self = self;
	}
	
	public Character getEnemyCharacter()
	{
		return enemyCharacter;
	}
	
	public void setEnemyCharacter(Character enemyCharacter)
	{
		this.enemyCharacter = enemyCharacter;
	}
	
	public Character getCharacter()
	{
		return character;
	}
	
	public void setCharacter(Character character)
	{
		this.character = character;
	}
	
	public void setItems(ArrayList<Item> items)
	{
		this.items = items;
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
	public Player getSelf()
	{
		return self;
	}
	
	public ArrayList<GeoPoint> getEnemyLoc()
	{
		return enemyLoc;
	}
	
	public Object[][] getSearchResults()
	{
		return searchResults;
	}

}

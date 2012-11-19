package org.shortrip.boozaa.plugins.boocron.persistence;

import java.util.HashMap;

/*
* Copyright (C) 2012  boozaa
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
public class Cache {

	private HashMap<String, Object> store = new HashMap<String, Object>();

	/**
	 * 
	 */
	public void add(String id, Object data) {
		this.store.put(id, data);
	}

	/**
	 * Removes the Object with the corresponding Id from the cache.
	 * @param id Reference
	 */
	public void remove(String id) {
		this.store.remove(id);
	}

	/**
	 *
	 */
	public boolean exists(String id) {
		return this.store.containsKey(id);
	}

	public Object getObject(String key) {
		return this.store.get(key);
	}

	public String getString(String key) {
		return getObject(key).toString();
	}

	public int getInteger(String key) {
		int i = 0;
		try {
			i = Integer.parseInt(getString(key));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return i;
	}

	public double getDouble(String key) {
		double i = 0;
		try {
			i = Double.parseDouble(getString(key));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return i;
	}

	public float getFloat(String key) {
		float i = 0;
		try {
			i = Float.parseFloat(getString(key));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return i;
	}

	public boolean getBoolean(String key){
		return Boolean.parseBoolean(getString(key));
	}

	/**
	 *
	 */
	public HashMap<String, Object> getCache() {
		return this.store;
	}

	/**
	 * 
	 */
	public void erase(){
		this.store.clear();
	}
	
}

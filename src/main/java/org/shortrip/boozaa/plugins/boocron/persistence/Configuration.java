package org.shortrip.boozaa.plugins.boocron.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


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
public class Configuration extends YamlConfiguration {

	private File source;
	
	
	
	public Configuration(JavaPlugin plugin) {
		this(plugin.getDataFolder() + File.separator + "config.yml");
	}
	public Configuration(String sourcepath) {
		this(new File(sourcepath));
	}
	public Configuration(File source) {
		this.source = source;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T parse(String path, T def) {
		T rval = (T) this.get(path, def);
		this.set(path, rval);
		return rval;
	}
	
	public boolean exists() {
		return this.source.exists();
	}
	
	public void init() {
		this.load();
		this.save();
	}

	public void load() {
		try {
			this.load(this.source);
		} catch (FileNotFoundException ex) {
			System.out.println("[BooCron] File '" + this.source + "' was not found");
		} catch (Exception ex) {
			System.out.println("[BooCron] Error while loading file '" + this.source + "':");
			ex.printStackTrace();
		}
	}
	public void save() {
		try {
			//boolean regen = !this.exists();
			this.save(this.source);
			//if (regen) System.out.println("[BooCron] File '" + this.source + "' has been regenerated");
		} catch (Exception ex) {
			System.out.println("[BooCron] Error while saving to file '" + this.source + "':");
			ex.printStackTrace();
		}
	}
	
	public <T> List<T> getListOf(String path) {
		return this.getListOf(path, new ArrayList<T>());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> getListOf(String path, List<T> def) {
		List list = this.getList(path, null);
		if (list == null) {
			return def;
		} else {
			List<T> rval = new ArrayList<T>();
			for (Object object : this.getList(path)) {
				try {
					rval.add((T) object);
				} catch (Throwable t) {}
			}
			return rval;
		}
	}
	
	public Set<String> getKeys(String path) {
		try {
			return this.getConfigurationSection(path).getKeys(false);
		} catch (Exception ex) {
			return new HashSet<String>();
		}
	}
	

}

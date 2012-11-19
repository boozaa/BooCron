package org.shortrip.boozaa.plugins.boocron.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.shortrip.boozaa.plugins.boocron.BooCron;

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
public class Log {

	// Logger
	private static Logger logger = Logger.getLogger("Minecraft");
	
	
	public static void info(String message){
		logger.log(Level.INFO, "[BooCron] " + message);
	}
	
	public static void warning(String message){
		logger.log(Level.WARNING, "[BooCron] " + message);
	}
	
	public static void severe(String message){
		logger.log(Level.SEVERE, "[BooCron] " + message);
	}
	
	// Debug si activé
	public static void debug(String message) {		
		if( BooCron.debug() ) {			
			logger.log(Level.INFO, "[BooCron] - DEBUG - " + message);
		}		
	}
	
}

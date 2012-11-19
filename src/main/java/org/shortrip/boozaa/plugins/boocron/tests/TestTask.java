package org.shortrip.boozaa.plugins.boocron.tests;

import org.shortrip.boozaa.plugins.boocron.CronTask;

import it.sauronsoftware.cron4j.TaskExecutionContext;

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
public class TestTask extends CronTask {

	
	
	public TestTask(String name, String pattern) {
		super(name, pattern);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(TaskExecutionContext arg0) throws RuntimeException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}

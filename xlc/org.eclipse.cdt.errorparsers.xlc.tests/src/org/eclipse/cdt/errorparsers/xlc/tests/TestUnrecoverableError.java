/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.errorparsers.xlc.tests;

import junit.framework.TestCase;

import org.eclipse.cdt.core.IMarkerGenerator;

public class TestUnrecoverableError extends TestCase {
	String err_msg;
	/**
	 * This function tests parseLine function of the
	 * XlcErrorParser class. Error message generated by
	 * xlc compiler with unrecoverable severity (U) is given as
	 * input for testing.
	 */
	public void testparseLine()
	{
		XlcErrorParserTester aix = new XlcErrorParserTester();
		aix.parseLine(err_msg);
		assertEquals("temp1.c", aix.getFileName());
		assertEquals(5, aix.getLineNumber());
		assertEquals(IMarkerGenerator.SEVERITY_ERROR_BUILD, aix.getSeverity());
		assertEquals("INTERNAL COMPILER ERROR",aix.getMessage());
	}
	public TestUnrecoverableError( String name)
	{
		super(name);
		err_msg = "\"temp1.c\", line 5.1: 1506-001 (U) "
				+ "INTERNAL COMPILER ERROR";
	}
}

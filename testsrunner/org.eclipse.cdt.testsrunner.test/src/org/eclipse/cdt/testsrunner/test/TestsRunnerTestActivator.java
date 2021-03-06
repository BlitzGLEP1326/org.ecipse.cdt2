/*******************************************************************************
 * Copyright (c) 2011 Anton Gorenkov 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Anton Gorenkov  - initial implementation
 *******************************************************************************/
package org.eclipse.cdt.testsrunner.test;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TestsRunnerTestActivator extends Plugin {

	/** The plug-in ID .*/
	public static final String PLUGIN_ID = "org.eclipse.cdt.testsrunner.test"; //$NON-NLS-1$

	/** Plug-in instance. */
	private static TestsRunnerTestActivator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared plug-in instance.
	 * 
	 * @return the plug-in instance
	 */
	public static TestsRunnerTestActivator getDefault() {
		return plugin;
	}

}

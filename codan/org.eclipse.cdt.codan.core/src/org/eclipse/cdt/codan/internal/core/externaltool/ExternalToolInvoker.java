/*******************************************************************************
 * Copyright (c) 2012 Google, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Ruiz  - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.codan.internal.core.externaltool;

import java.io.File;
import java.util.List;

import org.eclipse.cdt.codan.core.externaltool.ConfigurationSettings;
import org.eclipse.cdt.codan.core.externaltool.IArgsSeparator;
import org.eclipse.cdt.codan.core.externaltool.ICommandInvoker;
import org.eclipse.cdt.codan.core.externaltool.IOutputParser;
import org.eclipse.cdt.codan.core.externaltool.InvocationFailure;
import org.eclipse.cdt.codan.core.externaltool.InvocationParameters;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Invokes an external tool to perform checks on a single file.
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class ExternalToolInvoker {
	private final ICommandInvoker commandInvoker;

	/**
	 * Constructor.
	 * @param commandInvoker builds and launches the command necessary to invoke the external tool.
	 */
	public ExternalToolInvoker(ICommandInvoker commandInvoker) {
		this.commandInvoker = commandInvoker;
	}

	/**
	 * Invokes an external tool.
	 * @param parameters the parameters to pass to the external tool executable.
	 * @param configurationSettings user-configurable settings.
	 * @param argsSeparator separates the arguments to pass to the external tool executable. These
	 * arguments are stored in a single {@code String}.
	 * @param parsers parse the output of the external tool.
	 * @throws InvocationFailure if the external tool reports that it cannot be executed.
	 * @throws Throwable if the external tool cannot be launched.
	 */
	public void invoke(InvocationParameters parameters, ConfigurationSettings configurationSettings,
			IArgsSeparator argsSeparator, List<IOutputParser> parsers) throws InvocationFailure,
			Throwable {
		IPath executablePath = executablePath(configurationSettings);
		String[] args = argsToPass(parameters, configurationSettings, argsSeparator);
		boolean shouldDisplayOutput = configurationSettings.getShouldDisplayOutput().getValue();
		IProject project = parameters.getActualFile().getProject();
		try {
			commandInvoker.buildAndLaunchCommand(project,
					configurationSettings.getExternalToolName(), executablePath, args,
					parameters.getWorkingDirectory(), shouldDisplayOutput, parsers);
		} finally {
			reset(parsers);
		}
	}

	private IPath executablePath(ConfigurationSettings configurationSettings) {
		File executablePath = configurationSettings.getPath().getValue();
		return new Path(executablePath.toString());
	}

	private String[] argsToPass(InvocationParameters parameters,
			ConfigurationSettings configurationSettings, IArgsSeparator argsSeparator) {
		String[] configuredArgs = configuredArgs(configurationSettings, argsSeparator);
		String actualFilePath = parameters.getActualFilePath();
		return addFilePathToArgs(actualFilePath, configuredArgs);
	}

	private String[] configuredArgs(ConfigurationSettings configurationSettings,
			IArgsSeparator argsSeparator) {
		String args = configurationSettings.getArgs().getValue();
		return argsSeparator.separateArgs(args);
	}

	private String[] addFilePathToArgs(String actualFilePath, String[] configuredArgs) {
		int argCount = configuredArgs.length;
		String[] allArgs = new String[argCount + 1];
		// add file to process as the first argument
		allArgs[0] = actualFilePath;
		for (int i = 0; i < argCount; i++) {
			allArgs[i + 1] = configuredArgs[i];
		}
		return allArgs;
	}

	private void reset(List<IOutputParser> parsers) {
		for (IOutputParser parser : parsers) {
			parser.reset();
		}
	}
}

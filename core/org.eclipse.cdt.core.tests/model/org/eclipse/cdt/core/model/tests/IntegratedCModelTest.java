/*
 * Created on Jun 3, 2003
 * by bnicolle
 */
package org.eclipse.cdt.core.model.tests;

import java.io.FileInputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.model.TranslationUnit;
import org.eclipse.cdt.testplugin.CProjectHelper;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * @author bnicolle
 *
 */
public abstract class IntegratedCModelTest extends TestCase {

	private ICProject fCProject;
	private IFile sourceFile;
	private NullProgressMonitor monitor;

	/**
	 * 
	 */
	public IntegratedCModelTest() {
		super();
	}

	/**
	 * @param name
	 */
	public IntegratedCModelTest(String name) {
		super(name);
	}

	/**
	 * @return the subdirectory (from the plugin root) containing the required
	 *         test sourcefile (plus a trailing slash)
	 */
	abstract public String getSourcefileSubdir();

	/**
	 * @return the name of the test source-file
	 */
	abstract public String getSourcefileResource();

	public void setUp() throws Exception {
		monitor = new NullProgressMonitor();
		String pluginRoot=org.eclipse.core.runtime.Platform.getPlugin("org.eclipse.cdt.core.tests").find(new Path("/")).getFile();

		fCProject= CProjectHelper.createCProject("TestProject1", "bin");
	
		sourceFile = fCProject.getProject().getFile( getSourcefileResource() );
		if (!sourceFile.exists()) {
			try{
				FileInputStream fileIn = new FileInputStream(pluginRoot+ getSourcefileSubdir() + getSourcefileResource() ); 
				sourceFile.create(fileIn,false, monitor);        
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	
		if (!fCProject.getProject().hasNature(CCProjectNature.CC_NATURE_ID)) {
			addNatureToProject(fCProject.getProject(), CCProjectNature.CC_NATURE_ID, null);
		}
	}

	protected void tearDown() {
		try{
		  CProjectHelper.delete(fCProject);
		} 
		catch (ResourceException e) {} 
		catch (CoreException e) {} 
		
	}	

	private static void addNatureToProject(IProject proj, String natureId, IProgressMonitor monitor) throws CoreException {
		IProjectDescription description = proj.getDescription();
		String[] prevNatures= description.getNatureIds();
		String[] newNatures= new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length]= natureId;
		description.setNatureIds(newNatures);
		proj.setDescription(description, monitor);
	}

	protected ITranslationUnit getTU() {
		TranslationUnit tu = new TranslationUnit(fCProject, sourceFile);
		// parse the translation unit to get the elements tree		
		Map newElement = tu.parse(); 
		return tu;
	}
}

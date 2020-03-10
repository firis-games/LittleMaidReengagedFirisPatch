package net.blacklab.lmr.util.manager;

import java.io.InputStream;

/**
 * LoaderHandler must not have any arguments on Constructor 
 *
 */
@Deprecated
public abstract class LoaderHandler {
	
	public LoaderHandler() {
	}
	
	public abstract boolean isHandled(String pName);
	
	public abstract void doHandle(InputStream pStream, String pSearchName);

}

package net.blacklab.lib.vevent;

@SuppressWarnings("ALL")
public abstract class VEventCancelableBase implements IVEventCancelable {
	
	protected boolean canceled;

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean flag) {
		if (!isCanceled()) canceled = flag;
	}

}

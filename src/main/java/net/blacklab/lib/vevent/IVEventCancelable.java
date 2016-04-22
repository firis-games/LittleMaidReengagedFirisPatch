package net.blacklab.lib.vevent;

public interface IVEventCancelable extends IVEvent {

	public boolean isCanceled();
	public void setCanceled(boolean flag);

}

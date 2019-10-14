package net.blacklab.lib.vevent;

public interface IVEventCancelable extends IVEvent {

	boolean isCanceled();
	void setCanceled(boolean flag);

}

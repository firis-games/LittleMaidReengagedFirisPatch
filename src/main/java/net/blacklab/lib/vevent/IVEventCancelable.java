package net.blacklab.lib.vevent;

@SuppressWarnings("ALL")
public interface IVEventCancelable extends IVEvent {

	boolean isCanceled();
	void setCanceled(boolean flag);

}

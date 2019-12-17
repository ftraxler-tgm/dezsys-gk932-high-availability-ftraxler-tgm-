
package observer;
/*
 *  Is an Interface with the methodHeaders registerServer,removeServer and notifyObserver
 *  These Methodes need to be implemented in a class which work with observers because then they can register unregister and can be notifyed when something changed
 *  @project Observer
 *  @author Fabian Traxler {@literal <ftraxler@student.tgm.ac.at>}
 *  @version 2019-03-12
*/


import compute.Compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Loadbalancer extends Remote {
	public void removeServer(Compute stub) throws RemoteException;
	public void  registerServer(Compute e,Integer weight) throws RemoteException;
}

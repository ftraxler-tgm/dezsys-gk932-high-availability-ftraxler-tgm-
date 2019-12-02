package observer;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import compute.Compute;
import compute.Task;
import engine.ComputeEngine;

public class LoadbalancerServer implements Loadbalancer,Compute {

    private List<Compute> roundRobin;
    private Iterator<Compute> it;


    public LoadbalancerServer(){
        super();
        roundRobin= new ArrayList<Compute>();
        it = this.roundRobin.iterator();
    }


    public void registerServer(Compute e)
    {

        System.out.println("Server registered "+roundRobin.add(e));
        System.out.println("Number of engines:"+roundRobin.size());

    }
    public void removeServer(Compute stub){
       this.roundRobin.remove(stub);
       System.out.println("Server removed");



    }

    public static void main(String[] args) {



        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            Registry registryLb = LocateRegistry.createRegistry(1099);

            Compute loadbalancer = new LoadbalancerServer();




            String serverName="LoadBalancer";
            Loadbalancer serverStub = (Loadbalancer) UnicastRemoteObject.exportObject(loadbalancer, 0);
            registryLb.rebind(serverName,serverStub);
            System.out.println("ServerLb bound under "+serverName);


        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }


    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        System.out.println("Selecting Server...");
        System.out.println(roundRobin.isEmpty());



        if (it.hasNext()) {
            return it.next().executeTask(t);
        }else{
            return null;
        }


    }
}
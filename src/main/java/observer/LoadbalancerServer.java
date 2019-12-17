package observer;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import compute.Compute;
import compute.Task;

public class LoadbalancerServer implements Loadbalancer,Compute {

    private List<Compute> serverList;
    private Map<Compute,Integer> weightList;
    private Map<Compute,Integer> leastConnection;
    private Iterator<Compute> it;
    private boolean roundRobinOrLeastConnection = false;

    private Integer position = 0;


    public LoadbalancerServer(boolean loadBalancerAlg){
        super();
        this.roundRobinOrLeastConnection = loadBalancerAlg;
        serverList= new ArrayList<Compute>();
        weightList = new HashMap<Compute,Integer>();
        leastConnection = new HashMap<Compute,Integer>();
        System.out.println("Weighted Round-Robin: "+roundRobinOrLeastConnection);
        System.out.println("Weighted Least-Connection: "+!(roundRobinOrLeastConnection));

    }



    public void registerServer(Compute e,Integer weight)
    {

        System.out.println("Server registered "+ serverList.add(e)+ "with weight: "+weight);
        weightList.put(e,weight);
        leastConnection.put(e,0);
        System.out.println("Number of engines:"+ serverList.size());

    }
    public void removeServer(Compute stub){
       this.serverList.remove(stub);
       System.out.println("Server removed");



    }

    public static void main(String[] args) {



        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            Registry registryLb = LocateRegistry.createRegistry(1099);

            Compute loadbalancer = new LoadbalancerServer(Boolean.parseBoolean(args[0]));




            String serverName="LoadBalancer";
            Loadbalancer serverStub = (Loadbalancer) UnicastRemoteObject.exportObject(loadbalancer, 0);
            registryLb.rebind(serverName,serverStub);
            System.out.println("ServerLb bound under "+serverName);


        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }


    }
    private void connectionStatus(Compute e,Integer i){
        int aktuellerWert = leastConnection.get(e);
        aktuellerWert += i;
        leastConnection.replace(e,aktuellerWert);
    }

    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        System.out.println("Selecting Server...");


        if(roundRobinOrLeastConnection){
            List<Compute> liste = new ArrayList<>();
            Iterator<Compute> iterator = serverList.iterator();
            while (iterator.hasNext()) {
                Compute serverItem = iterator.next();
                Integer weight = weightList.get(serverItem);
                if (weight > 0) {
                    for (int i = 0; i < weight; i++) {
                        liste.add(serverItem);
                    }
                }

            }
            if (position > liste.size()) {
                position = 0;
            }

            return liste.get(position++).executeTask(t);

        }

        System.out.println("Starting Calculation....");
        for (Integer value : leastConnection.values()) {
            System.out.println("Value: " + value);
        }


        Compute smallest=null;
        int min = Integer.MAX_VALUE;
        for(Compute key : leastConnection.keySet()) {
            int value = leastConnection.get(key);
            if(value < min) {
                min = value;
                smallest = key;
            }
        }
        connectionStatus(smallest,1);


        System.out.println("-----------------");
        for (Integer value : leastConnection.values()) {
            System.out.println("value: " + value);
        }
        T object = smallest.executeTask(t);

        connectionStatus(smallest,-1);
        System.out.println("-----------------");
        for (Integer value : leastConnection.values()) {
            System.out.println("value: " + value);
        }
        System.out.println("-----------------");
        return object;





    }


}
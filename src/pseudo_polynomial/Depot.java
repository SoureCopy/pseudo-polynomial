package pseudo_polynomial;

import java.util.ArrayList;
import java.util.Comparator;

public class Depot extends Point  {

    ArrayList<Client> cluster = new ArrayList<Client>(); //set of assigned clients
    int depotFleet;

    Depot(int id, int startWindow, int endWindow, int depotFleet){
        this.id = id;
        this.isDepot = true;
        this.startWindow=startWindow;
        this.endWindow=endWindow;
        this.depotFleet = depotFleet;
    }

    Depot(){
    }

    //new comparator for depots
   /* @Override
    public int compare(Depot d1, Depot d2){
        return Double.compare((double)d2.demands, (double)d1.demands);
    }*/
}

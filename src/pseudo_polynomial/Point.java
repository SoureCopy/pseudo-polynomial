package pseudo_polynomial;

/**
 * Created by Soure on 18.04.2017.
 */
public abstract class Point {

    int id;
    boolean isDepot;
    int startWindow; //in minutes, 0=00:00, 1439=23:59
    int endWindow;

    @Override
    public String toString(){
        return "(id#"+id+"; Time:"+startWindow+":"+endWindow+")";
    }

}

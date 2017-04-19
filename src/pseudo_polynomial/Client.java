package pseudo_polynomial;

/**
 * Created by Soure on 18.04.2017.
 */
public class Client extends Point {

    Client(int id, int startWindow, int endWindow){
        this.id=id;
        this.isDepot=false;
        this.startWindow=startWindow;
        this.endWindow=endWindow;
    }

    Client(){
    }

    double getTime(MatrixDistance matr, int myPoint, int posPoint ){
        double pam_dist;
        pam_dist=0.5*Math.sqrt(Math.pow(matr.matrix[posPoint][myPoint].dist,2));
        pam_dist+=0.5*Math.abs((endWindow-startWindow-matr.matrix[posPoint][myPoint].point.endWindow+matr.matrix[posPoint][myPoint].point.startWindow)/2);
        return pam_dist;
    }

    //new comparator for clients
  /*  @Override
    public int compare(Client c1, Client c2){
        return Double.compare(c2.prior, c1.prior);
    }
*/
}

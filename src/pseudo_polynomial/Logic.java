package pseudo_polynomial;

import java.util.ArrayList;

public class Logic {

    MatrixDistance matr;
    int amountDepot;
    int amountClient;
    ArrayList<Depot> depots = new ArrayList<Depot>();

    public void test() {
        matr = new MatrixDistance();
        matr.matrix = new ObjectMatrix[7][6];
        amountClient=6;
        amountDepot=1;

        for (int i = 0; i < amountDepot; i++) {
            Depot d1 = new Depot();
            d1.id = i;
            d1.startWindow = (int)(Math.random()*1438);
            d1.isDepot=true;
            int endWindow = (int)(Math.random()*1439);
            while (endWindow<=d1.startWindow) {
                endWindow = (int)(Math.random()*1439);
            }
            d1.endWindow = endWindow;
            d1.depotFleet = 2;
            depots.add(d1);
        }
        for (int i = 0; i < amountClient; i++) {
            Client client = new Client();
            client.id = i;
            int startWindow = (int)(Math.random()*1438);
            while (startWindow>depots.get(0).startWindow){
                startWindow = (int)(Math.random()*1438);
            }
            client.startWindow = startWindow;
            int endWindow = (int)(Math.random()*1439);
            while ((endWindow<=client.startWindow)||(endWindow<depots.get(0).endWindow)) {
                endWindow = (int)(Math.random()*1439);
            }
            client.endWindow = endWindow;
            matr.clients.add(client);
        }
        for (int i = 0; i < matr.matrix.length; i++) {
            if (i==0){
                System.out.println("-----------------depots-------------------");
            }
            for (int j = 0; j < matr.matrix[i].length; j++) {
                ObjectMatrix objM;
                if (i < amountDepot) {
                    objM = new ObjectMatrix(depots.get(i), matr.clients.get(j), (int) (Math.random() * 15 + 1));
                }
                else if (i-amountDepot==j){
                    objM = new ObjectMatrix(matr.clients.get(i-amountDepot), matr.clients.get(j), 0);
                }
                else if (i-amountDepot<j){
                    objM = new ObjectMatrix(matr.clients.get(i-amountDepot), matr.clients.get(j), (int) (Math.random() * 15 + 1));
                }
                else {
                    objM = new ObjectMatrix(matr.clients.get(i-amountDepot), matr.clients.get(j), matr.matrix[j+amountDepot][i-amountDepot].dist);
                }
                matr.matrix[i][j] = objM;
                System.out.print(" " + matr.matrix[i][j].dist);
            }
            System.out.println();
            if (i==amountDepot-1){
                System.out.println("-----------------clients------------------");
            }
        }
        for(int i=0; i<amountClient; i++){
            depots.get(0).cluster.add(matr.clients.get(i));
        }
    }

    private ArrayList<Point> findRoutes(MatrixDistance matr){
        ArrayList<Point> routes = new ArrayList<Point>();
        return routes;
    }

    public void showRoutes(){
        for(int i=0; i<depots.size(); i++){
            System.out.print(depots.get(i)+" --- ");
            for(int k=0; k<depots.get(i).cluster.size(); k++){
                System.out.print(depots.get(i).cluster.get(k)+" ");
            }
            System.out.println();
        }
    }
}

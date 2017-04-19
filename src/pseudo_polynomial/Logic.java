package pseudo_polynomial;

import java.util.ArrayList;

public class Logic {

    MatrixDistance matr;
    int amountDepot;
    int amountClient;
    ArrayList<Depot> depots = new ArrayList<Depot>();
    int serviceTime = 5;

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

    private ArrayList<ArrayList<Point>> findRoutes(MatrixDistance matr){
        ArrayList<ArrayList<Point>> routes = new ArrayList<ArrayList<Point>>(3);
        int startTime=0, currentTime=0;
        double temporary=0;
        int cost = 0;
        boolean check = false;
        for(int k=0;; k++){
            ArrayList<Point> inner = new ArrayList<Point>();
            routes.add(inner);
            routes.get(k).add(depots.get(0));
            for(int i=0; i<amountClient; i++) {
                if(!matr.matrix[0][i].isChecked){
                    matr.matrix[0][i].isChecked = true;
                    if (matr.matrix[0][i].dist + serviceTime + matr.matrix[0][i].dist <= matr.matrix[0][i].point.endWindow) {
                        routes.get(k).add(matr.matrix[0][i].client);
                        startTime = matr.matrix[0][i].client.startWindow - serviceTime - (int) matr.matrix[0][i].dist;
                        currentTime = matr.matrix[0][i].client.startWindow + serviceTime;
                        for (int j = 0; j < amountClient; j++) {
                            temporary = 0;
                            boolean flag = false;
                            if ((j != i)&&(!matr.matrix[i][j].isChecked)) {
                                if (currentTime + matr.matrix[i][j].dist <= matr.matrix[i][j].client.startWindow){
                                   temporary = matr.matrix[i][j].client.startWindow - currentTime - matr.matrix[i][j].dist;
                                }
                                if (currentTime+matr.matrix[i][j].dist+temporary+serviceTime+matr.matrix[0][j].dist
                                    <= matr.matrix[0][j].point.endWindow){
                                    routes.get(k).add(matr.matrix[i][j].client);
                                    for(int m=0, l=1; m<amountClient && l<amountClient+1; m++, l++) {
                                        matr.matrix[i][m].isChecked = true;
                                        matr.matrix[l][i].isChecked = true;
                                    }
                                    if(temporary<=0) {
                                        currentTime = currentTime + (int)matr.matrix[i][j].dist + serviceTime;
                                    }
                                    else {
                                        currentTime = matr.matrix[i][j].client.startWindow + serviceTime;
                                    }
                                    flag = true;
                                }
                            }
                            if (((j==amountClient-1)&&((j==i)||(matr.matrix[i][j].isChecked)))&&(!flag)){
                                routes.get(k).add(matr.matrix[0][i].point);
                                for(int m=0, l=1; m<amountClient && l<amountClient+1; m++, l++) {
                                    matr.matrix[i][m].isChecked = true;
                                    matr.matrix[l][i].isChecked = true;
                                }
                                currentTime += (int)matr.matrix[0][i].dist;
                            }
                            i=j;
                        }
                     }
                 }
            }
            cost += currentTime - startTime;
            for(int i=0; i<amountClient; i++){
                if(!matr.matrix[0][i].isChecked) {
                    check = true;
                    break;
                }
            }
            if (!check){
                break;
            }
        }
        return routes;
    }

    public void showRoutes(){
        ArrayList<ArrayList<Point>> res = findRoutes(matr);
        for(int i=0; i<res.size(); i++){
            System.out.print(res.get(i)+" --- ");

            System.out.println();
        }
    }

    public void pamCluster(){
        for (int i=0; i<matr.matrix[0].length; i++){
            for(int j=0; j<matr.matrix.length; j++) {
                matr.matrix[j][i].dist = matr.matrix[j][i].client.getPamDistance(matr,i,j);
            }
        }
        for (int i=0; i<matr.clients.size(); i++){
            for(int j=0; j<matr.matrix[0].length; j++){
                if (matr.matrix[0][j].client.id == matr.clients.get(i).id){
                    depots.get(findBestDepot(j)).cluster.add(matr.clients.get(j));
                }
            }
        }
    }

    private int findBestDepot(int j){
        double record=0.0;
        double temp=0.0;
        int numbDepot=-1;
        for(int k=0; k<depots.size(); k++){
            for(int i=0; i<matr.matrix.length; i++){
                if((depots.get(k).id==matr.matrix[i][j].point.id)&&(matr.matrix[i][j].point.isDepot)){
                    temp = matr.matrix[i][j].dist;
                    break;
                }
            }
            for(int l=0; l<depots.get(k).cluster.size(); l++){
                for(int i=0; i<matr.matrix.length; i++){
                    if ((depots.get(k).cluster.get(l).id==matr.matrix[i][j].point.id)&&(!matr.matrix[i][j].point.isDepot)) {
                        temp += matr.matrix[i][j].dist;
                        break;
                    }
                }
            }
            temp/=(depots.get(k).cluster.size()+1);
            temp = findMedoid(temp, j, k);
            if ((temp<record)||(record==0.0)){
                record=temp;
                numbDepot=k;
            }
        }
        return numbDepot;
    }

    private double findMedoid(double temp, int j, int k){
        double eps=-1.0;
        double medoid=-1.0;
        for(int i=0; i<matr.matrix.length; i++){
            if((depots.get(k).id==matr.matrix[i][j].point.id)&&(matr.matrix[i][j].point.isDepot)){
                eps=Math.abs(temp-matr.matrix[i][j].dist);
                medoid=matr.matrix[i][j].dist;
                break;
            }
        }
        for(int l=0; l<depots.get(k).cluster.size(); l++){
            for(int i=0; i<matr.matrix.length; i++){
                if ((depots.get(k).cluster.get(l).id==matr.matrix[i][j].point.id)&&(!matr.matrix[i][j].point.isDepot)) {
                    if(Math.abs(matr.matrix[i][j].dist-temp)<eps){
                        eps=Math.abs(matr.matrix[i][j].dist-temp);
                        medoid=matr.matrix[i][j].dist;
                    }
                    break;
                }
            }
        }
        return medoid;
    }

    public void showCluster(){
        for(int i=0; i<depots.size(); i++){
            System.out.print(depots.get(i)+" --- ");
            for(int k=0; k<depots.get(i).cluster.size(); k++){
                System.out.print(depots.get(i).cluster.get(k)+" ");
            }
            System.out.println();
        }
    }

}

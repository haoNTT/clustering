/**
 * Created by haonantian on 25/11/2017.
 */
public class Cluster {
    private int [][] clusters;

    public Cluster(){}

    public Cluster(int i, int j){
        this.clusters = new int[i][j];
        for (int c1 = 0; c1 < i; c1++){
            for (int c2 = 0; c2 < j; c2++){
                clusters[c1][c2] = -1;
            }
        }
    }

    boolean checkInArray(int item){ /*Check if the centroid is already existed */
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i][0] == item) {
                return true;
            }
        }
        return false;
    }

    public void setLabel(int position,int label){
        clusters[position][0] = label;
    }

    public int getLabel(int i, int j){
        return clusters[i][j];
    }

    public int getCluster(int position){
        return clusters[position][0];
    }

    public int getClusterLength(int position){
        int counter = 0;
        while (clusters[position][counter]!=-1 && counter < clusters[position].length){
            counter = counter + 1;
        }
        return counter;
    }

    public void addToCluster(int position, int pointID){
        int counter = 0;
        while (counter < clusters[position].length){
            if (clusters[position][counter] == -1){
                clusters[position][counter] = pointID;
                break;
            } else {
                counter ++;
            }
        }
    }
    public int[] getIthCluster(int i){
        return clusters[i];
    }
}

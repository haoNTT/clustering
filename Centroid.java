/**
 * Created by haonantian on 27/11/2017.
 */
public class Centroid {
    float[][] centroid;
    int howMany;

    public Centroid(int numCluster, int maxDim){
        this.centroid = new float[numCluster][maxDim+1];
        this.howMany = 0;
    }

    public void add(float[] newCen){
        for (int i = 0 ; i < newCen.length; i ++){
            centroid[howMany][i] = newCen[i];
        }
        howMany ++;
    }

    public float[] getCertainCen(int position){
        return centroid[position];
    }
}

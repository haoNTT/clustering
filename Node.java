/**
 * Created by haonantian on 25/11/2017.
 */
public class Node {
    private int currDim;
    private float currValue;
    private Node nextNode;

    public Node(){}

    public Node(int currDim, float currValue){
        this.currDim = currDim;
        this.currValue = currValue;
    }

    public Node(int firstID){
        this.currValue = firstID;
    }

    public void setNextNode(Node next){
        this.nextNode = next;
    }

    public Node getNextNode(){
        return nextNode;
    }

    public void setCurrDim(int currDim){
        this.currDim = currDim;
    }

    public int getCurrDim(){
        return currDim;
    }

    public void setCurrValue(float value){
        this.currValue = value;
    }

    public float getCurrValue(){
        return currValue;
    }
}

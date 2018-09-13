/**
 * Created by haonantian on 25/11/2017.
 */
public class LinkedList {
    private Node firstNode;
    private int numberOfEntries;

    public LinkedList(){
        firstNode = null;
        numberOfEntries = 0;
    }
    public int getNumberOfEntries(){
        return numberOfEntries;
    }

    public LinkedList(Node firstNode){
        this.firstNode = firstNode;
    }

    public Node getFirstNode(){
        return firstNode;
    }

    public Node getNodeAt(int givenPosition){
        Node currentNode = firstNode;
        for (int counter = 0; counter < givenPosition; counter++){
            currentNode = currentNode.getNextNode();
        }
        return currentNode;
    }

    public boolean add(Node newNode){
        if (newNode == null) {
            return false;
        } else if (firstNode == null){
            firstNode = newNode;
            return true;
        } else {
            Node currNode = getNodeAt(numberOfEntries);
            currNode.setNextNode(newNode);
            numberOfEntries ++;
            return true;
        }
    }

    /*public Node contain(Node checkNode, LinkedList listToParse){
        Node currNode = listToParse.getFirstNode();
        for (int i = 0; i < numberOfEntries; i++){
            currNode = currNode.getNextNode();
            if (currNode!=null && currNode.getCurrDim() == checkNode.getCurrDim()){
                return currNode;
            }
        }
        return null;
    }*/

}

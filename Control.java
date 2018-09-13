/**
 * Created by haonantian on 24/11/2017.
 */
import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.*;
public class Control {
    public static float[] getCentroid(int position, Centroid cent){
        return cent.getCertainCen(position);
    }
    public static int findMaxDim(LinkedList[] dataSet){
        int maxDim = 0;
        for (int i =0; i < dataSet.length; i++){
            Node currNode = dataSet[i].getFirstNode();
            currNode = currNode.getNextNode();
            while (currNode != null){
                if (currNode.getCurrDim() > maxDim){
                    maxDim = currNode.getCurrDim();
                    currNode = currNode.getNextNode();
                } else {
                    currNode = currNode.getNextNode();
                }
            }
        }
        return maxDim;
    }
    public static float[][] convertSet(LinkedList[] dataSet,int maxDim){
        float[][] outputSet = new float[dataSet.length][maxDim+1];
        int counter = 0;
        while (counter < dataSet.length){
            Node currNode = dataSet[counter].getFirstNode();
            outputSet[counter][0] = currNode.getCurrValue();
            currNode = currNode.getNextNode();
            int currPo = 1;
            while (currNode!=null){
                int dimNum = currNode.getCurrDim();
                if (dimNum == currPo){
                    outputSet[counter][currPo] = currNode.getCurrValue();
                    currNode = currNode.getNextNode();
                    currPo ++;
                } else if (dimNum > currPo){
                    for (int l = currPo; l < dimNum; l++){
                        outputSet[counter][l] = 0;
                    }
                    currPo = dimNum+1;
                    outputSet[counter][dimNum] = currNode.getCurrValue();
                    currNode = currNode.getNextNode();
                }
            }
            counter ++;
        }
        return outputSet;
    }

    public static Node contains(Node node, LinkedList list){
        Node currNode = list.getFirstNode();
        currNode = currNode.getNextNode();
        while (currNode !=null){
            if (currNode.getCurrDim() == node.getCurrDim()) {
                return currNode;
            } else {
                currNode = currNode.getNextNode();
            }
        }
        return null;
    }

    public static float calculateDist(LinkedList checkNode, LinkedList central){/*Calculate distances between two lists*/
        Node currNodeForList = checkNode.getFirstNode();
        currNodeForList = currNodeForList.getNextNode();
        float total = 0;
        while (currNodeForList != null){
            Node nodeInCentral = contains(currNodeForList,central);
            if (nodeInCentral == null){
                float temp = (currNodeForList.getCurrValue()) * (currNodeForList.getCurrValue());
                total = total + temp;
            } else {
                float temp = (nodeInCentral.getCurrValue() - currNodeForList.getCurrValue())
                        * (nodeInCentral.getCurrValue() - currNodeForList.getCurrValue());
                total = total + temp;
            }
            currNodeForList = currNodeForList.getNextNode();
        }
        return total;
    }

    public static float calculateDist2(float[] arrayList, float[] central, int maxDim){
        float total = 0;
        for (int i = 1; i < maxDim+1; i++){
            total = total + (arrayList[i] - central[i]) * (arrayList[i] - central[i]);
        }
        return total;
    }

    public static float calculateDist3(float[] arrayList, float[] central, int maxDim){
        float result = 0;
        float sum1 = 0;
        for (int i = 1; i < maxDim+1; i++){
            sum1 = sum1 + arrayList[i] * central[i];
        }
        float length1 = 0;
        for (int j = 1; j < maxDim+1; j++){
            length1 = length1 + arrayList[j]*arrayList[j];
        }
        length1 = (float) Math.sqrt((double)length1);
        float length2 = 0;
        for (int h = 1; h < maxDim + 1; h++){
            length2 = length2 + central[h]*central[h];
        }
        length2 = (float) Math.sqrt((double)length2);
        result = sum1/(length1*length2);
        return result;
    }
    public static float calculateDist4(float[] arrayList, float[] central, float[] globalCent, int weight, int maxDim){
        float [] combined = new float[maxDim+1];
        for (int i = 1; i < maxDim+1; i ++){
            float value = arrayList[i] + central[i]*weight;
            value = value/(weight+1);
            combined[i] = value;
        }
        /*float afterAdd =  calculateDist3(combined,globalCent,maxDim);
        float beforeAdd = calculateDist3(central,globalCent,maxDim);*/
        float total = 0;
        for (int j = 1; j < maxDim+1; j++){
            total = total + (combined[j] * globalCent[j]);
        }
        float length = 0;
        for (int k = 1; k < maxDim+1 ; k++){
            length = length + combined[k]*combined[k];
        }
        length = (float)Math.sqrt((double)length);
        return (total/length)*(weight+1);
    }
    public static float help4(float[] arrayList, float[] globalCen, int weight, int maxDim){
        float total = 0;
        for (int i = 1; i < maxDim+1 ; i++){
            total = total + arrayList[i] * globalCen[i];
        }
        float length = 0;
        for (int j = 1; j < maxDim+1; j ++){
            length = length + arrayList[j]*arrayList[j];
        }
        length =(float)Math.sqrt((double)length);
        return (total/length)*weight;
    }
    public static float[] findGlobalCentroid(float[][] arraySet, int maxDim){
        float [] globalCen = new float[maxDim+1];
        for (int i = 1; i < maxDim+1; i++){
            float result = 0;
            for (int j = 0; j < arraySet.length; j++){
                result = result + arraySet[j][i];
            }
            result = result / arraySet.length;
            globalCen[i] = result;
        }
        return globalCen;
    }
    public static int inTheList(String topic, String[] list){
        for (int i = 0; i < list.length; i++){
            if (list[i]!=null && topic.equals(list[i])){
                return i;
            }
        }
        return -1;
    }
    public static void addToTopicList(String topic, String[] topicList){
        for (int i = 0; i < topicList.length; i ++){
            if (topicList[i]==null){
                topicList[i] = topic;
                break;
            }
        }
    }
    public static String findInClassMatrix(int ID, String[][] classM){
        String newID = String.valueOf(ID);
        for (int i = 0; i < classM.length; i++){
            if (classM[i][0].equals(newID)){
                return classM[i][1];
            }
        }
        return null;
    }

    public static void main(String args[]) throws IOException {
        /* Read in command line */
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();
        String[] inputCommand = name.split(" ");
        String inputFile = inputCommand[1];
        String criterionFunction = inputCommand[2];
        String classFile = inputCommand[3];
        int numberCluster = Integer.parseInt(inputCommand[4]);
        int numberTrail = Integer.parseInt(inputCommand[5]);
        String outputFile = inputCommand[6];
        /*Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your file name: ");
        String inputFile = scanner.next();
        System.out.print("Your criterionFunction: ");
        String criterionFunction = scanner.next();
        System.out.print("Enter your class file: ");
        String classFile = scanner.next();
        System.out.print("How many clusters: ");
        int numberCluster = scanner.nextInt();
        System.out.print("How many trails: ");
        int numberTrail = scanner.nextInt();*/
        /* Read in class file (i,j) articleID + label(topic) */
        BufferedReader classFileRder = new BufferedReader(new FileReader(classFile));
        int counter = 0;
        String line;
        while ((line = classFileRder.readLine()) != null){
            counter += 1;
        }
        System.out.println(counter);
        String[][] classMatrix = new String[counter][2];
        BufferedReader classFileRder2 = new BufferedReader(new FileReader(classFile));
        int counter2 = 0;
        String classLine;
        while ((classLine = classFileRder2.readLine())!=null){
            String[] currentLine = classLine.split(",");
            classMatrix[counter2][0] = currentLine[0];
            classMatrix[counter2][1] = currentLine[1];
            counter2 ++;
        }
        int topicNum = 0;
        String[] topicList = new String[counter2];
        for (int i = 0; i < classMatrix.length; i ++){
            if (inTheList(classMatrix[i][1],topicList) == -1){
                addToTopicList(classMatrix[i][1],topicList);
            }
        }
        int topicLength = 0;
        for (int i = 0; i < topicList.length; i++){
            if (topicList[i]!=null){
                topicLength ++;
            }
        }
        int [] topicStack = new int[20]; /*Used to store how many articles belong to each topic*/
        for (int i = 0; i < topicStack.length; i++){
            String topicName = topicList[i];
            int howMany = 0;
            for (int j = 0; j < classMatrix.length; j++){
                if (classMatrix[j][1].equals(topicName)){
                    howMany ++;
                }
            }
            topicStack[i] = howMany;
        }
        System.out.println(topicLength);
        System.out.println("!!!!!!!!!!!");
        /* Read in input File (vectors) */
        BufferedReader inputFileRder = new BufferedReader(new FileReader(inputFile));
        String line2;
        LinkedList[] dataSet = new LinkedList[counter2];
        int counter3 = 0;
        while ((line2 = inputFileRder.readLine())!= null){
            String[] result = line2.split(",");
            int len = result.length;
            Node newNode = new Node(Integer.parseInt(result[0]));
            LinkedList currList = new LinkedList(newNode);
            dataSet[counter3] = currList;
            int counter4 = 1;
            while (counter4 < (len-1)/2){
                Node currNod = new Node(Integer.parseInt(result[counter4]),Float.parseFloat(result[counter4+1]));
                currList.add(currNod);
                counter4 = counter4 + 2;
            }
            counter3 ++;
        }
        int maxDim = findMaxDim(dataSet);
        float[][] arraySet = convertSet(dataSet,maxDim);
        /* Operation Loop */
        int[] seedSet = {1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39};
        if (criterionFunction.equals("SSE")){
            float bestSSE = Long.MAX_VALUE;
            Centroid bestCent = new Centroid(numberCluster,maxDim);
            Cluster bestCluster = new Cluster(numberCluster,counter2);
            for (int trailCounter = 1; trailCounter <= numberTrail; trailCounter++){
                Random rand = new Random(seedSet[trailCounter-1]);
                Cluster centroids = new Cluster(numberCluster,counter2);
                Centroid centroid = new Centroid(numberCluster,maxDim);
                int previousNum = -1;
                for (int i = 0; i < numberCluster; i++){
                    int nextCen = rand.nextInt(counter2-1);
                    while (nextCen == previousNum){
                        nextCen = rand.nextInt(counter2-1);
                    }
                    float [] nextCentroid = arraySet[nextCen];
                    centroid.add(nextCentroid);
                    previousNum = nextCen;
                }
                float oldSSE = 0;
                while (true){
                    int counter5 = 0;
                    float newSSE = 0;
                    while (counter5 < counter2){
                        float minDict = Long.MAX_VALUE;
                        int minCluster = -1;
                        for (int i = 0; i < numberCluster; i ++){
                            float temp = calculateDist2(arraySet[counter5],getCentroid(i,centroid),maxDim);
                            if (temp < minDict){
                                minDict = temp;
                                minCluster = i;
                            }
                        }
                        newSSE = newSSE + minDict;
                        centroids.addToCluster(minCluster,counter5);
                        counter5 ++;
                    }
                    System.out.println("!!!!Finished One Load");
                    System.out.println("!!!!Clusters lengths are: "+ centroids.getClusterLength(0)
                            + " " + centroids.getClusterLength(1) + " " + centroids.getClusterLength(2));
                    if ((oldSSE == newSSE ||(float)((Math.abs(oldSSE-newSSE))/oldSSE)<0.05) && newSSE < bestSSE){
                        bestSSE = newSSE;
                        bestCent = centroid;
                        bestCluster = centroids;
                        break;
                    }
                    else if ((oldSSE == newSSE || (float)((Math.abs(oldSSE-newSSE))/oldSSE)<0.05) && newSSE >= bestSSE) {
                        break;
                    } else {
                        oldSSE = newSSE;
                        newSSE = 0;
                        Cluster newCentroids = new Cluster(numberCluster,counter2);
                        Centroid newCent = new Centroid(numberCluster,maxDim);
                        for (int p = 0; p < numberCluster; p++){
                            float[] newCen = new float[maxDim+1];
                            newCen[0] = -1;
                            for (int j = 1; j < maxDim+1; j++){
                                float value = 0;
                                for (int h = 0; h < centroids.getClusterLength(p); h++){
                                    value = value + arraySet[centroids.getLabel(p,h)][j];
                                }
                                value = value/centroids.getClusterLength(p);
                                newCen[j] = value;
                            }
                            newCent.add(newCen);
                        }
                        System.out.println("!!!!Finish One Round!!!!");
                        System.out.println("!!!!Clusters lengths are: "+centroids.getClusterLength(0)+" "
                                +centroids.getClusterLength(1)+" "+centroids.getClusterLength(2));
                        centroid = newCent;
                        centroids = newCentroids;
                    }
                }
                System.out.println("@@@Finish One Iterations@@@");
            }
            System.out.println("!!!!About to write out");
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                //String fileName = "outputForSSE" + String.valueOf(numberCluster);
                fw = new FileWriter(outputFile);
                bw = new BufferedWriter(fw);
                for (int i = 0; i < numberCluster; i ++){
                    for (int j = 0; j < bestCluster.getClusterLength(i); j++){
                        bw.write(String.valueOf((int)arraySet[bestCluster.getLabel(i,j)][0]) + "," + String.valueOf(i));
                        bw.write("\n");
                    }
                }

                System.out.println("Done");

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (bw != null)
                        bw.close();

                    if (fw != null)
                        fw.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                }

            }
            int [][] classReportMatrix = new int[numberCluster][20];
            for (int nC = 0; nC < numberCluster; nC++){
                for (int inner = 0; inner < bestCluster.getClusterLength(nC); inner++){
                    int ID = (int)arraySet[bestCluster.getLabel(nC,inner)][0];
                    String topi = findInClassMatrix(ID, classMatrix);
                    int where = inTheList(topi,topicList);
                    classReportMatrix[nC][where] ++;
                }
            }
            float[] entropy = new float[numberCluster];
            float[] purity = new float[numberCluster];
            int[] clusterSize = new int[numberCluster];
            for (int ro = 0; ro < numberCluster; ro++){
                int size = bestCluster.getClusterLength(ro);
                clusterSize[ro] = size;
                float total = 0;
                float maxTemp = 0;
                for (int ro2 = 0; ro2 < 20; ro2++){
                    float temp = ((float)(classReportMatrix[ro][ro2])/(float)(bestCluster.getClusterLength(ro)));
                    if (temp!=0){
                        total = total + (temp * (float)(Math.log((double)temp)/Math.log(2)));
                    }
                    if (temp != 0 && temp > maxTemp){
                        maxTemp = temp;
                    }
                }
                entropy[ro] = 0-total;
                purity[ro] = maxTemp;
            }
            float totalEntropy = 0;
            float totalPurity = 0;
            for (int la = 0; la < numberCluster; la++){
                totalEntropy = totalEntropy + entropy[la]*clusterSize[la];
                totalPurity = totalPurity + purity[la]*clusterSize[la];
            }
            totalEntropy = totalEntropy/counter2;
            totalPurity = totalPurity/counter2;
            System.out.println("Best SSE "+ bestSSE + " Entropy " + totalEntropy + " Purity " + totalPurity);
        } else if (criterionFunction.equals("I1")){
            float bestI1 = Long.MIN_VALUE;
            Centroid bestCent = new Centroid(numberCluster,maxDim);
            Cluster bestCluster = new Cluster(numberCluster,counter2);
            for (int trailCounter = 1; trailCounter <= numberTrail; trailCounter++) {
                Random rand = new Random(seedSet[trailCounter-1]);
                Cluster centroids = new Cluster(numberCluster,counter2);
                Centroid centroid = new Centroid(numberCluster,maxDim);
                int previousNum = -1;
                for (int i = 0; i < numberCluster; i++){
                    int nextCen = rand.nextInt(counter2-1);
                    while (nextCen == previousNum){
                        nextCen = rand.nextInt(counter2-1);
                    }
                    float [] nextCentroid = arraySet[nextCen];
                    centroid.add(nextCentroid);
                    previousNum = nextCen;
                }
                float oldI1 = 0;
                while (true){
                    int counter5 = 0;
                    float newI1 = 0;
                    while (counter5 < counter2){
                        float minDict = Long.MIN_VALUE;
                        int minCluster = -1;
                        for (int i = 0; i < numberCluster; i ++){
                            float temp = calculateDist3(arraySet[counter5],getCentroid(i,centroid),maxDim);
                            if (temp > minDict){
                                minDict = temp;
                                minCluster = i;
                            }
                        }
                        newI1 = newI1 + minDict;
                        centroids.addToCluster(minCluster,counter5);
                        counter5 ++;
                    }
                    System.out.println("!!!!Finished One Load");
                    System.out.println("!!!!Clusters lengths are: "+ centroids.getClusterLength(0)
                            + " " + centroids.getClusterLength(1) + " " + centroids.getClusterLength(2));
                    if ((oldI1 == newI1 || (float)((Math.abs(oldI1-newI1))/oldI1)<0.05)&&(newI1>bestI1)) {
                        bestI1 = newI1;
                        bestCent = centroid;
                        bestCluster = centroids;
                        break;
                    } else if ((oldI1 == newI1 || (float)((Math.abs(oldI1-newI1))/oldI1)<0.05)&&(newI1<=bestI1)) {
                        break;
                    } else {
                        oldI1 = newI1;
                        newI1 = 0;
                        Cluster newCentroids = new Cluster(numberCluster,counter2);
                        Centroid newCent = new Centroid(numberCluster,maxDim);
                        for (int p = 0; p < numberCluster; p++){
                            float[] newCen = new float[maxDim+1];
                            newCen[0] = -1;
                            for (int j = 1; j < maxDim+1; j++){
                                float value = 0;
                                for (int h = 0; h < centroids.getClusterLength(p); h++){
                                    value = value + arraySet[centroids.getLabel(p,h)][j];
                                }
                                value = value/centroids.getClusterLength(p);
                                newCen[j] = value;
                            }
                            newCent.add(newCen);
                        }
                        System.out.println("!!!!Finish One Round!!!!");
                        System.out.println("!!!!Clusters lengths are: "+centroids.getClusterLength(0)+" "
                                +centroids.getClusterLength(1)+" "+centroids.getClusterLength(2));
                        centroid = newCent;
                        centroids = newCentroids;
                    }
                }
                System.out.println("@@@Finish One Iterations@@@");
            }
            System.out.println("!!!!About to write out");
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                //String fileName = "outputForI1" + String.valueOf(numberCluster);
                fw = new FileWriter(outputFile);
                bw = new BufferedWriter(fw);
                for (int i = 0; i < numberCluster; i ++){
                    for (int j = 0; j < bestCluster.getClusterLength(i); j++){
                        bw.write(String.valueOf((int)arraySet[bestCluster.getLabel(i,j)][0]) + "," + String.valueOf(i));
                        bw.write("\n");
                    }
                }

                System.out.println("Done");

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (bw != null)
                        bw.close();

                    if (fw != null)
                        fw.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                }
            }
            int [][] classReportMatrix = new int[numberCluster][20];
            for (int nC = 0; nC < numberCluster; nC++){
                for (int inner = 0; inner < bestCluster.getClusterLength(nC); inner++){
                    int ID = (int)arraySet[bestCluster.getLabel(nC,inner)][0];
                    String topi = findInClassMatrix(ID, classMatrix);
                    int where = inTheList(topi,topicList);
                    classReportMatrix[nC][where] ++;
                }
            }
            float[] entropy = new float[numberCluster];
            float[] purity = new float[numberCluster];
            int[] clusterSize = new int[numberCluster];
            for (int ro = 0; ro < numberCluster; ro++){
                int size = bestCluster.getClusterLength(ro);
                clusterSize[ro] = size;
                float total = 0;
                float maxTemp = 0;
                for (int ro2 = 0; ro2 < 20; ro2++){
                    float temp = ((float)(classReportMatrix[ro][ro2])/(float)(bestCluster.getClusterLength(ro)));
                    if (temp!=0){
                        total = total + (temp * (float)(Math.log((double)temp)/Math.log(2)));
                    }
                    if (temp != 0 && temp > maxTemp){
                        maxTemp = temp;
                    }
                }
                entropy[ro] = 0-total;
                purity[ro] = maxTemp;
            }
            float totalEntropy = 0;
            float totalPurity = 0;
            for (int la = 0; la < numberCluster; la++){
                totalEntropy = totalEntropy + entropy[la]*clusterSize[la];
                totalPurity = totalPurity + purity[la]*clusterSize[la];
            }
            totalEntropy = totalEntropy/counter2;
            totalPurity = totalPurity/counter2;
            System.out.println("Best I1 "+ bestI1 + " Entropy " + totalEntropy + " Purity " + totalPurity);
        } else if(criterionFunction.equals("E1")){
            float bestE1 = Long.MAX_VALUE;
            Centroid bestCent = new Centroid(numberCluster,maxDim);
            Cluster bestCluster = new Cluster(numberCluster,counter2);
            for (int trailCounter = 1; trailCounter <= numberTrail; trailCounter++) {
                Random rand = new Random(seedSet[trailCounter-1]);
                Cluster centroids = new Cluster(numberCluster,counter2);
                Centroid centroid = new Centroid(numberCluster,maxDim);
                int previousNum = -1;
                for (int i = 0; i < numberCluster; i++){
                    int nextCen = rand.nextInt(counter2-1);
                    while (nextCen == previousNum){
                        nextCen = rand.nextInt(counter2-1);
                    }
                    float [] nextCentroid = arraySet[nextCen];
                    centroid.add(nextCentroid);
                    previousNum = nextCen;
                }
                float oldE1 = 0;
                float[] globalCen = findGlobalCentroid(arraySet,maxDim);
                while (true) {
                    int counter5 = 0;
                    float newE1 = 0;
                    while (counter5 < counter2) {
                        float minDict = Long.MAX_VALUE;
                        int minCluster = -1;
                        for (int i = 0; i < numberCluster; i++) {
                            float temp = calculateDist4(arraySet[counter5], getCentroid(i, centroid),
                                    globalCen, centroids.getClusterLength(i), maxDim);
                            for (int t = 0; t != i && t < numberCluster; t++) {
                                temp = temp + help4(centroid.getCertainCen(t), globalCen, centroids.getClusterLength(t), maxDim);
                            }
                            if (temp < minDict) {
                                minDict = temp;
                                minCluster = i;
                            }
                        }
                        newE1 = newE1 + minDict;
                        centroids.addToCluster(minCluster, counter5);
                        counter5++;
                    }
                    System.out.println("!!!!Finished One Load");
                    System.out.println("!!!!Clusters lengths are: " + centroids.getClusterLength(0)
                            + " " + centroids.getClusterLength(1) + " " + centroids.getClusterLength(2));
                    if (oldE1 == newE1 || (float) ((Math.abs(oldE1 - newE1)) / oldE1) < 0.05 && newE1 < bestE1) {
                        bestE1 = newE1;
                        bestCent = centroid;
                        bestCluster = centroids;
                        break;
                    } else if (oldE1 == newE1 || (float) ((Math.abs(oldE1 - newE1)) / oldE1) < 0.05 && newE1 >= bestE1) {
                        break;
                    }else {
                        oldE1 = newE1;
                        newE1 = 0;
                        Cluster newCentroids = new Cluster(numberCluster, counter2);
                        Centroid newCent = new Centroid(numberCluster, maxDim);
                        for (int p = 0; p < numberCluster; p++) {
                            float[] newCen = new float[maxDim + 1];
                            newCen[0] = -1;
                            for (int j = 1; j < maxDim + 1; j++) {
                                float value = 0;
                                for (int h = 0; h < centroids.getClusterLength(p); h++) {
                                    value = value + arraySet[centroids.getLabel(p, h)][j];
                                }
                                value = value / centroids.getClusterLength(p);
                                newCen[j] = value;
                            }
                            newCent.add(newCen);
                        }
                        System.out.println("!!!!Finish One Round!!!!");
                        System.out.println("!!!!Clusters lengths are: " + centroids.getClusterLength(0) + " "
                                + centroids.getClusterLength(1) + " " + centroids.getClusterLength(2));
                        centroid = newCent;
                        centroids = newCentroids;
                    }
                }
            }
            System.out.println("!!!!About to write out");
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                //String fileName = "outputForE1" + String.valueOf(numberCluster);
                fw = new FileWriter(outputFile);
                bw = new BufferedWriter(fw);
                for (int i = 0; i < numberCluster; i ++){
                    for (int j = 0; j < bestCluster.getClusterLength(i); j++){
                        bw.write(String.valueOf((int)arraySet[bestCluster.getLabel(i,j)][0]) + "," + String.valueOf(i));
                        bw.write("\n");
                    }
                }

                System.out.println("Done");

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (bw != null)
                        bw.close();

                    if (fw != null)
                        fw.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                }

            }
            int [][] classReportMatrix = new int[numberCluster][20];
            for (int nC = 0; nC < numberCluster; nC++){
                for (int inner = 0; inner < bestCluster.getClusterLength(nC); inner++){
                    int ID = (int)arraySet[bestCluster.getLabel(nC,inner)][0];
                    String topi = findInClassMatrix(ID, classMatrix);
                    int where = inTheList(topi,topicList);
                    classReportMatrix[nC][where] ++;
                }
            }
            float[] entropy = new float[numberCluster];
            float[] purity = new float[numberCluster];
            int[] clusterSize = new int[numberCluster];
            for (int ro = 0; ro < numberCluster; ro++){
                int size = bestCluster.getClusterLength(ro);
                clusterSize[ro] = size;
                float total = 0;
                float maxTemp = 0;
                for (int ro2 = 0; ro2 < 20; ro2++){
                    float temp = ((float)(classReportMatrix[ro][ro2])/(float)(bestCluster.getClusterLength(ro)));
                    if (temp!=0){
                        total = total + (temp * (float)(Math.log((double)temp)/Math.log(2)));
                    }
                    if (temp != 0 && temp > maxTemp){
                        maxTemp = temp;
                    }
                }
                entropy[ro] = 0-total;
                purity[ro] = maxTemp;
            }
            float totalEntropy = 0;
            float totalPurity = 0;
            for (int la = 0; la < numberCluster; la++){
                totalEntropy = totalEntropy + entropy[la]*clusterSize[la];
                totalPurity = totalPurity + purity[la]*clusterSize[la];
            }
            totalEntropy = totalEntropy/counter2;
            totalPurity = totalPurity/counter2;
            System.out.println("Best E1 "+ bestE1 + " Entropy " + totalEntropy + " Purity " + totalPurity);
        } else {
            System.out.println("!!!Wrong Objective Function!!!!");
        }

    }
}

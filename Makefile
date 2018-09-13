JCC = javac

JFLAGS = -g

default: Kcluster

Kcluster: Control.class

Control.class: Control.java
        $(JCC) $(JFLAGS) Control.java

Node.class: Node.java
        $(JCC) $(JFLAGS) Node.java

Cluster.class: Cluster.java
        $(JCC) $(JFLAGS) Cluster.java

Centroid.class: Centroid.java
	$(JCC) $(JFLAGS) Centroid.java

LinkedList.class: LinkedList.java
	$(JCC) $(JFLAGS) LinkedList.java

clean: 
        $(RM) *.class

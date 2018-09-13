# clustering
The purpose of this project is to write clustering algorithms based on k-means for clustering objects corresponding to sparse high dimensional vectors. The project consists of multiple components that involve getting the dataset, selecting the subset to cluster, pre-processing the dataset to convert it into a sparse representation, clustering the dataset, and evaluating the quality of the clustering solution. The steps associated with subset selection and pre-processing were done by using scripts in python, whereas the actual clustering were written in java.

The dataset for this assignment will be derived from the "Reuters-21578 Text Categorization Collection Data Set" that is available at the UCI Machine Learning Repository. The link to the dataset is: https://archive.ics.uci.edu/ml/datasets/Reuters-21578+Text+Categorization+Collection

The following is the pipeline of this project:

Load in dataset and clean the string file (This process is done by using python beautifulsoup package):
- Only article with single topic is selected
- Articles with 20 most frequent topics are retained

Obtain the sparce representation(This process is done by using python nltk package):
- Eliminate any nom-ascii characters
- Change all character cases to lower-case
- Replace any non alphanumeric characters with space
- Split the text into tokens, using space as the delimiter
- Eliminate any tokens that contain only digits
- Eliminate any tokens from the stop list that is provided (file stoplist.txt)
- Obtain the stem of each token using Porter's stemming algorithm; you can use any of the implementations provided at:https://tartarus.org/martin/PorterStemmer/
- Eliminate any tokens that occur less than 5 times

Vector representation Collect all the tokens that remained after the above preprocessing steps across all articles and use them to represent each article as a vector in the distinct token space. For each document, derive three different representations by using the following approaches to assign a value to each of the document's vector dimension that corresponds to a token t that it contains:
- The value will be the actual number of times that t occurs in the document (frequency)
- The value will be 1+sqrt(frequency) for those terms that have a non-zero frequency
- The value will be 1+log2(frequency) for those terms that have a non-zero frequency

Clustering A partitional clustering algorithm had been developed to implement
- Standard sum-of-squared-errors criterion function of k-means
- The I2 criterion function

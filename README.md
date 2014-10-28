# topic-extraction


This project is part of my master thesis project at KTH Royal Institute of Technology.

Project Title: Subtopic extraction using graph-based methods.

This code was used to explore subtopic extraction using search engine technology, combined with graph centrality ranking of topic candidates.

More info about the thesis project can be found at: http://www.nada.kth.se/~markusos/thesis/

## Example

Example result file: [result.html](docs/example/result.html)

### Input document:

Encyclopedia Britannicas definition of Tree:

> Tree, woody plant that regularly renews its growth (perennial).
> Most plants classified as trees have a single self-supporting trunk containing woody tissues,
> and in most species the trunk produces secondary limbs, called branches.

### Extracted Topics:

The system extracts the following topics, presented order of the ranking by the system:

1. Tree
2. Perennial plant
3. Plant stem
4. Herbaceous plant
5. Glossary of plant morphology terms

### Candidate Graph:

Extracted Candidate Graph used to rank the topics:

![example](docs/resultGraph.jpg)

## Setup

### Download:
- Download DBpedia Wikipedia Pagelinks Dataset: [page_links_bg.nt.bz2](http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/bg/page_links_bg.nt.bz2)
- Download and install [MongoDB](http://www.mongodb.org/)
- Download and install [ElasticSearch](http://www.elasticsearch.org/)
- Download and install [Graphviz](http://www.graphviz.org/) (Used to visualize the Candidate Graphs)

### Configure ElasticSearch

- Copy the stopwords.txt file to the config directory in your ElasticSearch folder.
- Add the following to your 'elasticsearch.yml' config file to use the provided stopword list:
>     index :
>       analysis :
>         analyzer :
>           default :
>             type : standard
>             stopwords_path : stopwords.txt

### Start services:
- Start MongoDB, run 'mongod' from your terminal.
- Start ElasticSearch, run 'elasticsearch' from your terminal.

### Setup the project:
- Extract 'page_links_bg.nt.bz2' in the project directory
- Run the command 'mvn clean install' in the project directory

### Run the topic indexing:

To build the ontology used for the topic extraction we use the DBpedia Wikipedia Pagelinks Dataset.

- Run 'java -jar topic-0.5-jar-with-dependencies.jar -i' to start the indexing.

   Expect the indexing to take several hours, depending on your computer performance and harddrive speed.
   It catalogues the whole link structure from Wikipedia into MongoDB for pre processing and then indexes the data into ElasticSearch.
   Keep an eye on the log.txt file to see the progress and the error.txt to see if any errors occurs.

#### Indexed Data Example

This is an example of the data indexed into ElasticSearch:

![Indexed data](docs/indexedData.png)

## Extract topics

- Run 'java -jar topic-0.5-jar-with-dependencies.jar -e' to extract topics from "input.txt" file.

   You can change the input file in 'com.findwise.topic.extractor.Main' and tweak the parameters for the system.
   When done, the file 'result.html' contains the result as well as the relationship graph of the found topics.

### Extraction Workflow

![workflow](docs/workflow.jpg)

### License

The MIT License (MIT)

Copyright (c) 2014 Markus Östberg

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

# topic-extraction


This project is part of my master thesis project at KTH Royal Institute of Technology.

Project Title: Subtopic extraction using graph-based methods.

This code was used to explore subtopic extraction using search engine technology, combined with graph centrality ranking of topic candidates.

More info about the thesis project can be found at: http://www.nada.kth.se/~markusos/thesis/

## Example

Encyclopedia Britannicas definition of Tree:

> Tree, woody plant that regularly renews its growth (perennial).
> Most plants classified as trees have a single self-supporting trunk containing woody tissues,
> and in most species the trunk produces secondary limbs, called branches.

### Extracted Topics:

1. Tree
2. Perennial plant
3. Plant stem
4. Herbaceous plant
5. Glossary of plant morphology terms

### Candidate Graph:

![example](docs/resultGraph.jpg)

## Setup

### Requirements

 - [MongoDB](http://www.mongodb.org/)
 - [ElasticSearch](http://www.elasticsearch.org/)

### Index Ontology

To build the Ontology used for the topic extraction we use the DBpedia Wikipedia Pagelinks Dataset.

- Download DBpedia Wikipedia Pagelinks Dataset: [page_links_bg.nt.bz2](http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/bg/page_links_bg.nt.bz2)
- Download and install MongoDB
- Download and install ElasticSearch


- Start MongoDB, run 'mongod' from your terminal.
- Start ElasticSearch, run 'elasticsearch' from your terminal.


- Extract 'page_links_bg.nt.bz2' in the project directory
- Run the command 'mvn clean install' in the project directory


- Run the 'com.findwise.topic.Main' class with the input parameter '-i' to start the indexing.

   Expect the indexing to take several hours, depending on your computer performance and harddrive speed.
   It catalogues the whole link structure from Wikipedia into MongoDB for pre processing and then indexes the data into ElasticSearch.
   Keep an eye on the log.txt file to see the progress and the error.txt to see if any errors occurs.

#### Indexed Data Example

![Indexed data](docs/indexedData.png)

### Extract topics

- Run the 'com.findwise.topic.Main' class with the input parameter '-e' to extract topics from "test_input.txt" file. You can change the input file in 'com.findwise.topic.extractor.Main' and tweak the parameters for the system.
- When done, the file 'result.html' contains the result as well as the relationship graph of the found topics.

### Workflow

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

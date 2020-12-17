# javaCrawler

This is a small web crawler written in java, provided with an asynchronous REST API. It uses the docker image Payara Micro, and Jakarta EE.

Forked from: <https://github.com/Carath/payara-micro-test>


## Installation (Linux)

Install docker, java 1.8 (jdk + jre), and maven 3.6.3. Don't forget to export the JAVA_HOME and M2_HOME environment variables, and if under a proxy to properly setup the setting.xml file, which must be placed in the ```~/.m2``` directory.

Download the Payara Micro docker image, found at <https://hub.docker.com/r/payara/micro>, by running:

```
sudo docker pull payara/micro
```


## Runtime:

Start the docker process with: ```sudo systemctl start docker```

Then run as sudoer: ```sh start_api.sh ```

To crawl from a web page, e.g here the english Wikipedia main page, use the following POST request:

```sh
curl -w '\n' -X POST -H "Content-Type: application/json" \
 --data '{"url": "https://en.wikipedia.org/wiki/Main_Page", "maxWebPagesToVisit": 3}' \
 http://localhost:8080/crawler/api/crawler
```

This will retrieve data from the starting web page, including the links it contains, and repeat the process on those same links. The 'maxWebPagesToVisit' value is used to limit the crawling recursion, therefore set it to 1 for getting data from a single page. Do note that the number of page data returned may be lower than 'maxWebPagesToVisit', indeed no content will be returned for non-existing pages or non-relevant ones (i.e not containing any metadata).

On the other hand, one could test the request part without any shell command (on a hardcoded example), simply by pasting the following link into a web browser:

```
http://localhost:8080/crawler/api/mock
```

Finally, below is a more sophisticated example where an user can specify custom queries for the crawling phase:

```sh
curl -w '\n' -X POST -H "Content-Type: application/json" \
 --data '{"url": "https://en.wikipedia.org/wiki/List_of_sovereign_states", "maxWebPagesToVisit": 1, "queries": [ ["title", "title=\"", "\"", "", ""], ["country", "span id=\"", "\">", "", ""] ]}' \
 http://localhost:8080/crawler/api/crawler
```

Precisely, queries must be of the following form: [queryName, startTag, endTag, prefix, suffix]. Note that the output of the previous commands may be saved in a file, by appending ``` > result.json``` at their end.


## Deployment:

Once the project is done, and needs to be deployed e.g on a server, java and maven need not to be reinstalled there again! Indeed, the docker image contains a java JRE. Therefore, the only files necessary for this to run are:

- This README
- Dockerfile
- target/javaCrawler-1.0-SNAPSHOT.war
- start_api.sh in which the 'build phase' has been removed.


## TODO:

- Restrict domain names when crawling;
- Use the robot.txt file on websites where is is available.

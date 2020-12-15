#!/bin/sh

# Some GET and POST request examples:

# curl -w '\n' http://localhost:8080/crawler/api

# curl -w '\n' http://localhost:8080/crawler/api/get/123

# curl -w '\n' -X POST --data 'This is my request.' http://localhost:8080/crawler/api/post

# curl -w '\n' http://localhost:8080/crawler/api/file


# Useful example:

curl -w '\n' -X POST -H "Content-Type: application/json" \
 --data '{"url": "https://en.wikipedia.org/wiki/Main_Page", "maxWebPagesToVisit": 3}' \
 http://localhost:8080/crawler/api/crawler

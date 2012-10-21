Java Search Engine API
======


Usage
--------------

To build the API
```
./build
Generates gen/image/search-version/
```

To initialise the crawler, indexer, ranker
```
bin/start <config> <directory> <polling rate>
bin/start config.properties /home/ 5000
```
To stop the crawler and using command line to search terms
```
bin/stop
bin/search <OPTION> <TERM>
```

**Search**
Options:
- -d to return documents only
- -x to return executables only
- -n to return a list of files with n length
- -dn to return a list of documents with n length
- -xn to return a list of documents with n length

### bin/search usage
```
search term    -- search for any files matching term, return top result
search -x term -- search for any files matching term that are executable
search -d term -- search for any document matching term
search -n 1 term -- search for any files matching term, but only return the top results
search -n 5 term -- search for any files matching term, but only return the top 5 results
search -xn 5 term -- search for any executables matching term, but only return the top 5 results
search -dn 5 term -- search for any documents matching term, but only return the top 5 results
```

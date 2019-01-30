Overview
========
A bank implementation which converts all the Bank API calls into corresponding HTTP calls.
To be used by banks who present HTTP endpoints which implement the Token Bank API.


IMPORTANT
=========
Regenerate certificates before deploying this in a production environment.
See `config/tls/README.md` for details.

Server
=======

Config
------
TokenOS and the bank use TLS for secure communication. Setting this up
involves generating and sharing cryptographic secrets.
These secrets are located in the `config/tls` directory.
`config/tls/README.md` has details.

The server responds to requests by calling the corresponding HTTP endpoints implemented by the bank.
Modify the URL of these HTTP endpoints in the `config/application.conf` file.

Build
------

To build the server run the command specified below. The server uses
gradle build tool.

```sh
./gradlew build
```

Run
------

The build produces shadow (fat) jar that can be run from the command line.
E.g., to run the server, passing the `--ssl` flag: 

```sh
java -jar build/libs/bank-sample-java-1.1.6-all.jar --ssl
```

Implementation
========
As written, all the service does is respond to Token Bank API calls by converting them into
HTTP calls. The bank needs to present the Token Bank API under HTTP form, and implement the
actual bank logic behind that API.

A sample bank implementation written in java is available in the following repository, and can
be used to draw inspiration: https://github.com/tokenio/bank-sample-java

PaNOSC Portal Desktop Service
=======
[![Java CI with Maven](https://github.com/panosc-portal/desktop-service/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/panosc-portal/cloud-service/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

The Desktop Service is a micro-service of the PaNOSC Common Portal.

The Desktop service acts as a relay between the [Apache Gaucamole](https://guacamole.apache.org) guacd service on a running instance and a web-socket to the browser client.

Both *guacd* and web-socket connections are managed by this service. A client connects to an instance by providing an instance ID and a time-limited access token. 
It uses the Cloud Service to validate the access token and obtain the IP and Port of the *guacd* server.  
It then establishes a connection (TCP Socket) to the instance running *guacd* and relays data from this connection to the web-socket (and vice-versa for user actions such as mouse and keyboard operations).

The Desktop Service also manages user access to a desktop-enabled instance. All members of an instance can access a remote desktop through this service however only the owner is able to initiate a session.
Users can only access the remote desktop while the owner has an active session. All clients are disconnected when the owner closes their session. 
Information concerning active sessions is stored in a local database.

Further documentation and the design can be found at [PaNOSC Portal Desktop Service Design](https://confluence.panosc.eu/x/2wCm) page.

## Installation
```
 mvn package
 ```

## Run
A script has been added to run the application with a dotenv file (`.env` by default):
```
./run.sh <dotenv filename>
```
or simply running the java command as follows:
```
java -jar application/target/desktop-service.jar server configuration.yml
```

The environment variables of the application are discussed below.

### Environment variables
The following environment variables are user to configure the Desktop Service:

| Environment variable | Default value | Usage |
| ---- | ---- | ---- |
| DESKTOP_SERVICE_SERVER_HOST | localhost | The host the rest API server listens to (currently unused)|
| DESKTOP_SERVICE_SERVER_PORT | 8085 | The port of the rest API server (currently unused) |
| DESKTOP_SERVICE_VDI_HOST | localhost | The host the web socket listens to |
| DESKTOP_SERVICE_VDI_PORT | 8087 | The port of the web socket |
| DESKTOP_SERVICE_DATABASE_USERNAME | | The database username |
| DESKTOP_SERVICE_DATABASE_PASSWORD | | The database password | 
| DESKTOP_SERVICE_DATABASE_URL | | The database URL |
| DESKTOP_SERVICE_CLOUD_SERVICE_URL | | The Cloud Service URL |

 

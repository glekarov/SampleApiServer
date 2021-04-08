# SampleApiServer

Following sample application is REST server that accepts json array of URLs. All URLs are stored in a list and 
downloaded onto the file system. Downloaded files are checked whether the files are images or not. If downloaded files 
are not images they'll be deleted. 

The server comes without any predefined data. Users can be added using the following request:
## add new user to the server
curl -d 'username=@user@&password=@pass@' -H 'Content-Type: application/x-www-form-urlencoded' -X POST 'localhost:8080/user/add_user'

@user@ - this is the username which will be stored into the server
@pass@ - this is the password for the specified username
All users added trough the server are stored in a data file, so they can be accessed in the future. But for the very 
first start the server comes without any data.

When an user is added, the system can generate a token for this user. The token is used for further operations. The token
is stored together with the user's credentials. Tokens can be generated using this way:
## generate token against specified credential
curl -d 'username=@user@&password=@pass@' -H 'Content-Type: application/x-www-form-urlencoded' -X POST 'localhost:8080/user/token'

@user@ - this is the username which will be stored into the server
@pass@ - this is the password for the specified username
The response is a json which contains the username and the generated token

This server also allow to monitor all URLs that were stored. The following request monitor those URLs list:
## Monitor/List URLs list as json response
curl --location --request POST 'localhost:8080/urls/get' --header 'Content-Type: application/json' -H 'Authorization: @token@'

@token@ - a valid token stored for some of the server's users
The response is a json array containing the URL and its status. The statues are as follows:
STORED - URL is stored and waiting for download
PROCESSING - URL is already downloading
PROCESSED - URL is processed and the file downloaded
REMOVED - Specified URL is not an image so the file was deleted

To set a list of URLs which will be downloaded use the following request:
## Set list of URLs for download
curl --location --request POST 'localhost:8080/urls/send' --header 'Content-Type: application/json' -H 'Authorization: @token@' --data-raw '[{
    \"url\": \"@URL1@\"
},
{
    \"url\": \"@URL2@\"
},
... ,
{
    \"url\": \"URLn\"
}]'

@token@ - a valid token stored for some of the server's users
URL1, URL2, ..., URLn - URLs for download.

# Build
To build the code you have to have:
- stable Internet connection (some third parties are downloaded)
- Java 8 or Java 11 installed. I didn't test it with latest version
Then run:
gradlew build

Gradle Wrapper tool will download the most relevant gradle version, download the main third parties and its dependencies

# Put into a Docker container
To put the build into a Docker container, first you have to have Docker installed and running as a service. Please check
whether your user is part of the Docker group, so you're able to build and run the container.

To build the container you can use the provided Dockerfile. It prepares a Docker image, downloading the latest Java 8,
install some packages that java needs to recognize the files MIME type. The API server recognize image file is using
Files.probeContentType() which needs some MIME types installed. The default Java 8 image have no MIME types installed.
To build a Docker image navigate to the project's root directory (the path where Dockerfile is) and run:
docker build . 
or
docker build -t <choose-an-image-name> .


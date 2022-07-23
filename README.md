## Das Notify Service
### _REST Service to send notifications to E-Mail, Telegram, Matrix_
[![https://img.shields.io/badge/Java-8-white](https://img.shields.io/badge/Java-8-white)](https://www.java.com/)
[![https://img.shields.io/badge/Apache-Maven-lightblue](https://img.shields.io/badge/Apache-Maven-lightblue)](https://maven.apache.org/)
[![https://img.shields.io/badge/Spring-Boot-g](https://img.shields.io/badge/Spring-Boot-g)](https://spring.io)
[![https://img.shields.io/badge/Project-Lombok-red](https://img.shields.io/badge/Project-Lombok-red)](https://projectlombok.org)
[![https://img.shields.io/badge/docker-engine-blue](https://img.shields.io/badge/docker-engine-blue)](https://www.docker.com/)
[![https://img.shields.io/badge/kamax-matrix_sdk-blue](https://img.shields.io/badge/kamax-matrix_sdk-blue)](https://github.com/kamax-matrix/matrix-java-sdk/)

### Table of contents
- [Getting application](#Get)
- [Starting application](#Start)
  - [start.sh](#startSh)
  - [startP.sh](#startpSh)
- [Starting in Docker](#startDocker)
- [Stopping application](#stop)
- [Configuration](#Config)
  - [Parameters in setEnv.sh](#setEnv)
  - [Parameters in application-prod.properties](#appProp)
- [API Description](#Api)
  - [Request](#Request)
  - [Response](#Response)

### Getting application<a id='Get'></a>
You can download **zip** Distribution including **jar** application in [Release](https://github.com/anrydas/NotifyService/releases) section of Repository.
Of course, you can clone our repository to build application by yourself. 

### Starting application<a id='Start'></a>
Use:
(prefer)
```
./startP.sh
```
or
```
./start.sh
```
or you can use direct CLI command (see in start.sh file)
```
java - jar -D... NotifyService.jar
```
or you can use external *application.properties* file
```
java - jar -Dspring.config.location=<file_name> NotifyService.jar
```
See also: [Configuration](#Config), [Parameters in *application-prod.properties*](#appProp)

#### start.sh<a id='startSh'></a>
This script uses to [start the Application](#Start) in way to initialize application's properties via [setEnv.sh](#setEnv) script

#### startP.sh<a id='startpSh'></a>
_This method most prefer than other_
This script uses to [start the Application](#Start) in way to initialize application's properties via [application-prod.properties](#appProp) file
Those method uses ```-Dspring.profiles.active=prod``` parameter to start application in **prod** profile and loading data from ```application-prod.properties``` file.
You can change profile name (and properties file name of course).

### Starting in Docker<a id='startDocker'></a>
To start application in Docker
* put all contents of **docker-compose** directory into any directory in you server (or computer)
* make sure you change values of all variables in **.env** file:
  * ```TCP_PORT``` - port you want the application runs on
  * ```PROFILE``` - profile name ('prod' by default)
  * ```LOG_FILE_NAME``` - application's log file ('logs/Notifier.log' by default)
* put **application-prod.properties** into **app** directory (**_note proper profile name_**)
* put last release version of **NotifyService.jar** file into **app** directory
* run the docker-compose project ```docker-compose up -d```<br/>
That's all.<br/>
Also you cam use our distribution. It already contains all files in zip archive.

See also [Configuration](#Config) section

### Stopping application<a id='stop'></a>
Use ```stop.sh``` script to stop application.

### Configuration<a id='Config'></a>
In general, you can start an application with following scripts
 * [start.sh](#startSh) - starts application with setting it's parameters in [setEnv.sh](#setEnv) script
 * [startP.sh](#startpSh) - starts application with setting it's parameters in [application-prod.properties](#appProp) file
Use one of you prefers method. 

#### Parameters in *setEnv.sh*<a id='setEnv'></a>
This script uses to configure Application before it will be started.
This script calls in [start.sh](#startSh) script.
* ```API_KEY_PARAMETER_NAME='app-key'``` Name of API key parameter.
It uses in HTTP Header to additionaly ptopect the request. 
* ```API_KEY_VALUE='ssXX22d'``` Defines API key. It may be any string you want
* ```APP_PORT='23445'``` application port to access to API. i.e. http://localhost:23445/
* ```TG_URL='https://api.telegram.org'``` URL to connect to Telegram service
* ```TG_API_KEY='xxx'``` Telegram API key
* ```TG_CHAT_ID='ddddddddd'``` Telegram default chat ID
* ```MX_BASE_URL='https://my-matrix.url.org'``` Matrix-Synapse server's URL
* ```MX_USER='bot_user'``` Matrix's user to send messages to Matrix-Synapse server's
* ```MX_USER_PASS='SuPeR_PassWoRd'``` Matrix user's password
* ```MX_ROOM_ID='!xxx:my-matrix.url.org'```Matrix's default Room ID to end messages to
* ```EML_SMTP='smtp.gmail.com'```
* ```EML_PORT='587'```
* ```EML_USER='my.user'```
* ```EML_PASS='PaSsWoRd'```
* ```EML_SMPT_AUTH='true'```
* ```EML_TLS='true'```
* ```EML_FROM='my@some.site.org'```
* ```EML_TO='my.user@gmail.com'``` Address to send E-Mail to. May be semicolon (;) separated 

#### Parameters in *application-prod.properties*<a id='appProp'></a>
You can found collation *setEnv.sh* parameters to *application-prod.properties* parameters in [start.sh](#statrSh) file.<br/>
To prepare the *application-prod.properties* you need to copy it from *application.properties* file which contains in application distribution.
Next you mast change all included parameters to you ones.<br/>
So it all. Then you can [start](#Start) the application with [startP.sh](#startpSh) script.
```bash
./startP.sh
```
Also you can use any other *application.properties* file
```
java - jar -Dspring.config.location=<file_name> NotifyService.jar
```
Of course you can use eny  *application.properties*'s parameter [used in Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html). 

### API Description<a id='Api'></a>
The Send message request need to be applied to ```http://localhost:APP_PORT/send``` endpoint.<br/>
There is **required** to set HTTP header with **API_KEY_PARAMETER_NAME** name and **API_KEY_VALUE** value.<br/>
Something like that
```
curl i -H "${API_KEY_PARAMETER_NAME}:${API_KEY_VALUE}" "http://localhost:${APP_PORT}"
```

#### Request<a id='Request'></a>
The general Request format is
```json
{
    "messenger": "EMAIL",
    "chat": "ID_of_chat",
    "message": "Message",
    "file": "/path/to/file/33273.png",
    "subject": "Test E-Mail"
}
```
* **messenger** - [*Required!*] one of ```TELEGRAMM, MATRIX, EMAIL```
* **chat** - for Telegram or Matrix - the ID of Chat/Room, for E-Mail - semicolon-separated (;) e-mail addresses which will override the ```eml.to.addr``` parameter (see [Parameters in *setEnv.sh*](#setEnv), [Parameters in *application-prod.properties*](#appProp))
* **message** - message text. Application uses HTML markup to send rich text.
* **file** - path to file which will be send. The file contains the full path to file in local system. 
* **subject** - the subject of E-Mail, for Telegram or Matrix ignored


#### Response<a id='Response'></a>
The general Response format is
```json
{
    "status": "OK",
    "comment": "E-Mail was sent to 'mail@mail.com' with file '/path/to/file/33273.png'",
    "errorMessage": null
}
```
* **status** - OK or ERROR
* **comment** - additional message in response
* **errorMessage** - contains Error Message if status==ERROR

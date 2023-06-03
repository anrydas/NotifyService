## Das Notify Service
### _REST Service to send notifications to E-Mail, Telegram, Matrix, Viber_
[![https://img.shields.io/badge/Java-8-white](https://img.shields.io/badge/Java-8-white)](https://www.java.com/)
[![https://img.shields.io/badge/Apache-Maven-lightblue](https://img.shields.io/badge/Apache-Maven-lightblue)](https://maven.apache.org/)
[![https://img.shields.io/badge/Spring-Boot-g](https://img.shields.io/badge/Spring-Boot-g)](https://spring.io)
[![https://img.shields.io/badge/Project-Lombok-red](https://img.shields.io/badge/Project-Lombok-red)](https://projectlombok.org)
[![https://img.shields.io/badge/docker-engine-blue](https://img.shields.io/badge/docker-engine-blue)](https://www.docker.com/)
[![https://img.shields.io/badge/kamax-matrix_sdk-blue](https://img.shields.io/badge/kamax-matrix_sdk-blue)](https://github.com/kamax-matrix/matrix-java-sdk/)
[![https://shields.io/badge/works%20with-Home%20Assistant-33E0FF](https://shields.io/badge/works%20with-Home%20Assistant-33E0FF)](https://www.home-assistant.io/)

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
- [Working with Home Assistant](#HASS)
- [Working with Viber](#Viber)
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
 * [start.sh](#startSh) - starts application with setting its parameters in [setEnv.sh](#setEnv) script
 * [startP.sh](#startpSh) - starts application with setting its parameters in [application-prod.properties](#appProp) file
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
Also, you can use any other *application.properties* file
```
java - jar -Dspring.config.location=<file_name> NotifyService.jar
```
Of course you can use eny  *application.properties*'s parameter [used in Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html). 

### Working with Home Assistant<a id='HASS'></a>
Once you've set up and run the app anywhere, you can decide to use it with Home Assistance.<br/>
In ```configuration.yaml```:
```yaml
rest_command:
send_my_command:
url: !secret notyf_url # here yours Notify Application URL
method: POST
headers:
app-key: !secret notif_api_key
accept: "application/json, text/html"
payload: '{"messenger": "MATRIX", "message": "{{ text }}"}'
content_type:  'application/json; charset=utf-8'
```
In ```includes/packages/matrix.yaml```:
```yaml
# The Matrix notifications
matrix:
    automation:
      - id: "Notification in Matrix"
        alias: "some_alias"
        trigger:
          - platform: state
            entity_id: binary_sensor.some_sensor
            from: "off"
            to: "on"
        action:
          - service: rest_command.send_my_command
            data:
              text: >
                {{ "\U00002757\U00002757\U00002757" }}
                Hello, World!
```
### Working with Viber<a id='Viber'></a>
<br/>To sending messages to Viber Bot you need:
* register Viber Bot on [Viber Admin Panel](https://partners.viber.com/)
* made Webhook to Notify Service Application's endpoint **_/api/v1/viber/webhook_**
* make sure the Application started on **https** host
* make sure the user(s) have subscribed to Bot (just send a message to Bot)

<br/>Application can send files to configured Viber bot
<br/>To **send images/videos/files** you need to:
* made changes in **application.properties** file in following parameters:
  * **app.baseUrl** - You_Application_Host_URL
  * **app.files.url.path** - path from Host URL root to configured end-point (default 'files')
  * **vb.media.folder** - path to directory where files will be saved (default 'media.tmp' in Application folder)
* send file with [Uploading](#upload) end-point
* the file URL after uploading will be **https://you_host/api/v1/files/file_name.ext**; it will be in response body of [Downloading](#download)
* send message to Viber using file URL
* auto removing old files from directory to save free space (if necessary)
#### From Application's version **1.2.0** if file sent in Request it will upload file to server and then send it to Viber 
<br/>_For send image you can use other image hosting of course._
<br/>The message will be sent to all subscribed to Bot recipients
<br/>See details on [Viber API Documentation](https://developers.viber.com/docs/api/rest-bot-api), [Viber Documentation](https://developers.viber.com/docs/)

### API Description<a id='Api'></a>
The **Send message request** need to be applied to ```http://localhost:APP_PORT/send``` endpoint.<br/>
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
* **messenger** - [*Required!*] one of ```TELEGRAM, MATRIX, EMAIL, VIBER```
* **chat** - for Telegram or Matrix - the ID of Chat/Room, for E-Mail - semicolon-separated (;) e-mail addresses which will override the ```eml.to.addr``` parameter (see [Parameters in *setEnv.sh*](#setEnv), [Parameters in *application-prod.properties*](#appProp))
* **message** - message text. Application uses HTML markup to send rich text.
* **file** - path to file which will be sent. The file contains the full path to file in local system. 
* **subject** - the subject of E-Mail, for Telegram or Matrix ignored


#### Response<a id='Response'></a>
The general Response format is
```json
{
    "status": "OK",
    "comment": "E-Mail was sent to 'mail@mail.com' with file '/path/to/file/33273.png'",
    "error_message": "",
    "file_info": {
      "name": "file_name",
      "url": "http://file/url/file",
      "size": "file_size",
      "mime": "file_mime_type"
    }
}
```
* **status** - OK or ERROR
* **comment** - additional message in response
* **error_message** - contains Error Message if status==ERROR
* **file_info** - contains information about uploaded file

#### Uploading<a id='upload'></a>
**/api/v1/upload** - end-point of **POST** request
**Request:**
```Content-Type=multipart/form-data```
```body=file```
**Response body:**
```json
{
  "status": "OK",
    "file_info": {
        "name": "file_name",
        "url": "https://you_host/api/v1/files/file_name.ext",
        "size": "file_size",
        "mime": "file_mime_type"
    }
}
```

#### Downloading<a id='download'></a>
**/api/v1/files/file_name.ext** - end-point of **GET** request
**Response:**
```Binary file data```
When error occurred then 500 (Internal Server Error) http code will be sent with empty body.

Any field might be absent in request if it is empty.

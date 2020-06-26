# sept2019-java-tours-CongressAPI

#Requirement:
    
    - mysql
    - java 8
    - java jdk 8 (or higher)
    
#Install
To install mysql go to [mysql website](https://dev.mysql.com/downloads/installer/) or 
use your package manager

Arch Linux:

```shell script
sudo pacman -Su mysql java
```

Debian:

```shell script
sudo apt-get install mysql java
```

#Setup
Connect to your database as user with all privileges

```shell script
mysql -u your_admin_user -p
```

In mysql to create your user and the database type the following command

```sql
CREATE DATABASE your_database;
GRANT ALL PRIVILEGES ON your_database.* TO 'your_user'@'localhost' IDENTIFIED BY 'your_password';
exit
```

Next you need to use dump.sql to feed the database
```shell script
mysql -u your_user -p -D your_database < dump.sql
```
#For developpers
If you want to test or add some features go to project root and type
```shell script
mvn spring-boot:run
```
#Build

You need to go in project root and type
```shell script
mvm package
```
#Production
You need the jar file to be already generated if not check #For developper section.

Put the generated from directory target and place your jar file desired location in server
```shell script
cd your_jar_directory/
java -jar your_jar.jar
```
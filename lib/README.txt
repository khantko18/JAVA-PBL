=======================================================================
MySQL JDBC Driver Required
=======================================================================

This directory should contain the MySQL Connector/J JAR file.

DOWNLOAD:
https://dev.mysql.com/downloads/connector/j/

FILE NEEDED:
mysql-connector-java-8.0.33.jar (or later version)

INSTALLATION:
1. Download ZIP from link above
2. Extract mysql-connector-java-8.0.x.jar
3. Place the JAR file in this directory

CURRENT STATUS:
[  ] JAR file not found
[  ] Place mysql-connector-java-8.0.x.jar here

After adding the JAR, compile with:
javac -cp ".:lib/*" -d bin src/**/*.java

=======================================================================


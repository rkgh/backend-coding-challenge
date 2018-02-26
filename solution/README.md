README
====
How to run the solution?

* Install at least Java 8
* Install Maven 3
* Install PostgreSQL 9.4
* Create main "expenses" and test "expenses-test" databases
* Change if necessary database username and password properties
    
    spring.datasource.username=postgres
    spring.datasource.password=postgres

  in both main and test application.properties files

    {path_to_project}/solution/src/main/resources/application.properties
    {path_to_project}/solution/src/test/resources/application.properties
 
* Check that default ports 8080 and 9090 for frontend and backend correspondingly are not in use. 
  The Backend REST API default port 9090 can be changed to another one by modifying
  
	"server.port" property in {path_to_project}/solution/src/main/resources/application.properties
	"apiroot" property in {path_to_project}/config.js
	
* Run Backend solution:

	cd {path_to_project}/solution
	mvn spring-boot:run
	or
	mvn clean install
	java -jar target/backend-rest-api-1.0-SNAPSHOT.jar
	
* Run Frontend:

	cd ../
	gulp
	
* Open Frontend
	http://localhot:8080

IMPORTANT
====
The main database "expenses" will be automatically populated with the required schema and initial data. 
After the first start, please change property spring.jpa.hibernate.ddl-auto in application.properties file to 
"update" value (spring.jpa.hibernate.ddl-auto=update). 
Otherwise all data will be lost after each restart and populated with the required initial data.
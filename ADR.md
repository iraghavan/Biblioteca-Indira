# Architecture decision record (ADR)

Notes on the key architectural design decisions taken by the team.

## Contents:

* [Version based database migration tool](#)
* [Postgres Database integration into different environments](#)
* [API Documentation tool](#)
* [Logging framework](#)
* [Integration test automation](#)
* [Configurability](#)
* [CI/CD for staging and production environment](#)
* [Exclude Files from Inteliji code coverage ](#)

## Version based database migration tool
Evaluated and chose Flyway, a version based database schema migration tool, to ensure schema updates 
without DB downtime.
Understood features of flyway for implementation in the project.

## Postgres Database integration into different environments
Researched on ways to configure different database instances in different environments.
Chose H2, an in-memory database , for development and unit testing.
Postgresql was pre-chosen as out database for production by the client. Understood how the application can 
switch to different database instances based on spring_profiles_active environment variable setting.

## API Documentation tool
Researched on how to use swagger to generate API documentation. Checked on all annotations that can be used in our project.

## Logging framework
Logger for Spring boot does not need any extra configuration, as Spring boot provides logback from any starter dependency.
logback-spring.xml can be used to change configurations. Heroku maintains the logs for any application that has been deployed.
This can either be seen on the Heroku CLI or on the View logs option. If your application needs logging to a file, 
then you can have add-ons in Heroku. It was decided that logger add-ons are not required for our application, 
hence going forward with the default logging option from Heroku.

## Integration test automation

Reasearched on the way to explore automatic integration testing using cucumber.
Cucumber is a framework written in bdd.
1. Configuration
In build.gradle, include the below line
   testImplementation 'io.cucumber:cucumber-java:' + cucumberVersion
   test {
   useJUnitPlatform()
   }

configurations {
cucumberRuntime {
extendsFrom testImplementation
}
}

task cucumber() {
dependsOn assemble, compileTestJava
doLast {
javaexec {
main = "io.cucumber.core.cli.Main"
classpath = configurations.cucumberRuntime + sourceSets.main.output + sourceSets.test.output
args = ['--plugin', 'html:reports/test-report', '--plugin', 'pretty', '--glue','cucumber','src/integrationTest/resources/','--strict']
}
}

2. Add the feature in below sorrce file
create folder src/integrationTest/cucumeber/movie.feature
   
3. Define the steps in the src/test/cucumber

run ./gradlew cucumber

## Configurability
It has been decided by the team to go with <>.properties, <>-staging.properties and <>-prod.properties naming conventions.
 
## CI/CD for staging and production environment
It has been decided by the team to use Heroku for manually promoting the build from 
test to stage and thenceforth to production.

## CI/CD for Development
action.yml file for gradle will be used for CI/CD for the development environment and will deploy into Heroku app on every push of the code.

##Exclude Files from Intellij code coverage
Click "RUN" -> "EDIT CONFIGURATION" -> click "CODE COVERAGE" tab ->
Add below files under "Package and classes to exclude from coverage data"
Click "APPLY" - "OK"

Packages
com.vapasi.biblioteca.model.*
com.vapasi.biblioteca.response.*
com.vapasi.biblioteca.request.*
com.vapasi.biblioteca.util.*
com.vapasi.biblioteca.config.*
com.vapasi.biblioteca.security.*
Files
com.vapasi.biblioteca.BibliotecaApplication



## assumptions / remarks
- use synchronous call to rest service, assuming it returns within min amount of schedule time
- I fixed and error in the yaml (query iso path param {city}) and I added some host related info there
- I used editor.swagger.io for client generation. I took a shortcut there to add the code to the source but 
of course this should be added as a separate lib. I am not sure if any maven open-api plugin also generates client code 
  (this would be the best option).
- it took me a bit more time than expected, this was because I tried to get a maven plugin for client
generation working, I abandoned that, but then I had to get the generated client api working, it has some
  extra dependencies.
- tests can be added, for instance, I did not create a test of all the TemperatureData methods, because
I saw they are correctly printed in the TemperatureServiceTest. In a real environment I would also take care of that.
  And I skipped the failed initialisation test for now.
- assumption: removing a city if not found is removing it from the DB.  
- assumption : CIT4: Default list of cities must be empty --> this assumes the service has state. So I interpreted it like
at the start there is no city in the database.


## design

A **TemperatureService** that initializes and fails if IntervalPeriod is invalid.
The interval is checked via Bean Validation, this is maybe a bit overkill but you can add
rules like @Min, @Max with proper error messages.
I use a @Scheduled method, for easy configuration sake. This can be solved in other ways (e.g. TimerTask) depending on what the requirements are.

There is a **TemperatureRestClient** that wraps the client API because I do no want to
clutter the code with the getTemp service client code. This client returns the data we are interested
in, the **TemperatureData**, or wraps a ApiClientException as a **RestClientException**.

The **TemperatureData** object is full of DDD (Domain Driven Design) functions where I let the
domain object do its own "computations/formatting".

At runtime, I use an in memory H2 database. See howto on how to add cities to the DB while
the system is running. I use plain Spring-Data-JPA for CRUD actions.

Currently the application runs on port 88. If you want scale more instances, which is possible
because the Service is stateless, you can change the server.port to 0 to startup 
with a random port.

## Howto

### run the application
mvn spring-boot:run (from the root of the project)

### Connect to DB to maintain cities
http://localhost:88/h2-console

url: jdbc:h2:file:~/cities
user/pass : coen/coen

Example:
insert into city (name) values 'Veldhoven';


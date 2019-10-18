


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uprm.cse.datastructures.cardealer.model.Car;
import edu.uprm.cse.datastructures.cardealer.model.CarList;
import org.glassfish.grizzly.http.server.HttpServer;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class APITest {

    private HttpServer server;
    private WebTarget target;
    private static final String BASE_URI = "http://localhost:8080/cardealer/";

    @Before
    public void setUp() throws Exception {
        // start the server
        server = startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    private HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in edu.uprm.cse.datastructures.cardealer package
        final ResourceConfig rc = new ResourceConfig().packages("edu.uprm.cse.datastructures.cardealer");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Test to see that Server can insert cars in order and return all or one specific car.
     */
    @Test
    public void test1() {
        Car car1 = new Car(1,"Toyota", "RAV4", "LE",15000.50);

        Response response1 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car1, MediaType.APPLICATION_JSON));

        // Check response status code is 201 (Created)
        assertEquals("Response Status not equal", 201, response1.getStatus());

        Car responseCar1 = target.path("cars/1").request().get(Car.class);

        // Check responseCar1 is car1
        assertEquals(car1, responseCar1);

        Car car2 = new Car(2,"Toyota", "RAV4", "SE",10000);

        Response response2 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car2, MediaType.APPLICATION_JSON));

        // Check response status code is 201 (Created)
        assertEquals("Response Status not equal",  201, response2.getStatus());

        Car responseCar2 = target.path("cars/2").request().get(Car.class);

        // Check responseCar2 is car2
        assertEquals(car2, responseCar2);

        Car[] cars = target.path("cars").request().get(Car[].class);

        // Check cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car1, car2}, cars);

        Car car3 = new Car(3,"Honda", "Civic", "Sedan",20000);

        Response response3 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car3, MediaType.APPLICATION_JSON));

        // Check response status code is 201 (Created)
        assertEquals("Response Status not equal", 201, response3.getStatus());

        Car responseCar3 = target.path("cars/3").request().get(Car.class);

        // Check responseCar3 is car3
        assertEquals(car3, responseCar3);

        cars = target.path("cars").request().get(Car[].class);

        // Check cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car3, car1, car2}, cars);
        CarList.resetCars();
    }

    @Test
    public void test2() {
        Car car1 = new Car(1,"Toyota", "RAV4", "LE",15000.50);

        Response response1 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car1, MediaType.APPLICATION_JSON));

        // Check response status code is 201 (Created)
        assertEquals("Response Status not equal", 201, response1.getStatus());

        Car responseCar1 = target.path("cars/1").request().get(Car.class);

        // Check responseCar1 is car1
        assertEquals(car1, responseCar1);

        Car car2 = new Car(2,"Toyota", "RAV4", "SE",10000);

        Response response2 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car2, MediaType.APPLICATION_JSON));

        // Check response status code is 201 (Created)
        assertEquals("Response Status not equal",  201, response2.getStatus());

        Car responseCar2 = target.path("cars/2").request().get(Car.class);

        Car[] cars = target.path("cars").request().get(Car[].class);

        // Check cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car1, car2}, cars);

        // Check responseCar2 is car2
        assertEquals(car2, responseCar2);

        car1 = new Car(1,"Toyota", "RAV4", "XLE",10000);

        // Updates Car 1
        Response ResponseCar1Update = target.path("cars/1/update").request(MediaType.APPLICATION_JSON).put(Entity.entity(car1, MediaType.APPLICATION_JSON));

        assertEquals("Response Status not equal",  200, ResponseCar1Update.getStatus());

        Car responseCar1Updated = target.path("cars/1").request().get(Car.class);

        // Check responseCar1 is car1
        assertEquals(car1, responseCar1Updated);

        cars = target.path("cars").request().get(Car[].class);

        // Check cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car2, car1}, cars);

        Car car3 = new Car(3,"Honda", "Civic", "Sedan",20000);

        Response response3 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car3, MediaType.APPLICATION_JSON));

        // Check response status code is 201 (Created)
        assertEquals("Response Status not equal", 201, response3.getStatus());

        Car responseCar3 = target.path("cars/3").request().get(Car.class);

        // Check responseCar3 is car3
        assertEquals(car3, responseCar3);

        cars = target.path("cars").request().get(Car[].class);

        // Check cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car3, car2, car1}, cars);

        Response deleteResponse = target.path("cars/3/delete").request(MediaType.APPLICATION_JSON)
                .delete();

        //Check Delete was OK
        assertEquals("Delete Response Status not 200", 200, deleteResponse.getStatus());

        Response getCar3Response = target.path("cars/3").request(MediaType.APPLICATION_JSON)
                .get();

        //Make sure Car 3 is not in list
        assertEquals("Get Response Status not 404", 404, getCar3Response.getStatus());

        cars = target.path("cars").request().get(Car[].class);

        // Check cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car2, car1}, cars);

        CarList.resetCars();
    }

    @Test
    public void test3(){
        Car car1 = new Car(1,"Toyota", "RAV4", "LE",15000.50);

        Response response1 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car1, MediaType.APPLICATION_JSON));

        //Check if status code is 201
        assertEquals("Response Status not equal",  201, response1.getStatus());

        Car responseCar1 = target.path("cars/1").request().get(Car.class);

        //Check Car is Car1
        assertEquals("Car1 is not equal to received Car1", car1, responseCar1);

        Response deleteResponse = target.path("cars/1/delete").request(MediaType.APPLICATION_JSON)
                .delete();

        //Check Delete was OK
        assertEquals("Delete Response Status not 200", 200, deleteResponse.getStatus());

        Response getCar1Response = target.path("cars/1").request(MediaType.APPLICATION_JSON)
                .get();

        //Make sure Car 1 is not in list
        assertEquals("Get Response Status not 404", 404, getCar1Response.getStatus());

        Response getCar1AgainResponse = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car1, MediaType.APPLICATION_JSON));

        //Check if status code is 201
        assertEquals("Response Status not equal",  201, getCar1AgainResponse.getStatus());

        Car responseCar1Again = target.path("cars/1").request().get(Car.class);

        //Check Car is Car1
        assertEquals("Car1 is not equal to received Car1", car1, responseCar1Again);



        Car car2 = new Car(2,"Toyota", "RAV4", "SE",15000.50);

        Response response2 = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car2, MediaType.APPLICATION_JSON));

        //Check if status code is 201
        assertEquals("Response Status not equal",  201, response2.getStatus());

        Car responseCar2 = target.path("cars/2").request().get(Car.class);

        //Check Car is Car2
        assertEquals("Car2 is not equal to received Car2", car2, responseCar2);

        Car[] cars = target.path("cars").request().get(Car[].class);

        //Check if cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car1, car2}, cars);


        Response deleteResponse2 = target.path("cars/2/delete").request(MediaType.APPLICATION_JSON)
                .delete();

        //Check Delete was OK
        assertEquals("Delete Response Status not 200", 200, deleteResponse2.getStatus());

        Response getCar2Response = target.path("cars/2").request(MediaType.APPLICATION_JSON)
                .get();

        //Make sure Car 2 is not in list
        assertEquals("Get Response Status not 404", 404, getCar2Response.getStatus());

        Response getCar2AgainResponse = target.path("cars/add").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(car2, MediaType.APPLICATION_JSON));

        //Check if status code is 201
        assertEquals("Response Status not equal", 201, getCar2AgainResponse.getStatus());

        Car responseCar2Again = target.path("cars/2").request().get(Car.class);

        //Check Car is Car2
        assertEquals("Car2 is not equal to received Car2", car2, responseCar2Again);

        cars = target.path("cars").request().get(Car[].class);

        //Check if cars are in order
        assertArrayEquals("CarList not in order", new Car[]{car1, car2}, cars);

        CarList.resetCars();
    }

}
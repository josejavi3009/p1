package edu.uprm.cse.datastructures.cardealer;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uprm.cse.datastructures.cardealer.model.Car;
import edu.uprm.cse.datastructures.cardealer.model.CarComparator;
import edu.uprm.cse.datastructures.cardealer.model.CarList;
import edu.uprm.cse.datastructures.cardealer.util.CircularSortedDoublyLinkedList;



@Path("/cars")
public class CarManager {

  CarComparator cmp = new CarComparator();
  private final CircularSortedDoublyLinkedList<Car> cList =CarList.getInstance();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Car[] getCarList() {
    return cList.toArray();
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Car getCar(@PathParam("id") long id){
	  for(Car c : cList){
			if(c.getCarId()==id){ return c;}  
		}  
	  throw new NotFoundException(new JsonError("Error", "Car " + id + " not found"));
  }
  
  @POST
  @Path("/add")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addCar(Car car){
	  for(Car c : cList){
			if(c.getCarId()==car.getCarId() || c.getCarBrand()==null  
			 || c.getCarModel()==null || c.getCarModelOption()==null){
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		}
    cList.add(car);
    return Response.status(Response.Status.CREATED).build();
  }
  
  @PUT
  @Path("{id}/update")
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateCar(Car car){
		for(Car c : cList){
			if(c.getCarId()==car.getCarId()){ //The new car has to have the same Id
				cList.remove(c);
				cList.add(car);
				return Response.status(Response.Status.OK).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
  }
  
  @DELETE
  @Path("{id}/delete")
  public Response deleteCar(@PathParam("id") long id){
	  for(Car c: cList) {
		  if(c.getCarId() == id) {
			  cList.remove(c);
			  return Response.status(Response.Status.OK).build(); 
		  }	  
	  }
	  return Response.status(Response.Status.NOT_FOUND).build();
  }
  
}


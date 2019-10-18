package edu.uprm.cse.datastructures.cardealer.model;

import java.util.Comparator;

public class CarComparator implements Comparator<Car> {


	@Override
	public int compare(Car c1, Car c2) {
		String param1 = c1.getCarBrand() + "-" + c1.getCarModel() + "-" + c1.getCarModelOption();
		String param2 = c2.getCarBrand() + "-" + c2.getCarModel() + "-" + c2.getCarModelOption();
		
		return param1.compareTo(param2);
		}

}

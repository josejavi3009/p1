package edu.uprm.cse.datastructures.cardealer;

public class JsonError{

private String type, message;

public JsonError(String type, String message) {
this.type = type;
this.message = message;
}

public String getType() {
return type;
}

public String getMessage() {
return message;
}
}
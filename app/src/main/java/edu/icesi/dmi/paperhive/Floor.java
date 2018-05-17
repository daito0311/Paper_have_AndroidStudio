package edu.icesi.dmi.paperhive;

/**
 * Created by estudiante on 17/05/18.
 */

public class Floor {
    int available_seats;
    String name, description, id;

    public Floor(){

    }

    public Floor(String name, String description){
        this.name = name;
        this.description = description;
    }

    public int getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(int available_seats) {
        this.available_seats = available_seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

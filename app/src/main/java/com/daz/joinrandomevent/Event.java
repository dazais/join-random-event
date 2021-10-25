package com.daz.joinrandomevent;

public class Event {

    String name;
    String description;
    int id_;
    //int image;

    public Event(String name, String description, int id_) {
        //, int image
        this.name = name;
        this.description = description;
        this.id_ = id_;
        //this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

//    public int getImage() {
//        return image;
//    }

    public int getId() {
        return id_;
    }
}
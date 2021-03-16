package com.example.proj_2;

public class list_contact {
    String id;
    String name;
    String number;

    list_contact(String id, String name, String number){
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setname(String name){
        this.name = name;
    }
    public void setnumber(String number){
        this.number = number;
    }

}

package com.example.covidrun.model;

public class UserModel {
    private String email;
    private String password;
    private String phonenum;
    private String name;
    private int id;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String password(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPhonenum(){
        return phonenum;
    }

    public void setPhoneum(String phonenum){
        this.phonenum = phonenum;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public UserModel(String email, String password, String phonenum, String name){
        this.email = email;
        this.password = password;
        this.phonenum = phonenum;
        this.name = name;
    }
}

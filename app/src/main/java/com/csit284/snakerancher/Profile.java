package com.csit284.snakerancher;

public class Profile {
    String username;
    String password;
    String email;

    public Profile(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Profile(){

    }

    public String toString(){
        return "Username: " + username + " | Password: " + password + " | Email: " + email;
    }


}

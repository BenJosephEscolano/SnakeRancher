package com.csit284.snakerancher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
// this is temporary
public class AccountManager {
    public static List<Profile> accounts = new ArrayList<>();
    private static AccountManager account;

    private AccountManager(){

    }
    public boolean validate(String username, String password){
        for (Profile p: accounts){
            if (Objects.equals(username, p.username) && Objects.equals(password, p.password)){
                return true;
            }
        }
        return false;
    }

    public boolean checkUsername(String username){
        for (Profile p: accounts){
            if (Objects.equals(username, p.username)){
                return true;
            }
        }
        return false;
    }

    public static AccountManager create(){
        if (account == null){
            //account = new AccountManager();
        }
        return account;
    }

    public void add(Profile profile){
        accounts.add(profile);
    }

    public void updatePassword(String username, String password){
        for (Profile p: accounts){
            if (Objects.equals(username, p.username)){
                p.password = password;
                return;
            }
        }
    }

}

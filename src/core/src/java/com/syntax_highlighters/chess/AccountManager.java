package com.syntax_highlighters.chess;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    static List<Account> myAccounts = new ArrayList<Account>();

    /**
     * Constructor.
     *
     * Account, YEY :)
     */

    public Account getAccount(String name){
        for(Account a: myAccounts)
            if(a.getName() == name)
                return a;
        return null;
    }

    public boolean addAccount(Account acc){
        boolean canAdd = true;
        for(Account a: myAccounts)
            if(a.equals(acc))
                canAdd = false;
        if(canAdd)
            myAccounts.add(acc);
        return canAdd;
    }

    public List<Account> getTop(int n){
        if(myAccounts.size() <= n)
            return(myAccounts);
        List<Account> returnAccounts = new ArrayList<Account>();
        for(int i=0; i<n; i++)
            returnAccounts.add(myAccounts.get(i));
        return returnAccounts;
    }

    public List<Account> getAll(){
        return myAccounts;
    }

    public void save(String filename){
        String filetext = "";
        for(Account a: myAccounts){
            filetext += a.getName() + "," + String.valueOf(a.getWinCount()) + "," + String.valueOf(a.getLossCount()) + "\n";
        }
        try {
            Files.write(Paths.get(filename), filetext.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            System.out.println("You fool, this cannot possibly be done!");
        }
    }

    public void load(String filename){
        String filetext = "";
        try{
            for(String s: Files.readAllLines(Paths.get(filename))){
                String[] stats = s.split(",");
                String name = stats[0];
                int wins = Integer.parseInt(stats[1]);
                int losses = Integer.parseInt(stats[2]);
                myAccounts.add(new Account(name, wins, losses));
            }
        }catch(IOException e){
            System.out.println("You fool, this cannot possibly be done!");
        }

    }
}
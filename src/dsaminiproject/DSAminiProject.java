/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsaminiproject;

import java.io.FileReader;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/**
 *
 * @author Jeffrey Kingori
 */
public class DSAminiProject {
    
    public static ArrayList<ArrayList<String>> readData(String file) throws FileNotFoundException, IOException 
    { 
        FileReader filereader = new FileReader(file); 
        CSVReader csvReader = new CSVReader(filereader); 
        String[] nextRecord; 
            
        int counter = 0;
        ArrayList<ArrayList<String> > teamsList = new ArrayList< >(); 
        
        System.out.println("Data read from file...\n Teams:-");
        while ((nextRecord = csvReader.readNext()) != null) {
            teamsList.add(new ArrayList<>());
            for (String cell : nextRecord) { 
                System.out.print(cell + "\t");      //print read Data
                teamsList.get(counter).add(cell);   //Inserting Data into 2D ArrayList
            } 
            System.out.println();
            counter++;
        }
        return teamsList;
    } 
    
    public static ArrayList<ArrayList<String>> scheduler(ArrayList<ArrayList<String>> teamslist){
        
        Collections.shuffle(teamslist);     //Shuffling for randomness
        
        ArrayList<ArrayList<String> > ls1 = new ArrayList< >();
        ArrayList<ArrayList<String> > ls2 = new ArrayList< >();
        ArrayList<ArrayList<String> > same = new ArrayList< >();
        
        teamslist.forEach((_item) -> {
            for(int x = 0; x < teamslist.size(); x++ ){
                ls2.add(new ArrayList<>(teamslist.get(x))); //Duplicating the passed Array
            }
        });
            
        for(int r = 0; r < teamslist.size(); r++){
            for (ArrayList<String> teamslist1 : teamslist) {
                ls1.add(new ArrayList<>(teamslist.get(r))); //Duplicating the passed Array
            }
        }

        for(int r = 0; r < ls2.size(); r++){        //Creating the fixture table/matrix
            ls1.get(r).add(ls2.get(r).get(0));
            ls1.get(r).add(ls2.get(r).get(1));
            ls1.get(r).add(ls2.get(r).get(2));      
        }
        
        int len = ls1.size();
        int sq = (int) Math.sqrt(len); //Getting size n of nxn matrix. n is also the number of teams
        int arr[][] = new int[sq][sq]; 
        int dum = 0;
        for (int i=0; i < sq ; i++) 
        { 
            for (int j=0; j < sq ; j++){   //inserting +ve integers from 0 to represent indices in the nxn matrix.
                arr[i][j] = dum;
                dum+=1;
            }  
        }
        
        ArrayList<Integer> leg2 = new ArrayList<>();
        ArrayList<Integer> leg1 = new ArrayList<>();
        int rows = arr.length ;  
        int cols = arr[0].length;  
        for(int i = 0; i < rows; i++){  
                for(int j = 0; j < cols; j++){  
                  if(i > j){
                        leg2.add(arr[i][j]);    //inserting indices of the lower triangle in the nxn matrix to a list representing leg 2
                  }
                  else{
                        leg1.add(arr[i][j]);    //inserting indeces of the upper triangle in the nxn matrix to a list representing leg 1
                }  
            }  
        }
      
        for(int i = 0; i < ls1.size(); i++){
            if(leg1.contains(i)){
                ls1.get(i).add("leg 1"); //set leg
                ls1.get(i).remove(5);     //remove away stadium
            }
            if(leg2.contains(i)){
                ls1.get(i).add("leg 2");    //set leg
                ls1.get(i).remove(5);   //remove away stadium
            }   
        }
        
        for(int i = 0; i < ls1.size(); i++){
            if(ls1.get(i).get(0).equals(ls1.get(i).get(3))){
                ls1.remove(i);      //removing same team fixtures
            }
            
        }
        
        for(int i = 0; i < ls1.size(); i++){
            if(ls1.get(i).get(1).equals(ls1.get(i).get(4))){
                same.add(ls1.get(i));   //transfering same city teams to another array
                ls1.remove(i);      
                }
        }
        Collections.shuffle(ls1, new Random(40));   //Shuffling again since same teams are grouped

        for(int i = 0; i < same.size(); i++){
            ls1.add(same.get(i));       //adding same town team matches at the end
        }
        Collections.sort(ls1, (ArrayList<String> o1, ArrayList<String> o2) -> o1.get(5).compareTo(o2.get(5))); // Sorting by leg
        
        int wknd = 1;
        for(int i = 0; i < ls1.size(); i++){    //Fixing weekends
            ls1.get(i).add("#"+wknd);
            ls1.get(i+1).add("#"+wknd); 
            i++;
            wknd++;
        }
        
        System.out.println(" \n Fixtures Stats:-\n Number of matches:"+ ls1.size() +"\n Number of Derbies:"+same.size()+
                            "\n Number of weekends:"+ls1.size()/2); 
        
        return ls1;

    }
    
    public static void print(ArrayList<ArrayList<String>> teamslist){
        
        String leftAlignFormat = "| %-18s | %-8s | %-2s | %-18s | %-8s | %-14s | %-6s | %-8s |%n";

        System.out.format("+--------------------+----------+----+--------------------+----------+----------------+--------+----------+%n");
        System.out.format("| Home               |City/Town | VS | Away               |City/Town | Stadium        | leg    | Weekend  |%n");
        System.out.format("+--------------------+----------+----+--------------------+----------+----------------+--------+----------+%n");
        for (int i = 0; i < teamslist.size(); i++) {
            System.out.format(leftAlignFormat, teamslist.get(i).get(0), teamslist.get(i).get(1), "VS" , teamslist.get(i).get(3),teamslist.get(i).get(4),
                    teamslist.get(i).get(2),teamslist.get(i).get(5),teamslist.get(i).get(6));
        }
        System.out.format("+--------------------+----------+----+--------------------+----------+----------------+--------+----------+%n");
    
    }
    
    public static void toCSV(ArrayList<ArrayList<String>> teamslist, String filePath){
        File file = new File(filePath); 
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile); 

            String[] header = { "Home", "City/Town", "Stadium", "Away", "City/Town", "Leg", "Weekend"}; 
            writer.writeNext(header);           // adding header to csv 

            for(int i = 0; i < teamslist.size();i++){       // add data to csv
                String[] temp = new String[teamslist.get(i).size()];
                for(int j = 0; j < teamslist.get(i).size(); j++){
                    temp[j] = teamslist.get(i).get(j);
                }
                writer.writeNext(temp);
            }

            writer.close(); 
        } 
        catch (IOException e) { 

            System.out.println("Error writing file: "+e);
        }    
    }
    public static void main(String[] args) throws IOException {
        ArrayList<ArrayList <String>> results = scheduler(readData("teams.csv"));
        print(results);
        toCSV(results,"fixtures.csv");
        
    }  
}

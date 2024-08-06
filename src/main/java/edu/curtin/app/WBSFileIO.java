package edu.curtin.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class WBSFileIO
{
    public Map<String, WBSItem> readFile(String filename) throws IOException 
    {
        Utility.lambdaLog( "Reading file: " + filename);
        Map<String, WBSItem> wbs = new LinkedHashMap<>(); // main map to store all tasks
        Map<String, WBSItem> temp = new HashMap<>(); // temporary map to store tasks that can break down but have a parent so they can be accessed.
        int lineNum = 1; // line number to keep track of the line being read
        try(BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line = br.readLine();
            while(line != null) //while not EOF 
            {
                if(!line.isEmpty())
                {
                    String[] parts = line.split(";", -1); //Split the line by the delimiter
                    String parentID = parts[0].strip(); //Parent ID
                    String id = parts[1].strip();  //Task ID
                    String description = parts[2].strip(); //Task Description
                    WBSItem task = null; //Task object to be created
                    if (parts.length == 4 && !parts[3].isEmpty()) //if line length is 4, then it has an effort estimate
                    {
                        String effort = parts[3].trim(); //Effort estimate
                        task = new UBDTask(parentID, id, description, Integer.parseInt(effort)); //Create a UBDTask object
                        Utility.lambdaLog( "New UBDTask created: " + id);
                    } 
                    else if(parts.length == 4 && parts[3].isEmpty()) //if line length is 4, but effort estimate is empty
                    {
                        task = new UBDTask(parentID, id, description, 0); //Create a UBDTask that doesnt have an effort yet. 
                        Utility.lambdaLog("New UBDTask created: " + id);
                    }
                    else if (parts.length == 3) //Create a task that can break down further
                    {
                        task = new BDTask(parentID, id, description);
                        Utility.lambdaLog( "New BDTask created: " + id);
                    }
            
                    if (parentID.isEmpty()) //if parent ID is empty, then it is a root task, for ex (C)
                    {
                        wbs.put(id, task);  //Add the task to the main map so it can act as a root and can be recursed through.
                        temp.put(id, task); //Add the task to the temporary map so that it can be accessed when adding sub tasks to it.

                    } 
                    else//if parent ID is not empty, it can be a subtask or a task that can break down further
                    {
                        if (temp.containsKey(parentID)) //if the parent ID is in the temporary map
                        {
                            ((BDTask) temp.get(parentID)).addSubTask(task); //Add the task to the parent task
                            temp.put(id, task); //Add the task to the temporary map so that you can add subtasks to it if you need
                            Utility.lambdaLog( "Subtask: " + id + " added to: " + parentID);
                        } 
                        else 
                        {
                            if (wbs.containsKey(parentID)) //if the parent ID is in the main map, a root node. 
                            {
                                ((BDTask) wbs.get(parentID)).addSubTask(task); //Add the task to the parent task
                                temp.put(id, task); //Add the task to the temporary map so that you can add subtasks to it if you need
                                Utility.lambdaLog("Subtask: " + id + " added to: " + parentID);
                            } 
                            else //if the parent ID is not in the main map or the temporary map, then the parent task is not found.
                            {
                                System.out.println("Parent task not found: " + parentID);
                                Utility.lambdaLog( "Parent task not found: " + parentID);
                            }
                        }
                    }
                    lineNum++; //increment the line number
                }
                line = br.readLine(); //read next line 
            }
            if(wbs.isEmpty()) //if the map is empty, then the file is empty
            {
                throw new IOException("Error! File is empty!");
            }
            Utility.log(Level.INFO, "File read successfully");
            
        }
        catch(IOException e)
        {
            Utility.logException("Error reading file: " + e.getMessage());
            System.out.println("Error reading file: " + e.getMessage());
        }
        catch(NumberFormatException e)
        {
            Utility.logException("Error reading file, incorrect format on Line " + lineNum + ": " + e.getMessage());
            System.out.println("Error reading file, incorrect format on Line " + lineNum + ": " + e.getMessage());
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            Utility.logException("Error reading file: Incorrect Formatting on Line " + lineNum + ": " + e.getMessage());
            System.out.println("Error reading file: Incorrect Formatting on Line " + lineNum + ": " + e.getMessage());
        }
        return wbs; //return filled map
    }

    public void writeFile(String filename, List<String> toWrite) throws IOException //Write the tasks to a file
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            if(toWrite.isEmpty())
            {
                Utility.log(Level.WARNING, "Write List is Empty!");
                throw new WBSException("Error! Write List is Empty!");
            }
            else
            {
                Utility.lambdaLog("Writing to file: " + filename);
                for (String line : (toWrite) ) // write contents of the list to the file 
                {
                    writer.write(line); 
                    writer.newLine(); //move to the next line
                }
                Utility.log(Level.INFO, "File written successfully");
            }
        }
        catch(IOException e)
        {
            System.out.println("Error writing to file: " + e.getMessage());
            Utility.logException("Error writing to file: " + e.getMessage());
        }
        catch(WBSException e)
        {
            System.out.println(e.getMessage());
        }
    }
}

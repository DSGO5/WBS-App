package edu.curtin.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class App
{
    private static Map<String, Option> options = new HashMap<>(); //Map to store the different types of options 
    public static void main(String[] args)
    {   
        try
        {
            if(args.length == 1) //Check if the number of arguments is correct, if not then crash gracefully. 
            {
                Utility.log(Level.INFO, "Program has started");
                addOption("Default", new DefaultOption());
                addOption("Highest", new HighEstOption());
                addOption("Median", new MedEstOption());
                //^^ add all of the options so that they can be used in the program
                new App().menu(new WBSFileIO(), args[0]);   
                Utility.log(Level.INFO, "Program has ended");
            }
            else
            {
                Utility.log(Level.WARNING, "Incorrect Number of Arguments! Usage: ./gradlew run --args=<filename>");
                throw new IllegalArgumentException("Incorrect Number of Arguments! Usage: ./gradlew run --args=<filename>");
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void menu(WBSFileIO wbsFileIO, String fileName)
    {
        try
        {
            boolean close = false; //flag to check if the program should close
            Map<String, WBSItem> wbs = wbsFileIO.readFile(fileName); //main Map for the WBS
            String opType = "Default"; //set initial option type to default
            int n = 3 /*Number of Effort Esitmates to be asked (initially 3) */, sumEffort /*Sum of all known efforts*/, unkownTask;/*Num of unkown tasks */
         ;
            Boolean change = false;    /*Flag to see if the file needs to be written to */
            if(!wbs.isEmpty()) //if returned map is not empty
            {
                //Display the WBS
                displayWBS(wbs);
                //Display the Sum of Efforts
                sumEffort = sumEffort(wbs);
                unkownTask = numUnkTasks(wbs);

                System.out.println("\nTotal Effort = " + sumEffort +
                                    "\nUnknown tasks = " + unkownTask);
                while(!close)
                {
                    try
                    {
                        Utility.log(Level.INFO, "Menu Displayed");
                        System.out.println("\nMenu: \n(1) Estimate Effort \n(2) Configure \n(3) Quit");
                        System.out.print("Enter option: ");
                        int option = Utility.readInt(1, 4);
                        switch(option)
                        {
                            case 1: //Estimate Effort

                                Utility.log(Level.INFO, "Estimate Effort Chosen");
                                System.out.println("!Estimate Effort! \nEnter the task ID: ");
                                String id = Utility.readString();
                                boolean isFound = false;
                                WBSItem found = null;

                                for(WBSItem task : wbs.values())
                                {
                                    if (!isFound) 
                                    {
                                        found = task.find(id); //find the task with the given ID
                                        if(found != null)
                                        {
                                            System.out.println("Task Found: ");
                                            found.display(""); //display the task
                                            isFound = true; //set the flag to true
                                        }
                                    }
                                }
                                if(isFound) //if the task is found
                                {
                                    Utility.lambdaLog("Task: " + id + " found!");
                                    performOptionOnSubtasks(found, options.get(opType), n); //perform the option on the task
                                    System.out.println("Task Effort for Task: " + found.getID() + " has been updated to a total of " + found.getEffort());
                                    Utility.lambdaLog("Task Effort for Task: " + found.getID() + " has been updated to a total of " + found.getEffort());
                                    change = true; //set the change flag to true so that it will write to file
                                }
                                else //if the task is not found 
                                {
                                    System.out.println("Task: " + id + " not found!");
                                    Utility.lambdaLog("Task: " + id + " not found!");
                                }

                            break;

                            case 2:
                                Utility.log(Level.INFO, "Configure Chosen");
                                System.out.println("!Configure!");
                                System.out.println("Enter the number of estimates to be entered: "); //ask for the number of estimates to be entered
                                Utility.log(Level.INFO, "Number of Estimates to be entered");
                                n = Utility.readInt(1, Integer.MAX_VALUE);
                                Utility.lambdaLog("Number of Estimates updated to: " + n);
                                System.out.println("Enter the type of estimation: "); //ask for the type of estimation
                                System.out.println("(1) Default \n(2) Highest \n(3) Median");
                                Utility.log(Level.INFO, "Estimation Type");
                                int type = Utility.readInt(1, 4);
                                switch(type)
                                {
                                    case 1:
                                        Utility.log(Level.INFO, "Estimation Type updated to Default");
                                        opType = "Default";
                                    break;

                                    case 2:
                                        Utility.log(Level.INFO, "Estimation Type updated to Highest");
                                        opType = "Highest";
                                    break;

                                    case 3:
                                        Utility.log(Level.INFO, "Estimation Type updated to Median");
                                        opType = "Median";
                                    break;

                                    default:
                                        opType = "Default";
                                    break;
                                }
                                System.out.println("Configuration Complete!");
                                Utility.log(Level.INFO, "Configuration Complete!");
                            break;

                            case 3:
                                if(change == true) //write the list to the file 
                                {
                                List<String> lines = new ArrayList<>(); //List to store the lines to be written to the file
                                    for(WBSItem line : wbs.values()) //write the updated WBS to the list in the correct format
                                    {
                                        line.writeString(lines);
                                    }
                                    wbsFileIO.writeFile(fileName, lines); 
                                    System.out.println("Quitting...");
                                }
                                else //if the file needs to be written to
                                {
                                    System.out.println("Quitting...");
                                }
                                close = true; //exit loop

                            break;

                            default:
                                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                            break;
                        }
                    }
                    catch(InputMismatchException e)
                    {
                        System.out.println("Invalid input! Please enter a valid input.");
                    }
                    catch(IllegalArgumentException e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        catch(IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
            Utility.logException(e.getMessage());;
        }
    }

    private static void addOption(String type, Option option) //method to add options to the map
    {
        options.put(type, option);
    }

    private void displayWBS(Map<String, WBSItem> wbs) //method to display the WBS
    {
        for(WBSItem task : wbs.values())
        {
            task.display("");
        }
    }

    private int sumEffort(Map<String, WBSItem> wbs) //method to calculate the sum of all known efforts
    {
        int sum = 0;
        for(WBSItem task : wbs.values())
        {
            sum += task.getEffort();
        }
        return sum;
    }

    private int numUnkTasks(Map<String, WBSItem> wbs) //method to calculate the number of unknown tasks
    {
        int unkown = 0;
        for(WBSItem task : wbs.values())
        {
            unkown += task.taskType();
        }
        return unkown;
    }

    public void performOptionOnSubtasks(WBSItem task, Option option, int n) //method to perform the option on the subtasks
    {
        List<Integer> inputs = new ArrayList<>();
        if (task.getSubTasks() == null) 
        {
            System.out.println("Change Effort Estimate for task: ( " + task.getID() + " )");
            for (int i = 0; i < n; i++) //Loop to get the effort estimates for n times
            {
                System.out.println("Enter estimate effort " + (i + 1) + ": ");
                int effort = Utility.readInt(0, Integer.MAX_VALUE);
                inputs.add(effort);
            }
            System.out.println("Estimate efforts: " + inputs); //Print the effort estimates
            option.doOption(inputs, task);
        } 
        else 
        {
            for (WBSItem subTask : task.getSubTasks()) 
            {
                performOptionOnSubtasks(subTask, option, n);
            }
        }
    }
}

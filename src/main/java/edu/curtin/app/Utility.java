package edu.curtin.app;
import java.util.logging.Logger;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
public class Utility 
{
    private static final Logger logger = Logger.getLogger(Utility.class.getName()); //Logger for the Util class

    public static void log(Level level, String message) //Logs a message
    {
        logger.log(level, () -> message);
    }

    public static void lambdaLog(String message) //Logs a message using a lambda expression
    {
        logger.info(() -> message);
    }

    public static void logException(String message) //Logs an exception
    {
        logger.severe(() -> message);
    }
    


    public static int readInt(int min, int max) //Reads an integer between the min and max values
    {
        lambdaLog("Reading integer between " + min + " and " + max);
        Scanner sc = new Scanner(System.in);
        int input = 0;
        try
        {
            input = sc.nextInt();
            while(input < min || input > max) //keep looping until the input is within the range
            {
                System.out.print("Invalid input. Please enter a number between " + (min) + " and " + (max-1) + " (Inclusive): ");
                input = sc.nextInt();
            }
        }
        catch(InputMismatchException e)
        {
            logger.log(Level.WARNING, "Invalid input! an integer was expected");
            System.out.println("Invalid input! Please enter a valid input.");
        }
        return input;
    }

    public static String readString() //Reads a string
    {
        logger.log(Level.INFO, "Reading string");
        Scanner sc = new Scanner(System.in);
        String input = "";
        try
        {
            input = sc.nextLine();
            if(input.trim().isEmpty()) //Check if the input is empty
            {
                throw new IllegalArgumentException("Input Cannot be Empty!");
            }
        }
        catch(IllegalArgumentException e)
        {
            logger.warning(() -> e.getMessage());
            System.out.println(e.getMessage());
        }
        return input;
    }

}

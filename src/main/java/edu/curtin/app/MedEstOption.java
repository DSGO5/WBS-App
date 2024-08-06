package edu.curtin.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MedEstOption implements Option //Median Option for changing effort estimate
{
    @Override
    public void doOption(List<Integer> estimates, WBSItem task)
    {
        int result;
        Set<Integer> uniqueEfforts = new HashSet<>(estimates); //Use set to filter unique efforts
        if(uniqueEfforts.size() == 1) //If all efforts are the same
        {
            result = estimates.get(0);
        }
        else
        {
            List<Integer> sortedEfforts = new ArrayList<>(uniqueEfforts); //Convert back to list so you can sort
            Collections.sort(sortedEfforts); //Sort the efforts
            int size = sortedEfforts.size(); //Get the size of the list
            System.out.println("Sorted Efforts: " + sortedEfforts); //Print the sorted efforts to the user so they can see the median and that duplicates have been removed
            double median;
            if(size % 2 == 0) //If the size is even
            {
                median = (sortedEfforts.get(size / 2 - 1) + sortedEfforts.get(size / 2)) / 2.0; //Get the average of the two middle numbers
            }
            else
            {
                median = sortedEfforts.get(size / 2); //Get the middle number
            }
            result = (int) median; //Convert the median to an integer
        }

        task.changeEffort(result); //Change the effort estimate of the task
    }
}

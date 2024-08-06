package edu.curtin.app;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HighEstOption implements Option //Highest Option for changing effort estimate
{
    @Override
    public void doOption(List<Integer> estimates, WBSItem task) 
    {
        int result;
        
        Set<Integer> uniqueEfforts = new HashSet<>(estimates); //Use set to filter unique efforts
        if(uniqueEfforts.size() == 1) //If all efforts are the same
        {
            result = estimates.get(0); //dont need to convert to set as all the values are the same
        }
        else //If efforts are not the same
        {
            result = Collections.max(uniqueEfforts); //Get the highest effort
        }

        task.changeEffort(result);
        
    }
}

package edu.curtin.app;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultOption implements Option //Default option for changing effort estimate
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
            System.out.println("Two or More Efforts are not the same, Please enter a Review Effort: ");
            result = Utility.readInt(0, Integer.MAX_VALUE); //Get the review effort
        }
        task.changeEffort(result); //Change the effort estimate of the task
    }
}

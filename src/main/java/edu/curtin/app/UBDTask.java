package edu.curtin.app;

import java.util.List;

public class UBDTask implements WBSItem
{
    private String pid; //Task ID
    private String id; //Task ID
    private String description; //Task description
    private int effort; //Tasks effort estimate

    public UBDTask(String pid, String id, String description, int effort) //Constructor
    {
        this.pid = pid;
        this.id = id;
        this.description = description;
        this.effort = effort;
    }

    @Override
    public String getID() //Getter
    {
        return id;
    }

    @Override
    public List<WBSItem> getSubTasks() //returns null as it doesnt have subtasks 
    {
        return null;
    }

    @Override
    public void display(String indent) //Display the task
    {
        if(effort == 0)
        {
            System.out.println(indent + id + ": " + description);
        }
        else
        {
            System.out.println(indent + id + ": " + description + ", effort = " + effort);
        }
    }

    @Override
    public WBSItem find(String id) //Find a task by ID
    {
        WBSItem task = null;
        if(this.id.equals(id))
        {
            task = this;
        }
        return task;
    }

    @Override
    public void changeEffort(int effort) //Change the effort estimate
    {
        this.effort = effort;
    }

    @Override
    public int getEffort() //Get the effort estimate
    {
        return effort;
    }

    @Override
    public int taskType() //Return 1 if effort is known, 0 if effort is unknown
    {
        if (effort == 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void writeString(List<String> lines) ////Writes task to a list of strings in proper format
    {

        if(pid.isEmpty() && effort == 0)
        {
            lines.add("; " + id + " ; " + description + " ;");
        }
        else if(effort == 0)
        {
            lines.add(pid + " ; " + id + " ; " + description + " ;");
        }
        else if(pid.isEmpty())
        {
            lines.add("; " + id + " ; " + description + " ; " + effort);
        } 
        else
        {
            lines.add(pid + " ; " + id + " ; " + description + " ; " + effort);
        }
    }
}

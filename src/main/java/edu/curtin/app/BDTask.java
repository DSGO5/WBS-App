package edu.curtin.app;
//if line only has length 3 then it is a task that can break down 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BDTask implements WBSItem
{
    private String pid; //Task ID
    private String id; //Task ID
    private String description; //Task description
    private List<WBSItem> subTasks; //List of sub-Tasks 

    public BDTask(String pid, String id, String description) //Constructor
    {
        this.pid = pid;
        this.id = id;
        this.description = description;
        subTasks = new ArrayList<>(); 
    }

    public void addSubTask(WBSItem task) //Add sub-task
    {
        subTasks.add(task);
    }


    @Override
    public String getID() //Getter
    {
        return id;
    }
    
    @Override
    public List<WBSItem> getSubTasks() //Getter
    {
        return subTasks;
    }

    @Override
    public void display(String indent) //Display the task and its sub-tasks
    {
        System.out.println(indent + id + ": " + description);
        for(WBSItem task : subTasks)
        {
            task.display(indent + "  ");
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
        else
        {
            boolean found = false;
            Iterator<WBSItem> iterator = subTasks.iterator();
            while (!found && iterator.hasNext()) 
            {
                task = iterator.next().find(id);
                if(task != null)
                {
                    found = true;
                }
            }
        }
        return task;
    }

    @Override
    public void changeEffort(int effort) //Change the effort of all sub-tasks
    {
        for(WBSItem task : subTasks)
        {
            task.changeEffort(effort);
        }
    }

    @Override
    public int getEffort() //Get the total effort of all sub-tasks
    {
        int total = 0;
        for(WBSItem task : subTasks)
        {
            total += task.getEffort();
        }
        return total;
    }

    @Override
    public void writeString(List<String> lines) //Writes task and sub-tasks to a list of strings in proper format
    {
        if(pid.isEmpty())
        {
            lines.add("; " + id + " ; " + description);
        }
        else
        {
            lines.add(pid + " ; " + id + " ; " + description);
        }
        for(WBSItem task : subTasks)
        {
            task.writeString(lines);
        }
    }
    

    @Override
    public int taskType() //used to calculate unknown tasks in the project
    {
        int total = 0;
        for(WBSItem task : subTasks)
        {
            total += task.taskType();
        }
        return total;
    }

}

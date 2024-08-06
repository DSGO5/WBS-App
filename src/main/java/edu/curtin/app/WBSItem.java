package edu.curtin.app;

import java.util.ArrayList;
import java.util.List;

public interface WBSItem //This interface represents any tasks in the work breakdown structure (WBS)
{
    /*
     * Displays the WBS item in a human-readable format, with the given indentation.
     * @param indent The indentation to use when displaying the item.
     */
    void display(String indent);

    /*
     * Finds a WBS item with the given ID.
     * @param ID is The ID of the item to find.
     * @return The item with the given ID, or null if no such item exists.
     */
    WBSItem find(String id);
    
    /*
     * Returns the type of the taskbased on if the estimate effort is known.
     * @return The type of the task.
     */
    int taskType();

    /*
     * Returns the sub-tasks of the task to iterate through for doOption.
     * @return The sub-tasks of the task.
     */
    List<WBSItem> getSubTasks();

    /*
     * Returns the effort estimate of the task for summing.
     * @return The effort estimate of the task.
     */
    int getEffort();

    /*
     * Changes the effort estimate of the task.
     * @param effort The new effort estimate.
     */
    void changeEffort(int effort);

    /*
     * Returns the ID of the task for clarification when changing the effort estimate 
     * (gets confusing when changing effort estimates for consecutive subtasks).
     * @return The ID of the task.
     */
    String getID();

    /*
     * Writes the task to a list of strings used for file writing in proper format.
     * @param lines The list of strings to write to.
     */
    void writeString(List<String> lines);

    /**
     * Convenience method for Writing to file, without having a pre-existing list to put the
     * results into.
     */
    default List<String> writeString()
    {
        List<String> lines = new ArrayList<>();
        writeString(lines);
        return lines;
    }
}

For Assignment 1, I have used the composite pattern to implement the work break down structure into a Map. Tasks that do not breakdown (don't have subtasks)
are represented as leaf nodes, while tasks that do breakdown are represented as composite nodes. The composite pattern allows me to save the tasks in a tree structure
and use recursive methods for traversing tasks that can break down. The composite pattern also allows me to treat both leaf and composite nodes uniformly, which makes it easier
to implement the methods that are common to both types of nodes, such as find(), display() and so on. 

Furthermore, using the stratergy pattern to implement the different ways of reconciling different task estimates allows me to easily add new ways of reconciling estimates in the future.
The stratergy pattern allows me to encapsulate the different ways of reconciling estimates into their own classes, which makes the program more structured and allows for easier implementation of 
the different alogirthms needed to reconcile estimates.

I decided to use a HashMap to store the tasks in the Map, as it allows me to easily access the tasks by their name. This makes it easier to implement the find() method, as I can simply
use the ID of the task to find it in the Map. Using a HashMap also gives me a time comlpexity of O(1) when finding tasks that dont have any parents, but a time complexity of O(n) when finding tasks that do have parents,
where n is the number of subtasks of the parent task. I chose to use a map instead of a Set as being able to use the ID of a task as a key allowed for instant search and made my implementation easier. 
Using a map would also act as a set as if two tasks have the same ID, the map would only store one of them, overwriting the task that is already in the map, as a set would. 
Also, to get the unique efforts from the list of inputted efforts, i chose casting it to a Set would be efficient as this would automatically remove any identical values from the list.

I also decided to use a flag to keep track of if the estimates of the tasks have been reconciled or not as a way to decide if the input file needed to be rewritten. 
This approach made more sense to me as it would lead to better preformance as I didn't need to re-read the file and compare the values to those in my map to see if the
file needs to be rewritten. Although, this implementation does have the downside of not being able to tell if the file has been changed to the same values as initially read in,
but I decided that this was an acceptable trade off for the better preformance.

The program also has a Utility class that stores the Logger i used and certain methods that are used to validate user input. This seemed more useful to me as it allowed me to keep the main class clean and
only have the methods that are used to run the program in the main class. I also didn't have to keep creating new instances of the Logger as i could call it directly from the Utility class.


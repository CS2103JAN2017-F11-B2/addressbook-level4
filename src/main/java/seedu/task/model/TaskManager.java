package seedu.task.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskTimeComparable;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Wraps all data at the task-manager level Duplicates are not allowed (by
 * .equals comparison)
 */
public class TaskManager implements ReadOnlyTaskManager {

    private final UniqueTaskList tasks;
    private final UniqueTagList tags;
    private UniqueTaskList backupTasks;

    /*
     * The 'unusual' code block below is an non-static initialization block,
     * sometimes used to avoid duplication between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are
     * other ways to avoid duplication among constructors.
     */
    {
    	tasks = new UniqueTaskList();
    	tags = new UniqueTagList();
    }

    public TaskManager() {
    }

    /**
     * Creates an Task Manager using the Tasks and Tags in the
     * {@code toBeCopied}
     */
    public TaskManager(ReadOnlyTaskManager toBeCopied) {
    	this();
		resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setTasks(List<? extends ReadOnlyTask> tasks) throws UniqueTaskList.DuplicateTaskException {
    	this.tasks.setTasks(tasks);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
    	this.tags.setTags(tags);
    }

    public void resetData(ReadOnlyTaskManager newData) {
		assert newData != null;
		try {
		    setTasks(newData.getTaskList());
		} catch (UniqueTaskList.DuplicateTaskException e) {
		    assert false : "Task Manager should not have duplicate tasks";
		}
		try {
		    setTags(newData.getTagList());
		} catch (UniqueTagList.DuplicateTagException e) {
		    assert false : "Task Manager should not have duplicate tags";
		}
		syncMasterTagListWith(tasks);
    }

    //// task-level operations

    /**
     * Adds a task to the task manager. Also checks the new task's tags and
     * updates {@link #tags} with any new tags found, and updates the Tag
     * objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException
     *             if an equivalent task already exists.
     */

    public void addJobTask(Task p) throws UniqueTaskList.DuplicateTaskException {

    	syncMasterTagListWith(p);
		tasks.add(p);
    }

    /**
     * Updates the task in the list at position {@code index} with
     * {@code editedReadOnlyTask}. {@code TaskManager}'s tag list will be
     * updated with the tags of {@code editedReadOnlyTask}.
     * 
     * @see #syncMasterTagListWith(Task)
     *
     * @throws DuplicateTaskException
     *             if updating the task's details causes the task to be
     *             equivalent to another existing task in the list.
     * @throws IndexOutOfBoundsException
     *             if {@code index} < 0 or >= the size of the list.
     */
    public void updateTask(int index, ReadOnlyTask editedReadOnlyTask) throws UniqueTaskList.DuplicateTaskException {
	assert editedReadOnlyTask != null;

	Task editedTask = new Task(editedReadOnlyTask);
	// TODO: the tags master list will be updated even though the below line
	// fails.
	// This can cause the tags master list to have additional tags that are
	// not tagged to any task
	// in the task list.
	tasks.updateTask(index, editedTask);
    }

    //Mark a task as completed
    public boolean completeTask(ReadOnlyTask target, ReadOnlyTask toBeComplete) throws UniqueTaskList.TaskNotFoundException {
        if (tasks.complete(target, toBeComplete)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
    
    /**
     * Ensures that every tag in this task: - exists in the master list
     * {@link #tags} - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Task task) {
		final UniqueTagList taskTags = task.getTags();
		tags.mergeFrom(taskTags);
	
		// Create map with values = tag object references in the master list
		// used for checking task tag references
		final Map<Tag, Tag> masterTagObjects = new HashMap<>();
		tags.forEach(tag -> masterTagObjects.put(tag, tag));
	
		// Rebuild the list of task tags to point to the relevant tags in the
		// master tag list.
		final Set<Tag> correctTagReferences = new HashSet<>();
		taskTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
		task.setTags(new UniqueTagList(correctTagReferences));
    }

    /**
     * Ensures that every tag in these tasks: - exists in the master list
     * {@link #tags} - points to a Tag object in the master list
     * 
     * @see #syncMasterTagListWith(Task)
     */
    private void syncMasterTagListWith(UniqueTaskList tasks) {
    	tasks.forEach(this::syncMasterTagListWith);
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
		if (tasks.remove(key)) {
		    return true;
		} else {
		    throw new UniqueTaskList.TaskNotFoundException();
		}
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
    	tags.add(t);
    }

    //// util methods

    @Override
    public String toString() {
    	return tasks.asObservableList().size() + " tasks, " + tags.asObservableList().size() + " tags";
	// TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {
    	return new UnmodifiableObservableList<>(tasks.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
    	return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
		return other == this // short circuit if same object
			|| (other instanceof TaskManager // instanceof handles nulls
				&& this.tasks.equals(((TaskManager) other).tasks));
    }

    @Override
    public int hashCode() {
	// use this method for custom fields hashing instead of implementing
	// your own
    	return Objects.hash(tasks);
    }
    public void undo() throws Exception {
    	if (backupTasks == null) {
    		throw new Exception("Can't undo without undo state");
    	} else {
    		tasks.clear();
    		for (Task t : backupTasks) {
    			tasks.add(t);
    		}
    		backupTasks = null;
    	}
    }
    public void updateBackup() throws DuplicateTaskException {
    	if (backupTasks != null) {
    		backupTasks.clear();
    	} else {
    		backupTasks = new UniqueTaskList();
    		backupTasks.clear();
    	}
    	for (Task t : tasks) {
    		backupTasks.add(new Task(t));
    	}
    }

	public void sortTasksByTime() {
		List<Task> taskList = new ArrayList<Task>();
		for (Task t : tasks) {
			taskList.add(t);
		}
		for (int i = 0; i < taskList.size() - 1; i++) {
			for (int j = i; j < taskList.size(); j++) {
				if (new TaskTimeComparable(taskList.get(i)).compareTo(new TaskTimeComparable(taskList.get(j))) > 0) {
					Task temp = taskList.get(i);
					taskList.set(i, taskList.get(j));
					taskList.set(j, temp);
				}
			}
		}
		tasks.clear();
		for (Task t : taskList) {
			try {
				tasks.add(t);
			} catch (DuplicateTaskException dte) {
				System.out.println("Unexpected error in TASKMANAGER sort by name");
			}
		}
	}

	public void sortTasksByName() {
		List<Task> taskList = new ArrayList<Task>();
		for (Task t : tasks) {
			taskList.add(t);
		}
		for (int i = 0; i < taskList.size() - 1; i++) {
			for (int j = i; j < taskList.size(); j++) {
				if (taskList.get(i).getTaskName().compareTo(taskList.get(j).getTaskName()) > 0) {
					Task temp = taskList.get(i);
					taskList.set(i, taskList.get(j));
					taskList.set(j, temp);
				}
			}
		}
		tasks.clear();
		for (Task t : taskList) {
			try {
				tasks.add(t);
			} catch (DuplicateTaskException dte) {
				System.out.println("Unexpected error in TASKMANAGER sort by name");
			}
		}
		
	}
}

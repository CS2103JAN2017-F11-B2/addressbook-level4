package seedu.task.model.task;

public class TaskStatus {

    public String status;
    
    public TaskStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return status;
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskStatus // instanceof handles nulls
                && this.status.equals(((TaskStatus) other).status)); // state check
    }

    @Override
    public int hashCode() {
        return status.hashCode();
    }
}

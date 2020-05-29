package ir.ac.kntu.Entity;

/**
 * wirks for both serve and delivery activation
 */
public class ServiceActivation {
    private int id;
    private boolean active;
    private boolean manuallyDisabled; //if true, means the manager does not want to accept services
    private String todaysServiceTime;

    public ServiceActivation(int id, boolean active, String todaysServiceTime) {
        this.id = id;
        this.active = active;
        this.manuallyDisabled = false;
        this.todaysServiceTime = todaysServiceTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isManuallyDisabled() {
        return manuallyDisabled;
    }

    public void setManuallyDisabled(boolean manuallyDisabled) {
        this.manuallyDisabled = manuallyDisabled;
    }

    public String getTodaysServiceTime() {
        return todaysServiceTime;
    }

    public void setTodaysServiceTime(String todaysServiceTime) {
        this.todaysServiceTime = todaysServiceTime;
    }
}

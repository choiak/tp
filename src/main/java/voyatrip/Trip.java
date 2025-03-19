package voyatrip;

import java.util.ArrayList;

import java.time.LocalDate;

/**
 * This is the trip class that will hold all the information about the trip.
 */
public class Trip {
    private final String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalBudget;
    private Integer numDays;
    private ArrayList<Transportation> transportations;
    private ArrayList<Accommodation> accommodations;

    /**
     * Constructor for the trip class.
     * @param startDate the start date of the trip.
     * @param endDate the end date of the trip.
     * €param numDays the number of days for the trip.
     * @param totalBudget the total budget for the trip.
     */
    public Trip(String name, LocalDate startDate, LocalDate endDate, Integer numDays, Integer totalBudget) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numDays = numDays;
        this.totalBudget = totalBudget;
        this.accommodations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTransportation(String transportMode, String transportName, Integer transportBudget) {
        transportations.add(new Transportation(transportMode, transportName, transportBudget));
    }

    public void deleteTransportation(Integer index) {
        if (index < 1 || index > transportations.size()) {
            System.out.println("Invalid index: " + index);
            return;
        }
        transportations.remove(index - 1);
        // index - 1, to convert to zero-based index
    }

    public void addAccommodation(String accommodationName, Integer accommodationBudget) {
        accommodations.add(new Accommodation(accommodationName, accommodationBudget));
    }

    public void deleteAccommodation(Integer index) {
        accommodations.remove(index - 1);
    }

    public Accommodation getAccommodation(Integer index) {
        return accommodations.get(index - 1);
    }

    public Accommodation getLastAccommodation() {
        return accommodations.get(accommodations.size() - 1);
    }

    public String abbrInfo() {
        return name + ": " + startDate + "->" + endDate + " (days: " + numDays + ", budget: " + totalBudget + ")";
    }
}


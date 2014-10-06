package data;

/**
 * Simple enum class for representing the different types
 * 
 * @author SG
 * @version 0.2
 */
public enum Types {
    SDP(11), SRS(12), SVVS(13), STLDD(14), SVVI(15), SDDD(16), SVVR(17), SSD(18), 
    ProjectFinalReport(19), FunctionalTesting(21), SystemTesting(22), RegressionTesting(23), Meeting(30),
    Lecture(41), Exercise(42), ComputerExercise(43), OutsideReading(44), Other(100);
    private int value;

    private Types(int value) {
    	this.value = value;
    }
    
    public int getValue() {
    	return this.value;
    }
};
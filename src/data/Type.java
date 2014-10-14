package data;

/**
 * Simple enum class which holds the different types in the system.
 * 
 * @author SG
 * @version 0.2
 */
public enum Type {
    SDP(11), SRS(12), SVVS(13), STLDD(14), SVVI(15), SDDD(16), SVVR(17), SSD(18), 
    ProjectFinalReport(19), FunctionalTesting(21), SystemTesting(22), RegressionTesting(23), Meeting(30),
    Lecture(41), Exercise(42), ComputerExercise(43), OutsideReading(44), Other(100);
    private int value;

    private Type(int value) {
    	this.value = value;
    }
    
    public static boolean isType(int number){
    	boolean isType = false;
    	for (Type t : Type.values()){
    		if(t.value == number){
    			isType=true;
    		}
    	}
    	return isType;
    }
    
    public int getValue() {
    	return this.value;
    }
};
package data;

/**
 * Simple enum class which holds the different numbers in the system.
 * 
 * @author SG
 * @version 0.2
 */
//TODO JavaDoc
public enum Number {
    SDP(11), SRS(12), SVVS(13), STLDD(14), SVVI(15), SDDD(16), SVVR(17), SSD(18), 
    ProjectFinalReport(19), FunctionalTesting(21), SystemTesting(22), RegressionTesting(23), Meeting(30),
    Lecture(41), Exercise(42), ComputerExercise(43), OutsideReading(44), Other(100);
    private long value;

    private Number(int value) {
    	this.value = value;
    }
    
    public static boolean isNumber(long number){
    	boolean isType = false;
    	for (Number t : Number.values()){
    		if(t.value == number){
    			isType=true;
    		}
    	}
    	return isType;
    }
    
    public long getValue() {
    	return this.value;
    }
};
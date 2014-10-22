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

    /***
     * The constructor of the enum class. It takes an integer value as
     * parameter to represent a type number.
     * 
     * @param an integer value representing a type number.
     */
    private Number(int value) {
    	this.value = value;
    }
    
    /***
     * This method checks if the given long in the parameters is a valid type number.
     * It loops through the different enum values and checks if one of them equals the
     * number given through the input. If a match is found it sets a boolean value to true
     * and returns it, otherwise it returns the boolean value false.
     * 
     * @param number represents the type number that is going to be checked.
     * @return true if the given input number represents a type number,
     * 			false otherwise.
     */
    public static boolean isNumber(long number){
    	boolean isType = false;
    	for (Number t : Number.values()){
    		if(t.value == number){
    			isType=true;
    		}
    	}
    	return isType;
    }
    
    /***
     * A getter returning the integer value of the type.
     * @return an integer that represents the type number.
     */
    public long getValue() {
    	return this.value;
    }
};
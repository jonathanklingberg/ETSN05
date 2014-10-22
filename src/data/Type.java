package data;

/**
 * Simple enum class which holds the different types in the system.
 * 
 * @author SG
 * @version 0.2
 */

public enum Type {
    Development('D'), Formal('F'), Informal('I'), Rework('R');
    private char value;
    
    /***
     * The constructor of the enum class. It takes an char value as
     * parameter to represent a type.
     * 
     * @param a char value representing a type.
     */
    private Type(char value) {
    	this.value = value;
    }
    
    /***
     * This method checks if the given char in the parameters is a valid type.
     * It loops through the different enum values and checks if one of them equals the
     * char given through the input. If a match is found it sets a boolean value to true
     * and returns it, otherwise it returns the boolean value false.
     * 
     * @param charType represents the type that is going to be checked.
     * @return true if the given input char represents a type,
     * 			false otherwise.
     */
    public static boolean isType(char charType){
    	boolean isExistingType = false;
    	for (Type t : Type.values()){
    		if(t.value == charType){
    			isExistingType=true;
    		}
    	}
    	return isExistingType;
    }
    
    /***
     * A getter returning the char value of the type.
     * @return a char that represents the type.
     */
    public char getValue() {
    	return this.value;
    }
};
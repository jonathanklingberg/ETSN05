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

    private Type(char value) {
    	this.value = value;
    }
    
    public static boolean isType(char charType){
    	boolean isExistingType = false;
    	for (Type t : Type.values()){
    		if(t.value == charType){
    			isExistingType=true;
    		}
    	}
    	return isExistingType;
    }
    
    public char getValue() {
    	return this.value;
    }
};
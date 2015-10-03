//File:         [PseudoString.java]
//Created:      [09/15/2015]
//Last Changed: $Date: 09/15/2015 $
//Author:       <A HREF="mailto:[ybai11@jhu.edu]">[Yu Bai]</A>
//
package mathcompiler;

public class PseudoString {
	/**
	[PseudoString provides a wrapper for character arrays. It unifies character arrays of varying sizes.]
	[Variables include a character array. Basic access/manipulation methods are implemented.
	Additional methods are available for single character array.]
	@see [inChars]
   	@see [PseudoString#setChars]
    @see [PseudoString#getChars]
    @see [PseudoString#getCharAt]
    @see [PseudoString#getLength]
    @see [PseudoString#subChars]
    @see [PseudoString#concatChars]
    @see [PseudoString#hasNext]
	**/
	
	private char[] myChars; //an array containing all characters that compose of a string
	
	//constructor: 
	public PseudoString(char[] inChars){
		setChars(inChars);
	}
	
	public PseudoString(char inChar){
		setChars(inChar);
	}
	
	public PseudoString(int inNum){
		setChars(inNum);
	}
	
	/**
	 * set/update field variables; overloaded version for a single character or integer input is available
	 * @param inChars, which is the input character array
	 * @return none
	 */
	public void setChars(char[] inChars){
		myChars=inChars;
	}
	
	public void setChars(char inChar){
		myChars=new char[]{inChar};
	}

	public void setChars(int inNum){
		myChars=MyUtils.int2Chars(inNum);
	}
	
	/**
	 * get the character array variable myChars.
	 * @param none
	 * @return myChars
	 */
	public char[] getChars(){return myChars;}

	/**
	 * get the character at a given index.
	 * @param index
	 * @return myChar
	 */
	public char getCharAt(int index){return myChars[index];}

	/**
	 * get the length of the char array myChars;
	 * @param none
	 * @return length
	 */
	public int getLength(){return myChars.length;}
	
	
	/**
	 * extract a part of myChars defined by the start and end indices;
	 * this method is dedicated to myChars only, as opposed to the static method cloneChars 
	 * that can be applied to any given char array.
	 * @param startIdx, endIdx
	 * @return a subset of characters
	 */
	public char[] subChars(int startIdx, int endIdx){
		return MyUtils.cloneChars(myChars, startIdx, endIdx);
	}
	
	/**
	 * concatenate input char arrays to the current char array.
	 * @param inChars
	 * @return none
	 */
	public void concatChars(char[] inChars){
		char[] catChars=new char[myChars.length+inChars.length];
		for(int i=0; i<myChars.length;i++){
			catChars[i]=myChars[i];
		}
		for(int i=0; i<inChars.length;i++){
			catChars[i+myChars.length]=inChars[i];
		}
		myChars=catChars;
	}

	/**
	 * test if the array myChars has a next, valid element with respect to the given index.
	 * @param index
	 * @param type
	 * @return true or false
	 */
	public boolean hasNext(int index,int type){
		if(type==MyUtils.POSTFIX){
			if(index>=-1 && index<myChars.length-1 && myChars.length>0){
				return true;
			}else{return false;}
		}else if(type==MyUtils.PREFIX){
			if(index>0 && index<=myChars.length && myChars.length>0){
				return true;
			}else{return false;}
		}else{
			return false;
		}
	}


	
}

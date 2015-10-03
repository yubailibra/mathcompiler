//File:         [MyQueue.java]
//Created:      [09/15/2015]
//Last Changed: $Date: 09/15/2015 $
//Author:       <A HREF="mailto:[ybai11@jhu.edu]">[Yu Bai]</A>
//
package mathcompiler;

public class MyQueue {
	/**
	[MyQueue provides a dynamic FIFO data structure.]
	[Basic functions add,remove, peek, empty are implemented]
	@see [size]
   	@see [first]
   	@see [rear]
   	@see [MyQueue#add]
   	@see [MyQueue#remove]
   	@see [MyQueue#removeAll]
   	@see [MyQueue#peek]
   	@see [MyQueue#empty]
   	@see [MyQueue#getSize]
	**/
	private PseudoStringNode first;
	private PseudoStringNode rear;
	private int size;
	public MyQueue(){
		first=null;
		rear=null;
		size=0;
	}	
	
	/**
	 * push a new element to the end of the list
	 * @param newString
	 * @return none
	 */
	public void add(PseudoString newString){
		PseudoStringNode temp=new PseudoStringNode();	
		temp.myString=newString;				
		if(first==null){	//if this is the first element ever, both first and rear nodes point to it
			temp.next=null;							
			first=temp;
			rear=temp;
		}else{				//otherwise, update the last node, do nothing for the first node
			temp.next=rear.next;
			rear.next=temp;
			rear=temp;
		}
		size++;
	}

	/**
	 * remove and return an element from the beginning of the list
	 * @param none
	 * @return item
	 */
	public PseudoString remove(){
		if(empty()){
			System.out.println("warning: the list is empty!");
			return null;
		}
		PseudoString item;
		PseudoStringNode temp=first;
		item=temp.myString;
		first=temp.next;
		if(first==null){ //if the last element was removed, update the rear pointer as well)
			rear=null;
		}
		temp.next=null;  //disconnect obsolete node from the queue
		size--;
		return item;
	}
	
	/**
	 * clean up all elements
	 */
	public void removeAll(){
		PseudoStringNode temp;
		while(!empty()){
			temp=first;
			first=temp.next;
			if(first==null){ //if the last element was removed, update the rear pointer as well)
				rear=null;
			}
			temp.next=null;  //disconnect obsolete node from the queue
			size--;
		}
	}

	/**
	 * peek the first element
	 * @param none
	 * @return item
	 */
	public PseudoString peek(){
		if(empty()){
			System.out.println("warning: the list is empty!");
			return null;
		}
		return first.myString;
	}

	/**
	 * test if the list is empty
	 * @param none
	 * @return true or false
	 */
	public boolean empty(){
		return (first==null);
	}

	/**
	 * get size of the current stack
	 * @param none
	 * @return size
	 */
	public int getSize(){
		return size;
	}

}

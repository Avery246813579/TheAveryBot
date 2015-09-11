package com.frostbyte.theaverybot.irc.server;

import java.util.Vector;

public class IQueue {
	private Vector<Object> queue = new Vector<Object>();

	public void add(Object object){
		synchronized (queue) {
			queue.addElement(object);
			queue.notify();
		}
	}
	
	public Object next(){
		Object object = null;
		
		synchronized (queue) {
			if(queue.size() == 0){
				try{
					queue.wait();
				}catch(Exception ex){
					return null;
				}
			}
			
			try{
				object = queue.firstElement();
				queue.removeElementAt(0);
			}catch(Exception ex){
				throw new InternalError("Error inside Object Queue");
			}
		}
		
		return object;
	}
	
	public boolean hasNext(){
		return size() != 0;
	}
	
	public void clear(){
		synchronized (queue) {
			queue.removeAllElements();
		}
	}
	
	public int size(){
		return queue.size();
	}
}

package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uprm.cse.datastructures.cardealer.model.Car;
import edu.uprm.cse.datastructures.cardealer.model.CarComparator;

public class CircularSortedDoublyLinkedList<E> implements SortedList<E>{
	private int size;
	private Node<E> header = new Node(null, null, null);
	private Comparator<E> cmp;  //(used to test the Data Structure)
//	private CarComparator cmp = new CarComparator();
	
	public CircularSortedDoublyLinkedList(){
		size = 0;
		header = new Node<E>(null,null,null);
		header.setNext(header);
		header.setPrev(header);
//		cmp = new CarComparator();  //comment when testing Data Structure with default Comparator<E>
	}
	
//	public CircularSortedDoublyLinkedList(CarComparator c) {
//		this();
//		this.cmp= c;
//	}
	
//  The constructor to test the Data Structure:

	public CircularSortedDoublyLinkedList(Comparator<E> c) {
		this();
		this.cmp= c;
	}
	
	private static class Node<E>{
		private E element;
	    private Node<E> prev, next;
	    
		public Node(){
			super();
		}
		public Node(E e) {
			this.element = e;
		}
		public Node(E element, Node<E> prev, Node<E> next) {
			super();
			this.element = element;
			this.prev = prev;
			this.next = next;
		}
		
		private E getElement() {
			return element;
		}
		private void setElement(E element) {
			this.element = element;
		}
		private Node<E> getPrev() {
			return prev;
		}
		private void setPrev(Node<E> prev) {
			this.prev = prev;
		}
		private Node<E> getNext() {
			return next;
		}
		private void setNext(Node<E> next) {
			this.next = next;
		}
		
		public void clear() {
			prev = next = null;
		}
		
	}

	//The Iterator Class for the CircularSortedDoublyLinkedList.
	private class CSDLLIterator implements Iterator<E>{
		
		private Node<E> nextNode;
		
		//constructor
		public CSDLLIterator() {
			this.nextNode = (Node<E>)header.getNext();
		}
		
		@Override
		public boolean hasNext() {
			return nextNode != header;
		}

		@Override
		public E next() {
			if(this.hasNext()) {
				E result = this.nextNode.getElement();
				this.nextNode = this.nextNode.getNext();
				return result;
			}
			else {
				throw new NoSuchElementException();
			}
		}
		
	}
	
	
	@Override
	public Iterator<E> iterator() {
		return new CSDLLIterator();
	}

	
	/**This method adds an element c to the list, basing sorting order with
	 * CarComparator.
	 * @param c - the element to add.
	 */
	@Override
	public boolean add(E c) {
		
		if(c == null) {
			return false;
		}
		
		Node<E> newNode = new Node<E>(c);
		
		
		if(size == 0) {
			header.setNext(newNode);
			header.setPrev(newNode);
			newNode.setNext(header);
			newNode.setPrev(header);
			size++; 
			return true;
		}
		
		Node<E> iNode = header.getNext();
		while(iNode != header) {
			
			if(cmp.compare(c, iNode.getElement()) <= 0) { //cast (Car) 
				newNode.setPrev(iNode.getPrev());
				newNode.setNext(iNode);
				iNode.setPrev(newNode);
				newNode.getPrev().setNext(newNode);
				size++;
				return true;
			}

			iNode = iNode.getNext();
		}
		newNode.setNext(header);
		newNode.setPrev(header.getPrev());
		header.getPrev().setNext(newNode);
		header.setPrev(newNode);
		size++;
		
		return true;
	}

	//This method returns the size of the target list.
	@Override
	public int size() {
		return this.size;
	}

	/**This method removes an element c in the list. If the element is not
	 * found or if the list is empty, it will return false.
	 * @param c - the element to remove.
	 */
	@Override
	public boolean remove(E c) {
		Node<E> iNode;
		for(iNode = header.getNext(); iNode != header; iNode = iNode.getNext()) {
			if(iNode.getElement().equals(c)) {
				iNode.getNext().setPrev(iNode.getPrev());
				iNode.getPrev().setNext(iNode.getNext());
				iNode.clear();
				size--;
				
				return true;
			}
		}
		//car not found or empty list
		return false;
	}

	/**This method removes an element given the respective index.
	 * @param index - the position where the element-to-remove resides.
	 */
	@Override
	public boolean remove(int index) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		int i = 0;
		Node<E> iNode = header.getNext();
		while(i < index) {
			iNode = iNode.getNext();
			i++;
		}
		iNode.getNext().setPrev(iNode.getPrev());
		iNode.getPrev().setNext(iNode.getNext());
		iNode.clear();
		size--;
		
		return true;
	}

	/**This method removes all elements c in the list and returns 
	 * its count.
	 * @param c - the element to remove;
	 */
	@Override
	public int removeAll(E c) {
		int count = 0;
		while(this.remove(c)) {
			count++;
		}
		return count;
	}

	//This method returns the first element at the target list.
	@Override
	public E first() {
		if(this.isEmpty()) {
			return null;
		}
		return header.getNext().getElement();
	}

	//This method returns the last element of the target list.
	@Override
	public E last() {
		if(this.isEmpty()) {
			return null;
		}
		return header.getPrev().getElement();
	}

	/**
	 * This method will return an element given a position (index).
	 * @param index - position where the method will get an element
	 * 
	 */
	@Override
	public E get(int index) {
		if(index < 0 || index >= size || this.isEmpty()) {
			return null;
		}
		else {
			int i = 0;
			Node<E> iNode = header.getNext();
			while(i < index) {
				iNode = iNode.getNext();
				i++;
			}
			return iNode.getElement();
		}
	}

	//clears the list
	@Override
	public void clear() {
		while(!this.isEmpty()) {
			this.remove(0);
		}
		
	}

	/**This method checks if the target list contains an element c in it.
	 * @param c - the element to search for.
	 * 
	 */
	@Override
	public boolean contains(E c) {
		if(this.isEmpty() || c == null) {
			return false;
		}
		Node<E> iNode;
		for(iNode = header.getNext(); iNode != header; iNode = iNode.getNext()) {
			if(iNode.getElement().equals(c)) {
				return true;
			}
		}
		return false;
	}

	//returns true if the list is empty or false otherwise;
	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	/**This method return the index of where the/a 
	 * element c is first found. If element is not found, return -1.
	 * @param c - the element to find.
	 */
	@Override
	public int firstIndex(E c) {
		int i=0;
		Node<E>iNode;			
		for(iNode=header.getNext(); iNode!=header; iNode=iNode.getNext(), i++) {
			if(c.equals(iNode.getElement()))
				return i;
		}
		//Element not found 
		return -1;
	}
	

	/**This method returns the index of where
	 * the/a element c is last found. If element is not found return -1.
	 * @param c - the element to find.
	 */
	@Override
	public int lastIndex(E c) {
		int i = size()-1;
		Node<E>iNode;
		for(iNode=header.getPrev(); iNode!=header; iNode=iNode.getPrev(), i--) {
			if(c.equals(iNode.getElement()))
				return i;
		}
		//Not found
		return -1; 

	}
	
	//converts list to array
	public Car[] toArray(){
		Car [] result = new Car[this.size];
		int i = 0;
		Node<E> temp = this.header.getNext();
		while(temp != this.header) {
			result[i] = (Car) temp.getElement();
			i++;
			temp = temp.getNext();
		}
		return result;
	}

}

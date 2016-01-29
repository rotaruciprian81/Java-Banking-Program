/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication15;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This is singleton class which return only one instance of this class.
 * GetInstance method return that singleton objects
 * @author Radu Ciprian Rotaru
 *
 */
public class PersistUtil {
	public final String FILE_LOCATION ="accounts";
	
	private static PersistUtil instance = new PersistUtil();
	//making it private as we dont want more than 1 object to get created
	private PersistUtil(){
	}
	//get method to return singleton object
	public static PersistUtil getInstance(){
		return instance;
	}
	/**
	 * Method to save the menu class object to the file named "accounts"
	 * @param menu
	 */
	public void store(Menu menu) {
		try {
			FileOutputStream fileOut = new FileOutputStream(FILE_LOCATION);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			//writing the object to objectOutputStream
			out.writeObject(menu);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			//i.printStackTrace();
		}
	}
	/**
	 * Method to load the Menu class object from file named "accounts"
	 * @return
	 */
	public Menu load() {
		Menu menu = null;
		try {
			FileInputStream fileIn = new FileInputStream(FILE_LOCATION);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			//reading the object from the objectInputStream
			menu = (Menu) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			//i.printStackTrace();
			try{
				new File(FILE_LOCATION).delete();
			}catch(Exception e){}
			return null;
		} catch (ClassNotFoundException c) {
			//c.printStackTrace();
			try{
				new File(FILE_LOCATION).delete();
			}catch(Exception e){}
			return null;
		}
		return menu;
	}
}

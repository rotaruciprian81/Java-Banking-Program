/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication15;

/**
 * Starting point of this class.
 * 
 */

public class JavaApplication15
{
	/**
	 * Main method
	 * @param args
	 */
    public static void main (String args[])
    {
    	Menu menu = PersistUtil.getInstance().load();
    	if(null == menu)
    		menu = new Menu();
        menu.main_menu();
    }
    
}

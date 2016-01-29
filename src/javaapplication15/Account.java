/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication15;

import java.io.Serializable;

/**
 * Account base interface
 * @author Radu Ciprian Rotaru
 *
 */
public interface Account extends Serializable
{ 
	/**
	 * To deposit the money
	 * @param amount
	 */
    void deposit(double amount); 
    /**
     * To get the balance of account
     * @return
     */
    double get_balance(); 
 
}

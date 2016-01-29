/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication15;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Loan class that captures the loan which is taken against bank
 * @author Radu Ciprian Rotaru
 *
 */
public class Loan implements Serializable {
	//duration of loan in months
    protected int duration;
    //Date at whcih loan was issued
    protected Date issuedTime;
    //amount of loan taken against bank
    protected double amountLoaned;
    //amount to be retuned back along with loan interest
    protected double amountToBePaidBack;
    //list of all monthly payments made to the bank
    protected List<Double> paymentAmountList;
    //list of all monthly payments date to keep track of all payment history
    protected List<Date> paymentDateList;

    /**
     * Constructor
     * @param _time
     * @param _transactionType
     * @param _amount
     */
    public Loan(int duration, double amountLoaned,double interestRate){
    	this.duration = duration;
    	//setting current time to loan issue time
    	this.issuedTime = new Date();
    	this.amountLoaned = amountLoaned;
    	//calculating interest charged over the loan amount
		double interest = interestRate * amountLoaned / 100;
		//amount that need to be returned is sum of amountLoaned + interest
    	this.amountToBePaidBack = amountLoaned +interest;
    	paymentAmountList = new ArrayList<Double>();
    	paymentDateList = new ArrayList<Date>();
    }
    
    
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return amountLoaned+ "  |  "+amountToBePaidBack+ "  |  "+ duration;
    }
}

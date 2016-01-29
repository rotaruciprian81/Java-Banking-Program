/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication15;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Radu Ciprian Rotaru
 *
 */
public class Transaction implements Serializable {
    protected Date time;
    protected String transactionType;
    protected double amount;
    /**
     * Constructor
     * @param _time
     * @param _transactionType
     * @param _amount
     */
    public Transaction(Date _time, String _transactionType, double _amount)
    {
        time = _time;
        transactionType = _transactionType;
        amount = _amount;

    }
    public Date getDate()
    {
        return time;
    }
    public String getType()
    {
        return transactionType;
    }
    public double getAmount()
    {
        return amount;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return getType()+ "  |  "+getDate()+ "  |  "+ getAmount();
    }
}


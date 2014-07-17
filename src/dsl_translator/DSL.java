/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dsl_translator;

/**
 *
 * @author Harry
 */
public class DSL {

    public DSL_Field seconds, minutes, hours, day_of_month, month, day_of_week, year;
    public boolean secondsValid, minutesValid, hoursValid, day_of_monthValid, monthValid, day_of_weekValid, yearValid;

    private String dsl;

    public String temp;

     public DSL()
    {
        seconds = new DSL_Field();
        minutes = new DSL_Field();
        hours = new DSL_Field();
        day_of_month = new DSL_Field();
        month = new DSL_Field();
        day_of_week = new DSL_Field();
        year = new DSL_Field();

        secondsValid = true;
        minutesValid = true;
        hoursValid = true;
        day_of_monthValid = true;
        monthValid = true;
        day_of_weekValid = true;
        yearValid = true;
    }

    public void setDSL(String d)
    {
        dsl = d;
    }

    public String getDSL()
    {
        /*
        String newDSL = "@<seconds:: " + seconds.getField() + ", minutes:: " + minutes.getField();
        newDSL = newDSL + ", hours:: " + hours.getField() + ", day_of_month:: " + day_of_month.getField();
        newDSL = newDSL + ", month:: " + month.getField() + ", day_of_week:: " + day_of_week.getField();
        newDSL = newDSL + ", year:: " + year.getField() + ">";
         * */

        String newDSL = "@<year:: " + year.getField() + ", day_of_week:: " + day_of_week.getField();
        newDSL = newDSL + ", month:: " + month.getField() + ", day_of_month:: " + day_of_month.getField();
        newDSL = newDSL + ", hours:: " + hours.getField() + ", minutes:: " + minutes.getField();
        newDSL = newDSL +", seconds:: " + seconds.getField() + ">";

        return newDSL;
    }

}

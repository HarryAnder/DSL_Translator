/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dsl_translator;

/**
 *
 * @author Harry
 */
public class Cron {

    public Cron_Field seconds, minutes, hours, day_of_month, month, day_of_week, year;
    public boolean secondsValid, minutesValid, hoursValid, day_of_monthValid, monthValid, day_of_weekValid, yearValid;

    private String cron;

    public String temp;

    public Cron()
    {
        seconds = new Cron_Field();
        minutes = new Cron_Field();
        hours = new Cron_Field();
        day_of_month = new Cron_Field();
        month = new Cron_Field();
        day_of_week = new Cron_Field();
        year = new Cron_Field();

        secondsValid = true;
        minutesValid = true;
        hoursValid = true;
        day_of_monthValid = true;
        monthValid = true;
        day_of_weekValid = true;
        yearValid = true;
    }

    public void setCron(String c)
    {
        cron = c;
    }

    public String getCron()
    {
        return cron;
    }

    public String getNewCron()
    {
        String cron = seconds.getField() + " " + minutes.getField() + " " + hours.getField() + " ";
        cron = cron + day_of_month.getField() + " " + month.getField() + " " + day_of_week.getField();
        cron = cron + " " + year.getField();
        return cron;
    }
}

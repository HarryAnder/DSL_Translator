/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dsl_translator;

/**
 *
 * @author Harry
 */
public class TranslateDSL {

    private Cron cron;
    private String test;
    private DSL dsl;

    public TranslateDSL(String d)
    {

        cron = new Cron();
        dsl = new DSL();
        dsl.setDSL(d);
        dsl.temp = d;

        seperateYear();
        seperateDayOfWeek();
        seperateMonth();
        seperateDayOfMonth();
        seperateHours();
        seperateMinutes();
        seperateSeconds();

        System.out.println("#translate DSL to cron#");
        System.out.println(cron.getNewCron());
    }

    private void seperateSeconds()
    {
        dsl.seconds.setField(seperate());
        System.out.println("seconds = " + dsl.seconds.getField());
        cron.seconds.setField(firstFieldTrans(dsl.seconds.getField(), 0, 59, false));

    }

    private void seperateMinutes()
    {
        dsl.minutes.setField(seperate());
        System.out.println("minutes = " + dsl.minutes.getField());
        cron.minutes.setField(firstFieldTrans(dsl.minutes.getField(), 0, 59, false));
    }

    private void seperateHours()
    {
        dsl.hours.setField(seperate());
        System.out.println("hours = " + dsl.hours.getField());
        cron.hours.setField(firstFieldTrans(dsl.hours.getField(), 0, 23, false));
    }

    private void seperateDayOfMonth()
    {
        dsl.day_of_month.setField(seperate());
        System.out.println("day of month = " + dsl.day_of_month.getField());
        cron.day_of_month.setField(secondFieldTrans(dsl.day_of_month.getField(), 1, 31));
    }

    private void seperateMonth()
    {
        dsl.month.setField(seperate());
        System.out.println("month = " + dsl.month.getField());
        cron.month.setField(firstFieldTrans(dsl.month.getField(), 1, 12, false));
    }

    private void seperateDayOfWeek()
    {
        dsl.day_of_week.setField(seperate());
        System.out.println("day of week = " + dsl.day_of_week.getField());
        cron.day_of_week.setField(thirdFieldTrans(dsl.day_of_week.getField(), 1, 7));
    }

    private void seperateYear()
    {
        dsl.year.setField(seperate());
        System.out.println("year = " + dsl.year.getField());
        
        if(!dsl.year.getField().equals(" "))
        {
            cron.year.setField(firstFieldTrans(dsl.year.getField(), 1970, 2099, false));
        }
        else
        {
            cron.year.setField("");
        }
       
    }
    //  search for :: then pass the rest of the string to be broken again
    private String seperate()
    {
        String field = "";

        for(int i = 1; i <= dsl.temp.length(); i++)
        {
            if(dsl.temp.substring(i - 1, i).equals(":") && (i + 1 < dsl.temp.length()))
            {
                if(dsl.temp.substring(i, i + 1).equals(":"))
                {
                    
                    field = seperateField(dsl.temp.substring(i + 1));
 
                    dsl.temp = dsl.temp.substring((i - 1) + field.length());
                    break;
                }
                //cron.temp = cron.temp.substring(i, cron.temp.length());

            }
            else
            {

            }
        }
        return field;
    }
    //  when a , is found that is the end of a field
    private String seperateField(String temp)
    {
        String field = "";

         for(int i = 1; i <= temp.length(); i++)
        {
            if(temp.substring(i - 1, i).equals(","))
            {
                break;
            }
            else if(temp.substring(i - 1, i).equals(">"))
            {
                break;
            }
            else
            {
                field = field + temp.substring(i - 1, i);
                if(temp.length() == i)
                {
                    temp = "";
                }
            }
        }

        if(field.length() > 1)
        {
            if(field.substring(0, 1).equals(" "))
            {
                field = field.substring(1);
            }
        }

        return field;
    }

    public boolean isNumber(String s)
    {

        if(s.length() > 1)
        {
            System.out.println("string bigger than one");
        }

        char c = s.charAt(0);
        if(Character.isDigit(c))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    //  covers seconds, minutes, hours, month, years
    public String firstFieldTrans(String field, int min, int max, boolean isYear)
    {
        String newField = "";

        if(field.substring(0, 1).equals("E") && field.length() == 5)
        {
            newField = "*";
        }
        else if(field.substring(0, 1).equals("S") && field.length() > 5)
        {
            //newField = add(field);
            if(!isYear)
            {
                newField = adding(field, min, max);
            }
            else
            {
                newField = addingYear(field, min, max);
            }
        }
        else
        {
            for(int i = 1; i <= field.length(); i++)
            {
                if(isNumber(field.substring(i - 1, i)))
                {
                    newField = newField + field.substring(i - 1, i);
                }
                else
                 {
                     if(i - 1 > 0)
                     {

                        if(field.substring(i - 1, i).equals("T") && field.substring(i-1).length() > 2)
                        {
                            newField = newField + to(field.substring(i - 1, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals("A") && field.substring(i-1).length() > 3)
                        {
                            newField = newField + and(field.substring(i - 1, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals(" "))
                        {
                            //  ignore space
                        }
                        else
                        {
                            newField = "~invalid char~";
                            break;
                        }
                     }
                     else
                     {
                         newField = "~invalid~";
                            break;
                     }


             }
                boolean over = false;

                 try
                 {
                     if(Integer.parseInt(newField) > max)
                     {
                         over = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(over)
                {
                    newField = "~invalid over~";
                    break;
                }

            }
            boolean under = false;
                 try
                 {
                     if(Integer.parseInt(newField) < min)
                     {
                        under = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(under)
                {
                    newField = "~invalid under~";

                }
        }
        
        return newField;
    
    }
    //  given 1 AND 3 trans to 1,3
    public String and(String field, int min, int max)
    {
        String newField = "", number = "";

        System.out.println(field.substring(0, 3));
        if(field.substring(0, 3).equals("AND"))
        {
            newField = ",";
            field = field.substring(3);

            for(int i = 1; i <= field.length(); i++)
            {
                if(isNumber(field.substring(i - 1, i)))
                {
                    newField = newField + field.substring(i - 1, i);
                    number = number + field.substring(i - 1, i);
                }
                else
                {
                    if(field.substring(i - 1, i).equals("A") && field.substring(i - 1).length() > 3)
                    {
                        if(field.substring(i - 1, i + 2).equals("AND"))
                        {
                            newField = newField + ",";
                            number = "";
                             i = i + 2;
                        }
                        else
                        {
                            newField = "~invalid~";
                        }
                    }
                    else if(field.substring(i - 1, i).equals(" "))
                    {
                        //  ignore space
                    }
                    else
                    {
                        newField = "~invalid~";
                        break;
                    }
                }
            }
        }
        else
        {
            newField = "~invalid~";
        }

        return newField;

    }
    //  given 1 TO 3 trans to 1-3
    public String to(String field, int min, int max)
    {
         String newField = "";

        System.out.println(field.substring(0, 2));
        if(field.substring(0, 2).equals("TO"))
        {
            field = field.substring(3);

            for(int i = 1; i <= field.length(); i++)
            {
                if(isNumber(field.substring(i - 1, i)))
                {
                    newField = newField + field.substring(i - 1, i);
                }
                else
                {
                    if(field.substring(i - 1, i).equals(" "))
                    {
                        System.out.println("space");
                        //  ignore space
                    }
                    else
                    {
                        System.out.println(field.substring(i - 1, i));
                        newField = "~invalid~";
                        break;
                    }
                }
            }

        newField = "-" + newField;

        }

        return newField;

    }
    //  given START 1 ADD 3 trans to 1/3
    public String adding(String field, int min, int max)
    {
        String newField = "";
        if(field.substring(0, 5).equals("START"))
        {
            field = field.substring(5);

             for(int i = 1; i <= field.length(); i++)
            {
                 System.out.println(field.substring(i - 1, i));
                if(isNumber(field.substring(i - 1, i)))
                {
                    newField = newField + field.substring(i - 1, i);
                }
                else if(field.substring(i - 1, i).equals(" "))
                {
                    //  ignore space
                }
                else if(field.substring(i - 1, i).equals("A") && field.substring(i - 1).length() > 3)
                {
                    if(field.substring(i - 1, i + 2).equals("ADD"))
                    {
                        i = i + 2;
                        System.out.println(field);
                        newField = newField + "/";
                    }
                    else
                    {
                        newField = "~invalid~";
                        break;
                    }
                }
                else
                {
                    newField = "~invalid~";
                    break;
                }

            }
        }
        
        return newField;

    }
    //  different to adding normal numbers as min is 1970 2012/1970 no good
    public String addingYear(String field, int before, int max)
    {

        String newField = "";

        for(int i = 1; i <= field.length(); i++)
        {
            if(isNumber(field.substring(i - 1, i)))
            {
                newField = newField + field.substring(i - 1, i);
            }
            else
            {
                newField = "~invalid~";
                break;
            }

            boolean over = false;
            try
            {
                if(Integer.parseInt(newField) > (max - before))
                {
                    over = true;
                }
            }catch(Exception e)
             {
                System.out.println("not a integer");
             }

             if(over)
             {
                System.out.println(newField + " > " + max);
                newField = "~invalid over~";
                break;
             }


        }

        boolean under = false;

        try
            {
                if(Integer.parseInt(newField) < 0)
                {

                    under = true;
                }
            }catch(Exception e)
             {
                System.out.println("not a integer");
             }

            if(under)
            {
                newField = "~invalid under~";
            }

        newField = " ADD " + newField;

        return newField;

    }
  //  used for day of month field
    public String secondFieldTrans(String field, int min, int max)
    {
        String newField = "";

        if(field.substring(0, 1).equals("E") && field.length() == 5)
        {
            newField = "*";
        }
        else if(field.substring(0, 1).equals("S") && field.length() > 4)
        {
            newField = adding(field, min, max);
        }
        else if(field.substring(0, 1).equals("B") && field.length() > 4)
        {
            if(field.substring(0, 5).equals("BLANK"))
            {
                newField = "?";
            }
        }
        else if(field.substring(0, 1).equals("L") && field.length() > 3)
        {
            if(field.length() > 11)
            {
                System.out.println("testl");
                if(field.substring(0, 12).equals("LAST WEEKDAY"))
                {
                    newField = "LW";
                }
                else
                {
                    newField = "~invalid~";
                }
            }
            else
            {
                
                if(field.substring(0, 4).equals("LAST"))
                {
                    System.out.println("testl");
                    newField = "L";
                }
                else
                {
                    newField = "~invalid~";
                }
            }
            
        }
        else if(field.substring(0, 1).equals("W") && field.length() > 6)
        {
            if(field.length() < 8)
            {
                if(field.substring(0, 7).equals("WEEKDAY"))
                {
                    newField = "W";
                }
                else
                {
                    newField = "~invalid~";
                }

            }
            else if(field.length() > 17)
            {
                if(field.substring(0, 18).equals("WEEKDAY CLOSEST TO"))
                {
                    newField = closestWeekday(field.substring(18)) + "W";
                }
                else
                {
                    newField = "~invalid~";
                }
            }
        }
        else
        {
            for(int i = 1; i <= field.length(); i++)
            {
                if(isNumber(field.substring(i - 1, i)))
                {
                    newField = newField + field.substring(i - 1, i);
                }
                else
                 {
                     if(i - 1 > 0)
                     {

                        if(field.substring(i - 1, i).equals("T") && field.substring(i-1).length() > 2)
                        {
                            newField = newField + to(field.substring(i - 1, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals("A") && field.substring(i-1).length() > 3)
                        {
                            newField = newField + and(field.substring(i - 1, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals("-") && field.substring(i-1).length() > 7)
                        {
                            //  -TO-LAST
                            if(field.substring(i - 1, i + 7).equals("-TO-LAST"))
                            {
                                newField = "L-" + newField;
                            }
                            else
                            {
                                newField = "~invalid char~";
                            }
                            break;
                        }
                        else if(field.substring(i - 1, i).equals(" "))
                        {
                            //  ignore space
                        }
                        else
                        {
                            newField = "~invalid char~";
                            break;
                        }
                     }
                     else
                     {
                         newField = "~invalid~";
                            break;
                     }


             }
                boolean over = false;

                 try
                 {
                     if(Integer.parseInt(newField) > max)
                     {
                         over = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(over)
                {
                    newField = "~invalid over~";
                    break;
                }

            }
            boolean under = false;
                 try
                 {
                     if(Integer.parseInt(newField) < min)
                     {
                        under = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(under)
                {
                    newField = "~invalid under~";

                }
        }

        return newField;

    }
    //  used for WEEKDAY CLOSEST TO 6, find the number and return to make 6W
    public String closestWeekday(String field)
    {
        String newField = "";

        for(int i = 1; i <= field.length(); i++)
            {
                if(isNumber(field.substring(i - 1, i)))
                {
                    newField = newField + field.substring(i - 1, i);
                }
                else if(field.substring(i - 1, i).equals(" "))
                {
                    //  leave
                }
                else
                {
                    newField = "~invalid~";
                    break;
                }
        }
        return newField;
    }
    //  used for day of week field
    public String thirdFieldTrans(String field, int min, int max)
    {
        String newField = "";

        if(field.substring(0, 1).equals("E") && field.length() == 5)
        {
            newField = "*";
        }
        else if(field.substring(0, 1).equals("S") && field.length() > 4)
        {
            newField = adding(field, min, max);
        }
        else if(field.substring(0, 1).equals("B") && field.length() > 4)
        {
            if(field.substring(0, 5).equals("BLANK"))
            {
                newField = "?";
            }
        }
        else if(field.substring(0, 1).equals("L") && field.length() > 3)
        {
           if(field.substring(0, 4).equals("LAST"))
           {
               if(field.length() > 4)
               {
                   newField = last(field.substring(4)) + "L";
               }
               else
               {
                    newField = "L";
               }
           }
           else
           {
                newField = "~invalid~";
           }
            

        }
        else if(field.substring(0, 1).equals("T") && field.length() > 2)
        {

            if(field.substring(0, 3).equals("THE"))
            {
                newField = the(field.substring(3));
            }
            else
            {
                newField = "~invalid~";
            }


        }
        else
        {
            for(int i = 1; i <= field.length(); i++)
            {
                if(isNumber(field.substring(i - 1, i)))
                {
                    newField = newField + field.substring(i - 1, i);
                }
                else
                 {
                     if(i - 1 > 0)
                     {

                        if(field.substring(i - 1, i).equals("T") && field.substring(i-1).length() > 2)
                        {
                            newField = newField + to(field.substring(i - 1, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals("A") && field.substring(i-1).length() > 3)
                        {
                            newField = newField + and(field.substring(i - 1, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals(" "))
                        {
                            //  ignore space
                        }
                        else
                        {
                            newField = "~invalid char~";
                            break;
                        }
                     }
                     else
                     {
                         newField = "~invalid~";
                            break;
                     }


             }
                boolean over = false;

                 try
                 {
                     if(Integer.parseInt(newField) > max)
                     {
                         over = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(over)
                {
                    newField = "~invalid over~";
                    break;
                }

            }
            boolean under = false;
                 try
                 {
                     if(Integer.parseInt(newField) < min)
                     {
                        under = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(under)
                {
                    newField = "~invalid under~";

                }
        }

        return newField;

    }

    public String last(String field)
    {
        String newField = "";

        newField = closestWeekday(field);
        return newField;
    }
   //  THE 4 3 "the 4th TUE" into 3#4
    public String the(String field)
    {
        String newField = "", number = "";

        if(field.substring(0, 1).equals(" "))
        {
            field.substring(1);
        }

        for(int i = 1; i <= field.length(); i++)
            {
                if(isNumber(field.substring(i - 1, i)))
                {
                    number = number + field.substring(i - 1, i);
                }
                else if(field.substring(i - 1, i).equals(" "))
                {
                    newField = "#" + number;
                    number = "";
                }
                else
                {
                    newField = "~invalid~";
                    break;
                }
        }
        newField = number + newField;

        return newField;
    }

    public String getTranslated()
    {
        return cron.getNewCron();
    }
}

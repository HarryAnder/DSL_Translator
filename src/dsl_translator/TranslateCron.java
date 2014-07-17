/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dsl_translator;

/**
 *
 * @author Harry
 */
public class TranslateCron {

    private Cron cron;
    private String test;
    private DSL dsl;

    public TranslateCron(String c)
    {

        dsl = new DSL();
        cron = new Cron();
        cron.setCron(c);
        cron.temp = cron.getCron();
        seperateSeconds();
        seperateMinutes();
        seperateHours();
        seperateDayOfMonth();
        seperateMonth();
        seperateDayOfWeek();
        seperateYear();
        System.out.println(dsl.getDSL());



    }

    public String getTranslated()
    {
        return dsl.getDSL();
    }

    private void seperateSeconds()
    {
        cron.seconds.setField(seperate());
        System.out.println("seconds = " + cron.seconds.getField());
        dsl.seconds.setField(firstFieldTrans(cron.seconds.getField(), 0, 59, false));
        
    }

    private void seperateMinutes()
    {
        cron.minutes.setField(seperate());
        System.out.println("minutes = " + cron.minutes.getField());
        dsl.minutes.setField(firstFieldTrans(cron.minutes.getField(), 0, 59, false));
    }

    private void seperateHours()
    {
        cron.hours.setField(seperate());
        System.out.println("hours = " + cron.hours.getField());
        dsl.hours.setField(firstFieldTrans(cron.hours.getField(), 0, 23, false));
    }

    private void seperateDayOfMonth()
    {
        cron.day_of_month.setField(seperate());
        System.out.println("day of month = " + cron.day_of_month.getField());
        dsl.day_of_month.setField(secondFieldTrans(cron.day_of_month.getField(), 1, 31, true));
    }

    private void seperateMonth()
    {
        cron.month.setField(seperate());
        System.out.println("month = " + cron.month.getField());
        dsl.month.setField(thirdFieldTrans(cron.month.getField(), 1, 12));

    }

    private void seperateDayOfWeek()
    {
        cron.day_of_week.setField(seperate());
        System.out.println("day of week = " + cron.day_of_week.getField());
        dsl.day_of_week.setField(fourthFieldTrans(cron.day_of_week.getField(), 1, 7));
    }

    private void seperateYear()
    {
        cron.year.setField(seperate());
        System.out.println("year = " + cron.year.getField());

        if(!cron.year.getField().equals(""))
        {
            dsl.year.setField(firstFieldTrans(cron.year.getField(), 1970, 2099, true));
        }
        else
        {
            dsl.year.setField("");
        }
        
    }

    private String seperate()
    {
        String field = "";

        for(int i = 1; i <= cron.temp.length(); i++)
        {
            if(cron.temp.substring(i - 1, i).equals(" "))
            {
                cron.temp = cron.temp.substring(i, cron.temp.length());
                break;
            }
            else
            {
                field = field + cron.temp.substring(i - 1, i);
                if(cron.temp.length() == i)
                {
                    cron.temp = "";
                }
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

    public boolean isBasicSpecial(String s)
    {
        if(s.length() > 1)
        {
            System.out.println("string bigger than one");
        }

        if(s.equals("*"))
        {
            return true;
        }
        else if(s.equals("/"))
        {
            return true;
        }
        else if(s.equals(","))
        {
            return true;
        }
        else if(s.equals("-"))
        {
            return true;
        }
        else if(s.equals("?"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean isBasicValid(String s)
    {
        boolean valid = true;

        System.out.println("String is:" + s);
        System.out.println("Length of said String is:" + s.length());
        for(int i = 1; i <= s.length(); i++)
        {
            if(!isNumber(s.substring(i - 1, i)))
            {
                System.out.println("'" + s.substring(i - 1, i) + "' is not a number");

                if(!isBasicSpecial(s.substring(i - 1, i)))
                {
                    System.out.println("'" + s.substring(i - 1, i) + "' is not a special");
                    valid = false;
                    break;
                }
            }
        }

        return valid;

    }

    public String firstFieldTrans(String field, int min, int max, boolean isYear)
    {
        String newField = "";

        if(field.substring(0, 1).equals("*"))
        {
            newField = "EVERY";
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
                         if(field.substring(i - 1, i).equals("/"))
                        {
                            if(!isYear)
                            {
                                newField = "START " + newField + adding(field.substring(i, field.length()), min, max);
                                break;
                            }
                            else
                            {
                                int before;
                                try
                                {
                                    before = Integer.parseInt(newField);

                                }catch(Exception e)
                                {
                                    System.out.println("not a integer");
                                    newField = "~invalid~";
                                    break;
                                }
                                newField = "START " + newField + addingYear(field.substring(i, field.length()), before, max);
                                break;
                            }

                        }
                        else if(field.substring(i - 1, i).equals("-"))
                        {
                            newField = newField + to(field.substring(i, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals(","))
                        {
                            newField = newField + and(field.substring(i, field.length()), min, max);
                            break;
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

    public String adding(String field, int min, int max)
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
                System.out.println(newField + " > " + max);
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

        newField = " ADD " + newField;

        return newField;

    }

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

    public String to(String field, int min, int max)
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

        newField = " TO " + newField;

        return newField;

    }

    public String and(String field, int min, int max)
    {
        String newField = " AND ", number = "";
        boolean comma = true;

        for(int i = 1; i <= field.length(); i++)
        {
            if(isNumber(field.substring(i - 1, i)))
            {
                newField = newField + field.substring(i - 1, i);
                number = number + field.substring(i - 1, i);
                comma = false;
            }
            else
            {
                if(field.substring(i - 1, i).equals(","))
                {
                    if(!comma)
                    {
                        newField = newField + " AND ";
                        comma = true;
                        number = "";
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

            boolean over = false;
            try
            {
                
                if(Integer.parseInt(number) > max)
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
                System.out.println(number + " > " + max);
                break;
             }
            

        }

         boolean under = false;

         try
            {
                 if(Integer.parseInt(number) < min)
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

        return newField;

    }

     public String secondFieldTrans(String field, int min, int max, boolean isMonth)
    {
        String newField = "";

        if(field.substring(0, 1).equals("*"))
        {
            newField = "EVERY";
        }
        else if(field.substring(0, 1).equals("?"))
        {
            newField = "BLANK";
        }
        else if(field.substring(0, 1).equals("W"))
        {
            if(isMonth)
            {
                newField = "WEEKDAY";
            }
            else
            {
                newField = "~inavlid~";
            }
            
        }
        else if(field.substring(0, 1).equals("L"))
        {
            newField = "LAST";
            if(field.length() > 1)
            {
                if(field.substring(1, 2).equals("W") && isMonth)
                {
                    newField = "LAST WEEKDAY";
                }
                else if(field.substring(1, 2).equals("-"))
                {
                    if(isMonth)
                    {
                        newField = " -TO-LAST";
                        newField = monthL(field.substring(2)) + newField;
                    }
                    else
                    {
                        newField = "~inavlid~";
                    }

                }
                else
                {
                    newField = "~inavlid~";
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
                         if(field.substring(i - 1, i).equals("/"))
                        {
                            newField = "START " + newField + adding(field.substring(i, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals("-"))
                        {
                            newField = newField + to(field.substring(i, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals(","))
                        {
                            newField = newField + and(field.substring(i, field.length()), min, max);
                            break;
                        }
                         else if(field.substring(i - 1, i).equals("W"))
                        {
                            newField = "WEEKDAY CLOSEST TO " + newField;
                            break;
                        }
                         else if(field.substring(i - 1, i).equals("L"))
                        {
                            if(!isMonth)  //  IF A WEEK THEN 6L ALLOWED LAST FRIDAY
                            {
                                newField = "LAST " + newField;
                                break;
                            }
                            else
                            {
                                newField = "~invalid~";;
                                break;
                            }

                        }
                         else if(field.substring(i - 1, i).equals("#"))
                        {
                            if(!isMonth)  //  IF A WEEK THEN 3#4
                            {
                                newField = "THE " + weekThe(field.substring(i)) + " " + newField;
                                break;
                            }
                            else
                            {
                                newField = "~invalid~";;
                                break;
                            }

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

     public String monthL(String field)
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
                     if(Integer.parseInt(newField) > 31)
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
         return newField;
     }

     public String weekThe(String field)
    {
         String newField;
         newField = monthL(field);
         return newField;
     }

     public String thirdFieldTrans(String field, int min, int max)
    {
        String newField = "";

        if(field.substring(0, 1).equals("*"))
        {
            newField = "EVERY";
        }
        else if(isMonthLetter(field.substring(0, 1)))
        {
            newField = monthInLetters(field);
            
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
                         if(field.substring(i - 1, i).equals("/"))
                        {
                            newField = "START " + newField + adding(field.substring(i, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals("-"))
                        {
                            newField = newField + to(field.substring(i, field.length()), min, max);
                            break;
                        }
                        else if(field.substring(i - 1, i).equals(","))
                        {
                            newField = newField + and(field.substring(i, field.length()), min, max);
                            break;
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

     public boolean isMonthLetter(String letter)
    {
         boolean validLetter = false;

         if(letter.equals("J"))
         {
             validLetter = true;
         }
         else if(letter.equals("A"))
         {
             validLetter = true;
         }
         else if(letter.equals("M"))
         {
             validLetter = true;
         }
         else if(letter.equals("D"))
         {
             validLetter = true;
         }
         else if(letter.equals("F"))
         {
             validLetter = true;
         }
         else if(letter.equals("N"))
         {
             validLetter = true;
         }
         else if(letter.equals("O"))
         {
             validLetter = true;
         }
         else if(letter.equals("S"))
         {
             validLetter = true;
         }

         return validLetter;
     }

     public String monthInLetters(String field)
     {
         String newField = "";

         if(field.length() > 2)
         {
            newField = String.valueOf(getMonth(field.substring(0, 3)));
            System.out.println("month : " + getMonth(field.substring(0, 3)));

            if(field.length() > 3)
            {
                if(field.substring(3, 4).equals(","))
                {
                    System.out.println("and");
                    if(field.length() > 4)
                    {
                        newField = newField + monthAnd(field.substring(4));
                    }
                    else
                    {
                        newField = "~invalid~";
                    }
                }
                else if(field.substring(3, 4).equals("-"))
                {
                    System.out.println("to");
                    if(field.length() > 4)
                    {
                        newField = newField + monthTo(field.substring(4));
                    }
                    else
                    {
                        newField = "~invalid~";
                    }
                }
                else if(field.substring(3, 4).equals("/"))
                {
                    System.out.println("add");
                    if(field.length() > 4)
                    {
                        newField = "START " + newField + monthAdd(field.substring(4));
                    }
                    else
                    {
                        newField = "~invalid~";
                    }
                }
                else
                {
                    newField = "~invalid~";
                }
            }

         }
         else
         {
             newField = "~invalid~";
         }

         return newField;
     }

     public int getMonth(String month)
    {
         int pos = 0;

         if(month.equals("JAN"))
             pos = 1;
         else if(month.equals("FEB"))
             pos = 2;
         else if(month.equals("MAR"))
             pos = 3;
         else if(month.equals("APR"))
             pos = 4;
         else if(month.equals("MAY"))
             pos = 5;
         else if(month.equals("JUN"))
             pos = 6;
         else if(month.equals("JUL"))
             pos = 7;
         else if(month.equals("AUG"))
             pos = 8;
         else if(month.equals("SEP"))
             pos = 9;
         else if(month.equals("OCT"))
             pos = 10;
         else if(month.equals("NOV"))
             pos = 11;
         else if(month.equals("DEC"))
             pos = 12;

         return pos;
     }

     public String monthAnd(String field)
    {
         String newField = " AND ";

         for(int i = 1; i <= field.length(); i++)
         {

             if(isMonthLetter(field.substring(i - 1, i)))
             {
                 String month = "";
                 if((i + 2) <=  field.length())
                 {
                    month = field.substring(i - 1, i + 2);
                 }
                 if(month.equals("JAN") || month.equals("FEB") || month.equals("MAR")
                          || month.equals("APR") || month.equals("MAY") || month.equals("JUN")
                           || month.equals("JUL") || month.equals("AUG") || month.equals("SEP")
                            || month.equals("NOV") || month.equals("OCT") || month.equals("DEC"))
                 {
                     newField = newField + String.valueOf(getMonth(field.substring(i - 1, i + 2)));
                     i = i + 2;
                 }
             }
             else if(field.substring(i - 1, i).equals(","))
             {
                 newField = newField + " AND ";

             }
             else
             {
                 newField = "~invalid~";
                 break;
             }

         }

         return newField;
     }

     public String monthTo(String field)
    {
         String newField = " TO ";

         if(field.length() == 3)
         {
              if(field.equals("JAN") || field.equals("FEB") || field.equals("MAR")
                          || field.equals("APR") || field.equals("MAY") || field.equals("JUN")
                           || field.equals("JUL") || field.equals("AUG") || field.equals("SEP")
                            || field.equals("NOV") || field.equals("OCT") || field.equals("DEC"))
             {
                newField = newField + String.valueOf(getMonth(field));
             }
         }
         else
         {
             newField = "~invalid~";
         }

         return newField;
     }

     public String monthAdd(String field)
    {
         String newField = " ADD ";
         String number = "";

         for(int i = 1; i <= field.length(); i++)
         {
            if(isNumber(field.substring(i - 1, i)))
            {
                number = number + field.substring(i - 1, i);
            }
            else
            {
                number = "~invalid~";
                break;
            }
            boolean over = false;

                 try
                 {
                     if(Integer.parseInt(number) > 12)
                     {
                         over = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(over)
                {
                    number = "~invalid over~";
                    break;
                }

        }
          boolean under = false;
                 try
                 {
                     if(Integer.parseInt(number) < 1)
                     {
                        under = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(under)
                {
                    number = "~invalid under~";
                }
         newField = newField + number;
         return newField;
     }

      public String fourthFieldTrans(String field, int min, int max)
    {
        String newField = "";
        System.out.println("test");
        if(field.substring(0, 1).equals("*"))
        {
            newField = "EVERY";
        }
        else if(field.length() > 1)
        {
            if(!isWeekLetter(field.substring(0, 2)))
            {
                newField = secondFieldTrans(field, 1, 7, false);
            }
            else
            {
                newField = weekInLetters(field);
            }
            
            

        }
        else
        {
            newField = secondFieldTrans(field, 1, 7, false);
        }

        return newField;
    }

      public boolean isWeekLetter(String letter)
    {
         boolean validLetter = false;

         if(letter.equals("SA"))
         {
             validLetter = true;
         }
         else if(letter.equals("SU"))
         {
             validLetter = true;
         }
         else if(letter.equals("TU"))
         {
             validLetter = true;
         }
         else if(letter.equals("TH"))
         {
             validLetter = true;
         }
         else if(letter.equals("MO"))
         {
             validLetter = true;
         }
         else if(letter.equals("WE"))
         {
             validLetter = true;
         }
         else if(letter.equals("FR"))
         {
             validLetter = true;
         }

         return validLetter;
     }

          public String weekInLetters(String field)
     {
         String newField = "";

         if(field.length() > 2)
         {
            newField = String.valueOf(getWeek(field.substring(0, 3)));
            System.out.println("week : " + getWeek(field.substring(0, 3)));

            if(field.length() > 3)
            {
                if(field.substring(3, 4).equals(","))
                {
                    System.out.println("and");
                    if(field.length() > 4)
                    {
                        newField = newField + weekAnd(field.substring(4));
                    }
                    else
                    {
                        newField = "~invalid~";
                    }
                }
                else if(field.substring(3, 4).equals("-"))
                {
                    System.out.println("to");
                    if(field.length() > 4)
                    {
                        newField = newField + weekTo(field.substring(4));
                    }
                    else
                    {
                        newField = "~invalid~";
                    }
                }
                else if(field.substring(3, 4).equals("/"))
                {
                    System.out.println("add");
                    if(field.length() > 4)
                    {
                        newField = "START " + newField + weekAdd(field.substring(4));
                    }
                    else
                    {
                        newField = "~invalid~";
                    }
                }
                else if(field.substring(3, 4).equals("L"))
                {
                    System.out.println("last");
                    newField = "LAST " + newField;
                }
                else if(field.substring(3, 4).equals("#"))
                {

                     newField = "THE " + weekThe(field.substring(4)) + " " + newField;

                }
                else
                {
                    newField = "~invalid~";
                }
            }

         }
         else
         {
             newField = "~invalid~";
         }

         return newField;
     }

          public int getWeek(String week)
    {
         int pos = 0;

         if(week.equals("SUN"))
             pos = 1;
         else if(week.equals("MON"))
             pos = 2;
         else if(week.equals("TUE"))
             pos = 3;
         else if(week.equals("WED"))
             pos = 4;
         else if(week.equals("THU"))
             pos = 5;
         else if(week.equals("FRI"))
             pos = 6;
         else if(week.equals("SAT"))
             pos = 7;

         return pos;
     }

          public String weekAnd(String field)
    {
         String newField = " AND ";

         for(int i = 1; i <= field.length(); i++)
         {

             if(field.substring(i).length() > 1)
             {
                 if(isWeekLetter(field.substring(i - 1, i + 1)))
                {
                    String month = "";
                    if((i + 2) <=  field.length())
                    {
                        month = field.substring(i - 1, i + 2);
                    }
                    if(month.equals("SUN") || month.equals("MON") || month.equals("TUE")
                          || month.equals("WED") || month.equals("THU") || month.equals("FRI")
                           || month.equals("SAT"))
                    {
                        newField = newField + String.valueOf(getWeek(field.substring(i - 1, i + 2)));
                        i = i + 2;
                    }
                }
             }
             else if(field.substring(i - 1, i).equals(","))
             {
                 newField = newField + " AND ";

             }
             else
             {
                 newField = "~invalid~";
                 break;
             }

         }

         return newField;
     }

          public String weekTo(String field)
    {
         String newField = " TO ";

         if(field.length() == 3)
         {
              if(field.equals("SUN") || field.equals("MON") || field.equals("TUE")
                          || field.equals("WED") || field.equals("THU") || field.equals("FRI")
                           || field.equals("SAT"))
             {
                newField = newField + String.valueOf(getWeek(field));
             }
         }
         else
         {
             newField = "~invalid~";
         }

         return newField;
     }

    public String weekAdd(String field)
    {
         String newField = " ADD ";
         String number = "";

         for(int i = 1; i <= field.length(); i++)
         {
            if(isNumber(field.substring(i - 1, i)))
            {
                number = number + field.substring(i - 1, i);
            }
            else
            {
                number = "~invalid~";
                break;
            }
            boolean over = false;

                 try
                 {
                     if(Integer.parseInt(number) > 7)
                     {
                         over = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(over)
                {
                    number = "~invalid over~";
                    break;
                }

        }
          boolean under = false;
                 try
                 {
                     if(Integer.parseInt(number) < 1)
                     {
                        under = true;
                     }

                 }catch(Exception e)
                 {
                     System.out.println("not a integer");
                 }

                if(under)
                {
                    number = "~invalid under~";
                }
         newField = newField + number;
         return newField;
     }
}

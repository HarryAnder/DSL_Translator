/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dsl_translator;

/**
 *
 * @author Harry
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TranslateCron translate = new TranslateCron("1/1 * * 7W * 4#5 1992");

        TranslateDSL translateBack = new TranslateDSL(translate.getTranslated());

        System.out.println(translate.getTranslated());
        System.out.println(translateBack.getTranslated());
        //TranslateDSL t = new TranslateDSL("@<year:: 1992, day_of_week:: THE 5 4, month:: EVERY, day_of_month:: WEEKDAY CLOSEST TO 7, hours:: EVERY, minutes:: EVERY, seconds:: START 1 ADD 1>");
    }

}

// Montana Williams
// 6/14/22
// CS&141
// Calendar 3: The Finale
// 
// This program is a functional calendar fit with the ability to plan events, print calendars to a file, and flick between months.
// For a little something extra, I have my method dateChecker which makes it near impossible to crash the program by typing a date stupid (see lines 207-231).
// Enjoy my final CS141 assignment! See ya in 142!

import java.util.Scanner;
import java.util.Calendar;
import java.io.*;

public class mwCalendar3 {

    public static void main(String[] args)
            throws FileNotFoundException {

        //Set up useful objects
        Calendar calendar = Calendar.getInstance();
        Scanner input = new Scanner(System.in);
        String[][] calendarEvents = calendarArraySetup(); //This array contains all events for the calendar.

        art();
        System.out.println("Welcome to your calendar.");

        int mode = 0;
        int month = 30;

        do {
            mode = menu(input);
            if (mode == 1) {
                month = givenDate(input, calendarEvents, calendar);
            } else if (mode == 2) {
                month = currentDate(calendarEvents, calendar);
            } else if (mode == 3) {
                month++;
                month = specificMonth(month, calendarEvents, calendar);
            } else if (mode == 4) {
                month--;
                month = specificMonth(month, calendarEvents, calendar);
            } else if (mode == 5) {
                calendarEvents = eventPlanning(input, calendarEvents, calendar);
            } else if (mode == 6) {
                filePrintMenu(input, calendarEvents, calendar);
            }//end if/else
        } while (mode < 20);

        System.out.println("Have a nice day!");
    }//end main


    public static int menu(Scanner input) {
        //This method displays options for the menu. Requires input of a scanner to collect user input. Returns a mode representing user's choice.
        System.out.println("What would you like to do?\n\tType a command:");
        System.out.println("\tType 'e' to enter a date and display the corresponding calendar\n\tType 't' to get today's date and display today's calendar");
        System.out.println("\tType 'n' to display the next month\n\tType 'p' to display the previous month\n\tType 'ev' to put an event on your calendar.");
        System.out.println("\tType 'fp' to print a month to a file.\n\tType 'q' to quit");

        String userChoice = input.next();
        int mode = menuControlled(userChoice);
        return mode;
    }//end menu


    public static int menuControlled(String userChoice) {
        //This method looks at the response to the menu and tells the program what to do.
        //Requires users' choice from the menu and returns what mode the program will do.
        int mode = 0;
        switch (userChoice) {
            case "e":
            case "E":
                mode = 1;
                break;
            case "t":
            case "T":
                mode = 2;
                break;
            case "n":
            case "N":
                mode = 3;
                break;
            case "p":
            case "P":
                mode = 4;
                break;
            case "ev":
            case "EV":
            case "Ev":
            case "eV":
                mode = 5;
                break;
            case "fp":
            case "Fp":
            case "FP":
            case "fP":
                mode = 6;
                break;
            case "q":
            case "Q":
                mode = 40;
                break;
            default:
                System.out.println("Please select a valid command.\n");
                break;
        }//end switch case
        return mode;
    }//end menuControlled


    public static void filePrintMenu(Scanner input, String[][] calendarEvents, Calendar calendar)
            throws FileNotFoundException {
        //This method prompts the user to print a calendar to a file. No outputs.
        //Inputs:   input: Scanner to collect user input.
        //          calendarEvents: String[][] array containing events to be placed on calendar.
        System.out.println("\nWhat would you like to name the file output?");
        String fileOutput = input.next() + ".txt";

        System.out.println("\nWhat month would you like to print?\nFor specific date highlighted, type mm/dd.\nFor just month, type mm/00");
        String date = dateCheck(input);

        printCalendar(date, calendarEvents, fileOutput, calendar);
        System.out.println("Your calendar has been saved as " + fileOutput + "\n");
    }//end filePrintMenu


    public static void printCalendar(String date, String[][] calendarEvents, String fileOutput, Calendar calendar)
            throws FileNotFoundException {
        //This method prints a user's calendar to a file with a name of their choosing. No outputs.
        //Inputs: date: day user is hoping to print a calendar of
        //        calendarEvents: String[][] array containing events to be placed on calendar.
        //        fileOutput:name of the file calendar will be printed to
        //        calendar: Calendar object to be passed into drawMonth
        int day = dayFromDate(date);
        int month = monthFromDate(date);
        PrintStream filePrint = new PrintStream(new File(fileOutput));
        PrintStream console = System.out;

        System.setOut(filePrint);//Instead of writing whole new methods, for this draw method the System.out is turned into a PrintStream for a file.
        drawMonth(month, day, calendarEvents, calendar);
        System.setOut(console);//And then turned back.
        System.out.println();
    }//end printCalendar


    public static String[][] eventPlanning(Scanner input, String[][] calendarEvents, Calendar calendar) {
        //This method allows a user to plan an event by entering it into the array CalendarEvents.
        //Inputs:   input:allows user to type information.
        //          calendarEvents: String[][] array containing events to be placed on calendar.
        //          calendar:calendar object to be passed into drawMonth
        System.out.println("\nPlease select the date for event:");
        String date = dateCheck(input);
        int month = monthFromDate(date);
        int day = dayFromDate(date);

        System.out.println("What is the name of this event? Mind only the first 8 characters will be shown on calendar.");
        String eventName = input.next() + input.nextLine();
        String event = date + " " + eventName.replace(" ", "_");

        calendarEvents[month - 1][day - 1] = event;
        drawMonth(month, 0, calendarEvents, calendar);
        System.out.println();
        return calendarEvents;
    }//end eventPlanning


    public static int givenDate(Scanner input, String[][] calendarEvents, Calendar calendar) {
        //This method makes a calendar based on a user input.
        //Input: input    scanner so user can input day to look at
        //       calendar pass from main to this method to drawMonth
        //output:month    month that current displayed calendar is looking at
        //Prompt user for date
        System.out.print("\nWhat day would you like to look at? (mm/dd)\n\t\t");
        String date = dateCheck(input);
        //Get day and month as integers
        int month = monthFromDate(date);
        int day = dayFromDate(date);
        System.out.println();
        //Make the calendar and display the date below
        drawMonth(month, day, calendarEvents, calendar);
        displayDate(month, day);
        System.out.println();
        return month;
    }//end givenDate


    public static int currentDate(String[][] calendarEvents, Calendar calendar) {
        //this method displays today's calendar
        //Input: calendar Calendar object pass from main to this method to drawMonth
        //Output:month month that current displayed calendar is looking at
        //Get today's day and month
        int thisMonth = calendar.get(calendar.MONTH) + 1;//Month using calendar obj are indexed from 0, whereas we start our months at 1, so add 1.
        int thisDay = calendar.get(calendar.DATE);

        //Make the calendar for this month and display the date
        drawMonth(thisMonth, thisDay, calendarEvents, calendar);
        displayDate(thisMonth, thisDay);
        return thisMonth;
    }//end currentDate


    public static int specificMonth(int month, String[][] calendarEvents, Calendar calendar) {
        //Displays next or previous month from one already displayed
        //Inputs: month    month that is going to be displayed
        //        calendar calendar to pass from main to this method to drawMonth
        //Output: month    month that current displayed calendar is looking at
        if (month == 29 || month == 31) {
            System.out.println("This is only a valid command if a calendar is already displayed. \n\nPlease select another option.");
            month = 30;
        } else if (month > 12) {
            drawMonth(1, 0, calendarEvents, calendar);
            System.out.println();
        } else if (month < 1) {
            drawMonth(12, 0, calendarEvents, calendar);
            System.out.println();
        } else {
            drawMonth(month, 0, calendarEvents, calendar);
            System.out.println();
        }//end if/else
        return month;
    }//end specificMonth


    public static String dateCheck(Scanner input) {
        //This method forces a user to type a date in a valid format. Requires input of a scanner for user input.
        boolean good = true;
        String date = "";
        do {
            date = input.next();
            if (date.matches("^\\d{2}/\\d{2}$")) {//User input must match dd/mm to move forwards
                int day = dayFromDate(date);
                int month = monthFromDate(date);
                good = true;
                if (month < 1 || month > 12) {//If date matches, month must be a valid month.
                    System.out.println("I'm sorry, that is not a valid number for a month. Please input date mm/dd.");
                    good = false;
                }//end month if
                if (day > 31) {//Finally, there must be an appropriate amount of days.
                    System.out.println("I'm sorry, this month does not have that many days. Please input date mm/dd.");
                    good = false;
                }//end day if
            } else {//if user can't listen to directions, make them try again.
                System.out.println("I'm sorry, that is not a valid date format. Please input date mm/dd.");
                good = false;
            }//end if/else
        } while (good == false);//cant move on until a valid format
        return date;
    }//end dateCheck


    public static void drawMonth(int month, int day, String[][] calendarEvents, Calendar calendar) {
        //Creates the actual calendar
        //Input: month    month that calendar will be made for
        //       day      day that user wants to look at (if 0, won't be shown on calendar)
        //       calendar object that will be used to find the first day of the month
        System.out.printf("\t\t %d\n\n", month);
        int firstDay = firstDayOfMonth(month, calendar);
        int daysInMonth = days(month);
        drawRow();
        System.out.println("|  SUN   |  MON   |  TUES  |  WED   | THURS  |  FRI   |  SAT   |");
        for (int i = 2 - firstDay; i <= daysInMonth; i += 7) { //Create a calendar with appropriate amount of days
            drawRow();
            columnDate(i, i + 6, firstDay, day); //number of Sunday of the week and Saturday
            columnEvent(i, i + 6, month, daysInMonth, calendarEvents);//Put events on that calendar
            column();
        }//end for loop
        drawRow();
    }//end drawMonth


    public static void columnEvent(int start, int end, int month, int daysInMonth, String[][] calendarEvents) {
        //This method puts events on the calendar.
        //Inputs:   start: Sunday of the week.
        //          end: Saturday of the week.
        //          month: Month calendar is being drawn of.
        //          daysInMonth: How many days are in the month total
        //          calendarEvents: String[][] array containing events to be placed on calendar.
        String event;
        for (int i = start; i <= end; i = i + 1) {
            if (i <= 0) {
                System.out.print("|        ");
            } else if (i >= daysInMonth) {//If the day of the month isn't yet 1, so just input same thing as in column method.
                System.out.print("|        ");
            } else {
                event = calendarEvents[month - 1][i - 1];
                if (event != null) {
                    thereIsEvent(event);
                } else {
                    System.out.print("|        ");
                }//end event if/else
            }//end if/else
        }//end for
        System.out.println("|");
    }//end columnEvent

    public static void thereIsEvent(String event) {
        //This method means there's an event! And we don't want the calendar block to mess up formatting!
        //Requires input of the event as stored in the array calendarEvents
        //Note only the first 8 characters of an event name will be outputted
        String eventName = event.substring(6, event.length());
        if (eventName.length() <= 8) {
            System.out.print("|" + eventName.replace("_", " "));
            for (int i = eventName.length(); i <= 7; i++) {
                System.out.print(" ");
            }//end for
        } else {
            System.out.print("|" + eventName.substring(0, 8).replace("_", " "));
        }//end if/else
    }//end thereIsEvent


    public static void columnDate(int start, int end, int firstDay, int day) {
        //Add date to the squares of the calendar.
        //Inputs: start    Sunday of week
        //        end      Saturday of week
        //        firstDay First day of the month (weekday)
        //        day      Specific day user is looking at in month
        for (int i = start; i <= end; i = i + 1) {
            if (i == day && i < 10 && i > 0) {//If specific day is less than 10
                System.out.printf("| %d **   ", i);
            } else if (i == day && i >= 10) {//If specific day is 10 or greater
                System.out.printf("| %d **  ", i);
            } else if (i <= 0) {//If the day of the month isn't yet 1, so just input same thing as in column method.
                System.out.print("|        ");
            } else if (i < 10) { //If the date is less than ten, and no specific day
                System.out.printf("| %d      ", i);
            } else if (i > 31) { //All months are at most 31 days, so if day is over 31, just input same thing as in column method.
                System.out.print("|        ");
            } else { //If a double digit day
                System.out.printf("| %d     ", i);
            }//end if/else
        }//end for
        System.out.println("|");
    }//end columnDate


    public static int firstDayOfMonth(int month, Calendar calendar) {
        //This method finds what day of the week the first day of the month falls on
        //Input: month    what month you want the day of the week for
        //       Calendar Calendar obj so you can set a dummy calendar to find the day of the week of the 1st
        //Output: day     what day of week the 1st falls on - 1 is Sunday
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }//end firstDayOfMonth


    public static int days(int month) {
        //This method provides the number of days in a given month
        //Input: month Month you're trying to make a calendar of
        //Output:day   How many days in the given month
        int days;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            default:
                days = 30;
        }//end switch/case
        return days;
    }//end days


    public static String[][] calendarArraySetup()
            throws FileNotFoundException {
        //This method sets up the calendar array that contains all events.
        String[][] calendarEvents = new String[12][30];//Default 30 days in a month.
        //These months all have 31 days, though.
        calendarEvents[0] = new String[31];
        calendarEvents[2] = new String[31];
        calendarEvents[4] = new String[31];
        calendarEvents[6] = new String[31];
        calendarEvents[7] = new String[31];
        calendarEvents[9] = new String[31];
        calendarEvents[11] = new String[31];

        File calendarEventsFile = new File("calendarEvents.txt");
        if (calendarEventsFile.exists()) {//Only try and read this file if it's actually here. Store events if it does exist.
            Scanner dateRead = new Scanner(calendarEventsFile);
            while (dateRead.hasNextLine()) {
                String eventPulled = dateRead.nextLine();
                String date = eventPulled.substring(0, 5);
                int day = dayFromDate(date);
                int month = monthFromDate(date);
                String eventName = eventPulled.substring(6, eventPulled.length());
                calendarEvents[month - 1][day - 1] = date + " " + eventName.replace(" ", "_");
            }//end while
        }//end if
        return calendarEvents;
    }//end calendarArraySetup


    public static int monthFromDate(String date) {
        //Pull integer for the month out of inputted date.
        //Input of the string given by the user
        //Output of an integer for the month
        String monthPulled = date.substring(0, 2);
        int month = Integer.parseInt(monthPulled);
        return month;
    }//end monthFromDate


    public static int dayFromDate(String date) {
        //Pull integer for the day out of the inputted date.
        //Input of the string given by the user
        //Output of an integer for the day
        String dayPulled = date.substring(3, 5);
        int day = Integer.parseInt(dayPulled);
        return day;
    }//end dayFromDate


    public static void displayDate(int month, int day) {
        //Using day and month integers, display the date.
        System.out.println("Month: " + month);
        System.out.println("Day: " + day + "\n");
    }//end displayDate


    public static void column() {
        //Make vertical columns for actual squares of the calendar.
        for (int i = 1; i <= 7; i++) {
            System.out.print("|        ");
        }//end for loop
        System.out.println("|");
    }//end column


    public static void drawRow() {
        //Make horizontal lines
        for (int i = 1; i <= 32; i++) {
            System.out.print("=+");
        }//end for loop
        System.out.println();
    }//end drawRow


    public static void art() {
        //Have some wonderful art at the start of your calendar.
        drawRow();
        System.out.println("\t\t\t\t\t\t\t\"Time is an illusion\"");
        drawRow();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 16; j++) {
                System.out.print("+ = ");
            }//end for
            System.out.println();
            for (int j = 1; j <= 16; j++) {
                System.out.print(" = +");
            }//end for
            System.out.println();
            for (int j = 1; j <= 16; j++) {
                System.out.print("= + ");
            }//end for
            System.out.println();
            for (int j = 1; j <= 16; j++) {
                System.out.print(" + =");
            }//end for
            System.out.println();
        }//end fors
        drawRow();
        System.out.println();
        drawRow();
        System.out.println();
    }//end art

}//end class
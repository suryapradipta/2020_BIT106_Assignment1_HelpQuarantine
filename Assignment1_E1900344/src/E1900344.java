import java.util.*; // Scanner class is in this package

/**
 * Java program to HELP QUARANTINE, a simple application to calculate
 * the final bill of quarantined guests and some other information.
 * Written by I Nyoman Surya Pradipta
 * Written on 11 October 2020
 * Student ID : E1900344
 */

public class E1900344 {
    /**
     * The main method which makes use to display
     * input and output from the program.
     * @param args Not used.
     * @return void No value.
     */
    public static void main(String[] args){
        // Declare and assign to variables.
        String guestName = "", topUp, nationality;
        char category;
        int numOfTrans = 0, malayCount = 0, foreignCount = 0,
            topUpAmount, highestIndex = 0;
        byte daysStayed;
        double charge, amountCharged, paidByGovt, paidBySelf,
            totalAmount = 0, malayAvg = 0, foreignAvg = 0,
            quarantineFund = 3000;

        // Declare array list.
        var highestName = new ArrayList<String>();
        var highestCategory = new ArrayList<String>();
        var highestAmountPaid = new ArrayList<Double>();

        // Print the output of Quarantine Fund.
        System.out.printf("Quarantine Fund amount: %.0f RM\n", quarantineFund);
        // Create a Scanner object to read data.
        Scanner input = new Scanner(System.in);
        while(!(guestName.equalsIgnoreCase("q"))){ // Testing.
            // Read each guest's name.
            System.out.print("\nEnter guest name (Q/q to quit): ");
            guestName = input.next();

            if (!(guestName.equalsIgnoreCase("q"))) { // Testing.
                // Read each category from the guest.
                System.out.print("Category: ");
                category = input.next().charAt(0);

                // Validate user input category.
                while (!validateCategory(category)) { // Testing.
                    // Read until a valid value provided.
                    System.out.println("Invalid Category");
                    System.out.print("Please enter category: ");
                    category = input.next().charAt(0);
                }

                // Read each day stayed from the guest.
                System.out.print("Days stayed: ");
                daysStayed = input.nextByte();
                /*
                Validate the number of days stayed between 14-42,
                otherwise the default value of 21
                will be assign and printed
                */
                if (!validateDaysStayed(daysStayed)) { // Testing.
                    daysStayed = 21;
                    System.out.println("Default value " + daysStayed + " set for Days stayed");
                }

                // The program will ask for top up or quit.
                if  (quarantineFund < 500) { // Testing.
                    // Read each choice.
                    System.out.println("\nInsufficient balance in quarantine fund");
                    System.out.println("Would you like to:");
                    System.out.println("T. Top Up , Any other key to quit");
                    System.out.print("Your choice: ");
                    topUp = input.next();

                    //Read "T" to top up or other key to quit.
                    if (topUp.equalsIgnoreCase("t")) { // Testing.
                        System.out.print("Top up amount: ");
                        topUpAmount = input.nextInt();
                        quarantineFund += topUpAmount;
                        System.out.printf("Quarantine fund balance: %.0f RM\n", quarantineFund);
                    } else {
                        break; // program will be quit.
                    }
                } else {
                    // Print the output auto generate transaction ID.
                    System.out.printf("Transaction ID: %03d\n\n", (int) autoGenerateTransID());
                    if (category == 'm' || category == 'M') {
                        /*
                         Initialize to add Malaysian
                         to array highest category.
                         */
                        nationality = "Malaysian";
                        charge = 80.3;
                        malayCount++; // Increase count from Malaysian.
                        /*
						Add amount charged to malayAvg
						to get average from Malaysian.
						 */
                        malayAvg += calculateAmountCharged(daysStayed,charge);
                    } else if (category == 'r' || category == 'R') {
                        /*
                         Initialize to add Resident
                         to array highest category.
                         */
                        nationality = "Resident";
                        charge = 100.6;
                        foreignCount++; // Increase count from Foreigner.
                        /*
						Add amount charged to foreignAvg
						to get average from Foreigner.
						 */
                        foreignAvg += calculateAmountCharged(daysStayed,charge);
                    } else {
                        /*
                         Initialize to add Expat
                         to array highest category.
                         */
                        nationality = "Expat";
                        charge = 120.9;
                        foreignCount++; // Increase count from Foreigner.
                        /*
						Add amount charged to foreignAvg
						to get average from Foreigner.
						 */
                        foreignAvg += calculateAmountCharged(daysStayed,charge);
                    }

                    // Assign calculation method to variables.
                    amountCharged = calculateAmountCharged(daysStayed, charge);
                    paidByGovt = calculatePaidByGovt(category, amountCharged);
                    paidBySelf = calculatePaidBySelf(amountCharged, paidByGovt);

                    /*
                     Add data highest name, category,
                     and amount paid to array list.
                     */
                    highestName.add(guestName);
                    highestCategory.add(nationality);
                    highestAmountPaid.add(amountCharged);
                    highestIndex = highestAmountPaidIndex(highestAmountPaid);

                    // Add amountCharged to totalAmount.
                    totalAmount += amountCharged;
                    /*
                     Add malayCount and foreignCount
                     to numOfTrans.
                     */
                    numOfTrans = malayCount + foreignCount;
                    // Subtraction paid by government to quarantine fund.
                    quarantineFund -= paidByGovt;

                    /*
					Print the output of amount charged, paid by self,
					government, and quarantine fund.
					 */
                    System.out.printf("Amount charged for %d days @%.2f RM/day: %.2f RM\n",
                        daysStayed, charge, amountCharged);
                    System.out.printf("Paid by Self: %.2f RM\n", paidBySelf);
                    System.out.printf("Paid by Govt.: %.2f RM\n", paidByGovt);
                    System.out.printf("Quarantine fund balance: %.0f RM\n", quarantineFund);
                }
            }
        }
        //If the program quit, some information will be printed.
        if (numOfTrans == 0) { // Testing.
            System.out.println("No transactions");
        } else {
            int rows = 43;
            for (int i = 1; i <= rows; ++i) {
                System.out.print('-');
            }
            /*
			Print output total, highest, average amount,
			and quarantine fund balance.
		     */
            System.out.printf("\n\nTotal Amount from %d guests is %.2f RM\n", numOfTrans, totalAmount );
            System.out.printf("Highest amount paid %.2f RM by %s, is an %s\n", highestAmountPaid.get(highestIndex),
                highestName.get(highestIndex), highestCategory.get(highestIndex));
            System.out.printf("Average amount from Malaysians: %.2f RM\n", calculateAvg(malayAvg, malayCount));
            System.out.printf("Average amount from Foreigners: %.2f RM\n", calculateAvg(foreignAvg, foreignCount));
            System.out.printf("Quarantine fund balance: %.0f RM\n", quarantineFund);
        }
    }

    /**
     * This method is used to validate nationality among Malaysian, Resident, and Expat
     * until a valid value is provided.
     * @param category The category value to validate.
     * @return boolean Expressions.
     */
    public static boolean validateCategory(char category) {
        boolean returnValue;
        returnValue =
            category == 'M' || category == 'm' ||
            category == 'R' || category == 'r' ||
            category == 'E' || category == 'e';
        return returnValue;
    }

    /**
     * This method is used to validate day stayed
     * @param daysStayed The daysStayed value to validate.
     * @return boolean Expressions.
     */
    public static boolean validateDaysStayed(byte daysStayed){
        boolean returnValue;
        returnValue  = daysStayed >= 14 && daysStayed <= 42;
        return returnValue;
    }

    /**
     * This method is used to calculation auto generate transaction ID.
     * @return double Calculation auto generate number between 001-999.
     */
    public static double autoGenerateTransID() {
        return (Math.random() * 999) + 1;
    }

    /**
     * This method is used to calculation amount charged.
     * @param daysStayed Value to be multiplied charge.
     * @param charge Value to be multiplied daysStayed.
     * @return double Multiplication daysStayed and charge.
     */
    public static double calculateAmountCharged(byte daysStayed, double charge) {
        return daysStayed * charge;
    }

    /**
     * This method is used to calculation charges paid by Government
     * according to nationality (malaysian or foreigner) and amount charged.
     * @param category Value to check statements.
     * @param amountCharged Value to be multiplied discount.
     * @return double Multiplication by the amount charged and discount
     * to get the charge.
     */
    public static double calculatePaidByGovt(char category, double amountCharged) {
        double charge;
        if (category == 'm' || category == 'M') {
            if (amountCharged >= 2500)
                charge = amountCharged * 275/1000;
            else if (amountCharged >= 2000)
                charge = amountCharged * 215/1000;
            else if (amountCharged >= 1500)
                charge = amountCharged * 15/100;
            else
                charge = 100;
        } else {
            if (amountCharged >= 3000)
                charge = amountCharged * 175/1000;
            else if (amountCharged >= 2000)
                charge = amountCharged * 95/1000;
            else
                charge = 80;
        }
        return charge;
    }

    /**
     * This method is used to calculation charges paid by Self.
     * @param amountCharged Value to be multiplied by paidByGovt.
     * @param paidByGovt Value to be multiplied by amountCharged.
     * @return double This returns subtraction by amountCharged and charge.
     */
    public static double calculatePaidBySelf(double amountCharged, double paidByGovt) {
        return amountCharged - paidByGovt;
    }

    /**
     * This method is used to calculation average from nationality (malaysian or foreigner).
     * @param nationality Value to be division by nationCount.
     * @param nationCount  Value to be division by nationality.
     * @return double Division by nationality and count.
     */
    public static double calculateAvg (double nationality, int nationCount) {
        return nationality / nationCount;
    }

    /**
     * This method is used to calculation to get index from array.
     * @param dataAmountPaid Value data in array.
     * @return int Highest index.
     */
    public static int highestAmountPaidIndex (ArrayList<Double> dataAmountPaid) {
        // Declare and initialize.
        Double[] arr = dataAmountPaid.toArray(Double[]::new);
        Double highest = arr[0];
        int highestIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (highest < arr[i]) {
                highest = arr[i]; // Get the highest amount.
                highestIndex = i; // Get the highest index.
            }
        }
        return highestIndex;
    }
}

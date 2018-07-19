//ParkingLotManagementSystem.java
/*
 This is a simple program which uses a menu format to allow users to store data about cars 
 parked in a parking lot. Developed for extra credit for a Java programming class, the project
 covers classes, methods, exceptions, and file I/O. In addition, the project focuses heavily on 
 filtering user input, using many techniques including regular expressions.  
*/

import java.io.*;
import java.util.Scanner;

//The class Cars is used to keep track of the cars in the parking lot.
class Cars implements Serializable {
	// Properties of the cars which will be parked in the lot.
	//Type of car parked in the space.
	String carType;
	//License plate number of the car in the space.
	String licenseNumber;
	//Number of the car's parking space.
	int parkingSpot;
	//Price paid by the car to park.
	double price;
	//Time, in hours, the car will be parked there.
	int time;

	//Constructor for class cars. 
	//An array of type Cars will be created. The parkingSpot property of each will be kept
	//at 0 to indicate it is available. When it is filled, it will be changed to 1 to indicate 
	//that space is full.
	Cars() {
		parkingSpot = 0;
	}

	//Standard set and get methods for all the properties of Cars.
	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public int getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(int parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}

public class ParkingLotManagementSystem {

	//Method to indicate invalid input. 
	//Because a main focus of this project is input filtering, this will be used often.
	public static void invalidInput() {
		System.out.print("Invalid input. Try again: ");
	}

	//Validates user-inputed space number.
	//Must be between 1 and 50, inclusive.
	public static boolean validateSpaceNumber(String s) {
		boolean goodNum = false;
		int spaceNum;
		if (s.matches("\\d+")) {
			spaceNum = Integer.parseInt(s);
			if (spaceNum > 0 && spaceNum <= 50) {
				goodNum = true;
			}
		}
		return goodNum;
	}
	//Validates user-inputed license plate number.
	//There is no restriction on the character types of a license plate, but it 
	//must not be blank or more than 6 characters.
	//Because multiple cars should not be issued the same license number, 
	//this program does not check for duplicate license numbers.
	public static boolean validateLicense(String s) {
		boolean goodLic = false;
		if (!s.isEmpty() && (s.length() <= 6))
			goodLic = true;
		return goodLic;
	}
	
	//Validates the user-inputed price for a parking space.
	//Must be in dollar format and be between 0.00 and 100.00 dollars.
	//Examples:
	//12, 12.21, 0.21 are all acceptable formats.
	//12.0, 12.210, .21, 12.21.21, etc are unacceptable formats.
	public static boolean validatePrice(String s) {
		boolean goodPrice = false;
		double price;
		if (s.matches("0\\.[0-9]\\d*|[0-9]\\d*(\\.\\d{2})?")) {
			price = Double.parseDouble(s);
			if (price > 0.00 && price < 100.00) {
				goodPrice = true;
			}
		}
		return goodPrice;
	}

	//Validates the user-inputed time the car will be parked.
	//Must be a whole number, more than 0, but not exceeding 24 hours.
	public static boolean validateTime(String s) {
		boolean goodTime = false;
		int time;
		if (s.matches("\\d+")) {
			time = Integer.parseInt(s);
			if (time > 0 && time <= 24) {
				goodTime = true;
			}
		}
		return goodTime;
	}

	//Method used to stop the program until further user input.
	public static void cont(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter any key to " + msg + ":");
		sc.next();
		System.out.println();
	}

	public static void main(String[] args) {
		//Declaring variables
		//userSelection holds the user input for the main level menu.
		String userSelection = "0";
		//i will hold the user-inputed parking spot number.
		int i = 0;
		//ostream and istream are created in anticipation of file I/O later in the project.
		ObjectOutputStream ostream = null;
		ObjectInputStream istream = null;
		//An array of 50 objects of type Cars is created and all objects are initiated. 
		Cars[] cars = new Cars[50];
		for (int x = 0; x < 50; x++) {
			cars[x] = new Cars();
		}
		Scanner sc = new Scanner(System.in);
		//Boolean mainMenu is set to true when the user exits the entire program. 
		//For now it is false.
		boolean mainMenu = false;
		// Beginning of top level menu.
		while (!mainMenu) {
			//Printing the main menu.
			System.out.println("Parking Lot Management Program");
			System.out.println("Choose one of the follwing options:");
			System.out.println("1. Assign a car to a parking spot.");
			System.out.println("2. Show number of empty parking spots.");
			System.out.println("3. Show number of taken spots.");
			System.out.println("4. Search for car using license number.");
			System.out.println("5. Exit program");
			System.out.println();
			System.out.println("6. Write information to file -- NEW");
			System.out.println("7. Read information from file -- NEW");
			System.out.println();
			System.out.print(">> Enter selection ->");
			userSelection = sc.next();
			switch (userSelection) {
			// Assigning a car to a parking spot.
			case "1":
				System.out.println("Assign a car to a parking spot:");
				// Parking spot info
				System.out.print("Enter parking spot number: ");
				String sN = sc.next();
				//Using validateSpaceNumber to validate the user-inputed space number.
				//If the number inputed is acceptable, the array index corresponding to that
				//number will be marked as taken.
				//If the number is not acceptable, the program will continue to prompt the user
				//for an acceptable number.
				boolean test = false;
				while (!test) {
					if (validateSpaceNumber(sN)) {
						i = Integer.parseInt(sN) - 1;
						if (cars[i].getParkingSpot() == 0) {
							cars[i].setParkingSpot(1);
							test = true;
						} else {
							invalidInput();
							sN = sc.next();
						}
					} else {
						invalidInput();
						sN = sc.next();
					}
				}
				//The type of car being parked is inputed by the user.
				//As long as some input is given, the input is acceptable.
				System.out.print("Enter car type: ");
				String cT = sc.next();
				while (cT.isEmpty()) {
					invalidInput();
					cT = sc.next();
				}
				cars[i].setCarType(cT);
				//The license plate number is inputed by the user.
				//validateLicense is used to validate the input.
				//Acceptable input may contain any characters, as long as the length
				//is more than 0 but not exceeding 6 characters.
				System.out.print("Enter car license :");
				String lic = sc.next();
				while (!validateLicense(lic)) {
					invalidInput();
					lic = sc.next();
				}
				cars[i].setLicenseNumber(lic);
				//The parking spot price is inputed by the user.
				//validatePrice is used to validate the input.
				//The program accepts only acceptable formats, as described above.
				//The user will be prompted for input until an acceptable format is provided.
				System.out.print("Enter spot price: ");
				String price = sc.next();
				while (!validatePrice(price)) {
					invalidInput();
					price = sc.next();
				}
				cars[i].setPrice(Double.parseDouble(price));
				//The reserve time is inputed by the user.
				//validateTime is used to validate the input.
				//Only a time as described above is accepted.
				System.out.print("Enter reserve time: ");
				String time = sc.next();
				while (!validateTime(time)) {
					invalidInput();
					time = sc.next();
				}
				cars[i].setTime(Integer.parseInt(time));
				//Method cont is used to pause the program until further user input.
				cont("save information and return to main menu");
				break;
			case "2":
				//A for loop is used to iterate through the array of parking spaces
				//and count the number of empty spaces in the parking lot.
				int n = 0;
				for (int j = 0; j < 50; j++) {
					if (cars[j].getParkingSpot() == 0) {
						n++;
					}
				}
				System.out.println();
				System.out.println("There are " + n + " empty spots.");
				//Method cont is used to pause the program until further user input.
				cont("return to main menu");
				break;
			case "3":
				//A for loop is used to iterate through the array of parking spaces
				//and count the number of taken spaces in the parking lot.
				int p = 0;
				for (int q = 0; q < 50; q++) {
					if (cars[q].getParkingSpot() != 0) {
						p++;
					}
				}
				System.out.println();
				System.out.println("There are " + p + " taken spots.");
				//Method cont is used to pause the program until further user input.
				cont("return to main menu");
				break;
			case "4":
				//A for loop is used, to iterate through the cars in the parking lot to find
				//a car with the user inputed license number.
				System.out.print("Enter car license number: ");
				String searchLicense;
				searchLicense = sc.next();
				boolean check = false;
				for (int t = 0; t < 50; t++) {
					if (searchLicense.equals(cars[t].getLicenseNumber())) {
						//If a matching car is found, it's information is displayed.
						System.out.println();
						System.out.println("Spot is: " + (t + 1));
						System.out.println("Car type is: " + cars[t].getCarType());
						System.out.println("Spot price is: " + cars[t].getPrice());
						System.out.println("Reserve time is: " + cars[t].getTime());
						System.out.println();
						cont("return to main menu");
						check = true;
					}
				}
				if (!check) {
					//If a car is not found, this is communicated to the user.
					System.out.println();
					System.out.println("Car not found.");
					//Method cont is used to pause the program until further user input.
					cont("return to main menu");
				}
				break;
			case "5":
				//Selection 5 ends the program.
				System.out.println("Program ending...");
				sc.close();
				mainMenu = true;
				break;
			case "6":
				//Writes the serializable Car class to a file.
				//All exceptions are handled by printing a stack trace.
				//Ensure the file path is appropriate for your machine.
				try {
					ostream = new ObjectOutputStream(
							new BufferedOutputStream(new FileOutputStream("c:\\temp\\file.pdf")));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					ostream.writeObject(cars);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					ostream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println();
				System.out.println("Information written to file.");
				//Method cont is used to pause the program until further user input.
				cont("return to main menu");
				break;
			case "7":
				//Reads information previously stored on the file and prints it for the user,
				//if any information was stored on the file.
				//All exceptions are handled by printing a stack trace.
				//If the program successfully writes to the file, 
				//the file path used here should work as well.				
				try {
					istream = new ObjectInputStream(new BufferedInputStream(new FileInputStream("c:\\temp\\file.pdf")));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Cars[] newCars = new Cars[50];
				try {
					newCars = (Cars[]) istream.readObject();
					System.out.println("The following information was read from the file:");
					System.out.println();
					for (int y = 0; y < 50; y++) {
						if (newCars[y].getParkingSpot() != 0) {
							System.out.println("License number: " + newCars[y].getLicenseNumber());
							System.out.println("Spot number: " + (y + 1));
							System.out.println("Car type: " + newCars[y].getCarType());
							System.out.println("Spot price: " + newCars[y].getPrice());
							System.out.println("Reserve time: " + newCars[y].getTime());
							System.out.println();
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//Method cont is used to pause the program until further user input.
				cont("return to main menu");
				break;
			default:
				//Default case for the main-level menu.
				System.out.println();
				System.out.println("Invalid input. Try again.");
				System.out.println();
			}
		}
	}
}
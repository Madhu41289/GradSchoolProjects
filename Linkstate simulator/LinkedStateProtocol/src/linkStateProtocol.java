import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Madhumitha Karthikeyan (A20320012)
 * 
 * Goal:To implement Link-State Routing Protocol
 */

public class linkStateProtocol

{
	public final static int MAX_VALUE = 1000;
	public static int[][] inputNetwork;
	public static int[][] distance;
	public static int[][] costMatrix;
	public static int[][] visitedNode;
	public static int[][] lastVisitedNode;
	public static int noOfRouters = 0;
	public static int source = 0;
	public static int destinationRouter = 0;
	public static int removedRouter = -1;
	Scanner scanChoice;
	int flag = 0;

	/**
	 * This function is to display the Menu
	 */
	public void menu() {
		// Display the menu for the operations
		System.out.println("CS542 Link State Routing Simulator ");
		System.out.println("\n (1) Create a network topology");
		System.out.println("\n (2) Build a Connection Table");
		System.out.println("\n (3) Shortest Path to Destination Router");
		System.out.println("\n (4) Modify a topology");
		System.out.println("\n (5) Exit");
		System.out.println("Command:");

		try {
			// scan the input choice from the user
			scanChoice = new Scanner(System.in);
			int choice = scanChoice.nextInt();
			simulator(choice);
		}
		catch (InputMismatchException ex) {
			// If the user inputs anything other than numbers
			System.out
					.println("You entered a bad input...please input a number between 1 to 5");
			menu();
		}
		finally {
			// close the scanner
			scanChoice.close();
		}
	}

	/**
	 * This function is to perform the user's commands
	 * 
	 * @param scanChoice
	 */
	public void simulator(int scanChoice) {

		// open the input file
		File inputFile = new File("topology.txt");

		Scanner readFile = null;
		String line = "";
		String eachLine[];
		Scanner scan;
		int visited = 0, w = 0, s = 0;
		int min;
		String newstr[];

		// Scan the input file
		scan = new Scanner(System.in);

		// Create the input topology
		if (scanChoice == 1) {

			System.out.println("\n Input original network topology matix data file:");
			System.out.println("\n");
			try {
				readFile = new Scanner(inputFile);
				System.out.println("\n\nReview original Topology matrix: ");
				noOfRouters = 0;
				while (readFile.hasNext()) {
					line = readFile.nextLine();
					// Display the topology saved in the file
					System.out.println(line);
					noOfRouters++;
				}
				if (noOfRouters < 8) {
					// Extra credit: Accepts only a minimum of 8 routers
					System.out
							.println("Sorry!, The minimum number of input routers must be 8");
					menu();
				}

				inputNetwork = new int[noOfRouters][noOfRouters];

				int t = 0;
				readFile = new Scanner(inputFile);
				while (readFile.hasNext()) {
					line = readFile.nextLine();
					for (int p = 0; p < noOfRouters; p++) {
						eachLine = line.split(" ");

						// store the file inputs in an array
						inputNetwork[t][p] = Integer.parseInt(eachLine[p]);
					}
					t++;
				}

				distance = new int[noOfRouters][noOfRouters];
				costMatrix = new int[noOfRouters][noOfRouters];
				visitedNode = new int[noOfRouters][noOfRouters];
				lastVisitedNode = new int[noOfRouters][noOfRouters];

				for (int i = 0; i < noOfRouters; i++) {
					for (int j = 0; j < noOfRouters; j++) {
						distance[i][j] = costMatrix[i][j] = inputNetwork[i][j];
						if (distance[i][j] < 0)

							// Change the index of the non connected routers to a big integer
							// value.
							distance[i][j] = costMatrix[i][j] = MAX_VALUE;
					}
				}

				// implementation of dijkstra's algo
				for (int i = 0; i < noOfRouters; i++) {
					for (int j = 0; j < noOfRouters; j++) {
						visitedNode[i][j] = 0;
						lastVisitedNode[i][j] = i;
					}
				}

				for (int a = 0; a < noOfRouters; a++) {
					visitedNode[a][a] = 1;
					for (int i = 0; i < noOfRouters; i++) {
						min = MAX_VALUE;
						for (w = 0; w < noOfRouters; w++) {
							if (visitedNode[a][w] == 0) {
								if (distance[a][w] < min) {
									visited = w;
									min = distance[a][w];
								}
							}
						}
						visitedNode[a][visited] = 1;

						for (w = 0; w < noOfRouters; w++) {
							if (visitedNode[a][w] == 0) {
								if ((min + costMatrix[visited][w]) < distance[a][w]) {
									distance[a][w] = min + costMatrix[visited][w];
									lastVisitedNode[a][w] = visited;
								}
							}
						}
					}
				}

				menu();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		else if (scanChoice == 2) {

			s = 0;

			if (noOfRouters == 0) {
				System.out
						.println("Create the Initial Network topology by choosing option 1 first.");
				menu();
			}

			try {

				newstr = new String[noOfRouters];

				// Extra credit: Display the connection table for all the routers as
				// default

				System.out.print("\n\n The routing table of all routers : \n");

				System.out.print("    " + " |" + "\t");

				for (int i = 0; i < noOfRouters; i++) {

					System.out.print("\n");

					if (i >= 10)
						System.out.print("R" + (i + 1) + "  |" + " \t");

					else
						System.out.print("R" + (i + 1) + "   |" + " \t");

					for (int j = 0; j < noOfRouters; j++) {
						if (distance[i][j] == MAX_VALUE)
							System.out.print("-\t");
						else
							System.out.print(distance[i][j] + "\t");

					}
				}

				// Get the source router if it is executed for the first time

				if (flag != 1 || source == 0) {
					System.out.println("\n\n Select a source router:");
					try {
						source = scan.nextInt() - 1;
						if(source>=noOfRouters)
						{
							System.out.println("Please input a number between 1 to "+ noOfRouters);
							source = scan.nextInt() - 1;
						}
					}
					
					catch (InputMismatchException ex) {

						// If the user inputs anything other than a number
						System.out
								.println("You entered a bad input...please input a number between 1 to "
										+ noOfRouters);
						simulator(2);
					}
				}
				newstr = new String[noOfRouters];

				if (source < noOfRouters) {
					// Print the connection table for the entered source

					System.out.print("\nThe connection table for router R" + (source + 1)
							+ " : ");

					for (int maxi = 1; maxi <= noOfRouters; maxi++) {
						int src = source;
						int dest = maxi - 1;

						int newarr[] = new int[noOfRouters];

						int incx = 0;

						if (src <= noOfRouters && dest <= noOfRouters) {
							if (distance[src][dest] != MAX_VALUE) {
								newarr[incx++] = (dest + 1);
								w = dest;
								while (w != src) {
									newarr[incx] = (lastVisitedNode[src][w] + 1);
									w = lastVisitedNode[src][w];
									incx++;
								}

								int counter = 0;
								for (int x = 0; x < noOfRouters; x++) {
									if (newarr[x] != 0) {
										counter++;
									}
								}

								if (counter == 1) {
									newstr[s] = "-";
									s++;
								}
								else if (counter > 2) {
									newstr[s] = "R" + newarr[1];
									s++;
								}
								else {
									newstr[s] = "R" + newarr[0];
									s++;
								}
							}
						}
					}

					// Print the destination and interface from the source

					System.out.println("\n Destination \t Interface");
					System.out.println("====================================");
					for (int c = 0; c < noOfRouters; c++) {
						System.out.println("   R" + (c + 1) + "\t\t" + newstr[c]);
					}

				}
				else {
					// In case the user enters a source router number greater the number
					// of routers
					System.out
							.println("You entered a bad input...please input a number between 1 to "
									+ noOfRouters);
					source=0;
					simulator(2);
				}
				if (flag == 0)
					// If topology is not modified
					menu();
				else
					// To print the updated path of the modified topology
					flag = 0;
				simulator(3);

			}
			catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}

			catch (Exception e) {
				e.printStackTrace();

			}
		}
		else if (scanChoice == 3) {

			try {

				if (noOfRouters == 0) {
					// Incase the inital topology is not read from the file
					System.out
							.println("Create the Initial Network topology by choosing option 1 first.");
					menu();
				}

				if (source == 0) {
					// Incase option 2 is not selected before
					System.out.println("Select a source router:");
					source = scan.nextInt() - 1;
				}
				
				if (source >= noOfRouters) {
					// In case the user enters the source router number more than the
					// number of routers
					System.out.println("Please input a number between 1 to "
							+ noOfRouters);
					source=0;
				simulator(3);
				}
				if (destinationRouter ==0) {
					// In case option 3 is not selected before
					System.out.println("Select the destination router:");

					destinationRouter = scan.nextInt() - 1;

				}
				if (destinationRouter >= noOfRouters) {
					// In case the user enters the destination router number more than the
					// number of routers
					System.out.println("Please input a number between 1 to "
							+ noOfRouters);
					destinationRouter=0;
				simulator(3);
				}
			}
			catch (InputMismatchException ex) {
				// In case the user enters anything other than an integer
				System.out
						.println("You entered a bad input...please input a number between 1 to "
								+ noOfRouters);
				simulator(3);

			}

			if (source < noOfRouters && destinationRouter < noOfRouters) {

				if (distance[source][destinationRouter] != MAX_VALUE) {
					System.out.println("\nThe shortest path between R" + (source + 1)
							+ " and R" + (destinationRouter + 1) + "  is :  ");

					System.out.print("\n\n R" + (destinationRouter + 1));

					w = destinationRouter;

					while (w != source) {
						// print the path

						System.out.print(" <-- R" + (lastVisitedNode[source][w] + 1));

						w = lastVisitedNode[source][w];

					}
					// Print the cost

					System.out.println("\n Total cost is  : "
							+ distance[source][destinationRouter]);

					menu();
				}
			}
			else {
				System.out
						.println("There is no path between the source and destination..!!");
				menu();
			}

		}
		else if (scanChoice == 4) {
			if (noOfRouters == 0) {
				// If the initial topology is not read from the file
				System.out
						.println("Initial Network topology is not created! Choose option 1 first.");
				menu();
			}

			System.out.println(" Select a router to be removed:");
			try {
				removedRouter = scan.nextInt() - 1;
				if (removedRouter >= noOfRouters) {
					// If the router to be removed is greater than the no of routers
					System.out.println("We can remove routers only between 1 and "
							+ noOfRouters);
					simulator(4);
				}
				int[][] newInputMatrix = new int[noOfRouters - 1][noOfRouters - 1];
				for (int i = 0, p = 0; i < noOfRouters - 1 && p < noOfRouters; i++, p++) {
					for (int j = 0, q = 0; j < noOfRouters - 1 && q < noOfRouters; j++, q++) {
						// remove the router and create a matrix
						if (p == removedRouter)
							p++;
						if (q == removedRouter)
							q++;

						newInputMatrix[i][j] = inputNetwork[p][q];
					}

				}
				inputNetwork = new int[noOfRouters - 1][noOfRouters - 1];
				System.out.println("Modified topology");
				for (int i = 0; i < noOfRouters - 1; i++) {
					for (int j = 0; j < noOfRouters - 1; j++) {
						// Print the new topology

						inputNetwork[i][j] = newInputMatrix[i][j];
						System.out.print(inputNetwork[i][j] + " ");
					}
					System.out.println();

				}
				noOfRouters = noOfRouters - 1;
				distance = new int[noOfRouters][noOfRouters];
				costMatrix = new int[noOfRouters][noOfRouters];
				visitedNode = new int[noOfRouters][noOfRouters];
				lastVisitedNode = new int[noOfRouters][noOfRouters];

				// dijkstra for the new toplogy
				for (int i = 0; i < noOfRouters; i++) {
					for (int j = 0; j < noOfRouters; j++) {
						distance[i][j] = costMatrix[i][j] = inputNetwork[i][j];
						if (distance[i][j] < 0)
							distance[i][j] = costMatrix[i][j] = MAX_VALUE;
					}
				}

				// save the last visited node
				for (int i = 0; i < noOfRouters; i++) {
					for (int j = 0; j < noOfRouters; j++) {
						visitedNode[i][j] = 0;
						lastVisitedNode[i][j] = i;
					}
				}

				for (int n = 0; n < noOfRouters; n++) {
					visitedNode[n][n] = 1;
					for (int i = 0; i < noOfRouters; i++) {
						min = MAX_VALUE;
						for (w = 0; w < noOfRouters; w++) {
							if (visitedNode[n][w] == 0) {
								if (distance[n][w] < min) {
									visited = w;
									min = distance[n][w];
								}
							}
						}
						visitedNode[n][visited] = 1;
						// calculate the distance

						for (w = 0; w < noOfRouters; w++) {
							if (visitedNode[n][w] == 0) {
								if ((min + costMatrix[visited][w]) < distance[n][w]) {
									distance[n][w] = min + costMatrix[visited][w];
									lastVisitedNode[n][w] = visited;
								}
							}
						}
					}
				}
				flag = 1;
				// display the updated connection table
				simulator(2);

			}

			// If the user inputs anything other than numbers

			catch (InputMismatchException ex) {
				System.out
						.println("You entered a bad input...please input a number between 1 to "
								+ noOfRouters);
				simulator(4);

			}
			catch (Exception e) {
				// Any other exception, display the menu

				menu();
			}
		}

		else if (scanChoice == 5) {
			// End the program
			System.out.println("Good Bye!");
			System.exit(0);

		}
		else {
			System.out.println("Please enter your choice between 1 and 5.");
			menu();
		}
		// close the scanner
		scan.close();

	}

	public static void main(String[] args) {
		linkStateProtocol lspSimulator = new linkStateProtocol();
		// call the menu and process the user's choice
		lspSimulator.menu();
	}
}

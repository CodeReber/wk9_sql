
//import projects.dao.DbConnection;
   // DbConnection.getConnection();
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;



public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	
	//@formatter:off
	private List<String> operations = List.of(
				"1) Add a project",
				"2) List projects",
				"3) Select a project"
			);
		//enter point for java app	
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();		  
	  }
	//method prints the operations, gets a user menu selecti, and perfoms the requested operations. It repeats until the user 
	// request that the app terminate.
	private void processUserSelections() {
		boolean done = false;
		
		while(!done) {
			try {
				int selection = getUserSelection();
				switch(selection) {
				case -1:
					done = exitMenu();
					break;
					
				case 1:
					createProject();
					break;
					
				case 2:
					listProject();
					break;
					
				case 3:
					selectProject();
					break;
					
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
			}catch(Exception e) {
				System.out.println("\nErro: " + e + "Try again.");
			}
		}
	}
	
	//Gather user input for a project row then call the project service to create the row
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created projects: " + dbProject);
	}
	// select projects method
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		//Un select the current project
		curProject = null;
		// this will throw an exception if an invalid project ID is entered.
		curProject = projectService.fetchProjectById(projectId);
	}
	// list projects
	private void printOperations() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects: ");
		
		projects.forEach(project -> System.out.println(" " + project.getProjectId() + ": " + project.getProjectName()));
		
	}
	
	
	// Gets the user input from the console and converts it to a BigDecimal
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		try {
			//create the Bigdecimal oject and set it ot two decimal places
			return new BigDecimal(input).setScale(2);
		}catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}
	// Called when the user wants to exit the application.It prints a message and returns.
	private boolean exitMenu() {
		System.out.println("Exiting the menu. ");
		return true;
	}
	// method prints the available menu selection. It then gets the users menu selection from the console and converts it to int
	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("Enter a menu selection");
		return Objects.isNull(input) ? -1 : input;
	}
	//Prints a prompt on the console and then gets the users input from the console. It then converts the input to an Integer.
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		}catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}
	//Prints a prompt on the console and then gets the users input from the console. If the user enters nothing null is returned. otherwise
	//the trimmed input is returned
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}
	//Print the menu selections, one per line.
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		//with Lambda expression
		operations.forEach(line -> System.out.println(" " + line));
		//with enhanced for loop
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project");
		}else {
			System.out.println("\nYou are working with a project: " + curProject);
		}
	}
	

}

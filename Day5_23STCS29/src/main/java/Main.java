import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager dbManager = new DatabaseManager();
        EmployeeService service = new EmployeeService(dbManager.getDatabase());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        while (true) {
            System.out.println("\nEmployee Management Menu:");
            System.out.println("1. Add Employee");
            System.out.println("2. Update Employee");
            System.out.println("3. Delete Employee");
            System.out.println("4. Search Employee");
            System.out.println("5. Paginate Employees");
            System.out.println("6. Department Stats");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Department: ");
                    String dept = scanner.nextLine();
                    System.out.print("Skills (comma-separated): ");
                    List<String> skills = Arrays.asList(scanner.nextLine().split(","));
                    System.out.print("Joining Date (yyyy-MM-dd): ");
                    Date date = sdf.parse(scanner.nextLine());
                    Employee emp = new Employee(name, email, dept, skills, date);
                    if (service.addEmployee(emp)) {
                        System.out.println("Employee added.");
                    }
                }
                case 2 -> {
                    System.out.print("Email to update: ");
                    String email = scanner.nextLine();
                    Map<String, Object> updates = new HashMap<>();
                    System.out.print("New Department (or blank): ");
                    String dept = scanner.nextLine();
                    if (!dept.isEmpty()) updates.put("department", dept);
                    System.out.print("New Skills (comma-separated, or blank): ");
                    String skills = scanner.nextLine();
                    if (!skills.isEmpty()) updates.put("skills", Arrays.asList(skills.split(",")));
                    service.updateEmployee(email, updates);
                    System.out.println("Employee updated.");
                }
                case 3 -> {
                    System.out.print("Delete by (1) Email or (2) ID? ");
                    int opt = Integer.parseInt(scanner.nextLine());
                    if (opt == 1) {
                        System.out.print("Email: ");
                        service.deleteEmployeeByEmail(scanner.nextLine());
                    } else {
                        System.out.print("ID: ");
                        service.deleteEmployeeById(scanner.nextLine());
                    }
                }
                case 4 -> {
                    System.out.println("Search by: 1) Name 2) Department 3) Skill 4) Date Range");
                    int opt = Integer.parseInt(scanner.nextLine());
                    List<Document> results = new ArrayList<>();
                    switch (opt) {
                        case 1 -> {
                            System.out.print("Name contains: ");
                            results = service.searchByName(scanner.nextLine());
                        }
                        case 2 -> {
                            System.out.print("Department: ");
                            results = service.searchByDepartment(scanner.nextLine());
                        }
                        case 3 -> {
                            System.out.print("Skill: ");
                            results = service.searchBySkill(scanner.nextLine());
                        }
                        case 4 -> {
                            System.out.print("Start date (yyyy-MM-dd): ");
                            Date start = sdf.parse(scanner.nextLine());
                            System.out.print("End date (yyyy-MM-dd): ");
                            Date end = sdf.parse(scanner.nextLine());
                            results = service.searchByJoiningDateRange(start, end);
                        }
                    }
                    results.forEach(System.out::println);
                }
                case 5 -> {
                    System.out.print("Page number: ");
                    int page = Integer.parseInt(scanner.nextLine());
                    System.out.print("Sort by (name/joiningDate): ");
                    String sortBy = scanner.nextLine();
                    System.out.print("Ascending? (true/false): ");
                    boolean asc = Boolean.parseBoolean(scanner.nextLine());
                    List<Document> list = service.listPaginated(page, 5, sortBy, asc);
                    list.forEach(System.out::println);
                }
                case 6 -> {
                    List<Document> stats = service.departmentStats();
                    for (Document d : stats) {
                        System.out.println("Department: " + d.get("_id") + " | Count: " + d.get("count"));
                    }
                }
                case 0 -> {
                    dbManager.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}

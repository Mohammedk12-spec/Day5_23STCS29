import java.util.Date;
import java.util.List;

public class Employee {
    private String name;
    private String email;
    private String department;
    private List<String> skills;
    private Date joiningDate;

    public Employee(String name, String email, String department, List<String> skills, Date joiningDate) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.skills = skills;
        this.joiningDate = joiningDate;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public List<String> getSkills() { return skills; }
    public Date getJoiningDate() { return joiningDate; }
}

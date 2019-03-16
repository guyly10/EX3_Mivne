import java.util.LinkedList;
import java.util.List;

public class Department implements Comparable<Department> {
	private Node node;
	private String departmentName;
	private LinkedList<Employee> empList;

	public Department(String dpName) {
		this.departmentName = dpName;
		this.empList = new LinkedList<>();
		this.node = new Node(this);
	}

	public void takeVacation(Integer numOfEmployees) {

		while (numOfEmployees-- > 0) {
			System.out.println("Employee " + empList.peek() + " is taking a vacation");
			empList.add(empList.pop());
		}
	}

	public LinkedList<Employee> getEmployeeList() {
		return this.empList;
	}

	public void merge(Department dp) {
		// Add external employees to this department
		for (Employee emp : dp.getEmployeeList()) {
			this.addEmployee(emp);
		}

		setName(getName() + " & " + dp.getName());

	}

	public String getName() {
		return this.departmentName;
	}

	public void setName(String newName) {
		this.departmentName = newName;
	}

	/**
	 * Adds an employee to the departments' list
	 * 
	 * @param empName
	 * @return
	 */
	public Boolean addEmployee(String empName) {
		Employee emp = new Employee(empName);

		if (this.empList.contains(emp))
			return false;

		return this.empList.add(emp);
	}

	@Override
	public int compareTo(Department arg0) {
		return this.departmentName.compareTo(arg0.departmentName);
	}

	public void addEmployee(Employee emp) {
		empList.add(emp);
	}

	public void removeEmploye(Employee emp){
	    empList.remove(emp);
    }

	@Override
	public String toString() {

		String s = this.departmentName;
		s += " Children: " + (SpecialTree.getNumberOfChildren(getNode()) - 1);
		if (this.empList.size() == 0)
			return s;

		s += " (";
		for (Employee e : empList) {
			s += e + ", ";
		}
		s = s.substring(0, s.length() - 2);
		s += ")";

		return s;
	}

	public Node getNode() {
		return node;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departmentName == null) ? 0 : departmentName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!departmentName.equals(other.departmentName))
			return false;
		return true;
	}

}

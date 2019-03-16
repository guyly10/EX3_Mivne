import java.lang.reflect.Array;
import java.util.*;

class Node {
    Node right;
    Node left;
    Department data;
    Node myFather;

    /**
     * Basic constructor
     *
     * @param data
     */
    public Node(Department data) {
        this.data = data;
    }

    /**
     * Counts how many direct children are associated to this node
     *
     * @return
     */
    public Integer getCountDirectChildren() {
        if(this.right==null&&this.left==null)
            return 0;
        if(this.right!=null&&this.left!=null)
            return 2;
        return 1;
    }

    /**
     * Removes a child of a node if the node given is his child
     *
     * @param toRemove
     */
    public void removeChild(Node toRemove) {
        if (this.right == toRemove){
            this.right = null;
        }
        else if (this.left == toRemove){
            this.left = null;
        }
    }

    /**
     * Adds an employee to the department data
     *
     * @param empName
     * @return
     */
    public Boolean addEmployee(String empName) {
        System.out.println("Attempting to add employee " + empName + " to department " + this.data);
        return data.addEmployee(empName);
    }

    /**
     * Merges a given node to this node
     *
     * @param dpNode
     */
    public void merge(Node dpNode) {
        if (!this.myFather.data.getName().equals(dpNode.myFather.data.getName())){
            System.out.println("Could not merge departments (Both not under the same department).");
        }
        else if (this.getCountDirectChildren() + dpNode.getCountDirectChildren() > 2){
            System.out.println("Could not merge departments (they run more than 2 dep. together).");
        }
        else {
            this.data.merge(dpNode.data);
            if (dpNode.right != null){
                this.connectNode(dpNode.right);
            }
            if (dpNode.left !=null){
                this.connectNode(dpNode.left);
            }
            Node parent = dpNode.myFather;
            parent.removeChild(dpNode);
            System.out.println("Successfully merged " + this.data + " and " + dpNode.data);
        }
    }

    /**
     * Returns true if node has a father
     *
     * @return
     */
    public Boolean hasFather() {
        return myFather != null;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    /**
     * Returns wether or not the node has space for one more child at least
     *
     * @return
     */
    public Boolean hasSpace() {
        if (this.left == null || this.right == null) {
            return true;
        }
        return false;
    }

    /**
     * Connects a given node to this node as a child node
     *
     * @param n
     */
    public void connectNode(Node n) {
        if (this.left == null) {
            this.left = n;
        } else if (this.right == null) {
            this.right = n;
        }
    }

    /**
     * Sets the nodes' father
     *
     * @param father
     */
    public void setFather(Node father) {
        this.myFather = father;
    }

    /**
     * Excludes numOfEmps employees to vacation
     *
     * @param numOfEmps
     */
    public void takeVacation(Integer numOfEmps) {
        this.data.takeVacation(numOfEmps);
    }

}

public class SpecialTree {
    Company company;
    Node head;
    Map<Department, Node> nodeMap;

    /**
     * Basic constructor
     *
     * @param company
     */
    public SpecialTree(Company company) {
        this.company = company;
        nodeMap = new HashMap<>();
    }

    /**
     * Adds an employee to the given department name of this company
     *
     * @param depName
     * @param empName
     * @return
     */
    public Boolean addEmployee(String depName, String empName) {
        Department dp = new Department(depName);
        Node n = getDepartmentNode(dp);
        n.addEmployee(empName);
        return true;
    }

    /**
     * Adds a new node to the tree
     * Prints the road to the added node once generated
     *
     * @param InCharge
     * @param newDP
     * @return
     */
    public Boolean addDepartment(String InCharge, String newDP) {
        Stack<Node> road = new Stack<>();
        if (!InCharge.equals("null")) {
            Department dInChrg = new Department(InCharge);
            Department newDp = new Department(newDP);
            Node nIncharge = getDepartmentNode(dInChrg);
            if (nIncharge != null) {
                System.out.println("Found department node " + nIncharge);
                Node newNode = getDepartmentNode(newDp);
                newNode.setFather(nIncharge);
                nIncharge.connectNode(newNode);
                System.out.println("Setting node father " + nIncharge + " to " + newNode);
                System.out.println("Connected node " + newNode + " with father node " + nIncharge);
                getRoad(road, newNode);
                System.out.println();
            }

        } else {
            Department newDp = new Department(newDP);
            Node newNode = getDepartmentNode(newDp);
            this.head = newNode;
            newNode.setFather(null);
            System.out.print("Generated new department ");
            getRoad(road, newNode);
        }

        return true;
    }

    /**
     * Returns the number of children including himself
     *
     * @param n
     * @return
     */
    public static Integer getNumberOfChildren(Node n) {
        if (n == null) {
            return 0;
        }
        if (n.left == null && n.right == null) {
            return 1;
        } else {
            return getNumberOfChildren(n.left) + getNumberOfChildren(n.right) + 1;
        }
    }

    /**
     * Adjusts the stack to display the nodes' path
     *
     * @param road
     * @param n
     */
    public void getRoad(Stack<Node> road, Node n) {
        while(n!=null){
            road.push(n);
            n=n.myFather;
        }
        printRoad(road);
    }

    /**
     * Retrieve a department node based of the special tree
     * Nodes are saved in the nodemap as well for easier access
     *
     * @param dp
     * @return
     */
    public Node getDepartmentNode(Department dp) {
        Node n = nodeMap.get(dp);
        if (n == null) {
            n = dp.getNode();
        }
        if (!nodeMap.containsKey(dp)){
            nodeMap.put(dp, n);
        }
        return n;
    }

    /**
     * Prints the road map given as a stack
     *
     * @param road
     */
    public void printRoad(Stack<Node> road) {
        if (road.size() == 1) {
            System.out.println("node head - " + road.pop().toString());
        }
        String s = "";
        while(!road.isEmpty()){
            s+= road.pop().toString();
            if(!road.isEmpty())
                s+=" -> ";
        }
        System.out.print(s);
    }

    /**
     * Prints the tree by using pre-order traversal
     *
     * @param head
     */
    public void printTree(Node head) {
        if (head == null) {
            return;
        }
        System.out.println(head.data);
        printTree(head.left);
        printTree(head.right);
    }

    /**
     * Returns the trees' head
     *
     * @return
     */
    public Node getHead() {
        return head;
    }

    /**
     * Release number of employees for a vacation, to the relevant department
     *
     * @param dpName
     * @param numOfEmployees
     */
    public void takeVacation(String dpName, String numOfEmployees) {
        Department dp = new Department(dpName);
        Node n = getDepartmentNode(dp);
        n.takeVacation(Integer.parseInt(numOfEmployees));
    }

    /**
     * Merges two given departments
     *
     * @param dp1
     * @param dp2
     */
    public void mergeDepartments(String dp1, String dp2) {
        Department mergeDp1 = new Department(dp1);
        Department mergeDp2 = new Department(dp2);
        Node n1 = getDepartmentNode(mergeDp1);
        Node n2 = getDepartmentNode(mergeDp2);
        n1.merge(n2);
    }

    /**
     * Separates two departments
     *In case a department doesn't have brothers, the department is splitted, so the original and the new departments will contains each half of the employees.
     *In case that the department has a brother and, assuming that the bigger department is splitted,
     * the employees will be splitted between the two departments so that each department will contain half of the employees (+1 in case of odd number)
     * @param dp
     */
    public void separateDepartments(String dp){
        Department separateDp = new Department(dp);
        Node n = getDepartmentNode(separateDp);
        Node nParent = n.myFather;
        LinkedList<Employee> empList = new LinkedList<>();
        empList = n.data.getEmployeeList();
        int numOfEmp = n.data.getEmployeeList().size();
        if (n.right == null && n.left == null){
            if (nParent.left == null || nParent.right == null) {
                String depName = nParent.data.getName() + "1";
                addDepartment(nParent.data.getName(), depName);
                Department newSeparatedDp = new Department(depName);
                Node newSepDp = getDepartmentNode(newSeparatedDp);
                calculateEmployees(n, empList, numOfEmp, newSepDp);
            }
            else {
                if (nParent.right.equals(n)){
                    Node nLeft = nParent.left;
                    int gap = (n.data.getEmployeeList().size() - nLeft.data.getEmployeeList().size()) / 2;
                    calculateEmployees(n, empList, gap, nLeft);
                } else {
                    Node nRight = nParent.right;
                    int gap = (n.data.getEmployeeList().size() - nRight.data.getEmployeeList().size()) / 2;
                    calculateEmployees(n, empList, gap, nRight);
                }
            }
        }

    }

    /**
     * Separates two departments
     *Checks whether the number of employees is odd or not.
     * @param n, emplist, numOfEmp, newSepDp
     */
    private void calculateEmployees(Node n, LinkedList<Employee> empList, int numOfEmp, Node newSepDp) {
        if (numOfEmp % 2 == 0) {
            balanceEmployees(n, empList, numOfEmp, newSepDp);
        } else {
            balanceEmployees(n, empList, numOfEmp + 1, newSepDp);
        }
    }

    /**
     * Transfers employees from one department to the other.
     * @param n, emplist, numOfEmp, newSepDp
     */
    private void balanceEmployees(Node n, LinkedList<Employee> empList, int numOfEmp, Node newSepDp) {
        for (int i = 0; i < numOfEmp / 2; i++) {
            Employee emp = empList.get(i);
            newSepDp.addEmployee(emp.toString());
            n.data.removeEmploye(emp);
        }
    }
}

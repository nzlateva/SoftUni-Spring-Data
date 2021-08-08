import entities.*;

import javax.persistence.*;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        System.out.println("Enter exercise number:");

        try {
            int exNum = Integer.parseInt(this.reader.readLine());

            switch (exNum) {
                case 2 -> changeCasing();
                case 3 -> isEmployeePresentInDatabase();
                case 4 -> getEmployeesWithSalaryOver50000();
                case 5 -> getEmployeesFromDepartment();
                case 6 -> setNewAddressToEmployee();
                case 7 -> getAddressesByEmployeeCount();
                case 8 -> getEmployeeWithProject();
                case 9 -> findLatestTenProjects();
                case 10 -> increaseSalaries();
                case 11 -> findEmployeesByFirstName();
                case 12 -> findMaxSalaryOfDepartments();
                case 13 -> removeTown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }


    private void changeCasing() {
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("UPDATE Town t " +
                "SET t.name = upper(t.name) " +
                "WHERE length(t.name) <= 5 ");

        int affectedRows = query.executeUpdate();
        System.out.println(affectedRows + " town names were changed.");

        entityManager.getTransaction().commit();
    }

    private void isEmployeePresentInDatabase() throws IOException {
        System.out.println("Enter employee full name:");
        String[] tokens = reader.readLine().split("\\s+");

        Long count = entityManager.createQuery("SELECT COUNT(e.id) FROM Employee e " +
                "WHERE e.firstName = :firstName AND e.lastName = :lastName", Long.class)
                .setParameter("firstName", tokens[0])
                .setParameter("lastName", tokens[1])
                .getSingleResult();

        System.out.println(count == 0 ? "No" : "Yes");
    }

    private void getEmployeesWithSalaryOver50000() {

        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.salary > :min_salary", Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void getEmployeesFromDepartment() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.department.name = :d_name " +
                "ORDER BY e.salary, e.id", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s from %s - $%s%n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getDepartment().getName(),
                        e.getSalary()));
    }

    private void setNewAddressToEmployee() throws IOException {
        System.out.println("Enter Employee's last name:");
        String lastName = reader.readLine();

        Employee employee = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.lastName = :lastName", Employee.class)
                .setParameter("lastName", lastName)
                .getSingleResult();

        Address address = createAddress("Vitoshka 15");
        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private Address createAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void getAddressesByEmployeeCount() {
        entityManager.createQuery("SELECT a FROM Address a " +
                "ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultStream()
                .forEach(a -> System.out.printf("%s, %s - %d employees%n",
                        a.getText(),
                        a.getTown() == null
                                ? "Unknown"
                                : a.getTown().getName(),
                        a.getEmployees().size()));

    }

    private void getEmployeeWithProject() throws IOException {
        System.out.println("Enter Employee ID:");
        int id = Integer.parseInt(reader.readLine());

        Employee employee = entityManager.find(Employee.class, id);

        System.out.printf("%s %s - %s%n%s",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getJobTitle(),
                employee.getProjects()
                        .stream()
                        .sorted(Comparator.comparing(Project::getName))
                        .map(p -> "\t" + p.getName())
                        .collect(Collectors.joining(System.lineSeparator())));


    }

    private void findLatestTenProjects() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        entityManager.createQuery("SELECT p FROM Project p " +
                "ORDER BY p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultStream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.println(
                        String.format("Project name: %s" + System.lineSeparator() +
                                        " \tProject Description: %s" + System.lineSeparator() +
                                        " \tProject Start Date: %s" + System.lineSeparator() +
                                        " \tProject End Date: %s",
                                p.getName(),
                                p.getDescription(),
                                p.getStartDate().format(dtf),
                                p.getEndDate()
                        )));
    }

    private void increaseSalaries() {
        entityManager.getTransaction().begin();

        entityManager.createQuery("UPDATE Employee e " +
                "SET e.salary = e.salary * 1.12 " +
                "WHERE e.department.id IN :departments")
                .setParameter("departments", Set.of(1, 2, 4, 11))
                .executeUpdate();

        entityManager.getTransaction().commit();

        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.department.id IN :departments", Employee.class)
                .setParameter("departments", Set.of(1, 2, 4, 11))
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s ($%s)%n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getSalary()));

    }

    private void findEmployeesByFirstName() throws IOException {
        System.out.println("Enter search pattern:");
        String pattern = reader.readLine();

        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.firstName LIKE CONCAT(:pattern, '%')", Employee.class)
                .setParameter("pattern", pattern)
                .getResultStream()
                .forEach(e -> System.out.printf(
                        "%s %s - %s - ($%s)%n",
                        e.getFirstName(),
                        e.getLastName(),
                        e.getJobTitle(),
                        e.getSalary()));
    }

    private void findMaxSalaryOfDepartments() {

        entityManager
                .createQuery("SELECT d FROM Department d " +
                        "JOIN d.employees e " +
                        "GROUP BY d.id " +
                        "HAVING MAX (e.salary) NOT BETWEEN 30000 AND 70000", Department.class)
                .getResultStream()
                .forEach(d -> System.out.printf(
                        "%s %s%n",
                        d.getName(),
                        d.getEmployees()
                                .stream()
                                .map(Employee::getSalary)
                                .max(Comparator.naturalOrder())
                                .orElseGet(() -> BigDecimal.valueOf(0))
                ));

    }

    private void removeTown() throws IOException {
        System.out.println("Enter town name:");
        String townName = reader.readLine();

        Town town = entityManager.createQuery("SELECT t FROM Town t " +
                "WHERE t.name = :name", Town.class)
                .setParameter("name", townName)
                .getSingleResult();

        int deletedAddressesCnt = deleteAddressesByTownId(town.getId());

        entityManager.getTransaction().begin();
        entityManager.remove(town);
        entityManager.getTransaction().commit();

        System.out.printf("%d %s in %s deleted",
                deletedAddressesCnt,
                deletedAddressesCnt > 1 ? "addresses" : "address",
                town.getName());
    }

    private int deleteAddressesByTownId(Integer id) {

        List<Address> addresses = entityManager
                .createQuery("SELECT a FROM Address a " +
                        "WHERE a.town.id = :townId", Address.class)
                .setParameter("townId", id)
                .getResultList();

        // all managers living on given address
        // should first be removed as manager_id from their subordinates and departments
        entityManager.getTransaction().begin();
        addresses.forEach(a -> removeManagersByAddressId(a.getId()));
        entityManager.getTransaction().commit();

        // in order to be able to cascade delete all employees (managers) on the given address
        // together with the address deletion
        entityManager.getTransaction().begin();
        addresses.forEach(entityManager::remove);
        entityManager.getTransaction().commit();

        return addresses.size();
    }

    private void removeManagersByAddressId(Integer id) {
        List<Employee> managers = entityManager
                .createQuery("SELECT e FROM Employee e " +
                        "WHERE e.address.id = :addressId", Employee.class)
                .setParameter("addressId", id)
                .getResultList();

        managers.forEach(m -> removeSubordinatesByManagerId(m.getId()));
        managers.forEach(m -> removeDepartmentsByManagerId(m.getId()));
    }

    private void removeSubordinatesByManagerId(Integer id) {
        entityManager
                .createQuery("UPDATE Employee e " +
                        "SET e.manager.id = null " +
                        "WHERE e.manager.id = :employee_id")
                .setParameter("employee_id", id)
                .executeUpdate();

    }

    private void removeDepartmentsByManagerId(Integer id) {
        entityManager
                .createQuery("UPDATE Department d " +
                        "SET d.manager.id = null " +
                        "WHERE d.manager.id = :employee_id")
                .setParameter("employee_id", id)
                .executeUpdate();
    }
}

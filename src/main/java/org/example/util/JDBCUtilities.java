package org.example.util;

import org.example.model.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JDBCUtilities {

    public static Connection connection = null;
    public static Employee currentEmployee;

    public static void connect() {
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GYM", properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> selectAllCliens() {
        List<User> users = new ArrayList<>();

        String q = "SELECT * FROM GymUser";

        try (PreparedStatement statement = connection.prepareStatement(q);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String notes = rs.getString("notes");

                User user = new User(id, firstName, lastName, dateOfBirth, notes);
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public static Employee selectEmployee(String userName, String pass) {
        String q = "SELECT * FROM Employee WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(q)) {
            statement.setString(1, userName);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");

                    if (!password.equals(pass)) {
                        return null;
                    }

                    int id = rs.getInt("employee_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                    boolean isAdmin = rs.getBoolean("admin");
                    String phoneNumber = rs.getString("phoneNumber");

                    return new Employee(id, firstName, lastName, dateOfBirth, userName, isAdmin, phoneNumber);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static boolean addNewUser(String firstName, String lastName, LocalDate dateOfBirth, String notes) {
        String checkQuery = "SELECT COUNT(*) FROM GymUser WHERE first_name = ? AND last_name = ? AND date_of_birth = ?";
        String insertQuery = "INSERT INTO GymUser (first_name, last_name, date_of_birth, notes) VALUES (?, ?, ?, ?)";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery)
        ) {
            checkStmt.setString(1, firstName);
            checkStmt.setString(2, lastName);
            checkStmt.setDate(3, Date.valueOf(dateOfBirth));

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            insertStmt.setString(1, firstName);
            insertStmt.setString(2, lastName);
            insertStmt.setDate(3, Date.valueOf(dateOfBirth));
            insertStmt.setString(4, notes);

            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Greška pri dodavanju korisnika: " + e.getMessage());
        }
    }

    public static List<Employee> selectAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee";

        try (
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String phoneNumber = rs.getString("phoneNumber");
                String userName = rs.getString("username");
                boolean isAdmin = rs.getBoolean("admin");

                Employee employee = new Employee(id, firstName, lastName, dateOfBirth, userName, isAdmin, phoneNumber);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom učitavanja zaposlenih: " + e.getMessage());
        }

        return employees;
    }

    public static int addSubscription(String type, int price, boolean treadmill, LocalDate start, LocalDate end, int userId, int employeeId) {
        String sql = "INSERT INTO subscription (type, price, treadmill_included, start_date, end_date, gym_user_id, employee_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, type);
            stmt.setInt(2, price);
            stmt.setBoolean(3, treadmill);
            stmt.setDate(4, Date.valueOf(start));
            stmt.setDate(5, Date.valueOf(end));
            stmt.setInt(6, userId);
            stmt.setInt(7, employeeId);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID nove pretplate
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // greška
    }



    public static List<Subscription> selectAllSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        String query = """
            SELECT s.*
            FROM Subscription s
            LEFT JOIN Payment p ON s.subscription_id = p.subscription_id
            GROUP BY s.subscription_id
            HAVING s.end_date >= CURRENT_DATE
            OR COALESCE(SUM(p.amount_paid), 0) < s.price
        """;

//        String query = "SELECT * FROM SUBSCRIPTION";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("subscription_id");
                String type = rs.getString("type");
                int price = rs.getInt("price");
                boolean treadmill = rs.getBoolean("treadmill_included");
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                int userId = rs.getInt("gym_user_id");
                int employeeId = rs.getInt("employee_id");

                User user = getUserById(userId);
                Employee employee = getEmployeeById(employeeId);

                Subscription subscription = new Subscription(id, type, price, treadmill, startDate, endDate, user, employee);

                List<Payment> payments = getPaymentsBySubscriptionId(id, subscription);
                subscription.setPayments(payments);

                subscriptions.add(subscription);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subscriptions;
    }

    public static List<Payment> getPaymentsBySubscriptionId(int subscriptionId, Subscription subscription) {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM Payment WHERE subscription_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, subscriptionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int paymentId = rs.getInt("payment_id");
                int userId = rs.getInt("user_id");
                int employeeId = rs.getInt("employee_id");
                LocalDate date = rs.getDate("payment_date").toLocalDate();
                int amount = rs.getInt("amount_paid");

                User user = getUserById(userId);
                Employee employee = getEmployeeById(employeeId);

                Payment payment = new Payment(user, subscription, employee, date, amount);
                payments.add(payment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    public static boolean deleteUserAndDependencies(User user) {
        String deletePayments = "DELETE FROM Payment WHERE subscription_id IN (SELECT subscription_id FROM Subscription WHERE gym_user_id = ?)";
        String deleteMembershipHistory = "DELETE FROM MembershipHistory WHERE subscription_id IN (SELECT subscription_id FROM Subscription WHERE gym_user_id = ?)";
        String deleteSubscriptions = "DELETE FROM Subscription WHERE gym_user_id = ?";
        String deleteUser = "DELETE FROM GymUser WHERE user_id = ?";

        Connection conn = null;

        try {
            conn = connection;
            conn.setAutoCommit(false);

            try (
                    PreparedStatement ps1 = conn.prepareStatement(deletePayments);
                    PreparedStatement ps2 = conn.prepareStatement(deleteMembershipHistory);
                    PreparedStatement ps3 = conn.prepareStatement(deleteSubscriptions);
                    PreparedStatement ps4 = conn.prepareStatement(deleteUser)
            ) {
                ps1.setInt(1, user.getId());
                ps1.executeUpdate();

                ps2.setInt(1, user.getId());
                ps2.executeUpdate();

                ps3.setInt(1, user.getId());
                ps3.executeUpdate();

                ps4.setInt(1, user.getId());
                ps4.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean addPayment(int userId, int subscriptionId, int employeeId, LocalDate date, int amount) {
        String sql = "INSERT INTO payment (user_id, subscription_id, employee_id, payment_date, amount_paid) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, subscriptionId);
            stmt.setInt(3, employeeId);
            stmt.setDate(4, Date.valueOf(date));
            stmt.setInt(5, amount);

            stmt.executeUpdate();
            System.out.println("Payment successfully added.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static Employee getEmployeeById(int id) {
        String q = "SELECT * FROM Employee WHERE employee_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phoneNumber = rs.getString("phoneNumber");
                String userName = rs.getString("username");
                boolean isAdmin = rs.getBoolean("admin");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();

                return new Employee(id, firstName, lastName, dateOfBirth, userName, isAdmin, phoneNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Payment> getPaymentsForSubscription(int subscriptionId) {
        List<Payment> payments = new ArrayList<>();

        String query = "SELECT p.*, " +
                "u.user_id AS user_id, u.first_name AS userFirstName, u.last_name AS userLastName, " +
                "u.date_of_birth AS userDateOfBirth, u.notes AS userNotes, " +
                "e.employee_id AS emp_id, e.first_name AS empFirstName, e.last_name AS empLastName, " +
                "e.date_of_birth AS empDateOfBirth, e.username AS empUsername, " +
                "e.admin AS empAdmin, e.phoneNumber AS empPhoneNumber " +
                "FROM Payment p " +
                "JOIN GymUser u ON p.user_id = u.user_id " +
                "JOIN Employee e ON p.employee_id = e.employee_id " +
                "WHERE p.subscription_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, subscriptionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("userFirstName"),
                        rs.getString("userLastName"),
                        rs.getDate("userDateOfBirth").toLocalDate(),
                        rs.getString("userNotes")
                );

                Employee emp = new Employee(
                        rs.getInt("emp_id"),
                        rs.getString("empFirstName"),
                        rs.getString("empLastName"),
                        rs.getDate("empDateOfBirth").toLocalDate(),
                        rs.getString("empUsername"),
                        rs.getBoolean("empAdmin"),
                        rs.getString("empPhoneNumber")
                );

                LocalDate date = rs.getDate("payment_date").toLocalDate();
                int amount = rs.getInt("amount_paid");

                Payment payment = new Payment(user, null, emp, date, amount);
                payments.add(payment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return payments;
    }

    public static MembershipHistory addMembershipHistory(User user, int month, int year, Subscription subscription, Employee employee) {
        String sql = "INSERT INTO MembershipHistory (user_id, month, year, subscription_id, employee_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, user.getId());
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            stmt.setInt(4, subscription.getId());
            stmt.setInt(5, employee.getId());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        MembershipHistory history = new MembershipHistory(user, month, year, subscription, employee);
                        return history;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean deleteEmployeeById(int id) {
        try {
            String sql1 = "UPDATE Payment SET employee_id = NULL WHERE employee_id = ?";
            String sql2 = "UPDATE Subscription SET employee_id = NULL WHERE employee_id = ?";

            try (PreparedStatement pstmt1 = connection.prepareStatement(sql1);
                 PreparedStatement pstmt2 = connection.prepareStatement(sql2)) {

                pstmt1.setInt(1, id);
                pstmt1.executeUpdate();

                pstmt2.setInt(1, id);
                pstmt2.executeUpdate();
            }

            String sql = "DELETE FROM employee WHERE employee_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int affected = pstmt.executeUpdate();
                return affected > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static List<MembershipHistory> getMembershipHistoryForUser(int userId) {
        List<MembershipHistory> historyList = new ArrayList<>();

        String query = """
            SELECT mh.*, 
                   e.employee_id AS emp_id, e.first_name AS emp_first, e.last_name AS emp_last, 
                   s.subscription_id AS sub_id, s.type AS sub_type, s.price AS sub_price
            FROM MembershipHistory mh
            JOIN employee e ON mh.employee_id = e.employee_id
            JOIN subscription s ON mh.subscription_id = s.subscription_id
            WHERE mh.user_id = ?
            ORDER BY mh.year DESC, mh.month DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("emp_id"));
                emp.setFirstName(rs.getString("emp_first"));
                emp.setLastNmae(rs.getString("emp_last"));

                Subscription sub = new Subscription();
                sub.setId(rs.getInt("sub_id"));
                sub.setName(rs.getString("sub_type"));
                sub.setPrice(rs.getInt("sub_price"));

                // MembershipHistory
                MembershipHistory mh = new MembershipHistory();
                //mh.set(rs.getInt("id"));
                mh.setUser(getUserById(userId));
                mh.setEmployee(emp);
                mh.setSubscription(sub);
                mh.setMonth(rs.getInt("month"));
                mh.setYear(rs.getInt("year"));

                historyList.add(mh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }




    public static boolean insertEmployee(Employee emp, String password) {
        String sql = "INSERT INTO employee (first_name, last_name, date_of_birth, username, password, admin, phoneNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastNmae());
            stmt.setDate(3, Date.valueOf(emp.getDateOfBirth()));
            stmt.setString(4, emp.getUserName());
            stmt.setString(5, password);
            stmt.setBoolean(6, emp.isAdmin());
            stmt.setString(7, emp.getPhoneNumber());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    emp.setId(generatedId);
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, MonthStats> getStatsByMonths() {
        Map<String, MonthStats> mapa = new HashMap<>();

        String sql = "SELECT DATE_FORMAT(start_date, '%Y-%m') AS month, " +
                "COUNT(DISTINCT gym_user_id) AS count_users, " +
                "SUM(price) AS sum_profit " +
                "FROM Subscription " +
                "WHERE start_date >= DATE_SUB(CURDATE(), INTERVAL 11 MONTH) " +
                "GROUP BY month ORDER BY month";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String mesec = rs.getString("month");
                int num = rs.getInt("count_users");
                int profit = rs.getInt("sum_profit");

                mapa.put(mesec, new MonthStats(mesec, num, profit));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mapa;
    }

    public static LocalDate getLatestSubscriptionEndDate(int userId) {
        String sql = "SELECT MAX(end_date) FROM Subscription WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Date date = rs.getDate(1);
                return date != null ? date.toLocalDate() : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<MonthStats> getLast12Months() {
        Map<String, MonthStats> podaci = getStatsByMonths();
        List<MonthStats> rezultat = new ArrayList<>();

        LocalDate danas = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int i = 11; i >= 0; i--) {
            String mesec = danas.minusMonths(i).format(formatter);

            MonthStats stat = podaci.getOrDefault(
                    mesec,
                    new MonthStats(mesec, 0, 0)
            );

            rezultat.add(stat);
        }

        return rezultat;
    }




    public static User getUserById(int id) {
        String q = "SELECT * FROM GymUser WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String notes = rs.getString("notes");

                return new User(userId, firstName, lastName, dateOfBirth, notes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}

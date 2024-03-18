import java.sql.*;
import java.util.Scanner;

public class Main {
    static Connection connection;
    public static void main(String[] args) {
        // Setting up connection and login credentials
        String url = "jdbc:postgresql://localhost:5432/comp3005_a3";
        String user = "postgres";
        String password = "Hk@7889348303";

        try {
            connection = DriverManager.getConnection(url, user, password);
            Scanner myObj = new Scanner(System.in);

            // Creating a main menu and setting up user input for all options
            int option = -1;
            while(option != 0) {
                System.out.println("Select an option");
                System.out.println("[1] View all students");
                System.out.println("[2] Add a new student");
                System.out.println("[3] Update a student");
                System.out.println("[4] Delete a student");
                System.out.println("[0] Exit");
                option = myObj.nextInt();
                switch (option) {
                    case 1:
                        getAllStudents();
                        break;
                    case 2:
                        // Getting all attributes needed to create a new student
                        System.out.println("Enter the students information in the following format:");
                        System.out.println("first_name last_name email YYYY-MM-DD");
                        // Getting all attributes from the same line
                        String fname = myObj.next();
                        String lname = myObj.next();
                        String email = myObj.next();
                        String date = myObj.next();
                        Date edate = java.sql.Date.valueOf(date);

                        addStudent(fname, lname, email, edate);
                        break;
                    case 3:
                        // Getting all attributes needed to update a student's email
                        System.out.println("Enter the students information in the following format:");
                        System.out.println("student_id new_email");
                        int id = myObj.nextInt();
                        email = myObj.next();

                        updateStudentEmail(id, email);
                        break;
                    case 4:
                        // Getting all attributes needed to delete a student
                        System.out.println("Enter the student's ID");
                        id = myObj.nextInt();
                        deleteStudent(id);
                        break;
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Gets all columns and all students
    public static void getAllStudents() {
        try{
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * from students");
            ResultSet results = statement.getResultSet();
            // Prints each attribute of a student seperated by a tab
            while (results.next()) {
                System.out.print(results.getString("student_id") + "\t");
                System.out.print(results.getString("first_name") + "\t");
                System.out.print(results.getString("last_name") + "\t");
                System.out.print(results.getString("email") + "\t");
                System.out.println(results.getString("enrollment_date"));
            }
            results.close();
            statement.close();
        } catch (SQLException e) {}
    }

    // Adding a student with the given arguments
    public static void addStudent(String first_name, String last_name, String email, Date date) {
        try{
            Statement statement = connection.createStatement();
            String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
            // Creating a prepared statement for security
            try(PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setString(1, first_name);
                pstmt.setString(2, last_name);
                pstmt.setString(3, email);
                pstmt.setDate(4, date);
                pstmt.executeUpdate();
                System.out.println("Added new student");
                pstmt.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {}
    }

    // Updating a given student's email
    public static void updateStudentEmail(int student_id, String new_email) {
        try{
            Statement statement = connection.createStatement();
            String insertSQL = "UPDATE students SET email=? WHERE student_id=?";
            // Creating a prepared statement for security
            try(PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setString(1, new_email);
                pstmt.setInt(2, student_id);
                pstmt.executeUpdate();
                System.out.println("Updated the student's email");
                pstmt.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {}
    }

    // Deleting the selected student
    public static void deleteStudent(int student_id) {
        try{
            Statement statement = connection.createStatement();
            String insertSQL = "DELETE FROM students WHERE student_id=?";
            // Creating a prepared statement for security
            try(PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setInt(1, student_id);
                pstmt.executeUpdate();
                System.out.println("Deleted the student");
                pstmt.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {}
    }
}

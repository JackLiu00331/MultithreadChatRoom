package Database;

import Model.User;
import Util.PasswordUtil;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:ChatRoom-Server/src/main/resources/database.sqlite";
    private static Connection connection;

    public static void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTablesIfNotExist();
            System.out.println("DB - Database connected successfully.");
        } catch (SQLException e) {
            System.err.println("DB - Database connection failed: " + e.getMessage());
        }
    }

    private static void createTablesIfNotExist() {
        String createTableSQL = """
                        create table users(
                        id INTEGER primary key AUTOINCREMENT,
                        username TEXT not null unique,
                        nickname TEXT not null,
                        password_hash TEXT not null,
                        registered_time TEXT,
                        last_login_time TEXT
                    )
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("DB - User table created successfully");
        } catch (SQLException e) {
            System.err.println("DB - Table creation fails :" + e.getMessage());
        }
    }


    public static boolean registerUser(User sender) {
        String insertSQL = "INSERT INTO users (username, nickname, password_hash, registered_time) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            String passwordHash = PasswordUtil.hashPassword(sender.getPassword());
            stmt.setString(1, sender.getUsername());
            stmt.setString(2, sender.getNickname());
            stmt.setString(3, passwordHash);
            stmt.setString(4, sender.getRegisteredTime().toString());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if(e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("DB - Registration failed: Username " + sender.getUsername() + " already exists.");
            } else {
                System.err.println("DB - Registration failed: " + e.getMessage());
            }
            return false;
        }
    }

    public static boolean loginUser(User sender) {
        String validateSql = "SELECT * FROM users where username = ?";
        String updateSql = "UPDATE users SET last_login_time = ? where username = ?";

        try(PreparedStatement selectStmt = connection.prepareStatement(validateSql)){
            selectStmt.setString(1,sender.getUsername());
            ResultSet rs = selectStmt.executeQuery();

            if(rs.next()){
                String storedHashPassword = rs.getString("password_hash");
                if(PasswordUtil.verifyPassword(sender.getPassword(),storedHashPassword)){
                    PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                    updateStmt.setString(1, sender.getLastLoginTime().toString());
                    updateStmt.setString(2, sender.getUsername());
                    updateStmt.executeUpdate();
                    return true;
                }
            }
            return false;
        }catch (SQLException e){
            System.err.println("DB - Unable to login: " + e.getMessage());
            return false;
        }
    }
}

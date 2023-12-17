package ExpDevices.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ExpDevices.entity.Device;

public class DeviceDataset {
    private static Set<Device> devices;
    private static String sqlReq;
    private static ResultSet res;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://wsl.localhost:3306/ExpDev";
    private static final String USER = "ExpDev";
    private static final String PASSWD = "Dev@0224";

    private static Connection connection = null;
    private static Statement statement = null;

    public static Set<Device> getDevices() {
        devices = new HashSet<Device>();

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库…");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWD);
            statement = connection.createStatement();
            sqlReq = "SELECT * FROM devices;";
            res = statement.executeQuery(sqlReq);

            while (res.next()) {
                String id = res.getString("id");
                String name = res.getString("name");
                String who = res.getString("who");
                String type = res.getString("type");
                boolean isBorrowed = res.getBoolean("isBorrowed");
                boolean isDeprecated = res.getBoolean("isDeprecated");
                devices.add(new Device(id, name, who, type, isBorrowed, isDeprecated));
            }

            res.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Class 未找到。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL 错误。");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("SQL 错误。");
                e.printStackTrace();
            }
        }

        return devices;
    }

    public static void setDevices(Set<Device> devices) {
        DeviceDataset.devices = devices;
    }

}

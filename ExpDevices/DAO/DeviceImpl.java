package ExpDevices.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ExpDevices.entity.Device;

public class DeviceImpl implements IDeviceDAO {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://wsl.localhost:3306/ExpDev";
    private final String USER = "ExpDev";
    private final String PASSWD = "Dev@0224";

    private Set<Device> devices;
    private String sqlReq;
    private ResultSet res;
    private Connection connection = null;
    private Statement statement = null;


    @Override
    public Set<Device> getDevices() {
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

    @Override
    public Device getDeviceByID(String id) {
        Device device = null;

        for (Device d : devices) {
            if (d.getId().endsWith(id)) {
                device = d;
            }
        }

        return device;
    }

    @Override
    public boolean add(Device device) {
        devices.add(device);
        this.setDevices(devices);
        return true;
    }

    @Override
    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

}
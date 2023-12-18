package ExpDevices.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import ExpDevices.entity.Device;

public class DeviceImpl implements IDeviceDAO {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String HOST = "wsl.localhost";
    private final long PORT = 3306;
    private final String DB_NAME = "ExpDev";
    private final String OPTIONS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + OPTIONS;
    private final String USER = "ExpDev";
    private final String PWD = "Dev@0224";

    private Set<Device> devices = new HashSet<Device>();
    private String sqlReq;
    private ResultSet res;
    private Connection connection = null;
    private Statement statement = null;
    private static DeviceImpl deviceImpl;

    private DeviceImpl() {
    }

    public static DeviceImpl getDeviceImpl() {
        if (deviceImpl == null) {
            deviceImpl = new DeviceImpl();
        }
        return deviceImpl;
    }
    @Override
    public Set<Device> getDevices() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库…");
            connection = DriverManager.getConnection(DB_URL, USER, PWD);
            statement = connection.createStatement();
            System.out.println("正在查询…");
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
        if (devices.isEmpty()) {
            devices = getDevices();
        }

        for (Device d : devices) {
            if (d.getId().endsWith(id)) {
                device = d;
            }
        }

        return device;
    }

    @Override
    public boolean add(Device device) {
        boolean noDuplicateEntry = true;

        for (Device d : devices) {
            if (d.getId().equals(device.getId())) {
                noDuplicateEntry = false;
                break;
            }
        }
        if (noDuplicateEntry) {
            devices.add(device);
        }

        return noDuplicateEntry;
    }

    @Override
    public boolean setDevices(Set<Device> devices) {
        boolean status = true;
        this.devices = devices;

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库…");
            connection = DriverManager.getConnection(DB_URL, USER, PWD);
            statement = connection.createStatement();

            for (Device device : devices) {
                String id = device.getId();
                String name = device.getName();
                String who = device.getWho();
                String type = device.getType();
                boolean isBorrowed = device.isBorrowed();
                boolean isDeprecated = device.isDeprecated();
                sqlReq = "INSERT INTO devices (`id`, `name`, `who`, `type`, `isBorrowed`, `isDeprecated`) VALUES ('"
                        + id + "', '" + name + "', '" + who + "', '" + type + "', '" + (isBorrowed ? 0 : 1) + "', '"
                        + (isDeprecated ? 0 : 1) + "');";
                System.out.println(id + ": status: " + statement.executeUpdate(sqlReq));
            }

            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Class 未找到。");
            status = false;
            e.printStackTrace();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("违反 SQL 完整性约束");
            status = false;
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL 错误。");
            status = false;
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
        return status;
    }

}
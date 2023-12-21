package ExpDevices.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ExpDevices.entity.Database;
import ExpDevices.entity.Device;

public class DeviceImpl implements IDeviceDAO {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private String host = "wsl.localhost";
    private long port = 3306;
    private String DB_name = "ExpDev";
    private final String OPTIONS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private String DB_url = "jdbc:mysql://" + host + ":" + port + "/" + DB_name + OPTIONS;
    private String user = "ExpDev";
    private String pwd = "Dev@0224";
    private final String[] DB_FIELD = { "id", "name", "who", "type", "isBorrowed", "isDeprecated" };

    private Set<Device> devices;
    private String sqlReq;
    private ResultSet res;
    private Connection connection = null;
    private Statement statement = null;
    private static DeviceImpl deviceImpl;

    private DeviceImpl() {
        getDB();
    }

    public static DeviceImpl getDeviceImpl() {
        if (deviceImpl == null) {
            deviceImpl = new DeviceImpl();
        }
        return deviceImpl;
    }

    @Override
    public Set<Device> getDevices() {
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

        if (getDeviceByID(device.getId()) != null) {
            noDuplicateEntry = false;
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
            connection = DriverManager.getConnection(DB_url, user, pwd);
            statement = connection.createStatement();

            for (Device device : devices) {
                String id = device.getId();
                String name = device.getName();
                String who = device.getWho();
                String type = device.getType();
                boolean isBorrowed = device.isBorrowed();
                boolean isDeprecated = device.isDeprecated();
                sqlReq = "INSERT INTO devices (`id`, `name`, `who`, `type`, `isBorrowed`, `isDeprecated`) VALUES ('"
                        + id + "', '" + name + "', '" + who + "', '" + type + "', '" + (isBorrowed ? 1 : 0) + "', '"
                        + (isDeprecated ? 1 : 0) + "');";
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

    public void saveChanges(List<String> id, List<Integer> col, List<String> val) {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库…");
            connection = DriverManager.getConnection(DB_url, user, pwd);
            statement = connection.createStatement();

            for (int i = 0; i < id.size(); i++) {
                String crtVal;
                if (col.get(i) == 4 || col.get(i) == 5) {
                    crtVal = val.get(i).equals("false") ? "0" : "1";
                } else {
                    crtVal = val.get(i);
                }
                sqlReq = "UPDATE devices " +
                        " SET `" + DB_FIELD[col.get(i)] + "` = '" + crtVal +
                        "' WHERE (`id` = '" + id.get(i) + "');";
                System.out.println(id.get(i) + ": status: " + statement.executeUpdate(sqlReq));
            }
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Class 未找到。");
            e.printStackTrace();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("违反 SQL 完整性约束");
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
    }

    public void getDB() {
        devices = new HashSet<Device>();
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库…");
            connection = DriverManager.getConnection(DB_url, user, pwd);
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
    }

    public boolean setDevice(Device device) {
        boolean status = true;

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库…");
            connection = DriverManager.getConnection(DB_url, user, pwd);
            statement = connection.createStatement();

            String id = device.getId();
            String name = device.getName();
            String who = device.getWho();
            String type = device.getType();
            boolean isBorrowed = device.isBorrowed();
            boolean isDeprecated = device.isDeprecated();
            sqlReq = "INSERT INTO devices (`id`, `name`, `who`, `type`, `isBorrowed`, `isDeprecated`) VALUES ('"
                    + id + "', '" + name + "', '" + who + "', '" + type + "', '" + (isBorrowed ? 1 : 0) + "', '"
                    + (isDeprecated ? 1 : 0) + "');";
            System.out.println(id + ": status: " + statement.executeUpdate(sqlReq));

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

    public void delChanges(List<String> id) {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库…");
            connection = DriverManager.getConnection(DB_url, user, pwd);
            statement = connection.createStatement();

            for (int i = 0; i < id.size(); i++) {
                sqlReq = "DELETE FROM devices WHERE (`id` = '" + id.get(i) + "');";
                System.out.println(id.get(i) + ": status: " + statement.executeUpdate(sqlReq));
            }
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Class 未找到。");
            e.printStackTrace();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("违反 SQL 完整性约束");
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
    }

    public void setDB(Database database) {
        host = database.getHost();
        port = database.getPort();
        DB_name = database.getDB_name();
        user = database.getUser();
        pwd = database.getPwd();
        DB_url = "jdbc:mysql://" + host + ":" + port + "/" + DB_name + OPTIONS;
    }

}
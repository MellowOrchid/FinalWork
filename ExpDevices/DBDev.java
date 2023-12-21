package ExpDevices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ExpDevices.entity.*;

public class DBDev {
    public static void main(String[] args) {
        FileOutputStream fOS = null;
        ObjectOutputStream oOS = null;
        FileInputStream fIS = null;
        ObjectInputStream oIS = null;
        File file = new File("ExpDevices/static/db.conf.txt");
        // DeviceImpl deviceImpl = DeviceImpl.getDeviceImpl();
        // Set<Device> devices = deviceImpl.getDevices();
        // Device device1 = new Device("001", "a1", "mo", "phy", false,
        // false);
        // Device device2 = new Device("002", "a2", "mo2", "chs", false,
        // false);
        // deviceImpl.add(device1);
        // deviceImpl.add(device2);
        // deviceImpl.add(null);

        // for (Device device : devices) {
        // System.out.println(device);
        // }

        // Database database = new Database("127.0.0.1", 3306, "name",
        //         "test", "test");
        try {
            fOS = new FileOutputStream(file);
            // 3. 创建一根粗管道与细管道对接
            oOS = new ObjectOutputStream(fOS);
            // 写
            // oOS.writeObject(database);
            // oOS.writeObject(new Database("127.0.0.12", 33067, "name7",
            //     "test7", "test7"));
            oOS.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oOS != null) {
                    oOS.close();
                }
                if (fOS != null) {
                    fOS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        Database c = null;
        try {
            fIS = new FileInputStream(file);
            oIS = new ObjectInputStream(fIS);
            while (fIS.available() > 0) {
                c = (Database) oIS.readObject();
                System.out.println(c);
                // fIS.skip(2);
            }
        } catch (FileNotFoundException e) {
            System.out.println("读：文件未找到");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("读：IO 错误");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("读：Class 错误");
            e.printStackTrace();
        } finally {
            try {
                if (oIS != null) {
                    oIS.close();
                }
                if (fIS != null) {
                    fIS.close();
                }
            } catch (IOException e) {
                System.out.println("关闭流：IO 错误");
                e.printStackTrace();
            }
        }
    }
}

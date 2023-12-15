package ExpDevices.DB;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import ExpDevices.entity.Device;

public class DeviceDataset {
    private static final String DeviceDBPath = "ExpDevices/DB/devices.db";
    private static File file = new File(DeviceDBPath);
    private static FileInputStream fIS;
    private static ObjectInputStream oIS;
    private static FileOutputStream fOS;
    private static ObjectOutputStream oOS;
    private static Set<Device> devices;

    public static Set<Device> getDevices() {
        devices = new HashSet<Device>();
        Device device = null;
        try {
            fIS = new FileInputStream(file);
            oIS = new ObjectInputStream(fIS);
            while (fIS.available() > 0) {
                device = (Device) oIS.readObject();
                System.out.println(device);
                devices.add(device);
                // fIS.skip(4);
            }
        } catch (FileNotFoundException e) {
            System.out.println("读：文件不存在。");
        } catch (IOException e) {
            System.out.println("读：IO 异常。");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("读：Class 缺省。");
        } finally {
            try {
                if (oIS != null) {
                    oIS.close();
                }
                if (fIS != null) {
                    fIS.close();
                }
            } catch (IOException e) {
                System.out.println("关：IO 异常。");
            }
        }

        return devices;
    }

    public static void setDevices(Set<Device> devices) {
        DeviceDataset.devices = devices;
        try {
            fOS = new FileOutputStream(file, false);
            oOS = new ObjectOutputStream(fOS);
            for (Device device : devices) {
                oOS.writeObject(device);
                oOS.flush();
            }
        } catch (FileNotFoundException e) {
            System.out.println("写：文件不存在。");
        } catch (IOException e) {
            System.out.println("写：IO 异常。");
        } finally {
            try {
                if (oOS != null) {
                    oOS.close();
                }
                if (fOS != null) {
                    fOS.close();
                }
            } catch (IOException e) {
                System.out.println("写：IO 异常。");
            }
        }

    }

}

package ExpDevices;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;

public class DBDev {
    public static void main(String[] args) {
        DeviceImpl deviceImpl = new DeviceImpl();
        // Device device1 = new Device("001", "a1", "mo", "phy", false,
        //         false);
        // Device device2 = new Device("002", "a2", "mo2", "chs", false,
        //         false);
        // deviceImpl.add(device1);
        // deviceImpl.add(device2);
        deviceImpl.add(null);

        // System.out.println(deviceImpl.getDeviceByID("002"));
    }
}

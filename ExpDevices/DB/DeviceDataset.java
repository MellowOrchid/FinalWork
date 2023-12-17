package ExpDevices.DB;

import java.util.HashSet;
import java.util.Set;

import ExpDevices.entity.Device;

public class DeviceDataset {
    private static Set<Device> devices;

    public static Set<Device> getDevices() {
        devices = new HashSet<Device>();


        return devices;
    }

    public static void setDevices(Set<Device> devices) {
        DeviceDataset.devices = devices;
    }

}

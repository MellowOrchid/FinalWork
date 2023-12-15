package ExpDevices.DAO;

import java.util.Set;

import ExpDevices.DB.DeviceDataset;
import ExpDevices.entity.Device;

public class DeviceImpl implements IDeviceDAO {
    private Set<Device> devices = DeviceDataset.getDevices();

    @Override
    public Set<Device> getDevices() {
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
        DeviceDataset.setDevices(devices);
        return true;
    }

}
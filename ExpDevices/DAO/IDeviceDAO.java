package ExpDevices.DAO;

import java.util.Set;

import ExpDevices.entity.Device;

public interface IDeviceDAO {
    Set<Device> getDevices();

    void setDevices(Set<Device> devices);

    Device getDeviceByID(String id);

    boolean add(Device device);
}

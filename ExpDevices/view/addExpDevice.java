package ExpDevices.view;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.*;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;
import ExpDevices.service.SetFont;

public class addExpDevice extends JFrame {
    private JLabel l_id, l_name, l_type;
    private JTextField t_id, t_name, t_type;
    private JButton addButton, cancelButton;
    private DeviceImpl deviceImpl = DeviceImpl.getDeviceImpl();
    private final Font FONT = new Font("仿宋", Font.PLAIN, 30);
    private final String ICON = "ExpDevices/static/iconImg.png";

    public addExpDevice() {
        Image image = Toolkit.getDefaultToolkit().getImage(ICON);
        this.setIconImage(image);
        this.setTitle("添加实验设备");
        final int WIDTH = 400;
        final int HEIGHT = 300;
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        init();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void init() {
        this.setLayout(new GridLayout(4, 2, 5, 5));
        l_id = new JLabel("设备编号");
        l_name = new JLabel("设备名称");
        l_type = new JLabel("设备类型");
        t_id = new JTextField();
        t_name = new JTextField();
        t_type = new JTextField();

        addButton = new JButton("添加");
        addButton.addActionListener(e -> onAddDev());
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            System.out.println("取消添加");
            dispose();
        });

        SetFont.setFont(FONT, l_id, l_name, l_type, t_id, t_name, t_type, addButton, cancelButton);

        this.add(l_id);
        this.add(t_id);
        this.add(l_name);
        this.add(t_name);
        this.add(l_type);
        this.add(t_type);
        this.add(addButton);
        this.add(cancelButton);
    }

    public void onAddDev() {
        String id = t_id.getText();
        String name = t_name.getText();
        String type = t_type.getText();
        Device newDevice;
        List<String> newDeviceList;
        Vector<String> newDeviceInfo;
        boolean isAdded = false; // 判断数据库是否接受该数据
        System.out.println("确认添加");

        if (name.isEmpty() || type.isEmpty()) {
            System.out.println("有空信息");
            JOptionPane.showMessageDialog(this, "请将信息填写完整");
            return;
        }
        newDevice = new Device(id, name, null, type, false, false);
        newDeviceList = Arrays.asList(id, name, "null", type, "false", "false");
        newDeviceInfo = new Vector<String>(newDeviceList);
        isAdded = deviceImpl.add(newDevice);
        if (!isAdded) {
            System.out.println("添加失败");
            JOptionPane.showMessageDialog(this, "添加失败，因为有重复的编号");
            return;
        }
        MainWindow.isChanged = true;
        MainWindow.dataVectors.add(newDeviceInfo);
        MainWindow.onFlush();
        System.out.println("添加成功");
        JOptionPane.showMessageDialog(this, "添加成功");
        this.dispose();
    }

}

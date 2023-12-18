package ExpDevices.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;
import ExpDevices.service.SetFont;

public class addExpDevice extends JFrame {
    private JLabel l_id, l_name, l_type;
    private JTextField t_id, t_name, t_type;
    private JButton addButton, cancelButton;
    private DeviceImpl deviceImpl = DeviceImpl.getDeviceImpl();
    private final Font FONT = new Font("仿宋", 0, 30);
    private final String ICON = "ExpDevices/static/iconImg.png";

    public addExpDevice() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(ICON);
        Dimension screenSize = toolkit.getScreenSize();
        this.setIconImage(image);
        this.setTitle("添加实验设备");
        final int WIDTH = 400;
        final int HEIGHT = 300;
        this.setSize(WIDTH, HEIGHT);
        int left = (screenSize.width - WIDTH) / 2;
        int right = (screenSize.height - HEIGHT) / 2;
        this.setLocation(left, right);
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
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("确认添加");
                onAddDev();
            }

        });
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("取消添加");
                dispose();
            }

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
        boolean isAdded = false; // 判断数据库是否接受该数据

        if (name.isEmpty() || type.isEmpty()) {
            System.out.println("有空信息");
            JOptionPane.showMessageDialog(this, "请将信息填写完整");
            return;
        }
        isAdded = deviceImpl.add(new Device(id, name, null,
                type, false, false));
        if (!isAdded) {
            System.out.println("添加失败");
            JOptionPane.showMessageDialog(this, "添加失败，因为有重复的编号");
            return;
        }
        MainWindow.setChanged(true);
        System.out.println("添加成功");
        JOptionPane.showMessageDialog(this, "添加成功");
        this.dispose();
    }

}

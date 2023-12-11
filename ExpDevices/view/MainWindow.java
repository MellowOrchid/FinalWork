package ExpDevices.view;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

public class MainWindow extends JFrame {
    private JLabel l_;
    private JTable t_devices;

    public MainWindow() {
        this.setTitle("实验设备管理系统");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int WIDTH = 800;
        final int HEIGHT = 800;
        this.setSize(WIDTH, HEIGHT);
        int left = (screenSize.width - WIDTH) / 2;
        int right = (screenSize.height - HEIGHT) / 2;
        this.setLocation(left, right);
        init();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void init() {
        this.setLayout(new GridLayout(3, 1));
        l_ = new JLabel("测试");
        t_devices = new JTable(5, 6);

        this.add(l_);
        this.add(t_devices);
    }
}

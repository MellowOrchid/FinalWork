package ExpDevices.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;
import ExpDevices.service.SetFont;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Set;

public class MainWindow extends JFrame {
    private JLabel l_;
    private JScrollPane scrollPane;
    private JTable table;
    private JTableHeader header;
    private DefaultTableModel model;
    private Font font = new Font("仿宋", 0, 30);
    private DeviceImpl deviceImpl = new DeviceImpl();

    public MainWindow() {
        this.setTitle("实验设备管理系统");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int WIDTH = 1600;
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
        Set<Device> devices = deviceImpl.getDevices();
        int devNum = devices.size();
        String[][] datas = new String[devNum][6];
        String[] titles = {
                /* 0 */ "设备编号",
                /* 1 */ "设备名称",
                /* 2 */ "领用人",
                /* 3 */ "设备类型",
                /* 4 */ "是否借出",
                /* 5 */ "是否是报废设备",
        };

        for (Device device : devices) {
            devNum--;
            datas[devNum][0] = device.getId();
            datas[devNum][1] = device.getName();
            datas[devNum][2] = device.getWho();
            datas[devNum][3] = device.getType();
            datas[devNum][4] = device.isBorrowed() + "";
            datas[devNum][5] = device.isDeprecated() + "";
        }

        table = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        table.setRowHeight(40);
        model = (DefaultTableModel) this.table.getModel();
        model.setColumnIdentifiers(titles);
        model = new DefaultTableModel(datas, titles);
        SetFont.setFont(font, header, table);
        this.setLayout(new GridLayout(3, 1));
        table.setModel(model);
        scrollPane = new JScrollPane();
        scrollPane.add(table);
        scrollPane.setViewportView(table);

        l_ = new JLabel("测试");

        this.add(l_);

        this.add(scrollPane);
    }
}

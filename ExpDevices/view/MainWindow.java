package ExpDevices.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;
import ExpDevices.service.SetFont;

import java.awt.*;
import java.util.Set;

public class MainWindow extends JFrame {
    private JLabel l_title;
    private JScrollPane scrollPane;
    private JTable table;
    private JTableHeader header;
    private DefaultTableModel model;
    private Font font = new Font("仿宋", 0, 30);
    private DeviceImpl deviceImpl = new DeviceImpl();
    private final String ICON = "ExpDevices/static/iconImg.png";

    public MainWindow() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(ICON);
        Dimension screenSize = toolkit.getScreenSize();
        this.setIconImage(image);
        this.setTitle("实验设备管理系统");
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
        this.setLayout(new BorderLayout(5, 5));
        Set<Device> devices = deviceImpl.getDevices();
        int devNum = devices.size();
        String[][] datas = new String[devNum][6];
        String[] titles = {
                /* 0 */ "设备编号",
                /* 1 */ "设备名称",
                /* 2 */ "领用人",
                /* 3 */ "设备类型",
                /* 4 */ "是否借出",
                /* 5 */ "是否报废",
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

        table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return true;
            }
        };
        header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        table.setRowHeight(40);
        model = (DefaultTableModel) this.table.getModel();
        model.setColumnIdentifiers(titles);
        model = new DefaultTableModel(datas, titles);
        model.getValueAt(devNum, devNum);
        table.setModel(model);
        scrollPane = new JScrollPane();
        scrollPane.add(table);
        scrollPane.setViewportView(table);

        l_title = new JLabel("欢迎使用实验设备管理系统");

        SetFont.setFont(font, header, table, l_title);
        this.add(l_title, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}

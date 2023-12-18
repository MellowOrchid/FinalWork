package ExpDevices.view;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;
import ExpDevices.service.SetFont;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

public class MainWindow extends JFrame {
    private boolean isChanged = false;
    private JLabel l_title;
    private JScrollPane scrollPane;
    private JTable table;
    private JTableHeader header;
    private JComboBox<String> comboBox;
    private TableCellEditor cellEditor;
    private DefaultTableModel model;
    private JButton saveButton, addButton;
    private JPanel buttonsJPanel;
    private DeviceImpl deviceImpl = new DeviceImpl();
    private final Font FONT = new Font("仿宋", 0, 30);
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
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
        init();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 自定义退出动作
        this.setVisible(true);
    }

    private void init() {
        this.setLayout(new BorderLayout(5, 5));
        Set<Device> devices = deviceImpl.getDevices();
        int devNum = devices.size();
        String[] status = {
                "false",
                "true"
        };
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
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                isChanged = true;
                System.out.println("表格改动");
            }

        });
        table.setModel(model);
        scrollPane = new JScrollPane();
        scrollPane.add(table);
        scrollPane.setViewportView(table);
        comboBox = new JComboBox<String>(status);
        comboBox.setPreferredSize(null);
        comboBox.setSelectedIndex(0);
        cellEditor = new DefaultCellEditor(comboBox);
        table.getColumnModel().getColumn(4).setCellEditor(cellEditor);
        table.getColumnModel().getColumn(5).setCellEditor(cellEditor);

        l_title = new JLabel("欢迎使用实验设备管理系统");

        addButton = new JButton("添加");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("添加设备");
                if (isChanged) {
                    System.out.println("未保存");
                    showMessage("有未保存的信息。");
                } else {

                    new addExpDevice();
                }
            }
            
        });

        saveButton = new JButton("保存");
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("保存按钮");
                onSave();
            }

        });

        buttonsJPanel = new JPanel(new GridLayout(1, 2, 2, 2));
        buttonsJPanel.add(addButton);
        buttonsJPanel.add(saveButton);

        SetFont.setFont(FONT, header, table, l_title, comboBox, addButton, saveButton);
        this.add(l_title, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonsJPanel, BorderLayout.SOUTH);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void onClose() {
        System.out.println("窗口关闭");
        if (isChanged) {
            System.out.println("未保存");
            int choice = JOptionPane.showConfirmDialog(null,
                    "是否保存更改？", "提示",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            System.out.println("choice: " + choice);

            if (choice == JOptionPane.YES_OPTION) {
                // 保存动作
                System.out.println("保存退出");
                onSave();
                this.dispose();
            } else if (choice == JOptionPane.NO_OPTION) {
                System.out.println("不保存退出");
                this.dispose();
            } else {
                System.out.println("取消");
            }
        } else {
            this.dispose();
        }
    }

    public void onSave() {
        if (!isChanged) {
            System.out.println("无需更改");
            showMessage("没有更改。");
            return;
        }
        // 保存动作
        isChanged = false;
        System.out.println("更改将保存");
    }

}

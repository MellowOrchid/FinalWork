package ExpDevices.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;
import ExpDevices.service.SetFont;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class MainWindow extends JFrame {
    public static boolean isChanged = false;
    private List<String> titleList = Arrays.asList(
            /* 0 */ "设备编号",
            /* 1 */ "设备名称",
            /* 2 */ "领用人",
            /* 3 */ "设备类型",
            /* 4 */ "是否借出",
            /* 5 */ "是否报废");
    private Vector<String> titleVector = new Vector<String>(titleList);
    public static Vector<Vector<String>> dataVectors = new Vector<Vector<String>>();
    private JLabel l_title;
    private JScrollPane scrollPane;
    private static JTable table;
    private JTableHeader header;
    private JComboBox<String> comboBox;
    private TableCellEditor cellEditor;
    private TableModel model;
    private JButton saveButton, flushButton, addButton, deleteButton;
    private JPanel buttonsJPanel;
    private DeviceImpl deviceImpl = DeviceImpl.getDeviceImpl();
    private final Font FONT = new Font("仿宋", Font.PLAIN, 30);
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
        String[] devStatus = { "false", "true" };
        String[][] dataStrings = new String[devNum][6];
        for (Device device : devices) {
            devNum--;
            dataStrings[devNum][0] = device.getId();
            dataStrings[devNum][1] = device.getName();
            dataStrings[devNum][2] = device.getWho();
            dataStrings[devNum][3] = device.getType();
            dataStrings[devNum][4] = device.isBorrowed() + "";
            dataStrings[devNum][5] = device.isDeprecated() + "";
            List<String> dataList = Arrays.asList(dataStrings[devNum]);
            Vector<String> data = new Vector<String>(dataList);
            dataVectors.add(data);
        }

        table = new JTable(dataVectors, titleVector) {
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        table.setRowHeight(40);
        model = table.getModel();
        model.addTableModelListener(e -> {
            isChanged = true;
            System.out.println("表格改动，在 " + (e.getLastRow() + 1) + " 行 " +
                    (e.getColumn() + 1) + " 列");
        });

        scrollPane = new JScrollPane();
        scrollPane.add(table);
        scrollPane.setViewportView(table);
        comboBox = new JComboBox<String>(devStatus);
        comboBox.setPreferredSize(null);
        comboBox.setSelectedIndex(0);
        cellEditor = new DefaultCellEditor(comboBox);
        table.getColumnModel().getColumn(4).setCellEditor(cellEditor);
        table.getColumnModel().getColumn(5).setCellEditor(cellEditor);

        l_title = new JLabel("欢迎使用实验设备管理系统");

        addButton = new JButton("添加");
        addButton.addActionListener(e -> onAdd());

        flushButton = new JButton("刷新");
        flushButton.addActionListener(e -> onFlush());

        deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> onDelete());

        saveButton = new JButton("保存");
        saveButton.addActionListener(e -> onSave());

        buttonsJPanel = new JPanel(new GridLayout(1, 4, 2, 2));
        buttonsJPanel.add(addButton);
        buttonsJPanel.add(flushButton);
        buttonsJPanel.add(deleteButton);
        buttonsJPanel.add(saveButton);

        SetFont.setFont(FONT, header, table, l_title, comboBox, addButton, flushButton, saveButton,
                deleteButton);
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

    public void onAdd() {
        System.out.println("添加设备");
        if (isChanged) {
            System.out.println("未保存");
            showMessage("有未保存的信息。");
            return;
        }
        new addExpDevice();
    }

    public void onSave() {
        System.out.println("保存按钮");
        if (!isChanged) {
            System.out.println("无需更改");
            showMessage("没有更改。");
            return;
        }
        // 保存动作
        // deviceImpl.saveChanges();
        isChanged = false;
        System.out.println("更改将保存");
    }

    public static void onFlush() {
        System.out.println("刷新");
        table.updateUI();
    }

    public void onDelete() {
        int[] selectedRows = table.getSelectedRows();
        int i = selectedRows.length;
        while (i-- != 0) {
            System.out.println(selectedRows[i]);
            dataVectors.remove(selectedRows[i]);
            table.updateUI();
            isChanged = true;
        }
        table.clearSelection();
    }

}

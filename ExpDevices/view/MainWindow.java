package ExpDevices.view;

import javax.swing.*;
import javax.swing.table.*;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Device;
import ExpDevices.service.SetFont;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class MainWindow extends JFrame {
    public static boolean isChanged = false; // 改变
    public static boolean isAdded = false; // 加入
    public static Device newDevice;
    private List<String> changedID = new ArrayList<String>();
    private List<String> changedValue = new ArrayList<String>();
    private List<Integer> changedCol = new ArrayList<Integer>();
    private String oldValue, newValue;
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
    private JButton saveButton, flushButton, addButton, deleteButton;
    private JPanel buttonsJPanel;
    private DeviceImpl deviceImpl = DeviceImpl.getDeviceImpl();
    private final Font FONT = new Font("仿宋", Font.PLAIN, 30);
    private final String ICON = "ExpDevices/static/iconImg.png";

    public MainWindow() {
        Image image = Toolkit.getDefaultToolkit().getImage(ICON);
        this.setIconImage(image);
        this.setTitle("实验设备管理系统");
        final int WIDTH = 1600;
        final int HEIGHT = 800;
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
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
                // return column == 4 || column == 5;
                return false;
            }
        };
        table.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (e.getClickCount() == 2) {
                    System.out.println("表格被双击，在 " + (row + 1) + " 行 " + (col + 1) + " 列");
                    oldValue = (String) table.getValueAt(row, col);
                    if (col == 4 || col == 5) {
                        int choice = JOptionPane.showConfirmDialog(null,
                                "反转其值？");
                        System.out.println("choice: " + choice);
                        if (choice == JOptionPane.OK_OPTION) {
                            isChanged = true;
                            newValue = oldValue.equals("false") + "";
                            table.setValueAt(newValue, row, col);
                            changedID.add(table.getValueAt(row, 0) + "");
                            changedCol.add(col);
                            changedValue.add(newValue);
                        }
                        return;
                    }
                    newValue = JOptionPane.showInputDialog(null,
                            "修改为：", oldValue);
                    if (newValue != null) {
                        isChanged = !newValue.equals(oldValue);
                        if (isChanged) {
                            table.setValueAt(newValue, row, col);
                            System.out.println("表格改动，在 "
                                    + (row + 1) + " 行 " + (col + 1) + " 列");
                            changedID.add(table.getValueAt(row, 0) + "");
                            changedCol.add(col);
                            changedValue.add(newValue);
                        }
                    } else {
                        System.out.println("取消修改");
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        });
        header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        table.setRowHeight(40);

        scrollPane = new JScrollPane();
        scrollPane.add(table);
        scrollPane.setViewportView(table);

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

        SetFont.setFont(FONT, header, table, l_title, addButton, flushButton, saveButton,
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
        if (isChanged || isAdded) {
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
        if (isChanged || isAdded) {
            System.out.println("未保存");
            showMessage("有未保存的信息。");
            return;
        }
        new addExpDevice();
    }

    public void onSave() {
        System.out.println("保存更改");
        if (!isChanged && !isAdded) {
            System.out.println("无需更改");
            showMessage("没有更改。");
            return;
        }
        // 保存动作
        if (isAdded) {
            deviceImpl.setDevice(newDevice);
            isAdded = false;
        }
        if (isChanged) {
            deviceImpl.saveChanges(changedID, changedCol, changedValue);
            changedCol.clear();
            changedID.clear();
            changedValue.clear();
        }
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
            System.out.println("行 " + selectedRows[i] + " 被删除");
            dataVectors.remove(selectedRows[i]);
            table.updateUI();
            isChanged = true;
        }
        table.clearSelection();
    }

}

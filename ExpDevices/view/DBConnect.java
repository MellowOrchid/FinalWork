package ExpDevices.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ExpDevices.DAO.DeviceImpl;
import ExpDevices.entity.Database;
import ExpDevices.service.SetFont;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class DBConnect extends JFrame {
    private JLabel l_hello, l_target;
    private JButton b_add, b_del, b_cnct;
    private JPanel buttonPanel;
    public static JComboBox<Database> chooseBox;
    private Vector<Database> connects = new Vector<>();
    private DeviceImpl deviceImpl = DeviceImpl.getDeviceImpl();
    private final Font FONT = new Font("仿宋", Font.PLAIN, 30);
    private final String ICON = "ExpDevices/static/iconImg.png";
    private final File FILE = new File("ExpDevices/static/db.conf.txt");

    public DBConnect() {
        Image image = Toolkit.getDefaultToolkit().getImage(ICON);
        this.setIconImage(image);
        this.setTitle("实验设备管理系统 - 选择数据库");
        this.setSize(800, 400);
        this.setLocationRelativeTo(null);
        init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 自定义退出动作
        this.setVisible(true);
    }

    public void init() {
        this.setLayout(new BorderLayout());
        l_hello = new JLabel("欢迎使用，请选择要连接的数据库：");
        l_target = new JLabel("目标：");

        getConnects();
        chooseBox = new JComboBox<Database>(connects);
        chooseBox.updateUI();

        b_add = new JButton("添加");
        b_add.addActionListener(e -> onAdd());

        b_del = new JButton("删除");
        b_del.addActionListener(e -> onDel());

        b_cnct = new JButton("连接");
        b_cnct.addActionListener(e -> onCnct());

        buttonPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        buttonPanel.add(b_add);
        buttonPanel.add(b_del);

        SetFont.setFont(FONT, l_hello, l_target, b_add, b_del, b_cnct, chooseBox);

        this.add(l_hello, BorderLayout.NORTH);
        this.add(l_target, BorderLayout.WEST);
        this.add(chooseBox, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.EAST);
        this.add(b_cnct, BorderLayout.SOUTH);
    }

    private void onCnct() {
        deviceImpl.setDB((Database) chooseBox.getSelectedItem());
        new MainWindow();
    }

    private void onDel() {
        int choose = JOptionPane.showConfirmDialog(this, "确认删除此连接？");
        System.out.println("choose: " + choose);
        if (choose == JOptionPane.OK_OPTION) {
            int i = chooseBox.getSelectedIndex();
            System.out.println("删除：" + i);
            if (i == -1) {
                System.out.println("空列表");
                JOptionPane.showMessageDialog(this, "列表为空");
                return;
            }
            connects.remove(i);
            chooseBox.updateUI();
            updateDBconf();
        }
    }

    private void updateDBconf() {
        File file = new File("ExpDevices/static/db.conf.txt");
        FileOutputStream fOS = null;
        ObjectOutputStream oOS = null;

        try {
            fOS = new FileOutputStream(file); // 改追加
            // 3. 创建一根粗管道与细管道对接
            oOS = new ObjectOutputStream(fOS);
            // 写
            for (Database database : connects) {
                oOS.writeObject(database);
                oOS.flush();
            }
        } catch (IOException e) {
            System.out.println("写：IO 异常");
            e.printStackTrace();
        } finally {
            try {
                if (oOS != null) {
                    oOS.close();
                }
                if (fOS != null) {
                    fOS.close();
                }
            } catch (IOException e) {
                System.out.println("关流：IO 异常");
                e.printStackTrace();

            }
        }
    }

    private void onAdd() {
        new addDB();
    }

    public void getConnects() {
        FileInputStream fIS = null;
        ObjectInputStream oIS = null;

        Database c = null;
        try {
            fIS = new FileInputStream(FILE);
            oIS = new ObjectInputStream(fIS);
            while (fIS.available() > 0) {
                c = (Database) oIS.readObject();
                System.out.println(c);
                connects.add(c);
                fIS.skip(4);
            }
        } catch (FileNotFoundException e) {
            System.out.println("读：文件未找到");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("读：IO 错误");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("读：Class 错误");
            e.printStackTrace();
        } finally {
            try {
                if (oIS != null) {
                    oIS.close();
                }
                if (fIS != null) {
                    fIS.close();
                }
            } catch (IOException e) {
                System.out.println("关闭流：IO 错误");
                e.printStackTrace();
            }
        }
    }
}
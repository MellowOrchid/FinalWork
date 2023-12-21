package ExpDevices.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ExpDevices.entity.Database;
import ExpDevices.service.SetFont;

public class addDB extends JFrame {
    private JLabel l_host, l_port, l_db, l_usr, l_pwd;
    private JTextField t_host, t_port, t_db, t_usr;
    private JPasswordField p_pwd;
    private JButton addButton, cancelButton;
    private final Font FONT = new Font("仿宋", Font.PLAIN, 30);
    private final String ICON = "ExpDevices/static/iconImg.png";

    public addDB() {
        Image image = Toolkit.getDefaultToolkit().getImage(ICON);
        this.setIconImage(image);
        this.setTitle("添加数据库连接");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        init();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void init() {
        this.setLayout(new GridLayout(6, 2));
        l_host = new JLabel("主机");
        l_port = new JLabel("端口");
        l_db = new JLabel("数据库名");
        l_usr = new JLabel("用户名");
        l_pwd = new JLabel("密码");
        t_host = new JTextField();
        t_port = new JTextField("3306");
        t_db = new JTextField();
        t_usr = new JTextField();
        p_pwd = new JPasswordField();

        addButton = new JButton("添加");
        addButton.addActionListener(e -> onAddDev());
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            System.out.println("取消添加");
            dispose();
        });

        SetFont.setFont(FONT, l_host, l_port, l_db, l_usr, l_pwd, t_host, t_port, t_db,
                t_usr, addButton, cancelButton);

        this.add(l_host);
        this.add(t_host);
        this.add(l_port);
        this.add(t_port);
        this.add(l_db);
        this.add(t_db);
        this.add(l_usr);
        this.add(t_usr);
        this.add(l_pwd);
        this.add(p_pwd);
        this.add(addButton);
        this.add(cancelButton);
    }

    public void onAddDev() {
        String host = t_host.getText();
        String DB_name = t_db.getText();
        String user = t_usr.getText();
        String pwd = new String(p_pwd.getPassword());
        Database newDatabase;
        long port = 0L;
        boolean isFormatOK = true;
        try {
            port = Long.parseLong(t_port.getText());
        } catch (NumberFormatException e) {
            System.out.println("字符转换出错");
            isFormatOK = false;
            e.printStackTrace();
        }
        System.out.println("确认添加");

        if (hasEmpty(host, DB_name, user, pwd)) {
            System.out.println("有空信息");
            JOptionPane.showMessageDialog(this, "请将信息填写完整");
            return;
        }
        if (!isFormatOK) {
            System.out.println("端口号错误");
            JOptionPane.showMessageDialog(this, "端口只可以是数字");
            return;
        }

        newDatabase = new Database(host, port, DB_name, user, pwd);
        add(newDatabase);

        System.out.println("添加成功");
        JOptionPane.showMessageDialog(this, "添加成功");
        this.dispose();
    }

    private boolean hasEmpty(String... strings) {
        for (String string : strings) {
            if (string.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean add(Database database) {
        File file = new File("ExpDevices/static/db.conf.txt");
        FileOutputStream fOS = null;
        ObjectOutputStream oOS = null;
        boolean isAdded = false;

        try {
            fOS = new FileOutputStream(file, true); // 改追加
            // 3. 创建一根粗管道与细管道对接
            oOS = new ObjectOutputStream(fOS);
            // 写
            oOS.writeObject(database);
            oOS.flush();
            isAdded = true;
            DBConnect.chooseBox.addItem(database);
            DBConnect.chooseBox.updateUI();
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

        return isAdded;
    }

}

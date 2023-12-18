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
import javax.swing.JTextField;

import ExpDevices.service.SetFont;

public class addExpDevice extends JFrame {
    private JLabel l_name, l_type;
    private JTextField t_name, t_type;
    private JButton addButton, cancelButton;
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
        init();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void init() {
        this.setLayout(new GridLayout(3, 2, 5, 5));
        l_name = new JLabel("设备名称");
        l_type = new JLabel("设备类型");
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

        SetFont.setFont(FONT, l_name, l_type, t_name, t_type, addButton, cancelButton);

        this.add(l_name);
        this.add(t_name);
        this.add(l_type);
        this.add(t_type);
        this.add(addButton);
        this.add(cancelButton);
    }

    public void onAddDev() {

    }

}

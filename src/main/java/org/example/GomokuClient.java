package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 五子棋客户端类，继承自JFrame并实现了MouseListener接口
 * 负责创建游戏界面，处理玩家的点击事件，并与服务器通信
 */
public class GomokuClient extends JFrame implements MouseListener {
    // 棋盘大小常量
    private static final int BOARD_SIZE = 15;
    // 用于显示棋盘的面板
    private JPanel boardPanel;
    // 二维按钮数组，表示棋盘上的每个位置
    private JButton[][] buttons;
    // 当前玩家，初始为'O'
    private char currentPlayer = 'O';
    // 与服务器通信的Socket
    private Socket socket;
    // 向服务器发送消息的输出流
    private PrintWriter out;
    // 从服务器接收消息的输入流
    private BufferedReader in;
    public static void main(String[] args) {
        new GomokuClient();
    }

    /**
     * GomokuClient构造函数
     * 初始化游戏界面，创建棋盘和按钮，并尝试连接到服务器
     */
    public GomokuClient() {
        super("五子棋");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建并设置棋盘面板
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // 初始化棋盘按钮
        buttons = new JButton[BOARD_SIZE][BOARD_SIZE];

        // 遍历棋盘大小，创建每个按钮并添加到面板
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                // 为按钮添加鼠标监听器
                button.addMouseListener(this);
                boardPanel.add(button);
                buttons[i][j] = button;
            }
        }


        // 将棋盘面板添加到框架，并显示出来
        add(boardPanel);
        setVisible(true);

        // 尝试连接到服务器
        connectToServer();
    }

    /**
     * 连接到服务器方法
     * 尝试连接到指定地址和端口的服务器，并创建读写流
     */
    private void connectToServer() {
        try {
            socket = new Socket("localhost", 8080);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 启动新线程监听服务器消息
            new Thread(() -> {
                try {
                    while (true) {
                        String input = in.readLine();
                        if (input == null) break;

                        // 处理服务器发来的消息
                        handleInput(input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            // 连接服务器失败时显示对话框
            JOptionPane.showMessageDialog(this, "无法连接到服务器，请检查服务器是否运行。");
            e.printStackTrace();
        }
    }

    /**
     * 处理服务器发来的消息
     * 根据消息类型更新棋盘或显示获胜信息
     * @param input 服务器发来的消息字符串
     */
    private void handleInput(String input) {
        String[] parts = input.split(",");
        if (parts.length == 3 && parts[0].equals("move")) {
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            // 更新棋盘状态
            updateBoard(row, col, currentPlayer);
        } else if (parts.length == 2 && parts[0].equals("win")) {
            // 显示获胜信息对话框
            JOptionPane.showMessageDialog(this, "玩家 " + parts[1] + " 获胜！");
        }
    }

    /**
     * 更新棋盘上的按钮状态
     * 根据指定的位置和玩家标记按钮
     * @param row 按钮所在的行
     * @param col 按钮所在的列
     * @param player 当前玩家标记('O'或'X')
     */
    private void updateBoard(int row, int col, char player) {
        JButton button = buttons[row][col];
        button.setText(String.valueOf(player));
        // 禁用已下棋的按钮
        button.setEnabled(false);
        // 根据当前玩家改变按钮背景色
        button.setBackground(player == 'O' ? Color.BLACK : Color.RED);
    }

    /**
     * 鼠标点击事件处理方法
     * 当按钮被点击时，确定点击的按钮位置，并发送位置信息到服务器，同时更新棋盘状态
     * @param e 鼠标事件
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = e.getComponent();
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            int row = -1;
            int col = -1;

            // 确定点击按钮的位置
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (buttons[i][j] == button) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            // 如果找到按钮位置，发送位置信息到服务器，并更新棋盘状态
            if (row != -1 && col != -1) {
                out.println("move," + row + "," + col);
                updateBoard(row, col, currentPlayer);
                // 切换当前玩家
                currentPlayer = (currentPlayer == 'O') ? 'X' : 'O';
            }
        }
    }

    // 以下方法为MouseListener接口的实现，暂不需要实现逻辑
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}





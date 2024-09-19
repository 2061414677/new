package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Five extends JFrame implements MouseListener {
    private static final int BOARD_SIZE = 15; // 棋盘大小
    private JPanel boardPanel;
    private JButton[][] buttons; // 按钮数组
    private char currentPlayer = 'O'; // 当前玩家

    public Five() {
        super("五子棋");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口

        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttons = new JButton[BOARD_SIZE][BOARD_SIZE];

        // 初始化按钮
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.addMouseListener(this);
                boardPanel.add(button);
                buttons[i][j] = button;
            }
        }

        add(boardPanel);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (buttons[i][j] == button) {
                    if (button.getText().isEmpty()) {
                        button.setText(String.valueOf(currentPlayer));
                        button.setEnabled(false); // 禁用已下过的按钮
                        checkWin(i, j);
                        currentPlayer = (currentPlayer == 'O') ? 'X' : 'O';
                    }
                    break;
                }
            }
        }
    }

    private void checkWin(int row, int col) {
        // 检查水平方向
        int count = 1;
        for (int i = 1; i < 5; i++) {
            if (col + i < BOARD_SIZE && buttons[row][col + i].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (col - i >= 0 && buttons[row][col - i].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            JOptionPane.showMessageDialog(this, "玩家 " + buttons[row][col].getText() + " 获胜！");
            resetBoard();
            return;
        }

        // 检查垂直方向
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (row + i < BOARD_SIZE && buttons[row + i][col].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (row - i >= 0 && buttons[row - i][col].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            JOptionPane.showMessageDialog(this, "玩家 " + buttons[row][col].getText() + " 获胜！");
            resetBoard();
            return;
        }

        // 检查对角线方向（左上到右下）
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (row + i < BOARD_SIZE && col + i < BOARD_SIZE && buttons[row + i][col + i].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (row - i >= 0 && col - i >= 0 && buttons[row - i][col - i].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            JOptionPane.showMessageDialog(this, "玩家 " + buttons[row][col].getText() + " 获胜！");
            resetBoard();
            return;
        }

        // 检查反向对角线方向（右上到左下）
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (row - i >= 0 && col + i < BOARD_SIZE && buttons[row - i][col + i].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (row + i < BOARD_SIZE && col - i >= 0 && buttons[row + i][col - i].getText().equals(String.valueOf(buttons[row][col].getText()))) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            JOptionPane.showMessageDialog(this, "玩家 " + buttons[row][col].getText() + " 获胜！");
            resetBoard();
            return;
        }
    }

    private void resetBoard() {
        currentPlayer = 'O';
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        new Five();
    }
}

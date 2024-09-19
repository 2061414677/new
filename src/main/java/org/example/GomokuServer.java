package org.example;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GomokuServer {
    private static final int PORT = 8080;
    private Map<String, ClientHandler> clients = new HashMap<>();
    private char currentPlayer = 'O';
    private Button[][] buttons;

    public GomokuServer() {
        initializeButtons();
    }

    private void initializeButtons() {
        buttons = new Button[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                buttons[i][j] = new Button(""); // 初始为空
            }
        }
    }


    public static void main(String[] args) {
        new GomokuServer().startServer();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器启动，等待客户端连接...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientId = "client" + clients.size();
                clients.put(clientId, new ClientHandler(clientSocket, clientId));
                System.out.println("客户端 " + clientId + " 已连接");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private String clientId;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket, String clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    String input = in.readLine();
                    if (input == null) break;

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
        }

        private void handleInput(String input) {
            String[] parts = input.split(",");
            if (parts.length == 3 && parts[0].equals("move")) {
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);

                if (isValidMove(row, col)) {
                    makeMove(row, col);
                    broadcastMove(row, col);
                    checkWin(row, col);
                }
            }
        }

        private boolean isValidMove(int row, int col) {
            return row >= 0 && row < 15 && col >= 0 && col < 15;
        }

        private void makeMove(int row, int col) {
            System.out.println("玩家 " + currentPlayer + " 下棋 (" + row + ", " + col + ")");
            buttons[row][col] = new Button(String.valueOf(currentPlayer));
            currentPlayer = (currentPlayer == 'O') ? 'X' : 'O';
        }

        private void broadcastMove(int row, int col) {
            for (ClientHandler client : clients.values()) {
                client.sendMove(row, col);
            }
        }

        private void sendMove(int row, int col) {
            out.println("move," + row + "," + col);
        }

        private void checkWin(int row, int col) {
            // 检查水平方向
            int count = 1;
            for (int i = 1; i < 5; i++) {
                if (col + i < 15 && buttons[row][col + i] != null && buttons[row][col + i].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            for (int i = 1; i < 5; i++) {
                if (col - i >= 0 && buttons[row][col - i] != null && buttons[row][col - i].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= 5) {
                broadcastWin(buttons[row][col].getText());
                return;
            }

            // 检查垂直方向
            count = 1;
            for (int i = 1; i < 5; i++) {
                if (row + i < 15 && buttons[row + i][col] != null && buttons[row + i][col].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            for (int i = 1; i < 5; i++) {
                if (row - i >= 0 && buttons[row - i][col] != null && buttons[row - i][col].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= 5) {
                broadcastWin(buttons[row][col].getText());
                return;
            }

            // 检查对角线方向（左上到右下）
            count = 1;
            for (int i = 1; i < 5; i++) {
                if (row + i < 15 && col + i < 15 && buttons[row + i][col + i] != null && buttons[row + i][col + i].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            for (int i = 1; i < 5; i++) {
                if (row - i >= 0 && col - i >= 0 && buttons[row - i][col - i] != null && buttons[row - i][col - i].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= 5) {
                broadcastWin(buttons[row][col].getText());
                return;
            }

            // 检查反向对角线方向（右上到左下）
            count = 1;
            for (int i = 1; i < 5; i++) {
                if (row - i >= 0 && col + i < 15 && buttons[row - i][col + i] != null && buttons[row - i][col + i].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            for (int i = 1; i < 5; i++) {
                if (row + i < 15 && col - i >= 0 && buttons[row + i][col - i] != null && buttons[row + i][col - i].getText().equals(buttons[row][col].getText())) {
                    count++;
                } else {
                    break;
                }
            }
            if (count >= 5) {
                broadcastWin(buttons[row][col].getText());
                return;
            }
        }

        private void broadcastWin(String winner) {
            for (ClientHandler client : clients.values()) {
                client.sendWin(winner);
            }
        }

        private void sendWin(String winner) {
            out.println("win," + winner);
        }
    }
}

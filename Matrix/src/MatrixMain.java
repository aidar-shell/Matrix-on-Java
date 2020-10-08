
import javax.swing.*;
import java.awt.*;
import java.util.SplittableRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MatrixMain {

    private static MatrixMain matrixMain = new MatrixMain();
    private static JFrame frame;
    private static JPanel panel;
    private final ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("img/Matrix.jpg"));
    private final ImageIcon appIcon = new ImageIcon(this.getClass().getResource("img/IconMatrix.png"));

    private static SplittableRandom random = new SplittableRandom();
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static  char[] alphabet;
    private static String[][] colors = {{"#00d615", "#fffcfc"}, {"#abd9b1", "#e0e5a1"},
            {"#1cffff", "#fffafa"}};
    private static double[] arr = new double[30];
    private static int l;

    public static void main(String[] args) {
        initAlphabet();
        initArr();
        setPanel();
        setFrame();
        fps();
    }

    private static void initArr() {
        double count = 0;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextDouble(0.03) + count;
            count += 0.005;
        }
    }

    private static void initAlphabet() {
        alphabet = new char[96];
        int n = 0;
        for (char c = '\u30A0'; c <= '\u30FF'; c++) {
            alphabet[n] = c;
            n++;
        }
    }


    private static void setFrame() {

        frame = new JFrame("Matrix Animation");
        frame.add(panel);
        frame.setSize(900, 900);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setIconImage(matrixMain.appIcon.getImage());
        frame.setVisible(true);
    }

    private static void setPanel() {

        panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(matrixMain.imageIcon.getImage(), 0, 0, null);

                for (Columns column : Columns.values()) {
                    int n = 0;
                    int N = -400;

                    while (n <= column.columnLength) {

                        g.setFont(column.font);
                        l = column.k;
                        column.columnColor = Color.decode(colors[l][random.nextInt(2)]);
                        g.setColor(column.columnColor);
                        g.create().drawString(String.valueOf(column.chars[n]), column.xCoord, (int) column.yCoord + N);
                        N += 20;
                        n++;
                    }
                }
            }
        };
    }

    private static void fps() {

        int n = 0;
        for (Columns column : Columns.values()) {

            column.k = random.nextInt(3);
            column.plus_yCoord = arr[random.nextInt(30)];
            column.columnLength = random.nextInt(30);
            column.xCoord = n;
            column.font = new Font("ms mincho", Font.BOLD, random.nextInt(14) + 15);
            column.yCoord = -250;
            n += 30;
            for (int i = 0; i < column.columnLength; i++) {
                for (int j = 0; j <= i; j++) {
                    column.chars[j] = alphabet[random.nextInt(96)];
                }
            }
        }

        for (Columns column : Columns.values()) {

            scheduledExecutorService.scheduleAtFixedRate(() -> {

                for (int i = 0; i <= column.columnLength; i++) {
                    for (int j = 0; j <= i; j++) {
                        column.chars[j] = alphabet[random.nextInt(96)];
                    }
                }
                panel.repaint();
            }, 0, random.nextInt(200) + 50, TimeUnit.MILLISECONDS);


            scheduledExecutorService.scheduleAtFixedRate(() -> {

                if (column.yCoord >= 1360) {
                    column.k = random.nextInt(3);
                    column.yCoord = -250;;
                    column.plus_yCoord = arr[random.nextInt(30)];
                    column.columnLength = random.nextInt(30);
                    column.font = new Font("ms mincho", Font.BOLD, random.nextInt(20) + 10);
                }

                column.yCoord += column.plus_yCoord;

            }, 0, 500, TimeUnit.MICROSECONDS);
        }
    }
}
package blayzer.logparser;

import javax.swing.*;
import java.awt.*;
import java.io.*;

class MachineGuardLogParser extends JFrame {

    private FileInputStream selectedFile;
    private BufferedReader stringFromFile;
    private final JFileChooser openFile = new JFileChooser();
    private File file;

    private MachineGuardLogParser() {
        super("MachineGuardLogParser");
        setLayout(new GridLayout(3, 1));
        setBounds(450, 250, 400, 400); //Добавить получение размера экрана и считать
        JLabel label1 = new JLabel("Выберите лог-файл");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        add(label1);
        JButton chooseLogFileButton = new JButton("Выбрать лог-файл");
        add(chooseLogFileButton);
        JButton editFile = new JButton("Преобразовать");
        add(editFile);
        chooseLogFileButton.addActionListener(actionListener -> {
            int reply = openFile.showDialog(null, "Открыть файл");
            try {
                if (reply == JFileChooser.APPROVE_OPTION) {
                    file = openFile.getSelectedFile();
                    selectedFile = new FileInputStream(new File(file.getPath()));
                    stringFromFile = new BufferedReader(new InputStreamReader(selectedFile, "UTF-8"));
                    label1.setText("<html> Выбран файл: " + file.getName() + "<br>Нажмите: Преобразовать </html>");
                }
            }catch (IOException exc){exc.printStackTrace();}
        });
        editFile.addActionListener(actionListener -> {
            String str, result = null;
            try {
                while ((str = stringFromFile.readLine()) != null) {
                    if (str.contains("Injected")) {
                        str = str.replaceAll(".+(material)", "-");
                        str = str.replaceAll("(\\s\\with.*)", "");
                        if (!str.isEmpty()) {
                            result += str + "\n";
                            //System.out.println(result);
                        }
                        FileWriter fw = new FileWriter(file.getPath() + ".mg.txt");
                        assert result != null;
                        fw.write(result);
                    }
                }
            }catch (IOException exc){exc.printStackTrace();}
            label1.setText("<html> Выбран файл: " + file.getName() + "<br>" + "Готово. Создан файл " + file.getName() + ".mg.txt. </html>");
        });
    }

    public static void main(String[] args){
        MachineGuardLogParser app = new MachineGuardLogParser();
        app.setLocationRelativeTo(null);
        app.setVisible(true);
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

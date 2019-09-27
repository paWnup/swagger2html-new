package com.pa.plugin.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.pa.plugin.util.Swagger2Html;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author puan
 * @version 1.0
 * @date 2019-08-12
 */
public class MainDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField urlText;
    private JLabel urlLabel;
    private JLabel outputPathLabel;
    private JTextField ouputPath;
    private JButton fileChoose;

    public static void main(String[] args) {
        new MainDialog(null);
    }

    public MainDialog(AnActionEvent anActionEvent) {
        createDialog();
    }

    private void createDialog() {
        centerDialog(this, "swagger2html", 400, 150);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        fileChoose.addActionListener(e -> showFileChooser());

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();
        setVisible(true);
    }

    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            ouputPath.setText(file.getAbsolutePath() + "\\接口文档.html");
        }
    }

    private void centerDialog(MainDialog dialog, String title, int width, int height) {
        dialog.setTitle(title);
        dialog.setPreferredSize(new Dimension(width, height));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);
    }

    private void onCancel() {
        dispose();
    }

    private void onOK() {
        Swagger2Html.generateAsciiDocsToFile(urlText.getText());
        Swagger2Html.convertAsciiDocsToHtml(ouputPath.getText());
        dispose();
    }
}

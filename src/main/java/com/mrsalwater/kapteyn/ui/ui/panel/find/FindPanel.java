package com.mrsalwater.kapteyn.ui.ui.panel.find;

import com.mrsalwater.kapteyn.decompiler.classfile.ClassFile;
import com.mrsalwater.kapteyn.decompiler.classfile.constantpool.ConstantPool;
import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;
import com.mrsalwater.kapteyn.decompiler.exception.CorruptClassFileException;
import com.mrsalwater.kapteyn.ui.data.AppData;
import com.mrsalwater.kapteyn.ui.decompiler.Decompiler;
import com.mrsalwater.kapteyn.ui.finder.FindOperation;
import com.mrsalwater.kapteyn.ui.finder.Finder;
import com.mrsalwater.kapteyn.ui.finder.result.FindResult;
import com.mrsalwater.kapteyn.ui.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public final class FindPanel extends JPanel {

    private final JComboBox<String> comboBoxType;
    private final JButton buttonFind;
    private final JTextField textFieldSearch;
    private final JList<String> listResults;

    public FindPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        /* Panel Settings */

        JPanel panelSettings = new JPanel();
        panelSettings.setLayout(new BoxLayout(panelSettings, BoxLayout.LINE_AXIS));

        comboBoxType = new JComboBox<>(new String[]{"All", "LDC", "Local Variable", "Method Reference", "Field Reference"});
        comboBoxType.setMaximumSize(new Dimension(300, 20));
        panelSettings.add(comboBoxType);

        panelSettings.add(Box.createRigidArea(new Dimension(10, 0)));

        buttonFind = new JButton("Find");
        panelSettings.add(buttonFind);

        add(panelSettings);

        add(Box.createRigidArea(new Dimension(0, 10)));

        /* Text Field Search */

        textFieldSearch = new JTextField();
        textFieldSearch.setMaximumSize(new Dimension(382, 20));
        add(textFieldSearch);

        add(Box.createRigidArea(new Dimension(0, 20)));

        /* Scroll Pane Results */

        JScrollPane scrollPaneListResults = new JScrollPane();

        listResults = new JList<>(new DefaultListModel<>());
        scrollPaneListResults.setViewportView(listResults);

        add(scrollPaneListResults);
    }

    public void initialize(Decompiler decompiler, MainFrame frame) {
        buttonFind.addActionListener(actionEvent -> {
            FindOperation findOperation = FindOperation.match((String) comboBoxType.getSelectedItem());
            String string = textFieldSearch.getText();

            if (!string.isEmpty() && decompiler.hasFile()) {
                try {
                    Finder finder = new Finder(decompiler.getClassFiles());

                    try {
                        List<FindResult<?>> findResults = finder.find(findOperation, string);
                        DefaultListModel<String> model = (DefaultListModel<String>) listResults.getModel();
                        model.clear();

                        for (FindResult<?> findResult : findResults) {
                            ClassFile classFile = findResult.getClassFile();
                            ConstantPool constantPool = classFile.getConstantPool();

                            String className = constantPool.getUTF8(constantPool.getClass(classFile.getThisClass()).getNameIndex()).getValue().replace("/", ".");
                            model.addElement(findResult.getString() + " " + className);
                        }
                    } catch (CorruptClassFileException e) {
                        e.printStackTrace();
                    }
                } catch (IOException | ClassFileException e) {
                    AppData.getLog().error(e.getMessage());
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });

        listResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String className = listResults.getSelectedValue().split(" ")[1];
                    try {
                        decompiler.selectFile(className.concat(".class"));
                    } catch (IOException | ClassFileException e) {
                        AppData.getLog().error(e.getMessage());
                        JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}

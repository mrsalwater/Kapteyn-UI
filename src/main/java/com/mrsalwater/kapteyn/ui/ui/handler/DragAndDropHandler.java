package com.mrsalwater.kapteyn.ui.ui.handler;

import com.mrsalwater.kapteyn.ui.data.AppData;
import com.mrsalwater.kapteyn.ui.decompiler.Decompiler;
import com.mrsalwater.kapteyn.ui.ui.MainFrame;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class DragAndDropHandler extends TransferHandler {

    private final Decompiler decompiler;
    private final MainFrame frame;

    public DragAndDropHandler(Decompiler decompiler, MainFrame frame) {
        this.decompiler = decompiler;
        this.frame = frame;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.isFlavorJavaFileListType()) {
                return true;
            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        List<File> files;
        try {
            files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            return false;
        }


        SwingUtilities.invokeLater(() -> {
            for (File file : files) {
                try {
                    decompiler.openFile(file);
                } catch (IOException e) {
                    AppData.getLog().error(e.getMessage());
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }

                break;
            }
        });

        return true;
    }

}

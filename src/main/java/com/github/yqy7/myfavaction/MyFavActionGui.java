package com.github.yqy7.myfavaction;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.ShortcutSet;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import org.jetbrains.annotations.Nullable;

public class MyFavActionGui {
    private JPanel rootPanel;
    private JPanel selectPanel;
    private JPanel favPanel;

    public MyFavActionGui() {
        MyFavActionService myFavActionService = MyFavActionService.getInstance();
        ActionListModel selectListModel = myFavActionService.getAllActionListModel();
        ActionListModel favListModel = myFavActionService.getFavListModel();

        // selectPanel

        JBList<AnAction> selectJBList = new JBList<>(selectListModel);
        selectJBList.setCellRenderer(new MyFavActionListCellRenderer());
        selectJBList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = selectJBList.getSelectedIndex();
                    if (index < 0) {
                        return;
                    }

                    String actionId = selectListModel.get(index);
                    if (!favListModel.contains(actionId)) {
                        favListModel.add(actionId);
                    }

                    return;
                }

                super.mouseClicked(e);
            }
        });
        selectJBList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int index = selectJBList.getSelectedIndex();
                    if (index < 0) {
                        return;
                    }

                    String actionId = selectListModel.get(index);
                    if (!favListModel.contains(actionId)) {
                        favListModel.add(actionId);
                    }
                    return;
                }

                super.keyPressed(e);
            }
        });

        JPopupMenu selectPopupMenu = new JPopupMenu();
        selectPopupMenu.add(new AbstractAction("add to favorite") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getClass());

                int index = selectJBList.getSelectedIndex();
                if (index < 0) {
                    return;
                }

                String actionId = selectListModel.get(index);
                if (!favListModel.contains(actionId)) {
                    favListModel.add(actionId);
                }
            }
        });
        selectJBList.setComponentPopupMenu(selectPopupMenu);
        selectJBList.setToolTipText("Double click to add to favorite actions");
        selectJBList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        new ListSpeedSearch(selectJBList);
        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setFill(GridConstraints.FILL_BOTH);
        selectPanel.add(new JBScrollPane(selectJBList), gridConstraints);

        // favPanel

        JBList<AnAction> favJBList = new JBList(favListModel);
        favJBList.setCellRenderer(new MyFavActionListCellRenderer());
        favJBList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = favJBList.getSelectedIndex();
                    if (index < 0) {
                        return;
                    }

                    if (index < favListModel.getSize()) {
                        favListModel.remove(index);
                    }
                    return;
                }

                super.mouseClicked(e);
            }

        });

        favJBList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int index = favJBList.getSelectedIndex();
                    if (index < 0) {
                        return;
                    }

                    if (index < favListModel.getSize()) {
                        favListModel.remove(index);
                    }
                    return;
                }

                super.keyPressed(e);
            }
        });

        favJBList.setDragEnabled(true);
        favJBList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        favJBList.setTransferHandler(new TransferHandler() {
            private int insertIndex;
            private int selectedIndex;

            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            @Nullable
            @Override
            protected Transferable createTransferable(JComponent c) {
                selectedIndex = favJBList.getSelectedIndex();
                if (selectedIndex < 0) {
                    return null;
                }

                String actionId = favListModel.get(selectedIndex);
                return new StringSelection(actionId);
            }

            @Override
            public boolean importData(TransferSupport support) {
                JList.DropLocation dropLocation = (JList.DropLocation)support.getDropLocation();
                insertIndex = dropLocation.getIndex();
                return true;
            }

            @Override
            public boolean canImport(TransferSupport support) {
                return true;
            }

            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {
                if (insertIndex < 0 || selectedIndex < 0) {
                    return;
                }

                String selectedActionId = favListModel.get(selectedIndex);
                favListModel.add(insertIndex, selectedActionId);
                if (selectedIndex <= insertIndex) {
                    favListModel.remove(selectedIndex);
                } else if (selectedIndex > insertIndex) {
                    favListModel.remove(selectedIndex + 1);
                }
            }
        });

        JPopupMenu favPopupMenu = new JPopupMenu();
        favPopupMenu.add(new AbstractAction("delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = favJBList.getSelectedIndex();
                if (selectedIndex < 0) {
                    return;
                }
                favListModel.remove(selectedIndex);
            }
        });
        favPopupMenu.add(new AbstractAction("delete all") {
            @Override
            public void actionPerformed(ActionEvent e) {
                favListModel.clear();
            }
        });
        favJBList.setComponentPopupMenu(favPopupMenu);
        favJBList.setToolTipText("Double click to delete");

        new ListSpeedSearch(favJBList);
        favPanel.add(new JBScrollPane(favJBList), gridConstraints);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private static class MyFavActionListCellRenderer implements ListCellRenderer<AnAction> {
        @Override
        public Component getListCellRendererComponent(JList<? extends AnAction> list, AnAction value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            String leftText = Util.getAnActionText(value);

            String actionId = ActionManager.getInstance().getId(value);
            String rightText = "#" + actionId;

            JBLabel leftLabel = new JBLabel(leftText);
            JBLabel rightLabel = new JBLabel(rightText);
            rightLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));
            rightLabel.setForeground(Color.GRAY);

            ShortcutSet shortcuts = KeymapUtil.getActiveKeymapShortcuts(actionId);
            String shortcutText = KeymapUtil.getPreferredShortcutText(shortcuts.getShortcuts());
            JBLabel shortCutLabel = new JBLabel(shortcutText);
            shortCutLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));

            JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jPanel.add(leftLabel);
            jPanel.add(rightLabel);
            jPanel.add(shortCutLabel);

            if (isSelected) {
                jPanel.setBackground(Color.lightGray);
            }
            return jPanel;
        }
    }

}

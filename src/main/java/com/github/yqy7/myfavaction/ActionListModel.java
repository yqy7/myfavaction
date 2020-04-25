package com.github.yqy7.myfavaction;

import java.util.List;

import javax.swing.*;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;

public class ActionListModel extends AbstractListModel<AnAction> {
    private List<String> actionIds;

    public ActionListModel(List<String> actionIds) {
        this.actionIds = actionIds;
    }

    @Override
    public int getSize() {
        return actionIds.size();
    }

    @Override
    public AnAction getElementAt(int index) {
        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.getAction(actionIds.get(index));
    }

    public String get(int index) {
        return actionIds.get(index);
    }

    public void add(String actionId) {
        int index = actionIds.size();
        actionIds.add(actionId);
        fireIntervalAdded(this, index, index);
    }

    public void add(int index, String actionId) {
        actionIds.add(index, actionId);
        fireIntervalAdded(this, index, index);
    }

    public boolean contains(String actionId) {
        return actionIds.contains(actionId);
    }

    public String remove(int index) {
        String actionId = actionIds.remove(index);
        fireIntervalRemoved(this, index, index);
        return actionId;
    }

    public void clear() {
        if (actionIds.size() == 0) {
            return;
        }
        int lastIndex = actionIds.size() - 1;
        actionIds.clear();
        fireContentsChanged(this, 0, lastIndex);
    }

}
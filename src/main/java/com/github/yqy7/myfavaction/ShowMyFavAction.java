package com.github.yqy7.myfavaction;

import java.util.List;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid;
import com.intellij.openapi.ui.popup.ListPopup;

public class ShowMyFavAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        MyFavActionService myFavActionService = MyFavActionService.getInstance();
        ActionManager actionManager = ActionManager.getInstance();
        List<String> actionIds = myFavActionService.getState().getActionIds();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionIds.stream().map(actionManager::getAction).forEach(actionGroup::add);

        ListPopup actionGroupPopup = JBPopupFactory.getInstance().createActionGroupPopup("Favorite Actions",
            actionGroup, e.getDataContext(), ActionSelectionAid.SPEEDSEARCH, false);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {
            actionGroupPopup.showInBestPositionFor(editor);
        } else {
            actionGroupPopup.showCenteredInCurrentWindow(e.getProject());
        }
    }
}

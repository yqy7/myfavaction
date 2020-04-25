package com.github.yqy7.myfavaction;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentFactory.SERVICE;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class MyFavActionToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        ContentFactory contentFactory = SERVICE.getInstance();
        MyFavActionGui myFavActionGui = new MyFavActionGui();
        Content content = contentFactory.createContent(myFavActionGui.getRootPanel(), "MyFavAction", false);
        contentManager.addContent(content);
    }
}

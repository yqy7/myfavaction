package com.github.yqy7.myfavaction;

import com.intellij.openapi.actionSystem.AnAction;

public class Util {
    public static String getAnActionText(AnAction anAction) {
        String text = anAction.getTemplateText();
        if (text == null || "".equals(text)) {
            text = anAction.getTemplatePresentation().getDescription();
        }
        if (text == null) {
            text = "";
        }
        return text;
    }
}

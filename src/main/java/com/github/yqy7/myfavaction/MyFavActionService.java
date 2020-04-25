package com.github.yqy7.myfavaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.yqy7.myfavaction.MyFavActionService.Data;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "MyFavActionData", storages = @Storage("myfavaction.xml"))
public class MyFavActionService implements PersistentStateComponent<Data> {

    private Data data = new Data();

    private ActionListModel favListModel;
    private ActionListModel allActionListModel;

    public MyFavActionService() {
        favListModel = new ActionListModel(data.getActionIds());

        ActionManager actionManager = ActionManager.getInstance();
        String[] actionIds = actionManager.getActionIds("");
        List<String> actionIdList = Arrays.asList(actionIds);
        Collections.sort(actionIdList, (o1, o2) -> {
            AnAction action1 = actionManager.getAction(o1);
            AnAction action2 = actionManager.getAction(o2);
            String text1 = Util.getAnActionText(action1);
            String text2 = Util.getAnActionText(action2);
            if ("".equals(text1) && "".equals(text2)) {
                return 0;
            } else if ("".equals(text1)) {
                return 1;
            } else if ("".equals(text2)) {
                return -1;
            } else {
                return text1.compareTo(text2);
            }
        });
        allActionListModel = new ActionListModel(actionIdList);
    }

    @Nullable
    @Override
    public Data getState() {
        return data;
    }

    @Override
    public void loadState(@NotNull Data state) {
        this.data = state;
        favListModel = new ActionListModel(data.getActionIds());
    }

    public ActionListModel getFavListModel() {
        return favListModel;
    }

    public synchronized ActionListModel getAllActionListModel() {
        return allActionListModel;
    }

    public static MyFavActionService getInstance() {
        return ServiceManager.getService(MyFavActionService.class);
    }

    public static class Data {
        private List<String> actionIds = new ArrayList<>();

        public List<String> getActionIds() {
            return actionIds;
        }

        public void setActionIds(List<String> actionIds) {
            this.actionIds = actionIds;
        }
    }
}

package org.jetbrains.contest.historyNavigator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Collections;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Date: 28.09.2006
 * Time: 16:26:38
 */
public abstract class HistoryListAction extends AnAction {

    protected void showHistoryPopup(AnActionEvent e, LinkedList places, final Project project, String title) {
        DataContext datacontext = e.getDataContext();
        Component component;
        Point point;
        if (e.getInputEvent() instanceof MouseEvent) {
            component = SwingUtilities.getAncestorOfClass(JFrame.class, (Component) e.getInputEvent().getSource());
            point = ((MouseEvent)e.getInputEvent()).getPoint();
            point = SwingUtilities.convertPoint((Component) e.getInputEvent().getSource(), point, component);
        } else {
            component = (Component) datacontext.getData(DataConstants.CONTEXT_COMPONENT);
        }

        Collections.reverse(places);
        final JList jList = new JList(places.toArray());
        PopupChooserBuilder listPopupBuilder = JBPopupFactory.getInstance().createListPopupBuilder(jList);
        listPopupBuilder.setItemChoosenCallback(new Runnable() {
            public void run() {
                Object placeInfoRaw = jList.getSelectedValue();
                PlaceInfo placeInfo = new PlaceInfo(placeInfoRaw);
                FileEditorManagerEx fileEditorManager = FileEditorManagerEx.getInstanceEx(project);
                ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
                boolean flag = toolWindowManager.isEditorComponentActive();
                Pair pair = fileEditorManager.openFileWithProviders(placeInfo.getFile(), flag);
                FileEditor afileeditor[] = (FileEditor[]) pair.getFirst();
                FileEditorProvider afileeditorprovider[] = (FileEditorProvider[]) pair.getSecond();
                for (int i1 = 0; i1 < afileeditor.length; i1++) {
                    String s1 = afileeditorprovider[i1].getEditorTypeId();
                    if (s1.equals(placeInfo.getEditorTypeId()))
                        afileeditor[i1].setState(placeInfo.getNavigationState());
                    break;
                }
            }
        });
        listPopupBuilder.setTitle(title);
        listPopupBuilder.createPopup().showUnderneathOf(component);
    }

    private class PlaceInfo {
        private Object placeInfoRaw;

        public PlaceInfo(Object placeInfoRaw) {
            this.placeInfoRaw = placeInfoRaw;
        }

        private <T> T reflectionGetterHelper(String getterName) {
            try {
                Method method = placeInfoRaw.getClass().getMethod(getterName);
                method.setAccessible(true);
                Object result = method.invoke(placeInfoRaw);
                return (T) result;
            } catch (Exception e) {
            }
            return null;
        }

        public VirtualFile getFile() {
            return reflectionGetterHelper("getFile");
        }

        public Object getEditorTypeId() {
            return reflectionGetterHelper("getEditorTypeId");
        }

        public FileEditorState getNavigationState() {
            return reflectionGetterHelper("getNavigationState");
        }
    }

}

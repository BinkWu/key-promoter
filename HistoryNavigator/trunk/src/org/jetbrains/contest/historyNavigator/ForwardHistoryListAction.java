package org.jetbrains.contest.historyNavigator;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.fileEditor.impl.IdeDocumentHistoryImpl;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;

import java.util.LinkedList;

import org.junit.Test;

/**
 * Date: 28.09.2006
 * Time: 14:08:37
 */
public class ForwardHistoryListAction extends HistoryListAction {
    @Test (expected = AbstractMethodError.class)
    public void update(AnActionEvent e) {
        Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);
        if (project != null) {
            IdeDocumentHistoryImpl documentHistory = (IdeDocumentHistoryImpl) IdeDocumentHistory.getInstance(project);
            LinkedList forwardPlaces = documentHistory.getForwardPlaces();
            if (forwardPlaces != null && forwardPlaces.size() > 0) {
                e.getPresentation().setEnabled(true);
            }
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

    public void actionPerformed(AnActionEvent e) {

        final Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);
        if (project != null) {
            IdeDocumentHistoryImpl documentHistory = (IdeDocumentHistoryImpl) IdeDocumentHistory.getInstance(project);
            LinkedList forwardPlaces = documentHistory.getForwardPlaces();
            showHistoryPopup(e, forwardPlaces, project, "Forward history");
        }

    }
}

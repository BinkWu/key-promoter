package org.jetbrains.contest.historyNavigator;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.fileEditor.impl.IdeDocumentHistoryImpl;
import com.intellij.openapi.project.Project;

import java.util.LinkedList;

/**
 * Date: 28.09.2006
 * Time: 14:07:08
 */
public class BackHistoryListAction extends HistoryListAction {

    public void update(AnActionEvent e) {
        Project project = (Project) e.getDataContext().getData(DataConstants.PROJECT);
        if (project != null) {
            IdeDocumentHistoryImpl documentHistory = (IdeDocumentHistoryImpl) IdeDocumentHistory.getInstance(project);
            LinkedList backPlaces = documentHistory.getBackPlaces();
            if (backPlaces != null && backPlaces.size() > 0) {
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
            LinkedList backPlaces = documentHistory.getBackPlaces();

            showHistoryPopup(e, backPlaces, project, "Back history");
        }

    }

}

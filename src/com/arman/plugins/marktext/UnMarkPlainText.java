package com.arman.plugins.marktext;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeFactory;
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class UnMarkPlainText extends AnAction {

    private Project project;

    public UnMarkPlainText() {

    }

    @Override
    public void update(AnActionEvent e) {
        this.project = (Project) e.getDataContext().getData(CommonDataKeys.PROJECT.getName());
        VirtualFile[] files = (VirtualFile[]) e.getDataContext().getData(CommonDataKeys.VIRTUAL_FILE_ARRAY.getName());
        Presentation presentation = e.getPresentation();
        if (files != null && files[0].exists() && EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(files[0])) {
            presentation.setVisible(true);
            presentation.setIcon(EnforcedPlainTextFileTypeFactory.ENFORCED_PLAIN_TEXT_ICON);
        } else {
            presentation.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.project = (Project) e.getDataContext().getData(CommonDataKeys.PROJECT.getName());
        VirtualFile[] files = (VirtualFile[]) e.getDataContext().getData(CommonDataKeys.VIRTUAL_FILE_ARRAY.getName());
        if (files != null && files[0].exists()) {
            for (VirtualFile file : files) {
                unmark(file);
            }
        }
    }

    private boolean unmark(VirtualFile file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            for (VirtualFile f : file.getChildren()) {
                unmark(f);
            }
        }
        EnforcedPlainTextFileTypeManager typeManager = EnforcedPlainTextFileTypeManager.getInstance();
        if (typeManager != null && typeManager.isMarkedAsPlainText(file)) {
            typeManager.resetOriginalFileType(project, file);
            return true;
        }
        return false;
    }

}

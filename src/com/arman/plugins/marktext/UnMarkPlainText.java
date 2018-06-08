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
        VirtualFile file = (VirtualFile) e.getDataContext().getData(CommonDataKeys.VIRTUAL_FILE.getName());
        Presentation presentation = e.getPresentation();
        if (file != null && file.exists() && EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(file)) {
            presentation.setVisible(true);
            presentation.setIcon(EnforcedPlainTextFileTypeFactory.ENFORCED_PLAIN_TEXT_ICON);
        } else {
            presentation.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.project = (Project) e.getDataContext().getData(CommonDataKeys.PROJECT.getName());
        VirtualFile plainFile = (VirtualFile) e.getDataContext().getData(CommonDataKeys.VIRTUAL_FILE.getName());
        if (plainFile != null && plainFile.exists()) {
            unmark(plainFile);
        }
    }

    private boolean unmark(VirtualFile file) {
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        EnforcedPlainTextFileTypeManager typeManager = EnforcedPlainTextFileTypeManager.getInstance();
        if (typeManager != null && typeManager.isMarkedAsPlainText(file)) {
            typeManager.resetOriginalFileType(project, file);
            return true;
        }
        return false;
    }

}

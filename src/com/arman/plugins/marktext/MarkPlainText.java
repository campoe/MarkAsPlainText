package com.arman.plugins.marktext;

import com.intellij.ide.scratch.ScratchUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeFactory;
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWithId;
import org.jetbrains.annotations.Nullable;

public class MarkPlainText extends AnAction {

    private Project project;

    public MarkPlainText() {

    }

    @Override
    public void update(AnActionEvent e) {
        this.project = (Project) e.getDataContext().getData(CommonDataKeys.PROJECT.getName());
        VirtualFile file = (VirtualFile) e.getDataContext().getData(CommonDataKeys.VIRTUAL_FILE.getName());
        Presentation presentation = e.getPresentation();
        if (file != null && file.exists() && !EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(file)) {
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
            mark(plainFile);
        }
    }

    private boolean mark(VirtualFile file) {
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        EnforcedPlainTextFileTypeManager typeManager = EnforcedPlainTextFileTypeManager.getInstance();
        if (typeManager != null && isApplicableFor(file) && !typeManager.isMarkedAsPlainText(file)) {
            typeManager.markAsPlainText(project, file);
            return true;
        }
        return false;
    }

    private static boolean isApplicableFor(VirtualFile file) {
        if (file instanceof VirtualFileWithId && !file.isDirectory()) {
            if (ScratchUtil.isScratch(file)) {
                return false;
            } else {
                FileType originalType = FileTypeManager.getInstance().getFileTypeByFileName(file.getName());
                return !originalType.isBinary() && originalType != FileTypes.PLAIN_TEXT;
            }
        } else {
            return false;
        }
    }

}

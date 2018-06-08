package com.arman.plugins.marktext;

import com.intellij.ide.scratch.ScratchUtil;
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeFactory;
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWithId;

public class Util {

    public static boolean containsPlainTextFile(VirtualFile file) {
        if (!file.isDirectory()) {
            return EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(file);
        }
        for (VirtualFile f : file.getChildren()) {
            if (containsPlainTextFile(f)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isApplicableFor(VirtualFile file) {
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

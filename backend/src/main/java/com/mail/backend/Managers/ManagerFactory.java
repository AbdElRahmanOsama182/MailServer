package com.mail.backend.Managers;

import com.mail.backend.Managers.ManagerInterface;

public class ManagerFactory {
    public static ManagerInterface getManager(String managerName) {
        switch (managerName) {
            case "UserManager":
                return UserManager.getInstance();
            case "EmailManager":
                return EmailManager.getInstance();
            case "FolderManager":
                return FolderManager.getInstance();
            case "ContactManager":
                return ContactManager.getInstance();

            case "AttachmentManager":
                return AttachmentManager.getInstance();
            default:
                return null;
        }
    }
}

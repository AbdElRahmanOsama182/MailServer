package com.mail.backend.Managers;

public class ManagerFactory {
    public static ManagerInterface getManager(String managerName){
        switch(managerName){
            case "UserManager":
                return UserManager.getInstance();
            case "EmailManager":
                return EmailManager.getInstance();
            case "FolderManager":
                return FolderManager.getInstance();
            case "ContactManager":
                return ContactManager.getInstance();
            default:
                return null;
        }
    }
}

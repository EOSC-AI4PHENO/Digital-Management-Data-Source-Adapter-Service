package com.siseth.adapter.ssh;

import com.siseth.adapter.constant.AppProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateNewFTPUser {

    private SSHConnection connection;

    private String command = "echo -e \"__sudoPassword__\" | sudo -S /ftp/create_user.sh __user__ __password__ __folderName__";

    private String commandToExec;
    public CreateNewFTPUser() {
        this.connection = new SSHConnection();
        this.commandToExec = this.command.replace("__sudoPassword__", AppProperties.SSH_PASSWORD);
    }

    public CreateNewFTPUser createUser(String user, String password) {
        this.commandToExec = this.commandToExec
                .replaceAll("__user__", user)
                .replaceAll("__folderName__", user)
                .replaceAll("__password__", password);
        System.out.println("COMMAND: " + this.commandToExec);
        this.connection.sendCommand(commandToExec);
        return this;
    }

}

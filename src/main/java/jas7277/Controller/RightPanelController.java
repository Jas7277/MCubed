package jas7277.Controller;

import jas7277.Model.ConsoleHelper;
import jas7277.Model.FileManager;
import jas7277.Model.ServerProcesses;

import java.awt.*;

public class RightPanelController {
    ConsoleHelper helper;

    public RightPanelController() {
        helper = new ConsoleHelper();
    }

    public void SendButtonClicked(String command) {
        if (ServerProcesses.processes.isEmpty()) {
            helper.AppendConsole("There are currently no servers running!", Color.RED);
            return;
        }

        helper.ProcessStarted(ServerProcesses.processes.getFirst());
        helper.SendCommand(command + "\n");
    }
}

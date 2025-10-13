package jas7277.View;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new LeftPanel(), new RightPanel());
        splitPane.setDividerLocation(300);

        add(splitPane);
    }
}

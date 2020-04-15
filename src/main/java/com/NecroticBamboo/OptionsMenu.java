package com.NecroticBamboo;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class OptionsMenu {
    private static Screen screen = null;

    private Options options;

    public OptionsMenu(Screen screenIn, Options optionsIn) {
        screen = screenIn;
        options = optionsIn;
    }

    public void selectOptions() {
        try {
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            screen.startScreen();

            final Window window = new BasicWindow("Options Window");
            Panel contentPanel = new Panel(new GridLayout(1));

            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);

            CheckBox borders = new CheckBox("Remove borders");
            borders.setChecked(options.getBorderOption());
            contentPanel.addComponent(borders);

            CheckBox doublePoints = new CheckBox("Enable double points");
            doublePoints.setChecked(options.getDoublePointsOption());
            contentPanel.addComponent(doublePoints);

            contentPanel.addComponent(new Button("BACK", window::close));

            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);

            CheckOptions(borders, doublePoints);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CheckOptions(CheckBox borders, CheckBox doublePoints) {
        options.setBorderOption(borders.isChecked());
        options.setDoublePointsOption(doublePoints.isChecked());
    }
}

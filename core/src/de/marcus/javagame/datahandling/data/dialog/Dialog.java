package de.marcus.javagame.datahandling.data.dialog;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class Dialog {

    private String dialogTitle;
    private String dialogText;
    private List<String> buttonTexts;
    private List<Dialog> nextDialogs;


    public Dialog(String title, String dialogText, List<String> buttonTexts, List<Dialog> nextDialogs, boolean topDialog) {
        this.dialogTitle = Objects.requireNonNullElse(title, "");
        this.dialogText = Objects.requireNonNullElse(dialogText, "");
        this.buttonTexts = Objects.requireNonNullElse(buttonTexts, Arrays.asList("", "", ""));
        this.nextDialogs = Objects.requireNonNullElse(nextDialogs, new ArrayList<>());
        if (topDialog) {
            overwriteTitles(this);
        }
    }

    public Dialog getNextDialog(int dialog) {
        return nextDialogs.get(dialog);
    }

    public void overwriteTitles(Dialog dialog) {
        List<Dialog> nextDials = dialog.getNextDialogs();
        System.out.println("next dials are " + nextDials);
        if (nextDials != null) {
            for (Dialog dialogInLoop : nextDials) {
                System.out.println("--------------");
                System.out.println("Current title for " + dialogInLoop.getDialogText() + " is " + dialogInLoop.getDialogTitle());
                if (dialogInLoop.getDialogTitle().equalsIgnoreCase("") || dialogInLoop.getDialogTitle() == null) {
                    System.out.println("overwriting title for " + dialogInLoop.getDialogText() + " with " + dialogTitle);
                    dialogInLoop.setDialogTitle(dialogTitle);
                    overwriteTitles(dialogInLoop);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "dialogTitle='" + dialogTitle + '\'' +
                ", dialogText='" + dialogText + '\'' +
                ", nextDialogs=" + nextDialogs.size() +
                "}  \n";
    }
}

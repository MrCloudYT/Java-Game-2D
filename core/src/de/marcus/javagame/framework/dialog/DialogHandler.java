package de.marcus.javagame.framework.dialog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.marcus.javagame.framework.data.SavedataHandler;
import de.marcus.javagame.player.inventory.InventoryItem;
import de.marcus.javagame.framework.shop.Shops;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DialogHandler {

    @JsonIgnore
    private Dialogs currentDialogType;

    @JsonIgnore
    private boolean isDialogActive;
    @JsonIgnore
    private Dialog currentDialog;

    @JsonIgnore
    private DialogWindow window;

    private DialogCompletionData dialogCompletionData;

    public DialogHandler(DialogWindow window) {
        isDialogActive = false;
        currentDialog = Dialogs.WEAPON_SHOP_DIALOG.dialog;
        this.window = window;
        dialogCompletionData = SavedataHandler.load(DialogCompletionData.class);

    }

    public boolean hasDialogBeenCompletedBefore(Dialogs dialog) {
        return dialogCompletionData.getCompletedDialogs().contains(dialog);
    }

    public boolean hasCurrentDialogBeenCompletedBefore() {
        return dialogCompletionData.getCompletedDialogs().contains(currentDialogType);
    }

    public Dialog dialogButtonPressed(int buttonNumber) {
        if (buttonNumber < currentDialog.getNextDialogs().size()) {
            Dialog nextDialog = currentDialog.getNextDialog(buttonNumber);
            this.currentDialog = nextDialog;
            return nextDialog;
        } else {
            return null;
        }
    }

    public void setCurrentDialog(Dialogs currentDialog) {
        isDialogActive = true;
        this.currentDialog = currentDialog.dialog;
        this.currentDialogType = currentDialog;
        window.setVisible(true);
        window.setDialogMenuOptions(currentDialog.dialog);
    }

    public boolean isDialogFinished() {
        boolean empty = currentDialog.getNextDialogs().isEmpty();
        System.out.println("This dialog is now " + empty);
        this.isDialogActive = !empty;
        if (!isDialogActive) {

            System.out.println("Overwriting dialog");
        }
        return empty;
    }


    /**
     * Wie erstellt man einen Dialog?
     * <br><br>
     *
     * <p>1. Kopiere den {@link DialogHandler.Dialogs#DIALOG_DEFAULT}</p><br>
     * <p>2. Welche Variablen? <br>
     * <p style="text-indent:40px">2.1. DialogTitle der oben links im Dialog angezeigt werden.<b style="color:orange">!!! Muss nur für die Klasse gesetzt werden, bei der .markAsTop aufgerufen würde(oberste)!!!</b></p>
     * <p style="text-indent:40px">2.2 DialogText der Text über den der NPC redet</p>
     * <p style="text-indent:40px">  2.3 ButtonTexts: (Optional) Diese Methode muss nur aufgerufen werden, wenn du Buttons haben willst in diesem Dialog.
     * Wenn du nur Text willst (z.B. wenn es der letzte Dialog der Reihe ist oder ein Monolog) dann brauchst du die Methode
     * .setButtonTexts(); nicht aufzurufen</p>
     * <p style="text-indent:40px"> 2.4 NextDialogs: (Optional: Nur wenn Buttons Texts gesetzt wurden). Der erste Button Text führt zum ersten Dialog
     * der zweite zum zweiten etc. <b style="color:red">ES KANN NIE MEHR ALS 3 GEBEN!!!!!</b></p>
     * <br>
     * 3. Structure
     * <p style="text-indent:40px"> 3.1 Die folgende Struktur muss immer existieren:
     * new DialogBuilder()
     * .setDialogTitle("This is the title")
     * .setDialogText("This is some text")
     * .markAsTop()
     * .createDialog()
     * </p>
     * <p>
     * Das ist die Basisstruktur. Dies wird einen Dialog erzeugen, welcher nur Text, einen Titel aber keine Überschrift hat.
     * Wenn der Dialog fertig erzählt ist, wird dir gesagt, du kannst Enter drücken, um den Dialog zu schließen
     *
     * <br>
     *
     * <p>
     * <br>
     * 4. Events
     * <br>
     * Verfügbare Events:<br>
     * GiftItem, OpenShop, GiftMoney, DialogFinished<br><br>
     * Für alle musst du ein zweites Event aufrufen<br>
     * <p>
     * Bei GiftItem muss das zweite Event den Namen eines Items aus {@link InventoryItem} benutzen (SELBE SCHREIBWEISE) oder einen Shop aus {@link Shops}
     * oder die Menge an Geld bei GiftMoney (Als Ganzzahl!) oder bei DialogFinished den Name den Dialogs.
     * <br>
     * Events ruft man auf indem man {EVENT=EVENTNAME} in den Text schreibt. Es empfiehlt sich, davor eine Sekunde zu warten, damit der Spieler Zeit hat, den Dialog zu ende zu lesen.
     * <br>
     * Example: "Here you go.{WAIT=1} {EVENT=GiftItem} {EVENT=HEAL_POTION}"
     *
     * </p>
     */
    @Getter
    public enum Dialogs {
        TEST_DIALOG
                (
                        new DialogBuilder().
                                setDialogTitle("Fortnite").
                                setDialogText("Ich liebe Fortnite").
                                setButtonTexts("ich auch", "Halts maul", "Für fortnite").
                                setNextDialogs(new DialogBuilder().
                                        setDialogText("").
                                        createDialog()).markAsTop().createDialog()),

        DIALOG_DEFAULT(
                new DialogBuilder().
                        setDialogTitle("NPC1").
                        setDialogText("Ich bin ein tolles NPC und ich esse Katzen").
                        setButtonTexts("Text2", "test3", "test4").
                        setNextDialogs(
                                new DialogBuilder().
                                        setDialogText("Cat text because why not?").
                                        createDialog(),
                                new
                                        DialogBuilder().
                                        setDialogText("Cat text because why not?2").
                                        createDialog(),
                                new
                                        DialogBuilder().
                                        setDialogText("Cat text because why not?3").
                                        createDialog()

                        )
                        .markAsTop()
                        .createDialog()
        ),
        WEAPON_SHOP_DIALOG(
                new DialogBuilder()
                        .setDialogTitle("Waffenhändler")
                        .setDialogText("Oh, neue Kundschaft. Wie großartig! Was kann ich für dich tun?")
                        .setButtonTexts("Ich schaue mich erstmal um", "Ich bräuchte Hilfe, um Ausrüstung zusammenzustellen", "Ich sehe hier nicht interessantes. Auf Wiedersehen!")
                        .setNextDialogs(
                                new DialogBuilder().
                                        setDialogText("Lass mich wissen, wenn du Hilfe brauchst!{WAIT=1} {EVENT=OpenShop,POTION_SHOP}").
                                        setAsDefaultDialog().
                                        createDialog(),
                                new DialogBuilder().
                                        setDialogText("Gerne! Wofür brauchst du denn Ausrüstung?")
                                        .setButtonTexts("Das geht sie nichts an! ", "Ich möchte in die Fußstapfen meines Vaters treten.", "Ich muss mich verteidigen können.")
                                        .setNextDialogs(
                                                new DialogBuilder().
                                                        setDialogText("Entschuldigung, ich wollte dir nicht zu nahe treten … \n Mit einem Schwert kann man nie was falsch machen.{WAIT=1} {EVENT=OpenShop,EQUIPMENT_SHOP}").
                                                        createDialog(),
                                                new DialogBuilder().
                                                        setDialogText("Viel Glück dabei! Ich denke dieses Schwert könnte dir sicherlich dabei helfen.{WAIT=1} {EVENT=GiftItem,STARTER_SWORD}").
                                                        createDialog(),
                                                new DialogBuilder().
                                                        setDialogText("Angriff ist die beste Verteidigung, daher würde ich dir das Schwert aus dem Shop empfehlen.{WAIT=1}{EVENT=OpenShop}{EVENT=DialogFinished}").
                                                        createDialog()
                                        ).
                                        createDialog(),
                                new DialogBuilder().
                                        setDialogText("Schönen Tag noch, Auf Wiedersehen!").
                                        setAsDefaultDialog().
                                        createDialog()
                        )
                        .markAsTop()
                        .createDialog()


        ), POTION_SHOP_DIALOG(new DialogBuilder().
                setDialogTitle("Torben - Tränke").
                setDialogText("Guten Tag Der Herr, was kann ich für sie tun?").
                setButtonTexts("Ich benötige Beratung bei den Tränken.", "Ich möchte mich erstmals ein wenig umschauen.", "Wie viel kosten die Tränke jeweils?  ").
                setNextDialogs(
                        new DialogBuilder().
                                setDialogText("Das kommt auf die Situation an in der sie sich befinden." +
                                        "\nManchmal muss man sich schnell regenerieren, ein andermal sollte man besser schneller sein als der Gegner und manchmal ist es von Vorteil Stärker zu sein als er.{WAIT=1}{EVENT=OpenShop,POTION_SHOP}").
                                setAsDefaultDialog().
                                createDialog(),
                        new DialogBuilder().
                                setDialogText("Falls sie Hilfe benötigen, geben sie mir bescheid. {WAIT=1}{EVENT=OpenShop,POTION_SHOP}").
                                setAsDefaultDialog()
                                .createDialog(),
                        new DialogBuilder().
                                setDialogText("Der Heiltrank kostet 5, der Stärketrank kostet 10 und der Geschwindigkeitstrank kostet 5. {WAIT=1}{EVENT=OpenShop,POTION_SHOP}").
                                setAsDefaultDialog().
                                createDialog()
                ).
                markAsTop().createDialog()


        ),

        VILLAGER_DIALOG_1(new DialogBuilder().

                setDialogTitle("Alf - Dorfbewohner").

                setDialogText("Hallo, Fremder! Du kommst mir bekannt vor. …. Jetzt fällt es mir ein, du bist Ryu.").

                setButtonTexts("Ich glaube sie verwechseln mich.", "Woher kennen sie mich?", "Sie verwechseln mich, ich bin sein Bruder, Shyu.").

                setNextDialogs(
                        new DialogBuilder().

                                setDialogText("Oh, tut mir leid. Als Entschuldigung nimm diese Rhoades.").

                                createDialog(),
                        new

                                DialogBuilder().

                                setDialogText("Ich kannte deinen Vater. Ein toller Mann, ehrlicher Mann, ein guter Mann ein Mann mit Ehre, ein Mann mit Glauben. Nimm daher diese 3 Tränke.").

                                createDialog(),
                        new

                                DialogBuilder().

                                setDialogText("Ich wusste gar nicht das Ryu einen Bruder hat. Gib ihm diese Rüstung von mir. Euer Vater war ein Mann mit Idealen.").

                                createDialog(),
                        new DialogBuilder().
                                setDialogText("Touristen sind immer so unfreundlich!").
                                createDialog()


                ).
                markAsTop().
                createDialog()


        ),

        VILLAGER_DIALOG_2(new DialogBuilder().

                setDialogTitle("Quentin - Dorfbewohner").
                setDialogText("Hallo Fremder! Du siehst aus als könntest du Hilfe gebrauchen?").
                setButtonTexts("Ich suche jemanden ", "Kannten sie meinen Vater? Er war ein großer Magier. ", "Lass mich inn Ruhe!").
                setNextDialogs(
                        new DialogBuilder().
                                setDialogText("Wen suchst du?").
                                setButtonTexts("Ich suche einen Waffenhändler.", "Ich suche einen Tränkehändler", "Ich suche einen Kartographen").
                                setNextDialogs(
                                        new DialogBuilder().
                                                setDialogText("Da gibt es hier den besten. ").
                                                createDialog(),
                                        new DialogBuilder().
                                                setDialogText("Da gibt es hier den besten. ").
                                                createDialog(),
                                        new DialogBuilder().
                                                setDialogText("Da gibt es hier den besten. ").
                                                createDialog()

                                ).createDialog(),
                        new DialogBuilder().
                                setDialogText("Ah … Ryu jetzt erkenne ich dich. Natürlich kannte ich ihn. Er hatte das Herz am rechten Fleck. Nimm las Zeichen der Dankbarkeit diesen Trank der Stärke.")
                                .createDialog()).createDialog()
                ),

                VILLAGER_DIALOG_3(new DialogBuilder().
                        setDialogTitle("Lukas - Villager").
                        setDialogText("Du scheinst nicht von hier zu sein. Was führt dich in unser Dorf?").
                        setButtonTexts("Ich will inn die Fußstapfen meines Vaters treten.", "Dein Mundgeruch.", "Ich bin auf der Suche nach jemanden. Und bin hierhergekommen, um mich auf der Durchreise auszurüsten.").
                        setNextDialogs(
                                new DialogBuilder().
                                        setDialogText("Oh, Viel Glück dabei. Vielleicht helfen dir diese Rhoades.").
                                        createDialog(),
                                new DialogBuilder().
                                        setDialogText("So eine Unverschämtheit lass ich mir nicht bieten! Ich werde das allen hier im Dorf erzählen! Das wird teuer für dich!").
                                        createDialog(),
                                new DialogBuilder().
                                        setDialogText("Wir haben hier ein paar gute Händler. Lass mich dir unter die Arme greifen mit ein paar Rhoades.").
                                        createDialog()
                        ).
                        markAsTop().
                        createDialog()
                ),


        KARTOGRAPH(new DialogBuilder().
                setDialogTitle("Adrian - Kartograph").
                setDialogText("Hallo! Was kann ich für dich tun?").
                setButtonTexts("Hallo, mein Name ist Ryu. Ich habe erfahren das mein Vater bei ihnen sich vor einigen Jahren eine Karte gekauft hat. ", "", "").
                setNextDialogs(
                        new DialogBuilder().
                                setDialogText("Ryu? Ryu? ….. Ahhh, dein Vater hat mir von dir sehr stolz erzählt. Ich kann mich noch genau an die Karte erinnern, die er gekauft hat.").
                                setButtonTexts("", "", "Das ist super! Ich bräuchte nämlich eine Kopie der Karte. Er ist nämlich verschwunden und ich will ihn Wiederfinden.").
                                setNextDialogs(new DialogBuilder().
                                        setDialogText("Oh … Das tut mir leid! Dein Vater war ein großartiger Mensch, er hat unserem Dorf viel geholfen als er hier war. Ich hoffe du findest ihn wieder! Hier hast du die Kopie der Karte.").
                                        setButtonTexts("", "", "Vielen Dank! Was bin ich ihnen für die Kopie schuldig?").
                                        setNextDialogs(new DialogBuilder()
                                                .setDialogText("Ach lass gut sein kleiner! Viel Glück auf deiner Reise.").
                                                setButtonTexts("", "", "Vielen Dank! Auf Wiedersehen!").
                                                setNextDialogs(new DialogBuilder().
                                                        setDialogText("Dafür doch nnicht! Sei vorsichtig und wenn du deinen Vater " +
                                                                "gefunden hast müsst ihr in unserem Dorf vorbeikommen unnd wir trinken was zusammen.")
                                                        .createDialog())
                                                .createDialog())
                                        .createDialog()


                ).createDialog()).markAsTop().createDialog());

        Dialogs(Dialog dialog) {
            this.dialog = dialog;
        }

        private Dialog dialog;
        }
}



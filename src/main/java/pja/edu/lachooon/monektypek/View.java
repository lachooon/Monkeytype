package pja.edu.lachooon.monektypek;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class View {
    private final Stage primaryStage;
    BorderPane root;

    //-------------------------
    StackPane buttonPanel;

    Text languages;
    Text oneFive;
    Text twoZero;
    Text fourFive;
    Text sixZero;
    Text nineZero;
    Text twelveZero;
    Text thirtyZero;
    Rectangle backgroundButtons;
    //-------------------------
    StackPane writingPanel;

    TextFlow systemWriting;
    TextArea userWriting;

    //-------------------------
    HBox masthead;

    Text timerText;
    Text stopka;
    //-------------------------
    Stage languageBox;

    ListView<String> languageList;

    Text accept;
    Text cancel;

    private final Font myFont = Font.font("Courier New", FontWeight.BOLD, 11);

    public View(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.languageList = new ListView<>();
        this.accept = createText("Accept", myFont);
        this.cancel = createText("Cancel", myFont);
    }

    public void showMainWindow() {
        root = new BorderPane();
        root.setBackground(Background.fill(Color.rgb(30, 30, 30)));

        Scene scene = new Scene(root, 1000, 400);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Monektypek");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        buttonPanel = createButtonPanel();

        root.setTop(buttonPanel);
        BorderPane.setMargin(buttonPanel, new Insets(20));

        writingPanel = createWritingPanel();

        root.setCenter(writingPanel);

        masthead = createMasthead();
        root.setBottom(masthead);

        primaryStage.show();
    }

    public StackPane createWritingPanel() {
        systemWriting = new TextFlow();
        systemWriting.setMouseTransparent(true);
        systemWriting.setPrefWidth(800);
        systemWriting.setPrefHeight(35);
        systemWriting.setPadding(new Insets(10, 40, 10, 40));

        userWriting = new TextArea();
        userWriting.setFont(myFont);
        userWriting.setMouseTransparent(true);
        userWriting.setPrefWidth(550);
        userWriting.setPrefHeight(175);
        userWriting.setPadding(new Insets(10, 40, 10, 40));
        userWriting.setWrapText(true);

        StackPane temp = new StackPane();
        temp.getChildren().addAll(systemWriting, userWriting);
        return temp;
    }

    public HBox createMasthead() {
        HBox temp = new HBox(340);
        temp.setPrefWidth(800);
        temp.setPrefHeight(24);
        temp.setPadding(new Insets(10, 40, 10, 40));

        Text text = new Text("Timer: 45");
        text.setFont(Font.font("Courier New", FontPosture.ITALIC, 24));
        text.setFill(Color.rgb(4, 107, 177));
        timerText = text;

        Text stopka = new Text("Ctrl + Enter - restart test | Ctrl + P - pause | Esc - end test");
        stopka.setFont(myFont);
        stopka.setFill(Color.rgb(97, 99, 103));
        this.stopka = stopka;

        temp.getChildren().addAll(text, stopka);
        temp.setMargin(stopka, new Insets(6.5, 0, 6.5, 0));

        return temp;
    }

    public StackPane createButtonPanel() {
        //clock: 🕒

        languages = createLanguageBox("Languages", myFont);
        oneFive = createText("\uD83D\uDD52 15s", myFont);
        twoZero = createText("\uD83D\uDD52 20s", myFont);
        fourFive = createText("\uD83D\uDD52 45s", myFont);
        sixZero = createText("\uD83D\uDD52 60s", myFont);
        nineZero = createText("\uD83D\uDD52 90s", myFont);
        twelveZero = createText("\uD83D\uDD52 120s", myFont);
        thirtyZero = createText("\uD83D\uDD52 300s", myFont);

        backgroundButtons = new Rectangle(430, 50, Color.rgb(25, 25, 25));
        backgroundButtons.setArcHeight(20);
        backgroundButtons.setArcWidth(20);

        HBox temp = new HBox();
        temp.setSpacing(10);
        temp.setAlignment(Pos.CENTER);

        temp.getChildren().addAll(
                languages,
                oneFive,
                twoZero,
                fourFive,
                sixZero,
                nineZero,
                twelveZero,
                thirtyZero
        );

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);

        stackPane.getChildren().add(backgroundButtons);
        stackPane.getChildren().add(temp);

        return stackPane;
    }

    private Text createText(String value, Font myFont) {
        Text temp = new Text(value);
        temp.setFont(myFont);
        temp.setCursor(Cursor.CROSSHAIR);
        temp.setFill(Color.rgb(97, 99, 103));

        return temp;
    }

    private Text createLanguageBox(String value, Font myFont) {
        Text temp = new Text(value);
        temp.setFont(myFont);
        temp.setCursor(Cursor.CROSSHAIR);
        temp.setFill(Color.rgb(97, 99, 103));

        return temp;
    }

    public void languageBox() {
        this.languageBox = new Stage();

        GridPane myContentPane = new GridPane();

        Scene scene = new Scene(myContentPane);

        myContentPane.setBackground(Background.fill(Color.rgb(25, 25, 25)));

        languageList.setPrefHeight(300);

        scene.getStylesheets().add("languageListStyle.css");
        languageList.setStyle("-fx-control-inner-background: rgb(25, 25, 25);" +
                "-fx-padding: 10px, 10px, 10px, 10px;" +
                "-fx-border-color: rgb(25, 25, 25);" +
                "-fx-region-background: rgb(25, 25, 25);");

        languageList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setFont(myFont);
                } else {
                    setText(item);
                    setFont(myFont);
                }
            }
        });

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(15);
        buttons.setPadding(new Insets(10, 0, 20, 0));

        buttons.getChildren().addAll(accept, cancel);

        Region invisibleFocusRedirect = new Region();
        invisibleFocusRedirect.setPrefSize(0, 0);
        invisibleFocusRedirect.setMouseTransparent(true);

        myContentPane.add(languageList, 0, 0);
        myContentPane.add(buttons, 0, 1);
        myContentPane.add(invisibleFocusRedirect, 0, 2);

        languageBox.setScene(scene);
        languageBox.setOnShown(event -> invisibleFocusRedirect.requestFocus());
        languageBox.initStyle(StageStyle.TRANSPARENT);

        languageBox.show();
    }

    public void shadowButtonPanel() {
        Rectangle rec = new Rectangle(430, 50);
        rec.setFill(Color.TRANSPARENT);
        buttonPanel.getChildren().add(rec);
    }

    public void brightenButtonPanel() {
        buttonPanel.getChildren().remove(buttonPanel.getChildren().get(2));
    }

    public void addTextListener(Text text, EventHandler event, String s) {
        if (s.equals("hover")) {
            text.setOnMouseEntered(event);
        } else if (s.equals("out")){
            text.setOnMouseExited(event);
        } else if (s.equals("click")) {
            text.setOnMouseClicked(event);
        }
    }

    public void addLanguageListener(EventHandler event) {
        languages.setOnMouseClicked(event);
    }

    public void addAcceptTextListener(EventHandler event, String s) {
        if (s.equals("hover")) {
            accept.setOnMouseEntered(event);
        } else if (s.equals("out")){
            accept.setOnMouseExited(event);
        } else {
            accept.setOnMouseClicked(event);
        }
    }

    public void addCancelTextListener(EventHandler event, String s) {
        if (s.equals("hover")) {
            cancel.setOnMouseEntered(event);
        } else if (s.equals("out")){
            cancel.setOnMouseExited(event);
        } else {
            cancel.setOnMouseClicked(event);
        }
    }

    public boolean undoSystemWriting(int index) {
        Text text = (Text) systemWriting.getChildren().get(index);

        if (text.getFill() == Color.ORANGE) {
            systemWriting.getChildren().remove(text);
            return true;
        } else if (text.getFill() == Color.GREEN || text.getFill() == Color.RED) {
            text.setFill(Color.rgb(97, 99, 103));
        }

        return false;
    }

    public int undoSystemWritingWithSkip(int index) {
        ((Text) systemWriting.getChildren().get(index)).setFill(Color.rgb(97, 99, 103));

        int counter = 0;

        int i = 0;
        Text text = (Text) systemWriting.getChildren().get(index - i);

        while (text.getFill() != Color.RED && text.getFill() != Color.GREEN && text.getFill() != Color.rgb(97, 99, 103)) {
            text.setFill(Color.rgb(97, 99, 103));
            counter++;
            i++;
            text = (Text) systemWriting.getChildren().get(index - i);
        }

        counter--;
        return counter;
    }

    public void addUserInputListener(ChangeListener event) {
        userWriting.textProperty().addListener(event);
    }

    public void blueTextFill(Text text) {
        text.setFill(Color.rgb(4, 107, 177));
    }

    public void grayTextFill(Text text) {
        text.setFill(Color.rgb(97, 99, 103));
    }
}

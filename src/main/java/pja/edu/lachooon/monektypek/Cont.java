package pja.edu.lachooon.monektypek;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Cont {
    private final View thisView;
    private Model thisModel;

    private Timer timer;
    private boolean gameStart = true;

    boolean ctrlpress, enterpress, ppress, escpress;
    boolean isPaused = false;
    boolean preend = false;
    boolean isShadowed = false;

    int begin;
    int end;
    int lettersCounter = 0;

    int carry = 0;
    int tempCarry = 0;

    int skip = 0;

    Cont(Stage primaryStage) {
        thisView = new View(primaryStage);
        thisModel = new Model();

        thisView.showMainWindow();
        writeOnPanel();

        addBasicListeners();
        addWindowListeners();
    }

    private void addWindowListeners() {
        thisView.root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER -> enterpress = true;
                    case CONTROL -> ctrlpress = true;
                    case P -> ppress = true;
                    case ESCAPE -> escpress = true;
                }

                if (escpress) {
                    preend = true;
                    gameStart = false;
                    timer.stopTimer();
                    escpress = false;
                }

                if (ctrlpress && ppress) {
                    if (isPaused) {
                        gameStart = false;
                        timer = new Timer();
                        thisModel.timer = thisModel.tempTimer;
                        timer.start();

                        thisView.userWriting.setEditable(true);
                        isPaused = false;
                    } else {
                        gameStart = true;
                        thisModel.tempTimer = thisModel.timer;
                        timer.stopTimer();

                        thisView.userWriting.setEditable(false);
                        isPaused = true;
                    }
                }

                if (enterpress && ctrlpress) {
                    gameStart = true;

                    if (timer != null) {
                        timer.stopTimer();
                    }

                    thisModel.milis.clear();
                    thisModel.wpmPerSecond.clear();

                    thisView.writingPanel = thisView.createWritingPanel();
                    userInputListenerUwu();
                    thisView.root.setCenter(thisView.writingPanel);
                    writeOnPanel();

                    carry = 0;
                    skip = 0;

                    try {
                        Thread.sleep(20);
                        thisModel.timer = 45;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    thisView.timerText.setText("Timer: " + thisModel.timer);
                    thisView.userWriting.setEditable(true);
                    if (isShadowed) {
                        thisView.brightenButtonPanel();
                    }
                }
            }
        });

        thisView.root.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case ENTER -> enterpress = false;
                    case CONTROL -> ctrlpress = false;
                    case P -> ppress = false;
                    case ESCAPE -> escpress = false;
                }
            }
        });
    }

    private void addBasicListeners() {
        EventHandler hover;
        EventHandler out;

        for (int i = 0; i < 8; i++) {
            Text text = (Text) ((Pane) thisView.buttonPanel.getChildren().get(1)).getChildren().get(i);

            hover = event -> thisView.blueTextFill(text);
            out = event -> thisView.grayTextFill(text);
            thisView.addTextListener(text, hover, "hover");
            thisView.addTextListener(text, out, "out");
        }

        hover = event -> {
            thisView.blueTextFill(thisView.accept);
        };
        out = event -> {
            thisView.grayTextFill(thisView.accept);
        };

        EventHandler click = event -> {
            thisModel.setSelectedLanguage(thisView.languageList.getSelectionModel().getSelectedItem());
            thisModel.setSelectedLanguageBefore(thisModel.getSelectedLanguage());
            thisModel.setCurrentLanguage(thisModel.getSelectedLanguage());

            thisView.writingPanel = thisView.createWritingPanel();
            userInputListenerUwu();
            thisView.root.setCenter(thisView.writingPanel);
            writeOnPanel();

            thisView.brightenButtonPanel();
            thisView.languageBox.close();
        };

        thisView.addAcceptTextListener(hover, "hover");
        thisView.addAcceptTextListener(out, "out");
        thisView.addAcceptTextListener(click, "click");

        hover = event -> {
            thisView.blueTextFill(thisView.cancel);
        };
        out = event -> {
            thisView.grayTextFill(thisView.cancel);
        };

        click = event -> {
            thisModel.setSelectedLanguage(thisModel.getSelectedLanguageBefore());
            thisView.brightenButtonPanel();
            thisView.languageBox.close();
        };

        thisView.addCancelTextListener(hover, "hover");
        thisView.addCancelTextListener(out, "out");
        thisView.addCancelTextListener(click, "click");

        EventHandler open = event -> {
            thisView.languageList.getItems().addAll(DictionaryLoader.toStringArray(thisModel.getAvailableLanguages()));
            thisView.shadowButtonPanel();
            thisView.languageBox();
        };

        thisView.addLanguageListener(open);
        userInputListenerUwu();

        EventHandler press;
        int tab[] = {15, 20, 45, 60, 90, 120, 300};

        for (int i = 1; i < 8; i++) {
            Text text = (Text) ((Pane) thisView.buttonPanel.getChildren().get(1)).getChildren().get(i);
            int temp = tab[i - 1];

            press = event -> {
                thisView.timerText.setText("Timer: " + temp);
                thisModel.timer = temp;
            };

            thisView.addTextListener(text, press, "click");
        }
    }

    public void writeOnPanel() {
        Font font = Font.font("Courier New", FontWeight.MEDIUM, 24);

        thisModel.createContent();

        for (int i = 0; i < thisModel.content.length(); i++) {
            Text temp = new Text(String.valueOf(thisModel.content.charAt(i)));
            temp.setFont(font);
            temp.setFill(Color.rgb(97, 99, 103));
            thisView.systemWriting.getChildren().add(temp);
        }

    }

    private void userInputListenerUwu() {
        ChangeListener change = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.length() >= 1 && gameStart) {
                    gameStart = false;
                    timer = new Timer();
                    timer.start();
                    thisView.shadowButtonPanel();
                    isShadowed = true;
                }

                if (t1.length() == 1 || t1.charAt(t1.length() - 2) == ' ') {
                    begin = timer.getCurrTime();
                }

                if(t1.charAt(t1.length() - 1) == ' ') {
                    end = timer.getCurrTime();
                    thisModel.milis.add(end - begin);
                }

                if (t1.charAt(t1.length() - 1) == thisModel.content.charAt(t1.length() - carry + skip - 1)) {
                    lettersCounter++;
                }

                int index;
                thisModel.userInput = t1;

                if (t1.length() < s.length()) {
                    index = s.length() - 1;

                    boolean decrementCarry = false;

                    if (s.charAt(s.length() - 1) == ' ') {
                        skip -= thisView.undoSystemWritingWithSkip(index + skip);
                    } else {
                        decrementCarry = thisView.undoSystemWriting(index + skip);
                    }

                    if (decrementCarry) {
                        carry--;
                    }
                } else {
                    char character = t1.charAt(t1.length() - 1);
                    tempCarry = fillingSystemWriting(character, t1.length() - carry + skip - 1, thisModel.content);

                    if (tempCarry != -1) {
                        carry = tempCarry;
                    }

                }
            }
        };

        thisView.addUserInputListener(change);
    }

    public int fillingSystemWriting(char character, int index, String content) {
        Text temp = (Text) thisView.systemWriting.getChildren().get(index + carry);
        LetterJump lj = new LetterJump(temp);

        if (character == content.charAt(index)) {
            temp.setFill(Color.GREEN);
        } else if (character != ' ' && content.charAt(index) == ' ') {
            return repetitionUpdater();
        } else if (character == ' ' && content.charAt(index) != ' '){
            skipUpdater(temp);
        } else {
            temp.setFill(Color.RED);
        }

        lj.waveAnimation();

        return -1;
    }

    public void skipUpdater(Text temp) {
        String[] contentTemp = thisModel.content.split(" ");
        String[] inputTemp = thisModel.userInput.split(" ");

        String inputWord = inputTemp[inputTemp.length - 1];
        String contentWord = contentTemp[inputTemp.length - 1];

        int toPaint = contentWord.length() - inputWord.length();
        int startIndex = thisView.systemWriting.getChildren().indexOf(temp);

        for (int i = 0; i < toPaint; i++) {
            Text text = (Text) thisView.systemWriting.getChildren().get(startIndex + i);

            text.setFill(Color.BLACK);
            skip++;
        }
    }

    public int repetitionUpdater() {
        Font font = Font.font("Courier New", FontWeight.MEDIUM, 24);

        thisView.systemWriting.getChildren().clear();

        String[] contentTemp = thisModel.content.split(" ");
        String[] inputTemp = thisModel.userInput.split(" ");

        int carry = 0;

        int i = 0;
        for (; i < contentTemp.length; i++) {
            if (i >= inputTemp.length) {
                break;
            }

            String contentWord = contentTemp[i];
            String inputWord = inputTemp[i];

            for (int j = 0; j < inputWord.length(); j++) {
                Text text;

                if (j >= contentWord.length() && inputWord.charAt(j) != ' ') {
                    text = new Text(String.valueOf(inputWord.charAt(j)));
                    text.setFill(Color.ORANGE);
                    text.setFont(font);
                    thisView.systemWriting.getChildren().add(text);

                    if (j == inputWord.length() - 1 && i == inputTemp.length - 1) {
                        LetterJump lj = new LetterJump(text);
                        lj.errorAnimation();
                    }

                    carry++;
                } else {
                    if (contentWord.charAt(j) == inputWord.charAt(j)) {
                        text = new Text(String.valueOf(contentWord.charAt(j)));
                        text.setFont(font);
                        text.setFill(Color.GREEN);
                        thisView.systemWriting.getChildren().add(text);
                    } else {
                        text = new Text(String.valueOf(contentWord.charAt(j)));
                        text.setFont(font);
                        text.setFill(Color.RED);
                        thisView.systemWriting.getChildren().add(text);
                    }

                    if ((inputWord.length() < contentWord.length()) && j == inputWord.length() - 1) {
                        for (int k = j + 1; k < contentWord.length(); k++) {
                            text = new Text(String.valueOf(contentWord.charAt(k)));
                            text.setFont(font);
                            text.setFill(Color.BLACK);
                            thisView.systemWriting.getChildren().add(text);
                        }
                    }
                }
            }

            Text space = new Text(" ");
            space.setFont(font);
            thisView.systemWriting.getChildren().add(space);
        }

        for (; i < contentTemp.length; i++) {
            String contentWord = contentTemp[i];

            for (int j = 0; j < contentWord.length(); j++) {
                Text text = new Text(String.valueOf(contentWord.charAt(j)));
                text.setFont(font);
                text.setFill(Color.rgb(97, 99, 103));

                thisView.systemWriting.getChildren().add(text);
            }

            Text space = new Text(" ");
            space.setFont(font);

            if (i != contentTemp.length - 1) {
                thisView.systemWriting.getChildren().add(space);
            }
        }

        return carry;
    }

    class Timer extends Thread {
        private int currTime = 0;

        public void run() {
            while (thisModel.timer > 0) {
                try {
                    if (currTime % 1000 == 0) {
                        thisModel.timer--;
                        thisView.timerText.setText("Timer: " + thisModel.timer);

                        int mywpm = 60 * lettersCounter / 5;
                        thisModel.wpmPerSecond.add(mywpm);
                        lettersCounter = 0;
                    }

                    currTime += 10;
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!gameStart) {
                thisModel.postgameStatistics();
                try {
                    thisView.shadowButtonPanel();
                } catch (IllegalStateException ignored) {

                }
                isShadowed = true;
            }

            thisView.userWriting.setEditable(false);
            preend = false;
            gameStart = true;
        }

        public int getCurrTime() {
            return currTime;
        }

        public void stopTimer() {
            thisModel.timer = 0;
        }
    }

}

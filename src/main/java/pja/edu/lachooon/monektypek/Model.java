package pja.edu.lachooon.monektypek;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Model {
    private ArrayList<Language> availableLanguages;
    private Language currentLanguage;

    private String selectedLanguage = "polish";
    private String selectedLanguageBefore = "polish";

    public String content;
    public String userInput = "";
    public ArrayList<Integer> milis = new ArrayList<>();
    public ArrayList<Integer> wpmPerSecond = new ArrayList<>();
    public ArrayList<Integer> wpmAvg = new ArrayList<>();

    private LineChart<Number, Number> lineChart;

    public int timer = 45;
    public int tempTimer = 45;

    Model() {
        availableLanguages = DictionaryLoader.loadDictionary();
        setCurrentLanguage("polish");
    }

    public void setCurrentLanguage(String languageName) {
        Language temp = null;

        for (Language language : availableLanguages) {
            if (languageName.equals(language.toString())) {
                temp = language;
            }
        }

        if (temp == null) {
            System.out.println("Nie zaladowalo jezyka");
        } else {
            this.currentLanguage = temp;
        }
    }

    public void createContent() {
        ArrayList<String> dictionary = getCurrentLanguage().getDictionary();
        StringBuilder constructor = new StringBuilder();

        for (int i = 0; i < 30; i++) {
            int choice = (int) (Math.random() * (dictionary.size()));
            constructor.append(dictionary.get(choice)).append(" ");
        }

        content = constructor.toString();
    }

    public void postgameStatistics() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String[] contentWords = content.split(" ");
                String[] userWords = userInput.split(" ");

                int greenLetters = 0;
                int redLetters = 0;
                int blackLetters = 0;
                int orangeLetters = 0;

                for (int i = 0; i < userWords.length; i++) {
                    String cw = contentWords[i];
                    String uw = userWords[i];

                    for (int j = 0; j < cw.length(); j++) {
                        if ((uw.length() < cw.length()) && j == uw.length() - 1) {
                            for (int k = j + 1; k < cw.length(); k++) {
                                System.out.println("J: " + j + ", k: " + k);
                                blackLetters++;
                            }

                            break;
                        }

                        if ((uw.length() > cw.length()) && j == cw.length() - 1) {
                            for (int k = j + 1; k < uw.length(); k++) {
                                orangeLetters++;
                            }

                            break;
                        }

                        if (cw.charAt(j) == uw.charAt(j)) {
                            greenLetters++;
                        } else {
                            redLetters++;
                        }
                    }
                }

                Text green = new Text("Correct characters: " + greenLetters);
                Text red = new Text("Incorrect characters: " + redLetters);
                Text orange = new Text("Redundant characters: " + orangeLetters);
                Text black = new Text("Omitted characters: " + blackLetters);

                green.setFont(Font.font("Courier New", FontWeight.MEDIUM, 24));
                red.setFont(Font.font("Courier New", FontWeight.MEDIUM, 24));
                orange.setFont(Font.font("Courier New", FontWeight.MEDIUM, 24));
                black.setFont(Font.font("Courier New", FontWeight.MEDIUM, 24));

                green.setFill(Color.rgb(97, 99, 103));
                red.setFill(Color.rgb(97, 99, 103));
                orange.setFill(Color.rgb(97, 99, 103));
                black.setFill(Color.rgb(97, 99, 103));

                int sum = greenLetters + redLetters + orangeLetters + blackLetters;
                Text acc = new Text("Accuracy: " + (greenLetters * 100 / (sum)) + "%");
                acc.setFont(Font.font("Courier New", FontWeight.MEDIUM, 24));
                acc.setFill(Color.rgb(4, 107, 177));

                sum = 0;
                for (int i = 0; i < wpmPerSecond.size(); i++) {
                    sum += wpmPerSecond.get(i);
                    int temp = (sum / (i + 1));
                    wpmAvg.add(temp);
                }

                sum = 0;
                for (int i = 0; i < wpmAvg.size(); i++) {
                    sum += wpmAvg.get(i);
                }

                Text avg = new Text("Overall WPM: " + (sum / wpmAvg.size()));
                avg.setFont(Font.font("Courier New", FontWeight.MEDIUM, 24));
                avg.setFill(Color.rgb(4, 107, 177));

                VBox info = new VBox();
                info.getChildren().addAll(green, red, orange, black, acc, avg);
                info.setAlignment(Pos.CENTER);

                lineChart = null;
                new ChartDisplay().returningChart();

                GridPane root = new GridPane();
                root.setStyle("-fx-background-color: rgb(50, 52, 55);");

                GridPane.setConstraints(lineChart, 0, 0);
                GridPane.setConstraints(info, 0, 1);
                GridPane.setMargin(info, new Insets(10));

                root.getChildren().addAll(lineChart, info);

                Scene scene = new Scene(root);
                scene.getStylesheets().add("chartStyle.css");

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Statistics");
                stage.show();

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        stage.close();
                    }
                });
            }
        });

        File fileName = new File("output.txt");

        try {
            BufferedWriter writer = new BufferedWriter((new FileWriter(fileName)));

            LocalDateTime now = java.time.LocalDateTime.now();
            writer.write(now.getYear() + "-" + now.getMonth() + "-" + now.getDayOfMonth() + " " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond() + "\n\n");

            String[] contentWords = content.split(" ");
            for (int i = 0; i < milis.size(); i++) {
                String temp = contentWords[i] + " -> " + (60000 / milis.get(i) + " wpm");
                writer.write(temp + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Problem z plikiem");
        }
    }

    public ArrayList<Language> getAvailableLanguages() {
        return availableLanguages;
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getSelectedLanguageBefore() {
        return selectedLanguageBefore;
    }

    public void setSelectedLanguageBefore(String selectedLanguageBefore) {
        this.selectedLanguageBefore = selectedLanguageBefore;
    }

    class ChartDisplay {
        public void returningChart() {
            lineChart = createLineChart(wpmPerSecond, wpmAvg);
        }

        private LineChart<Number, Number> createLineChart(ArrayList axisValues, ArrayList axisValues2) {
            final NumberAxis ox = new NumberAxis();
            final NumberAxis oy = new NumberAxis();

            ox.setLabel("Seconds");
            oy.setLabel("WPM");

            final LineChart<Number, Number> lineChart = new LineChart<>(ox, oy);

            XYChart.Series<Number, Number> series = new LineChart.Series<>();

            for (int i = 0; i < axisValues.size(); i++) {
                XYChart.Data<Number, Number> data = new LineChart.Data<>(i, (Number) axisValues.get(i));
                series.getData().add(data);
            }

            lineChart.getData().add(series);
            series.setName("WPM");

            XYChart.Series<Number, Number> series2 = new LineChart.Series<>();

            for (int i = 0; i < axisValues2.size(); i++) {
                XYChart.Data<Number, Number> data = new LineChart.Data<>(i + 1, (Number) axisValues2.get(i));
                series2.getData().add(data);
            }

            lineChart.getData().add(series2);
            series2.setName("Average WPM");

            return lineChart;
        }
    }
}

module pja.edu.lachooon.monektypek {
    requires javafx.controls;
    requires javafx.fxml;


    opens pja.edu.lachooon.monektypek to javafx.fxml;
    exports pja.edu.lachooon.monektypek;
}
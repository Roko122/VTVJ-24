module toimistovarausjarjestelma {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.checkerframework.checker.qual;
    requires org.controlsfx.controls;


    opens asiakas;
    opens main;
    opens palvelu;
    opens toimipiste;
    opens varaus;

    exports asiakas;
    exports main;
    exports palvelu;
    exports toimipiste;
    exports varaus;
}
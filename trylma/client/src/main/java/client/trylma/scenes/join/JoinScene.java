package client.trylma.scenes.join;

import client.trylma.ClientApp;

import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Klasa odpowiedzialna za stworzenie sceny ekranu dołączenia (Join Scene).
 * Umożliwia użytkownikowi podanie pseudonimu i portu, a następnie nawiązanie połączenia z serwerem.
 */
public class JoinScene extends Scene {

    /** Referencja do głównej aplikacji klienta. */
    private final ClientApp clientApp;

    /**
     * Konstruktor sceny JoinScene.
     *
     * @param clientApp referencja do głównej klasy aplikacji
     */
    public JoinScene(ClientApp clientApp) {
        super(new VBox(), 750, 750);
        this.clientApp = clientApp;

        // Tworzenie layoutu i jego konfiguracja
        VBox layout = (VBox) this.getRoot();
        layout.setSpacing(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: rgb(30, 30, 50);");

        // Tworzenie komponentów interfejsu
        Label nicknameLabel = createLabel("Nickname:");
        TextField nicknameField = createTextField("Enter your nickname");

        Label portLabel = createLabel("Port:");
        TextField portField = createTextField("Enter server port");
        portField.setText(ClientApp.defaultPort);

        Button connectButton = createConnectButton(nicknameField, portField);

        // Dodanie komponentów do layoutu
        layout.getChildren().addAll(
            nicknameLabel, nicknameField, 
            portLabel, portField, 
            connectButton
        );
    }

    /**
     * Tworzy etykietę z określonym tekstem.
     *
     * @param text tekst etykiety
     * @return skonfigurowana etykieta
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * Tworzy pole tekstowe z podpowiedzią.
     *
     * @param promptText podpowiedź widoczna w polu tekstowym
     * @return skonfigurowane pole tekstowe
     */
    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle(
            "-fx-background-color: rgb(45, 45, 80); " +
            "-fx-text-fill: white; " +
            "-fx-prompt-text-fill: #6A6A80;"
        );
        textField.setMaxWidth(250); // Ustawienie maksymalnej szerokości pola
        return textField;
    }

    /**
     * Tworzy przycisk "Connect" i konfiguruje jego akcję.
     *
     * @param nicknameField pole tekstowe do wprowadzenia pseudonimu
     * @param portField pole tekstowe do wprowadzenia portu
     * @return skonfigurowany przycisk
     */
    private Button createConnectButton(TextField nicknameField, TextField portField) {
        Button connectButton = new Button("Connect");
        connectButton.setStyle(
            "-fx-background-color: rgb(90, 90, 255); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10 20 10 20;"
        );

        // Akcja po kliknięciu przycisku
        connectButton.setOnAction(event -> {
            if (validateInput(portField)) {
                try {
                    String nickname = nicknameField.getText();
                    int port = Integer.parseInt(portField.getText());

                    // Próba połączenia z serwerem
                    clientApp.ioManager.joinLobby(port, nickname);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number!");
                } catch (IOException e) {
                    System.out.println("Connection error: " + e.getMessage());
                }
            }
        });

        return connectButton;
    }

    /**
     * Weryfikuje dane wejściowe z pól tekstowych.
     *
     * @param nicknameField pole tekstowe pseudonimu
     * @param portField pole tekstowe portu
     * @return true, jeśli dane są poprawne; false w przeciwnym razie
     */
    private boolean validateInput(TextField portField) {
        String portText = portField.getText();

        if (portText.isEmpty()) {
            System.out.println("Port must not be empty!");
            return false;
        }

        // Sprawdzenie, czy port jest liczbą
        try {
            int port = Integer.parseInt(portText);
            if (port <= 0 || port > 65535) {
                System.out.println("Port must be a number between 1 and 65535!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Port must be a valid number!");
            return false;
        }

        return true; // Wszystkie dane są poprawne
    }
}

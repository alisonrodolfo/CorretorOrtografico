/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corretorortografico;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Rodolfo Barreiro
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextArea text;

    private List<String> listCodes = new ArrayList<String>();
    private List<String> listPalavras = new ArrayList<String>();

    String coorecao = "";

    @FXML
    private void handleButtonAction(ActionEvent event) {
        String linha[] = text.getText().split(" ");

        for (int i = 0; i < linha.length; i++) {
            if (!linha[i].equalsIgnoreCase("")) {
                if (listPalavras.contains(linha[i])) {
                    System.out.println("Palavra correta: " + linha[i]);
                    coorecao += (" " + linha[i]);
                } else {
                    errorSound();
                    System.out.println("Palavra errada: " + linha[i]);
                }
            }
        }

        text.setText(coorecao);

    }

    @FXML
    private void handleMenuLimpar(ActionEvent event) {
        text.setText("");
    }

    public void errorSound() {
        try {
            URL url = getClass().getResource("/computererror.wav");
            AudioClip audio = Applet.newAudioClip(url);
            audio.play();
            // If you want the sound to loop infinitely, then put: clip.loop(Clip.LOOP_CONTINUOUSLY); 
            // If you want to stop the sound, then use clip.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addAll(String file) {
        System.out.println("LENDO ARQUIVO");
        try {
            FileReader arq = new FileReader(file);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine();
            int iLinhas = 0;
            while (linha != null) {
                iLinhas++;
                listPalavras.add(linha);
                linha = lerArq.readLine(); // lê da segunda até a última linha
            }
            System.out.println(iLinhas + " Palavras Carregadas");

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addAll("src/palavras.txt");
        new Thread(t1).start();
    }

    private Runnable t1 = new Runnable() {
        public void run() {
            try {
                while (true) {

                    text.setOnKeyPressed(event -> {
                        if (event.getCode() == KeyCode.ENTER) {
                            analisaTextto();
                        }
                        if (event.getCode() == KeyCode.SPACE) {
                            analisaTextto();
                        }
                    });
                }
            } catch (Exception e) {
            }

        }
    };

    private void analisaTextto() {
        coorecao = "";

        String linha[] = text.getText().split("\\r?\\n");

        for (int j = 0; j < linha.length; j++) {
            String palavras[] = linha[j].split(" ");
            for (int i = 0; i < palavras.length; i++) {
                if (!palavras[i].equalsIgnoreCase("")) {
                    if (listPalavras.contains(palavras[i])) {
                        System.out.println("Palavra correta: " + palavras[i]);
                        coorecao += (palavras[i] + " ");
                    } else {
                        errorSound();
                        System.out.println("Palavra errada: " + palavras[i]);
                    }
                }
                if(linha.length<0){coorecao += "\n";}
            }
            
        }

        text.setText(coorecao);
        text.positionCaret(text.getText().length());
    }

}

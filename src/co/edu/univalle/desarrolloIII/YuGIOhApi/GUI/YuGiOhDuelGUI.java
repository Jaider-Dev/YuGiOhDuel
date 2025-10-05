package co.edu.univalle.desarrolloIII.YuGIOhApi.GUI;

import co.edu.univalle.desarrolloIII.YuGIOhApi.Listener.BattleListener;
import co.edu.univalle.desarrolloIII.YuGIOhApi.api.YuGiOhApiClient;
import co.edu.univalle.desarrolloIII.YuGIOhApi.logic.Duel;
import co.edu.univalle.desarrolloIII.YuGIOhApi.model.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class YuGiOhDuelGUI extends JFrame implements BattleListener {

    private final YuGiOhApiClient apiClient = new YuGiOhApiClient();
    private final List<Card> playerCards = new ArrayList<>();
    private final List<Card> cpuCards = new ArrayList<>();
    private JPanel mainPanel;
    private JLabel header;
    private JPanel logPanel;
    private JTextArea LogTexArea;
    private JPanel playerPanel;
    private JLabel playerLabel;
    // Player Card 1
    private JPanel playerPanelCard1;
    private JLabel playerImgCard1;
    private JLabel playerLabelNameCard1;
    private JLabel playerNameCard1;
    private JLabel playerLabelAtakCard1;
    private JLabel playerAtakCard1;
    private JLabel playerLabelDefCard1;
    private JLabel playerDefCard1;
    private JButton play1;
    // Player Card 2
    private JPanel playerPanelCard2;
    private JLabel playerImgCard2;
    private JLabel playerLabelNameCard2;
    private JLabel playerNameCard2;
    private JLabel playerLabelAtakCard2;
    private JLabel playerAtakCard2;
    private JLabel playerLabelDefCard2;
    private JLabel playerDefCard2;
    private JButton play2;
    // Player Card 3
    private JPanel panelPlayerCard3;
    private JLabel playerImgCard3;
    private JLabel playerLabelNameCard3;
    private JLabel playerNameCard3;
    private JLabel playerLabelAtakCard3;
    private JLabel playerAtakCard3;
    private JLabel playerLabelDefCard3;
    private JLabel playerDefCard3;
    private JButton play3;
    private JLabel cpuLabel;
    private JPanel cpuPanel;
    // CPU Card 1
    private JPanel cpuPanelCard1;
    private JLabel cpuImgCard1;
    private JLabel cpuLabelNameCard1;
    private JLabel cpuNameCard1;
    private JLabel cpuLabelAtakCard1;
    private JLabel cpuAtakCard1;
    private JLabel cpuLabelDefCard1;
    private JLabel cpuDefCard1;
    private JLabel EmuButtonCard1;
    // CPU Card 2
    private JPanel cpuPanelCard2;
    private JLabel cpuImgCard2;
    private JLabel cpuLabelNameCard2;
    private JLabel cpuNameCard2;
    private JLabel cpuLabelAtakCard2;
    private JLabel cpuAtakCard2;
    private JLabel cpuLabelDefCard2;
    private JLabel cpuDefCard2;
    private JLabel EmuButtonCard2;
    // CPU Card 3
    private JPanel cpuPanelCard3;
    private JLabel cpuImgCard3;
    private JLabel cpuLabelNameCard3;
    private JLabel cpuNameCard3;
    private JLabel cpuLabelAtakCard3;
    private JLabel cpuAtakCard3;
    private JLabel cpuLabelDefCard3;
    private JLabel cpuDefCard3;
    private JLabel EmuButtonCard3;
    private JScrollPane Log;
    private JLabel PlayerScoreLabel;
    private JButton startDuelButton;
    private JLabel playerScore;
    private JLabel cpuScoreLabel;
    private JLabel cpuScore;
    private JLabel logActivity;
    private JButton chooseCardButton;
    private Duel duel;
    private String modoBatallaActual;
    private boolean playerTurn = true;

    public YuGiOhDuelGUI() {
        super("Yu-Gi-Oh! Duel");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1500, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        attachListeners();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            YuGiOhDuelGUI gui = new YuGiOhDuelGUI();
            gui.setVisible(true);
        });
    }

    private void attachListeners() {

        chooseCardButton.addActionListener(e -> {
            playerImgCard1.setIcon(new ImageIcon(getClass().getResource("/images/Dorso.png")));
            playerImgCard2.setIcon(new ImageIcon(getClass().getResource("/images/Dorso.png")));
            playerImgCard3.setIcon(new ImageIcon(getClass().getResource("/images/Dorso.png")));
            cpuImgCard1.setIcon(new ImageIcon(getClass().getResource("/images/Dorso.png")));
            cpuImgCard2.setIcon(new ImageIcon(getClass().getResource("/images/Dorso.png")));
            cpuImgCard3.setIcon(new ImageIcon(getClass().getResource("/images/Dorso.png")));
            startNewDuel();
            // Activar botón de iniciar duelo
            startDuelButton.setEnabled(true);
            this.playerScore.setText("0");
            this.cpuScore.setText("0");
        });

        startDuelButton.addActionListener(e -> {
            if (duel == null) {
                appendLog("Primero selecciona las cartas con 'Choose Cards'.");
                return;
            }

            // Selección aleatoria del modo de batalla
            String[] modos = {"Ataque vs Defensa", "Ataque vs Ataque", "Defensa vs Ataque"};
            modoBatallaActual = modos[new Random().nextInt(modos.length)];

            appendLog("Modo de batalla: " + modoBatallaActual);

            // Activar botones de juego
            play1.setEnabled(true);
            play2.setEnabled(true);
            play3.setEnabled(true);
            startDuelButton.setEnabled(false);
            this.playerScore.setText("0");
            this.cpuScore.setText("0");
        });


        play1.addActionListener(e -> onPlayerChoose(0));
        play2.addActionListener(e -> onPlayerChoose(1));
        play3.addActionListener(e -> onPlayerChoose(2));
    }

    private void startNewDuel() {
        LogTexArea.append("========================================================" + "\n");
        LogTexArea.append("Cargando cartas... " + "\n");
        playerCards.clear();
        cpuCards.clear();

        SwingWorker<Void, Void> loader = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                playerCards.addAll(apiClient.getRandomCards(3));
                cpuCards.addAll(apiClient.getRandomCards(3));
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    updatePlayerUI();
                    updateCpuUI();
                    duel = new Duel(new ArrayList<>(playerCards), new ArrayList<>(cpuCards), YuGiOhDuelGUI.this);
                    playerTurn = new Random().nextBoolean();
                } catch (ExecutionException | InterruptedException ex) {
                    showError("Error cargando cartas: " + ex.getCause().getMessage());
                }
            }
        };
        loader.execute();
    }

    private void updatePlayerUI() {
        setCardUI(playerCards.get(0), playerImgCard1, playerNameCard1, playerAtakCard1, playerDefCard1);
        setCardUI(playerCards.get(1), playerImgCard2, playerNameCard2, playerAtakCard2, playerDefCard2);
        setCardUI(playerCards.get(2), playerImgCard3, playerNameCard3, playerAtakCard3, playerDefCard3);
    }

    private void updateCpuUI() {
        setCardUI(cpuCards.get(0), cpuImgCard1, cpuNameCard1, cpuAtakCard1, cpuDefCard1);
        setCardUI(cpuCards.get(1), cpuImgCard2, cpuNameCard2, cpuAtakCard2, cpuDefCard2);
        setCardUI(cpuCards.get(2), cpuImgCard3, cpuNameCard3, cpuAtakCard3, cpuDefCard3);
    }

    private void setCardUI(Card c, JLabel imgLabel, JLabel nameLabel, JLabel atkLabel, JLabel defLabel) {
        nameLabel.setText(c.getName());
        atkLabel.setText(String.valueOf(c.getAtk()));
        defLabel.setText(String.valueOf(c.getDef()));
        imgLabel.setText("");
        imgLabel.setIcon(loadImageIcon(c.getImageUrl(), 120, 160));
    }

    private void onPlayerChoose(int index) {
        if (duel == null) {
            appendLog("Primero inicia el duelo (Iniciar duelo).");
            return;
        }
        if (index < 0 || index >= playerCards.size()) {
            appendLog("Carta inválida.");
            return;
        }

        Card playerCard = playerCards.get(index);
        Card cpuCard = cpuCards.get(new Random().nextInt(cpuCards.size())); // CPU elige aleatoriamente una carta

        appendLog("Jugador juega: " + playerCard.getName());
        appendLog("CPU juega: " + cpuCard.getName());

        // Comparar valores según el modo de batalla
        int playerValue = 0;
        int cpuValue = 0;

        switch (modoBatallaActual) {
            case "Ataque vs Defensa":
                playerValue = playerCard.getAtk();
                cpuValue = cpuCard.getDef();
                break;
            case "Ataque vs Ataque":
                playerValue = playerCard.getAtk();
                cpuValue = cpuCard.getAtk();
                break;
            case "Defensa vs Ataque":
                playerValue = playerCard.getDef();
                cpuValue = cpuCard.getAtk();
                break;
        }

        appendLog("Jugador: " + playerValue + "  |  CPU: " + cpuValue);

        // Determinar ganador de la ronda
        String winner = null;
        if (playerValue > cpuValue) {
            winner = "Jugador gana el punto!";
            duel.addPointToPlayer();
        } else if (playerValue < cpuValue) {
            winner = "CPU gana el punto!";
            duel.addPointToCpu();
        } else {
            winner = "Empate!";
        }

        // Si el duelo ya terminó, no continuar
        if (duel.isFinished()) {
            return;
        }

        appendLog("Resultado: " + winner);

        // Desactivar la carta usada por el jugador
        switch (index) {
            case 0 -> play1.setEnabled(false);
            case 1 -> play2.setEnabled(false);
            case 2 -> play3.setEnabled(false);
        }

        // Cambiar modo de batalla para la siguiente ronda
        cambiarModoBatalla();

    }

    private ImageIcon loadImageIcon(String urlStr, int w, int h) {
        try {
            URL url = new URL(urlStr);
            BufferedImage img = ImageIO.read(url);
            if (img != null) {
                Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (IOException ignored) {
        }
        return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB));
    }

    private void appendLog(String msg) {
        LogTexArea.append(msg + "\n");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }


    @Override
    public void onTurn(Card playerCard, Card cpuCard, String winner) {
        appendLog("CPU juega: " + cpuCard.getName() + " (ATK: " + cpuCard.getAtk() + ", DEF: " + cpuCard.getDef() + ")");
        appendLog("Resultado de la ronda: " + winner);
    }

    @Override
    public void onScoreChanged(int playerScore, int cpuScore) {
        this.playerScore.setText(String.valueOf(playerScore));
        this.cpuScore.setText(String.valueOf(cpuScore));
    }

    @Override
    public void onDuelEnded(String winner) {
        appendLog("¡Duelo finalizado! Ganador: " + winner + "\n");
        JOptionPane.showMessageDialog(this, "Ganador: " + winner);

        // Desactivar botones de juego
        play1.setEnabled(false);
        play2.setEnabled(false);
        play3.setEnabled(false);
    }

    private void cambiarModoBatalla() {
        String[] modos = {"Ataque vs Defensa", "Ataque vs Ataque", "Defensa vs Ataque"};
        modoBatallaActual = modos[new Random().nextInt(modos.length)];
        appendLog("Nuevo modo de batalla: " + modoBatallaActual);
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipeSpawnTimer;
    int gravity = 1;
    boolean gameOver = false;
    int score = 0;
    JLabel scoreLabel;

    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);

        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(10, 10, 150, 30);
        add(scoreLabel);

        initGame();
    }

    private void initGame() {
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<>();
        score = 0;
        gameOver = false;
        updateScoreLabel();

        // setup game timer
        if (gameLoop != null) {
            gameLoop.stop();
        }
        gameLoop = new Timer(20, this);
        gameLoop.start();

        // setup pipe spawn timer
        if (pipeSpawnTimer != null) {
            pipeSpawnTimer.stop();
        }
        pipeSpawnTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    placePipe();
                }
            }
        });
        pipeSpawnTimer.start();
    }

    public void placePipe() {
        int randomPosY = (int)(pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, this);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), this);
        }

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), this);

        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g.fillRect(0, 0, frameWidth, frameHeight);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String gameOverText = "Game Over";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(gameOverText);
            g.drawString(gameOverText, (frameWidth - textWidth) / 2, frameHeight / 2 - 30);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            String restartText = "Press 'R' to restart";
            textWidth = fm.stringWidth(restartText);
            g.drawString(restartText, (frameWidth - textWidth) / 2, frameHeight / 2 + 30);
        }
    }

    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());

        if (player.getPosY() + player.getHeight() >= frameHeight) {
            player.setPosY(frameHeight - player.getHeight());
            gameOver();
        }

        player.setPosY(Math.max(player.getPosY(), 0));

        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.setPosX(pipe.getPosX() - pipe.getVelocityX());

            if (pipe.getPosX() + pipe.getWidth() < 0) {
                iterator.remove();
                continue;
            }

            if (checkCollision(player, pipe)) {
                gameOver();
            }

            // Check if player has passed the pipe
            if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                pipe.setPassed(true);
                if (pipe.getImage() == upperPipeImage) {
                    score++;
                    updateScoreLabel();
                }
            }
        }
    }

    private boolean checkCollision(Player player, Pipe pipe) {
        // Simple rectangle collision detection
        return player.getPosX() < pipe.getPosX() + pipe.getWidth() &&
                player.getPosX() + player.getWidth() > pipe.getPosX() &&
                player.getPosY() < pipe.getPosY() + pipe.getHeight() &&
                player.getPosY() + player.getHeight() > pipe.getPosY();
    }

    private void gameOver() {
        gameOver = true;
        gameLoop.stop();
        pipeSpawnTimer.stop();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            player.setVelocityY(-10);
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            initGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
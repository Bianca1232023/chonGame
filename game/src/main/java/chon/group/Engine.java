package chon.group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import chon.group.agent.Agent;
import chon.group.agent.HeroMovement;
import chon.group.enviroment.Environment;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Engine extends Application {

    private final ArrayList<String> input = new ArrayList<>();
    private final ArrayList<Environment> lifeIcons = new ArrayList<>(); // Lista para armazenar os lifeIcons

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            ArrayList<Agent> agents = new ArrayList<>();

            Agent agentA = new Agent(920, 420, 134, 80, "/images/agent/Asteroid/asteroide1.png", false, 0);
            agents.add(agentA);

            Agent agentB = new HeroMovement(70, 410, 120, 100, "/images/agent/Spaceship/spaceship_andando1.png", true, 5);
            agents.add(agentB);

			Agent agentC = new Agent(920, 110, 74, 54, "/images/agent/enemys_space/enemy_drone.png", false, 0);
			agents.add(agentC);

			Agent agentD = new Agent(920, 540, 74, 54, "/images/agent/enemys_space/enemy_drone.png", false, 0);
			agents.add(agentD);

            if (Agent.numberProtagonist == 1) {
                StackPane root = new StackPane();
                Scene scene = new Scene(root, 1180, 780);
                theStage.setTitle("Chon: The Learning Game");
                theStage.setScene(scene);
                Canvas canvas = new Canvas(1180, 780);

                root.getChildren().add(canvas);
                theStage.show();

                Environment atmosphere = new Environment(0, 0, 1180, 780, "/images/environment/background space.png",
                        agents, canvas.getGraphicsContext2D());

                // Inicializa a lista de lifeIcons com 3 ícones
                for (int i = 0; i < 3; i++) {
                    Environment lifeIcon = new Environment(964 + (i * (56 + 10)), 638, 56, 47, "/images/agent/Spaceship/Life_icon.png", agents, canvas.getGraphicsContext2D());
                    lifeIcons.add(lifeIcon);
                }

                startAnimation(canvas, scene, atmosphere, agents);

                theStage.show();
            } else {
                throw new IllegalArgumentException("The number of protagonists didn't reach the minimum number or exceeded it");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            Platform.exit();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    // Método para gerar números aleatórios dentro de um intervalo
    private int generateRandomPosition(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public void startAnimation(Canvas canvas, Scene scene, Environment atmosphere, ArrayList<Agent> agents) {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.clear();
                input.add(code);
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.remove(code);
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                atmosphere.setGc(canvas.getGraphicsContext2D());

                if (!input.isEmpty()) {
                    atmosphere.getProtagonist().move(input.get(0));
                    if (atmosphere.limitsApprove()) {
                        atmosphere.drawAgents(agents);
                    }
                }
                atmosphere.clearRect();
                atmosphere.drawBackground();

                if (atmosphere.limitsApprove()) {
                    atmosphere.drawAgents(agents);
                    for (Environment lifeIcon : lifeIcons) {
                        lifeIcon.drawLifeIcon();
                    }
                }

                if (agents.size() > 1) {
                    Agent asteroid = atmosphere.getAgents().get(0); // O asteroide
                    Agent spaceship = atmosphere.getAgents().get(1); // A nave espacial

                    // Verifica a colisão e faz o asteroide se mover
                    if (atmosphere.checkCollision(asteroid, spaceship)) {
                        System.out.println("Colisão detectada entre o asteroide e a espaçonave!");
                        spaceship.desacrease_life(1);
                        if (spaceship.getLife() <= 0) {
                            spaceship.startExplosion(); // Inicia a explosão da nave
                            System.out.println("A nave foi destruída!");

                            // Remove o último lifeIcon da lista 
                            if (!lifeIcons.isEmpty()) {
                                lifeIcons.remove(lifeIcons.size() - 1);
                            }
                        }
                        asteroid.setAlive(false); // Destrói o asteroide
                        asteroid.startExplosion(); // Inicia a explosão do asteroide

                        // Gera um novo asteroide em uma posição aleatória 
                        int randomX = generateRandomPosition(911, 1071);
                        int randomY = generateRandomPosition(51, 570);  
                        Agent newAsteroid = new Agent(randomX, randomY, 134, 80, "/images/agent/Asteroid/asteroide1.png", false, 0);
                        atmosphere.getAgents().set(0, newAsteroid); // Substitui o asteroide antigo
                    }

                    // Verifica se o asteroide passou de x: 0 e reaparece em uma nova posição 
                    if (asteroid.isAlive() && asteroid.getPositionX() < 0) {
                        int randomX = generateRandomPosition(911, 1071); 
                        int randomY = generateRandomPosition(51, 570); 
                        asteroid.setPositionX(randomX);
                        asteroid.setPositionY(randomY);
                    }

                    // Move o asteroide para a esquerda
                    if (asteroid.isAlive()) {
                        asteroid.move("LEFT");
                    }
                }

                // Atualiza e redesenha os agentes
                for (Iterator<Agent> iterator = agents.iterator(); iterator.hasNext();) {
                    Agent agent = iterator.next();
                    if (!agent.isAlive()) {
                        iterator.remove();
                    }
                }

                atmosphere.drawAgents(agents);
            }
        }.start();
    }
}

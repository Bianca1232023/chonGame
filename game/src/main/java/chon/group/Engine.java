package chon.group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import chon.group.agent.Agent;
import chon.group.agent.HeroMovement;
import chon.group.agent.Shot;
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

            Agent agentA = new Agent(920, 420, 134, 80, "/images/agent/Asteroid/asteroide1.png", false, 0, false);
            agentA.setSpeed(2); // Define a velocidade do asteroide
            agentA.setPursuer(false);

            Agent agentB = new HeroMovement(70, 410, 120, 100, "/images/agent/Spaceship/spaceship_andando1.png", true, 5);
            agents.add(agentB);

			Agent agentC = new Agent(920, 110, 74, 54, "/images/agent/enemys_space/enemy_drone.png", false, 0, true);
			agents.add(agentC);
            agentC.setSpeed(2); // Define a velocidade do agentC

			Agent agentD = new Agent(920, 540, 74, 54, "/images/agent/enemys_space/enemy_drone.png", false, 0, true);
			agents.add(agentD);
            agentD.setSpeed(2); 

            agents.add(agentA);
            agents.add(agentC);
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
                /*for (int i = 0; i < 3; i++) {
                    Environment lifeIcon = new Environment(964 + (i * (56 + 10)), 638, 56, 47, "/images/agent/Spaceship/Life_icon.png", agents, canvas.getGraphicsContext2D());
                    lifeIcons.add(lifeIcon);
                }*/

                // atmosphere.getAgents().get(3).chase(atmosphere.getProtagonist().getPosX(),
                //                 atmosphere.getProtagonist().getPosY());

                        // for (Agent bot : atmosphere.getAgents()) {
                        //     if (!bot.isPursuer) {
                        //         bot.chase(atmosphere.getProtagonist().getPosX(), atmosphere.getProtagonist().getPosY()); 
                        //     }
                        // }


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

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.clear();
                input.add(code);

				if (code.equals("P")) {
					if (atmosphere.isPaused()) { 
						atmosphere.setPause(false); 
					} else {
						atmosphere.setPause(true); 
					}
                }

                if (code.equals("SPACE")) { // Disparar tiro
                    Agent protagonist = atmosphere.getProtagonist();
                    if (protagonist != null) {
                        int shotX = protagonist.getPositionX() + protagonist.getWidth();
                        int shotY = protagonist.getPositionY() + protagonist.getHeight() / 2;
                        Shot shot = new Shot(shotX, shotY, 10, "/images/agent/shot.png"); // Cria um novo tiro
                        atmosphere.addShot(shot);
                }
							
            }
        }});

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.remove(code);
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                // Obtém o protagonista e verifica se ele está morto
                Agent protagonist = atmosphere.getProtagonist();
                if (protagonist == null || !protagonist.isAlive()) {
                    return; // Para o jogo se o protagonista não existir ou estiver morto
                }
        
                atmosphere.setGc(canvas.getGraphicsContext2D());
        
                if (atmosphere.isPaused()) {
                    atmosphere.pauseScreen();
                    return;
                }
        
                // Move o protagonista com base na entrada do teclado
                if (!input.isEmpty()) {
                    protagonist.move(input.get(0));
                    if (atmosphere.limitsApprove()) {
                        atmosphere.drawAgents(agents);
                    }
                }
        
                atmosphere.clearRect();
                atmosphere.drawBackground();
        
                // Movimenta os inimigos (chama o método chase)
                // for (Agent agent : agents) {
                //         if (!agent.getIsProtagonist() && agent.isAlive() && agent.isPursuer()) { // Verifica se é um inimigo, está vivo e deve perseguir
                //         agent.chase(protagonist.getPositionX(), protagonist.getPositionY());
                //     }
                // }

        
                // Desenha os agentes
                if (atmosphere.limitsApprove()) {
                    atmosphere.drawAgents(agents);
                    for (Environment lifeIcon : lifeIcons) {
                        lifeIcon.drawLifeIcon();
                    }
                }
        
                // Move os tiros e verifica colisões
                for (Iterator<Shot> shotIterator = atmosphere.getShots().iterator(); shotIterator.hasNext();) {
                    Shot shot = shotIterator.next();
                    if (shot.isAlive()) {
                        shot.move(); // Move o tiro
        
                        // Verifica se o tiro saiu da tela
                        if (shot.getPositionX() > atmosphere.getWidth()) {
                            shot.setAlive(false);
                        }
        
                        for (Agent asteroid : agents) {
                            if (asteroid.isAlive() && atmosphere.checkCollisionShot(shot, asteroid)) {
                                System.out.println("Tiro atingiu o asteróide!");
                                asteroid.setAlive(false); // Destrói o asteróide
                                shot.setAlive(false); // Destrói o tiro
                            }
                        }
                    } else {
                        shotIterator.remove(); // Remove tiros "mortos"
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

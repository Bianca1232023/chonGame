package chon.group.enviroment;

import java.util.ArrayList;

import chon.group.agent.Agent;
import chon.group.agent.HeroMovement;
import chon.group.agent.Shot;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Environment {
    private int width;
    private int height;
    private int positionX;
    private int positionY;
    private Image image;
    private Agent protagonist;
    private ArrayList<Agent> agents = new ArrayList<>();
    private GraphicsContext gc;
    private Image pauseScreen;
    private Image gameOverScreen;
    private boolean paused = false;
    private ArrayList<Shot> shots = new ArrayList<>();

    public Environment() {

    }

    public Environment(int positionX, int positionY, int width, int height, String pathImage, ArrayList<Agent> agents, GraphicsContext gc) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.height = height;
        this.width = width;
        this.gc = gc;
        setImage(pathImage);
        setAgents(agents);
        this.gameOverScreen = new Image(getClass().getResource("/images/agent/gameOver/Gemini_Generated_Image_pyu4swpyu4swpyu4.jpg").toExternalForm());
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Image getImage() {
        return image;
    }

    public final void setImage(String pathImage){
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.drawBackground();
        this.drawLifeIcon();
    }

    public Agent getProtagonist() {
        return protagonist;
    }

    public ArrayList<Agent> getAgents(){
        return this.agents;
    }

    public void setAgents(ArrayList<Agent> agents){
        this.agents = agents;

        for (Agent agent : agents){
            if(agent.getIsProtagonist()){
                this.protagonist = agent;
                break;
            }
        }

        this.drawAgents(agents);
    }

    public GraphicsContext getGc() {
        return this.gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public boolean isPaused() {
        return paused;
    }
    
    public void setPause(boolean paused) {
        this.paused = paused;
    }

    public void drawBackground() {
        gc.drawImage(this.image, this.positionX, this.positionY, this.width, this.height);
    }

    public void drawLifeIcon() {
        gc.drawImage(this.image, this.positionX, this.positionY, this.width, this.height);
    }

    public void drawAgents(ArrayList<Agent> agents){
        for (Agent agent : agents){
            gc.drawImage(agent.getImage(), agent.getPositionX(), agent.getPositionY(), agent.getWidth(), agent.getHeight());
        }
        drawShots();
        printStatusPanel(this.protagonist);
        printLifeEnergybar(this.protagonist);
    }    

    public Image getPauseScreen() {
        return pauseScreen;
    }

    public void setPauseScreen() {
        this.pauseScreen = pauseScreen;
    }

    public Image getGameOverScreen() {
        return gameOverScreen;
    }

    public void setGameOverScreen(Image gameOverScreen) {
        this.gameOverScreen = gameOverScreen;
    }

    public void addShot(Shot shot) {
        this.shots.add(shot);
    }
    
    public ArrayList<Shot> getShots() {
        return this.shots;
    }

    public void clearRect(){
        gc.clearRect(0, 0, 1180, 780);
    }

    public void printLifeEnergybar(Agent agent) {
        Image lifeBarImage = null;
        if (agent.getLife() == 5) {
            lifeBarImage = new Image(getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de vida1.png").toExternalForm());
        } else if (agent.getLife() == 4) {
            lifeBarImage = new Image(getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de vida2.png").toExternalForm());
        } else if (agent.getLife() == 3) {
            lifeBarImage = new Image(getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de vida3.png").toExternalForm());
        } else if (agent.getLife() == 2) {
            lifeBarImage = new Image(getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de vida4.png").toExternalForm());
        } else if (agent.getLife() == 1) {
            lifeBarImage = new Image(getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de vida5.png").toExternalForm());
        } else if (agent.getLife() == 0) {
            lifeBarImage = new Image(getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de vida6.png").toExternalForm());
        }
    

        gc.drawImage(lifeBarImage, agent.getPositionX() + 10, agent.getPositionY() - 5);
    
        Image energyBarImage = new Image
        (getClass().getResource("/images/agent/Life_bar_and_energy_bar/barra de energia 1.png").toExternalForm());
        gc.drawImage(energyBarImage, agent.getPositionX() + 10, agent.getPositionY() - -6);
	}
    
    public void printStatusPanel(Agent agent) {

        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        Font theFont = Font.font("Verdana", FontWeight.BOLD, 14);
        gc.setFont(theFont);
        gc.fillText("X: " + agent.getPositionX(), agent.getPositionX() + 10, agent.getPositionY() - 25);
        gc.fillText("Y: " + agent.getPositionY(), agent.getPositionX() + 10, agent.getPositionY() - 10);
        
	}

    public Boolean limitsApprove(){

        if(this.protagonist.getPositionX() <= 0){
            this.protagonist.setPositionX(1);
        }else if(this.width == (this.protagonist.getPositionX() + this.protagonist.getWidth())){
            this.protagonist.setPositionX(this.protagonist.getPositionX() - 1);
        }else if(this.protagonist.getPositionY() <= 0){
            this.protagonist.setPositionY(1);
        }else if(this.height == (this.protagonist.getPositionY() + this.protagonist.getHeight())){
            this.protagonist.setPositionY(this.protagonist.getPositionY() - 1);
        }

        return true;
    }

public boolean checkCollision(Agent agent1, Agent agent2) {
    boolean collisionDetected = agent1.getPositionX() < agent2.getPositionX() + agent2.getWidth() &&
                                agent1.getPositionX() + agent1.getWidth() > agent2.getPositionX() &&
                                agent1.getPositionY() < agent2.getPositionY() + agent2.getHeight() &&
                                agent1.getPositionY() + agent1.getHeight() > agent2.getPositionY();

    if (collisionDetected) {
        if (agent1 instanceof HeroMovement) {
            HeroMovement hero = (HeroMovement) agent1;
            hero.desacrease_life(1); 

            if (hero.getLife() <= 0) {
                hero.setAlive(false);
                gameOverScreen();
            }
        }
    }


    return collisionDetected;
}

public void drawShots() {
    for (Shot shot : shots) {
        if (shot.isAlive()) {
            gc.drawImage(shot.getImage(), shot.getPositionX(), shot.getPositionY());
        }
    }
}

public boolean checkCollisionShot(Shot shot, Agent asteroid) {
    boolean collisionDetected = shot.getPositionX() < asteroid.getPositionX() + asteroid.getWidth() &&
                                shot.getPositionX() + shot.getImage().getWidth() > asteroid.getPositionX() &&
                                shot.getPositionY() < asteroid.getPositionY() + asteroid.getHeight() &&
                                shot.getPositionY() + shot.getImage().getHeight() > asteroid.getPositionY();

    if (collisionDetected) {
        shot.setAlive(false);
        asteroid.setAlive(false); 
    }

    return collisionDetected;
}
public void pauseScreen() {
    if (pauseScreen != null && gc != null) {
        double centerX = (this.width - pauseScreen.getWidth()) / 2;
        double centerY = (this.height - pauseScreen.getHeight()) / 2;

        // Draw image on the center of screen
        gc.drawImage(pauseScreen, centerX, centerY);
    } else {
        System.out.println("Pause image not set or GraphicsContext is null.");
    }
}

public void gameOverScreen() {
    if (gameOverScreen != null && gc != null) {
        gc.clearRect(0, 0, width, height);

        double centerX = (this.width - gameOverScreen.getWidth()) / 2;
        double centerY = (this.height - gameOverScreen.getHeight()) / 2;

        gc.drawImage(gameOverScreen, centerX, centerY);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        gc.fillText("GAME OVER", width / 2 - 150, height / 2 - 50);
    } else {
        System.out.println("Game Over image not set or GraphicsContext is null.");
    }
}


}

package chon.group.agent;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Asteroid extends ImageView {

    private boolean alive;

    public Asteroid(int positionX, int positionY, int width, int height, String imagePath, boolean isProtagonist, int life) {
        super(new Image(imagePath));  // Carrega a imagem
        setX(positionX);
        setY(positionY);
        setFitWidth(width);
        setFitHeight(height);
        this.alive = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void move(String direction) {
        if (direction.equals("LEFT")) {
            setX(getX() - 10);  // Move o asteroide para a esquerda
        }
    }

    public void startExplosion() {
        // Implemente o efeito de explosão aqui (se necessário)
    }
}

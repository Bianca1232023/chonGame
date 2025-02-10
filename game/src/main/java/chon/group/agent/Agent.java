package chon.group.agent;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Agent {
    private int positionX;
    private int positionY;
    private int height;
    private int width;
    private Image image;
    private Boolean isProtagonist;
    public static int numberProtagonist = 0;
    private ArrayList<Image> explosionImages; // ArrayList de imagens para a explosão
    private int explosionFrame = 0; // Controlador de frames da explosão
    private boolean isExploding = false;
    private long explosionStartTime = 0; // Marca o tempo que a explosão começa
    private int life;
    private boolean alive = true;
    private int posX;
    private int posY;
    private int speed;
    public boolean isPursuer;

    /** Indicates if the agent is facing left. */
    private boolean flipped = false;

    public Agent(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist,
            int life, boolean isPursuer) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
        this.isProtagonist = isProtagonist;
        this.life = life;
        System.out.println("Vida inicial: " + this.life); // Verifique o valor aqui!
        this.explosionImages = new ArrayList<>();
        this.explosionFrame = 0;
        this.isExploding = false;
        this.isPursuer = isPursuer;

        for (int i = 1; i <= 8; i++) {
            explosionImages.add(new Image(
                    getClass().getResource("/images/agent/Death_animation/explosao" + i + ".png").toExternalForm()));
        }

        if (isProtagonist) {
            numberProtagonist++;
        }
    }

    
    public boolean isPursuer() {
        return isPursuer;
    }


    public void setPursuer(boolean isPursuer) {
        this.isPursuer = isPursuer;
    }


    public int getPosX() {
        return posX;
    }


    public void setPosX(int posX) {
        this.posX = posX;
    }


    public int getPosY() {
        return posY;
    }


    public void setPosY(int posY) {
        this.posY = posY;
    }


    public boolean isFlipped() {
        return flipped;
    }


    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Boolean getIsProtagonist() {
        return isProtagonist;
    }

    public void setIsProtagonist(Boolean isProtagonist) {
        this.isProtagonist = isProtagonist;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String pathImage) {
        this.image = new Image(getClass().getResource(pathImage).toExternalForm());
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void move(String direction) {
        if (direction.equals("LEFT")) {
            this.positionX -= 10;
        } else if (direction.equals("RIGHT")) {
            this.positionX += 10;
        } else if (direction.equals("UP")) {
            this.positionY -= 10;
        } else if (direction.equals("DOWN")) {
            this.positionY += 10;
        }
    }    

    // Método para iniciar a explosão
    public void startExplosion() {
        this.isExploding = true;
        this.explosionFrame = 0; // Reinicia a animação
        this.explosionStartTime = System.currentTimeMillis(); // Marca o tempo em que a explosão começa
        updateExplosion(); // Atualiza a explosão imediatamente
        this.image = explosionImages.get(explosionFrame); // A primeira imagem da explosão é imediatamente exibida
    }

    // Método para atualizar a animação da explosão
    public void updateExplosion() {
        if (isExploding) {
            // Se a explosão foi iniciada, verifica o tempo que passou desde o início
            if (explosionFrame == 0) {
                explosionStartTime = System.currentTimeMillis(); // Registra o tempo de início da explosão
            }

            long elapsedTime = System.currentTimeMillis() - explosionStartTime; // Tempo decorrido desde o início da
                                                                                // explosão
            int frameDuration = 100; // Duração de cada frame da explosão em milissegundos

            // Avança os frames da explosão conforme o tempo decorrido
            if (elapsedTime >= frameDuration * explosionFrame && explosionFrame < explosionImages.size()) {
                this.image = explosionImages.get(explosionFrame);
                explosionFrame++; // Avança para o próximo frame
            }

            // Se todos os frames da explosão foram exibidos, finaliza a animação da
            // explosão
            if (explosionFrame >= explosionImages.size()) {
                isExploding = false; // Finaliza a explosão
            }
        } else
            this.alive = false;
    }

    // Método para verificar se está em explosão
    public boolean isExploding() {
        return isExploding;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void desacrease_life(int amount) {
        this.life = this.life - amount;
        if (this.life <= 0) {
            this.life = 0;
        }
        System.out.println(this.life);

    }

    public void gameOver(int life) {
        if (life <= 0) {

        }
    }

    private void flipImage() {
        ImageView flippedImage = new ImageView(image);
        flippedImage.setScaleX(-1);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        this.flipped = !this.flipped;
        this.image = flippedImage.snapshot(params, null);
    }

    /**
     * Moves the agent based on the movement commands provided.
     *
     * @param movements a list of movement directions ("RIGHT", "LEFT", "UP",
     *                  "DOWN")
     */
    public void move(List<String> movements) {
        if (movements.contains("RIGHT")) {
            if (flipped)
                this.flipImage();
            setPosX(posX += speed);
        } else if (movements.contains("LEFT")) {
            if (!flipped)
                this.flipImage();
            setPosX(posX -= speed);
        } else if (movements.contains("UP")) {
            setPosY(posY -= speed);
        } else if (movements.contains("DOWN")) {
            setPosY(posY += speed);
        }
    }

    /**
     * Makes the agent chase a target based on its coordinates.
     *
     * @param targetX the target's X (horizontal) position
     * @param targetY the target's Y (vertical) position
     */
     
     public void chase(int targetX, int targetY) {
        if (targetX > this.positionX) {
            this.positionX += speed; // Move para a direita
        } else if (targetX < this.positionX) {
            this.positionX -= speed; // Move para a esquerda
        }
    
        if (targetY > this.positionY) {
            this.positionY += speed; // Move para baixo
        } else if (targetY < this.positionY) {
            this.positionY -= speed; // Move para cima
        }
    }
}

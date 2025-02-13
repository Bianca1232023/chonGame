package chon.group.agent;

public class DroneMovement extends EnemyMovement {

    private Agent spaceship; 

    public DroneMovement(int positionX, int positionY, int width, int height, String pathImage, boolean isProtagonist, int life, Agent spaceship) {
        super(positionX, positionY, width, height, pathImage, isProtagonist, life);
        this.spaceship = spaceship; //referência da nave
    }

    @Override
    public void move() {
        // Movimento do drone: segue a nave (spaceship)
        if (spaceship != null) {
            int spaceshipX = spaceship.getPositionX();
            int spaceshipY = spaceship.getPositionY();

            if (getPositionX() < spaceshipX) {
                setPositionX(getPositionX() + 2); 
            } else if (getPositionX() > spaceshipX) {
                setPositionX(getPositionX() - 2); 
            }

            if (getPositionY() < spaceshipY) {
                setPositionY(getPositionY() + 2); 
            } else if (getPositionY() > spaceshipY) {
                setPositionY(getPositionY() - 2); 
            }
        }
    }
}


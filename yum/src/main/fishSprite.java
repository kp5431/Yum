package main;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;


public class fishSprite {
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private String color;
    private boolean collision;

    public fishSprite()
    {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
        collision=false;
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setColor(filename);
        setImage(i);
    }
    public void setColor(String color){
        if(color.equals("file:src\\main\\blue.png")){
            this.color="Blue";
        }
        else if(color.equals("file:src\\main\\pink.png")){
            this.color="Pink";
        }
        else if(color.equals("file:src\\main\\brown.png")){
            this.color="Brown";
        }
        else{
            this.color="Gold";
        }
    }
    public String getColor(){
        return this.color;
    }
    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public double getPositionX(){
        return positionX;
    }
    public double getPositionY(){
        return positionY;
    }

    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }
    public boolean getCollision(){
        return collision;
    }

    public void setCollision(boolean a){
        collision=a;
    }

    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width*.75,height*.75);
    }

    public boolean intersects(fishSprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }

}


package main;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.Iterator;


public class Main extends Application {
    private int numScore=0;
    private long lastNanoTime=System.nanoTime();
    private long Time = System.nanoTime();

    private final String background="file:src\\main\\background.jpg";
    private String eatColor="";
    private final String blueFish="file:src\\main\\blue.png";
    private final String blackFish="file:src\\main\\black.png";
    private final String brownFish="file:src\\main\\brown.png";
    private final String goldFish="file:src\\main\\gold.png";

    public boolean checkTime(){
        long elapsedTime=System.nanoTime()/1000000000-Time/1000000000;
        if (elapsedTime>10){
            Time=System.nanoTime();
            return true;
        }
        return false;
    }
    public String random(int min, int max) {
        int range = Math.abs(max - min) + 1;
        int numColor = (int) (Math.random() * range) + (min <= max ? min : max);
        int numColor2 = (int) (numColor * (Math.random() * range) + (min <= max ? min : max));
        if (numColor == 1) {
            return this.blueFish;
        }
        else if (numColor == 2) {
            return this.blackFish;
        }
        else if ((numColor2 == numColor)) {
            return this.goldFish;
        }
        else if (numColor == 3) {
            return this.brownFish;
        }
        return brownFish;
    }
    public String randomEat(int min, int max) {
        int range = Math.abs(max - min) + 1;
        int numColor = (int) (Math.random() * range) + (min <= max ? min : max);
        if (numColor == 1) {
            return "Blue";
        } else if (numColor == 2) {
            return "Black";
        } else{
            return "Brown";
        }
    }
    public void createEnemy(ArrayList enemies){
        fishSprite enemyFish=new fishSprite();
        enemyFish.setImage((random(1,3)));
        enemyFish.setPosition(0,Math.random()*400);
        if(enemyFish.getColor().equals("Gold")){
            enemyFish.setVelocity(300+Math.random()*500,0);
        }
        else {
            enemyFish.setVelocity(100 + Math.random() * 150, 0);
            enemies.add(enemyFish);
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("Num");

        Group root = new Group();//creates root group
        Scene theScene = new Scene(root);//creates scene object with root as top level
        primaryStage.setScene(theScene);//adds scene to stage

        Canvas canvas = new Canvas();//interactive pane
        canvas.setHeight(586);
        canvas.setWidth(960);
        root.getChildren().add(canvas);//adds to scene

        Image backgroundi = new Image(background);

        fishSprite playerFish=new fishSprite();
        playerFish.setPosition(400,450);
        playerFish.setImage("file:src\\main\\fish.png");
        ArrayList<fishSprite> enemies=new ArrayList<>();//enemy fish array

        Text score= new Text("Score: "+numScore);
        score.setFont((Font.font("Verdana",15)));
        score.setFill(Color.BLACK);
        score.setX(25);
        score.setY(550);

        Text eat= new Text("Eat this color: "+eatColor);
        eat.setFont((Font.font("Verdana",15)));
        eat.setFill(Color.BLACK);
        eat.setX(25);
        eat.setY(575);

        root.getChildren().add(score);
        root.getChildren().add(eat);



        for(int i=0;i<10;i++){
            createEnemy(enemies);
        }

        ArrayList<String> input= new ArrayList<>();//list for keyboard input

        theScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        String code= keyEvent.getCode().toString();
                        if(!input.contains(code)){
                            input.add(code);
                        }
                    }
                }
    );
        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        String code= keyEvent.getCode().toString();
                        input.remove(code);
                    }
                }
        );

        GraphicsContext gc = canvas.getGraphicsContext2D();//object that allows customization of rendering
        new AnimationTimer() {
            public void handle(long currentNanoTime) {

                //time since last update
                double elapsedTime=(currentNanoTime-lastNanoTime)/1000000000.0;

                //if time is greater than 10s, different eat color
                if(checkTime()){
                    eatColor=randomEat(1,3);
                }
                lastNanoTime=currentNanoTime;
                //move playerFish
                playerFish.setVelocity(0,0);
                if (input.contains("LEFT")) {
                    playerFish.setImage("file:src\\main\\flipped.png");
                    playerFish.addVelocity(-100, 0);
                }
                if (input.contains("RIGHT")) {
                    playerFish.setImage("file:src\\main\\fish.png");
                    playerFish.addVelocity(100, 0);
                }
                if (input.contains("UP")) {
                    playerFish.addVelocity(0, -100);
                }
                if (input.contains("DOWN")) {
                    playerFish.addVelocity(0, 100);
                }

                playerFish.update(elapsedTime);

                Iterator<fishSprite> enemyFishIter=enemies.iterator();
                while(enemyFishIter.hasNext()){
                    fishSprite enemy=enemyFishIter.next();
                    if(enemy.getCollision()){
                        enemy.setCollision(playerFish.intersects(enemy));
                        continue;
                    }
                    if(playerFish.intersects(enemy)&&enemy.getColor().equals(eatColor)){
                        numScore+=100;
                        enemyFishIter.remove();
                    }
                    else if(playerFish.intersects(enemy)&&enemy.getColor().equals("Gold")){
                        numScore+=500;
                        enemyFishIter.remove();
                    }
                    else if(playerFish.intersects(enemy)&&!enemy.getColor().equals(eatColor)){
                        enemy.setCollision(true);
                        numScore-=100;
                    }
                    else{
                        //do nothing
                    }
                }
                //render
                gc.clearRect(0, 0, 960,586);
                gc.drawImage(backgroundi, 0, 0);
                playerFish.render(gc);
                for(fishSprite fish:enemies){
                    String filename=random(1,3);
                    fish.isDead(filename);
                    fish.update(elapsedTime);
                    fish.render(gc);

                }
                if(enemies.size()<10){
                    createEnemy(enemies);
                }
                score.setText("Score: "+numScore);
                eat.setText("Eat this color: "+eatColor);
            }
        }.start();
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}


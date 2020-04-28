package com.Entities.Creature;

import com.Entities.Entity;
import com.Entities.Static.Dimension;
import com.Graphics.Animation;
import com.Graphics.Assets;
import com.Inventory.Inventory;
import com.Main.Game;
import com.Main.Handler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Creature{
    private Animation animDown,animUp,animLeft,animRight;
    private long lastAttackTimer,attacooldown=800,attackTimer=attacooldown;
    private Inventory inventory;
    private Dimension dimension;
    public Player(Handler handler, float x, float y) {
        super(handler,x, y,Creature.DEFAULT_CREATE_WIDTH,Creature.DEFAULT_CREATE_HEIGHT);
        bounds.x=15;
        bounds.y=32;
        bounds.width=20;
        bounds.height=32;
        animDown=new Animation(500,Assets.player_down);
        animUp=new Animation(500,Assets.player_up);
        animLeft=new Animation(500,Assets.player_left);
        animRight=new Animation(500,Assets.player_right);
        inventory=new Inventory(handler);
    }
    @Override
    public void tick() {
        animDown.tick();
        animUp.tick();
        animLeft.tick();
        animRight.tick();
        getInput();
        move();
        handler.getGameCemera().centeronEntity(this);
        checkAttacks();
        inventory.tick();

    }
    private void checkAttacks(){
        attackTimer+=System.currentTimeMillis()-lastAttackTimer;
        lastAttackTimer=System.currentTimeMillis();
        if(attackTimer < attacooldown)
            return;
        if(inventory.isActive()) return;
        Rectangle cb=getCollisionBounds(0,0);
        Rectangle ar=new Rectangle();
        int arSize=20;
        ar.width=arSize;
        ar.height=height;
        if(handler.getKeyManeger().up){
            ar.x=cb.x+cb.width/2-arSize/2;
            ar.y=cb.y-arSize;
        }
        if(handler.getKeyManeger().down){
            ar.x=cb.x+cb.width/2-arSize/2;
            ar.y=cb.y+cb.height;
        }
        if(handler.getKeyManeger().left){
            ar.x=cb.x-arSize;
            ar.y=cb.y+cb.height/2-arSize/2;
        }
        if(handler.getKeyManeger().right){
            ar.x=cb.x+cb.width/2;
            ar.y=cb.y+cb.height/2-arSize/2;
        }else
            return;
        attackTimer=0;
        for(Entity e:handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(this))
                continue;
            if(e.getCollisionBounds(0,0).intersects(ar)){
                e.touch(true);
                return;
            }
        }
    }
    public void passThrough(){
        System.out.println("You lose");
    }

    private void getInput(){
        xmove=0;
        ymove=0;
        if(inventory.isActive()) return;
        if(handler.getKeyManeger().up)
            ymove=-speed;
        if(handler.getKeyManeger().down)
            ymove=speed;
        if(handler.getKeyManeger().left)
            xmove=-speed;
        if(handler.getKeyManeger().right)
            xmove=speed;
    }

    @Override
    public void render(Graphics g) {
        if(!handler.getKeyManeger().down && !handler.getKeyManeger().up && !handler.getKeyManeger().left && !handler.getKeyManeger().right)
            g.drawImage(animDown.getFirstFrame(),(int)(x-handler.getGameCemera().getxOffset()-20),(int)(y-handler.getGameCemera().getyOffset()+10),(width+width/2-20),(height+height/2-20),null);
        else g.drawImage(getCurrentAnimation(),(int)(x-handler.getGameCemera().getxOffset()-20),(int)(y-handler.getGameCemera().getyOffset()+10),(width+width/2-20),(height+height/2-20),null);


    }
    public void postRender(Graphics g){
        inventory.render(g);
    }
    private BufferedImage getCurrentAnimation(){
        if(xmove <0){
            return animLeft.getCurrentFrame();
        }
        else if(xmove >0){
            return animRight.getCurrentFrame();
        }
        else if(ymove <0){
            return animUp.getCurrentFrame();
        }
        else return animDown.getCurrentFrame();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public  float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public  void setX( float a){
        this.x=a;
    }
    public void setY(float b){
        this.y=b;
    }
    public void relocate(float a, float b){


    }
}

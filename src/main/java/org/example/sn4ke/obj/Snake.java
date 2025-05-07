package org.example.sn4ke.obj;


import java.util.ArrayList;
import java.util.List;

public class Snake {

    private SnakeEye viewPos;

    private final List<SnakeSkin> length = new ArrayList<>();

    public Snake() {
        this.length.add(new SnakeSkin(5,5));
    }

    public void move()
    {
        SnakeSkin skinPart = length.get(0);
        int x = skinPart.getX();
        int y = skinPart.getY();

        switch(viewPos) {
            case UP: y --; break;
            case DOWN: y ++; break;
            case LEFT: x --; break;
            case RIGHT: x ++; break;
        }

        //Add a new head segment at the new position
        length.add(0, new SnakeSkin(x, y));

        //Remove the last segment to maintain current length
        length.remove(length.size() - 1);
    }

    public void eat() {
        length.add(new SnakeSkin(length.get(length.size() - 1).getX(),
                length.get(length.size() - 1).getY()));
    }

}

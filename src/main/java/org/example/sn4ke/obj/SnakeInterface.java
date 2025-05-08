package org.example.sn4ke.obj;

import java.util.List;

public interface SnakeInterface {
    void move();
    void grow();
    int getHeadX();
    int getHeadY();
    List<int[]> getBody(); // Returnerer slangens krop som en liste af (x, y)-par
}

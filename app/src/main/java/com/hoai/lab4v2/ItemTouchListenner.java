package com.hoai.lab4v2;

public interface ItemTouchListenner {
    void onMove(int oldPosition, int newPosition);
    boolean onItemMove(int fromPosition, int toPosition);


}

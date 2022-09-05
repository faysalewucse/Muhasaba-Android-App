package edu.bd.ewu.finalproject;

public class SortByPoints {
    public int compare(LeaderBoardData a, LeaderBoardData b)
    {
        return Integer.parseInt(a.points) - Integer.parseInt(b.points);
    }
}

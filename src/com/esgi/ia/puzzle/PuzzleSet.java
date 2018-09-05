package com.esgi.ia.puzzle;

import java.util.ArrayList;

public class PuzzleSet extends ArrayList<Puzzle> {

    public Puzzle getBestPuzzle(){

        Puzzle tmp = new Puzzle(this.get(0).getRaw(), this.get(0).getColumn());

        tmp.copyFromPuzzle(this.get(0));

        for(Puzzle puzzle : this){

            if(tmp.getDistance()>puzzle.getDistance()) tmp.copyFromPuzzle(puzzle);
        }
        return tmp;

    }

    public boolean containPuzzle(Puzzle puzzle){

        boolean in = false ;

        for(Puzzle p : this){

            if(p.isEqual(puzzle)) in = true;
        }

        return in;

    }

    public void deletePuzzle(Puzzle puzzle){

        if(!puzzle.isEmpty()){

            for(Puzzle p : this){

                if(p.isEqual(puzzle)) this.remove(p);
            }

        }



    }

}

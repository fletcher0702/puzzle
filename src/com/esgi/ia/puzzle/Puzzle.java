package com.esgi.ia.puzzle;

import java.util.ArrayList;

public class Puzzle extends ArrayList<ArrayList<Point>> {

    private int raw;
    private int column;
    private boolean original = false;
    private long distance = 0 ;
    private int heuristic = -1;
    private long generation = 0;
    private Puzzle parent;
    private PuzzleSet childrens = new PuzzleSet();

    public  Puzzle(){
        raw = 0;
        column = 0;
    }

    public Puzzle(int raw, int column, boolean original){

        this.raw = raw;
        this.column = column;
        this.original = original;
    }

    public Puzzle(int raw, int column){

        this.raw = raw;
        this.column = column;

        for(int i =0; i<raw;i++){
            this.add(i, new ArrayList<>());
            for(int j=0; j< column;j++){

                this.get(i).add(new Point(-1, i,j));

            }
        }

    }

    public int getRaw() {
        return raw;
    }

    public int getColumn() {
        return column;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getGeneration() {
        return generation;
    }

    public void setGeneration(long generation) {
        this.generation = generation;
    }

    // Compute the exact position numbers from the target
    public int heuristic(Puzzle target){

        int differences = 0;

        for(int i=0 ; i< target.getRaw();i++){

            for(int j=0;j<target.getColumn();j++){

                if(target.get(i).get(j).getValue() != this.get(i).get(j).getValue()) differences+=1;

            }
        }
        return differences;
    }

    // compute the distance between two puzzles
    public static int distanceFromTarget(Puzzle current, Puzzle target){

       int h=0; // the distance between points
       Point tmpCurrent;
       Point tmpTarget;

       for(int i=0; i<current.getRaw();i++){

           for(int j=0;j<current.getColumn();j++) {

               tmpCurrent = current.get(i).get(j);
               tmpTarget = getTargetPointByValue(target, tmpCurrent.getValue());

               if(tmpCurrent.getValue()!=0){

                   h += Math.abs(tmpCurrent.getX() - tmpTarget.getX()) + Math.abs(tmpCurrent.getY() - tmpTarget.getY());
               }

           }
       }
        return h;
    }

    // We get a point by his value
    private static Point getTargetPointByValue(Puzzle target, int value){

        Point toReturn = new Point();

        for(ArrayList<Point> raw : target){

            for(Point currentPoint : raw){

                if(currentPoint.getValue() == value) toReturn = currentPoint;
            }
        }

        return toReturn;
    }

    // return a point by Index
    public Point getTargetPointByIndex(Puzzle target, int x, int y){

        return target.get(x).get(y);

    }

    // We return all the valid points around the Blank case
    public ArrayList<Point> computeValidPoints(){

        ArrayList<Point> validPoints = new ArrayList<>();

        Point zeroPoint = getTargetPointByValue(this, 0);

        Point leftNeighBor = (isValid(zeroPoint.getX(),zeroPoint.getY()-1 ))?  getTargetPointByIndex(this,zeroPoint.getX(), zeroPoint.getY()-1) : null;
        Point rightNeighBor = (isValid(zeroPoint.getX(), zeroPoint.getY()+1 ))?  getTargetPointByIndex(this,zeroPoint.getX(), zeroPoint.getY()+1) : null;
        Point topNeighBor = (isValid(zeroPoint.getX()-1, zeroPoint.getY()))?  getTargetPointByIndex(this,zeroPoint.getX()-1, zeroPoint.getY()) : null;
        Point bottomNeighBor = (isValid(zeroPoint.getX()+1, zeroPoint.getY() ))?  getTargetPointByIndex(this,zeroPoint.getX()+1, zeroPoint.getY()) : null;


        if(leftNeighBor!=null) validPoints.add(leftNeighBor);
        if(rightNeighBor!=null) validPoints.add(rightNeighBor);
        if(topNeighBor!=null) validPoints.add(topNeighBor);
        if(bottomNeighBor!=null) validPoints.add(bottomNeighBor);


        return validPoints;
    }

    private boolean isValid(int x, int y){
        return (x>=0) && (x < this.getRaw())
                && (y>=0) && (y < this.getColumn());
    }

    // We swap the values, and add this child to childrens variable
    public void createChild(Point first, Puzzle target){


        // We swap the values
        Puzzle newChild = new Puzzle(this.getRaw(),this.getColumn());
        int tmp = first.getValue();
        Point blankPoint = getTargetPointByValue(this, 0);

        newChild.copyFromPuzzle(this);

        newChild.get(first.getX()).get(first.getY()).setValue(blankPoint.getValue());
        newChild.get(blankPoint.getX()).get(blankPoint.getY()).setValue(tmp);

        if(!newChild.isEqual(this)){

            // we store the child
            int heuristic = newChild.heuristic(target);
            newChild.generation++;
            newChild.parent = this;
            newChild.setHeuristic(heuristic);
            newChild.setDistance(distanceFromTarget(newChild,target)+ newChild.getGeneration());
            childrens.add(newChild);
        }
    }

    public void computeChildrens(Puzzle target){

        ArrayList<Point> validPoints = this.computeValidPoints();

        for(Point point : validPoints){

            this.createChild(point, target);

        }

    }

    public void printChildrens(){

        for(Puzzle child : this.childrens){

            System.out.println("Distance : " + child.getDistance());
            child.printPuzzle();

        }

    }

    public void printPuzzle(){

        for(ArrayList<Point> raw: this){

            for(Point currentPoint : raw){

                System.out.print(currentPoint.getValue() + "("+currentPoint.getX() + "," + currentPoint.getY() + ") ");

            }
            System.out.println();

        }

    }

    public void copyFromPuzzle(Puzzle puzzleToCopy){

        for(int i=0;i<getRaw();i++){

            for(int j=0;j<getColumn();j++){

                this.get(i).get(j).setValue(puzzleToCopy.get(i).get(j).getValue());
                this.setGeneration(puzzleToCopy.getGeneration());

                this.childrens.clear();
                this.childrens.addAll(puzzleToCopy.childrens);
                this.setDistance(puzzleToCopy.getDistance());

            }
            System.out.println();
        }
    }

    private Puzzle getBestChild(Puzzle target){

        Puzzle tmpBest = this.childrens.get(0);

        for(int i=1; i<this.childrens.size();i++) {

            if (tmpBest.getDistance() > this.childrens.get(i).getDistance()) tmpBest.copyFromPuzzle(this.childrens.get(i));

            else if(tmpBest.getDistance() == this.childrens.get(i).getDistance()){

                tmpBest.computeChildrens(target);

                long bestHeuristic = tmpBest.getDistance();

                for (Puzzle p : tmpBest.childrens){

                    if(p.getDistance()<bestHeuristic) bestHeuristic = p.getDistance();

                }



                childrens.get(i).computeChildrens(target);
                long bestHeuristic2 = this.childrens.get(i).getDistance();
                for (Puzzle p : tmpBest.childrens){

                    if(p.getDistance()<bestHeuristic2) bestHeuristic2 = p.getDistance();

                }

                if(bestHeuristic2<bestHeuristic) tmpBest.computeChildrens(this.childrens.get(i));

            }

        }
        return  tmpBest;

    }

    public boolean isEqual(Puzzle puzzle){

        boolean res = true ;

        for(int i=0;i < this.getRaw();i++){

            for(int j=0;j<this.getColumn();j++){

                if(this.get(i).get(j).getValue()!=puzzle.get(i).get(j).getValue()){
                    res = false;
                    break;

                }

            }

        }
        return res;

    }

    public static void sortSet(PuzzleSet set){

        Puzzle tmp = new Puzzle();

        for (int i=1; i<set.size();i++){

            for(int j=i;j>0;j--){

                if(set.get(j).getDistance() < set.get(j-1).getDistance()){

                    tmp.copyFromPuzzle(set.get(j));
                    tmp.setDistance(set.get(j).getDistance());

                    set.set(j, set.get(j-1));
                    set.set(j-1, tmp);

                }
            }
        }
    }

    public static void a_star(Puzzle puzzle, Puzzle target){

        System.out.println("-----Initial Puzzle-----");
        puzzle.printPuzzle();

        System.out.println("-----Target Puzzle-----");
        target.printPuzzle();


        Puzzle tmpCurrent = new Puzzle(puzzle.getRaw(), puzzle.getColumn());

        tmpCurrent.copyFromPuzzle(puzzle);

        int tourCounter = 1;

        System.out.println("-----Beginning of while loop-----");
        while (distanceFromTarget(tmpCurrent, target) != 0){

            System.out.println("Tour " + tourCounter);
            System.out.println("-----Heuristic : " + distanceFromTarget(tmpCurrent, target));
            System.out.println("Current");
            tmpCurrent.printPuzzle();



            Puzzle copyTempPuzzle = new Puzzle(tmpCurrent.getRaw(), tmpCurrent.getColumn());

            copyTempPuzzle.copyFromPuzzle(tmpCurrent);

            copyTempPuzzle.computeChildrens(target);

            System.out.println("-----Created Childs-----");
            copyTempPuzzle.printChildrens();

            tmpCurrent = copyTempPuzzle.getBestChild(target);

            System.out.println("-----Picked Child-----");
            tmpCurrent.printPuzzle();

            tourCounter+=1;
//            if(tourCounter==6) break;
        }

        System.out.println("Puzzle solved in " + tourCounter + " hits !");
    }

    public static void a_star_2(Puzzle puzzle, Puzzle target){

        PuzzleSet open = new PuzzleSet();
        PuzzleSet closed = new PuzzleSet();

        puzzle.setDistance(puzzle.heuristic(target));
        open.add(puzzle);

        System.out.println("-----Initial-----");
        puzzle.printPuzzle();
        System.out.println("-----Target------");
        target.printPuzzle();



        while(!open.isEmpty()){

            Puzzle currentBest = open.getBestPuzzle();

            if(currentBest.isEqual(target)){
                System.out.println("trouvé");
                currentBest.printPuzzle();
                break;
            }

            open.remove(currentBest);

            closed.add(currentBest);

            ArrayList<Point> validPoints = currentBest.computeValidPoints();

            for(Point point : validPoints){

                currentBest.createChild(point,target);

            }

            System.out.println("-----Childs-----");
            for(Puzzle child : currentBest.childrens){

                child.printPuzzle();
                System.out.println();

                if(closed.containPuzzle(child)) continue;

                if(!open.containPuzzle(child)) open.add(child);

                else if (child.getDistance()>=currentBest.getDistance()) continue;

            }


            sortSet(open);
            System.out.println("Current Best, size : " + open.size());
            currentBest.printPuzzle();
            System.out.println();
        }



    }

}

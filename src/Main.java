import com.esgi.ia.puzzle.Point;
import com.esgi.ia.puzzle.Puzzle;
import com.esgi.ia.puzzle.PuzzleSet;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(2, 0, 1);
        Point p3 = new Point(3, 0, 2);
        Point p4 = new Point(4, 1, 0);
        Point p5 = new Point(5, 1, 1);
        Point p6 = new Point(6, 1, 2);
        Point p7 = new Point(7, 2, 0);
        Point p8 = new Point(8, 2, 1);
        Point p0 = new Point(9, 2, 2);
        Point p9 = new Point(10, 3, 0);
        Point p10 = new Point(11, 3, 1);
        Point p11 = new Point(0, 3, 2);

        Puzzle target = new Puzzle(4,3, true);

        ArrayList<Point> firstLine = new ArrayList<>();
        ArrayList<Point> secondLine = new ArrayList<>();
        ArrayList<Point> thirdLine = new ArrayList<>();
        ArrayList<Point> fourthLine = new ArrayList<>();

        // adding credentials

        firstLine.add(p1);
        firstLine.add(p2);
        firstLine.add(p3);

        secondLine.add(p4);
        secondLine.add(p5);
        secondLine.add(p6);

        thirdLine.add(p7);
        thirdLine.add(p8);
        thirdLine.add(p0);

        fourthLine.add(p9);
        fourthLine.add(p10);
        fourthLine.add(p11);


        // We add the Points to the Puzzle

        target.add(firstLine);
        target.add(secondLine);
        target.add(thirdLine);
        target.add(fourthLine);

        // We'll print the puzzle
//        target.printPuzzle();

        Puzzle test = new Puzzle(4,3);

        test.copyFromPuzzle(target);
//        4	1	3
//        2	5	6
//        7	11	8
//        0	10	9

        test.get(0).get(0).setValue(4);
        test.get(0).get(1).setValue(1);
        test.get(0).get(2).setValue(3);

        test.get(1).get(0).setValue(2);
        test.get(1).get(1).setValue(5);
        test.get(1).get(2).setValue(6);

        test.get(2).get(0).setValue(7);
        test.get(2).get(1).setValue(11);
        test.get(2).get(2).setValue(8);

        test.get(3).get(0).setValue(0);
        test.get(3).get(1).setValue(10);
        test.get(3).get(2).setValue(9);


        Puzzle puzzle1 = new Puzzle(3,3);
        Puzzle puzzle2 = new Puzzle(3,3);

        puzzle1.get(0).get(0).setValue(1);
        puzzle1.get(0).get(1).setValue(2);
        puzzle1.get(0).get(2).setValue(3);

        puzzle1.get(1).get(0).setValue(6);
        puzzle1.get(1).get(1).setValue(7);
        puzzle1.get(1).get(2).setValue(4);

        puzzle1.get(2).get(0).setValue(5);
        puzzle1.get(2).get(1).setValue(8);
        puzzle1.get(2).get(2).setValue(0);

        puzzle2.get(0).get(0).setValue(1);
        puzzle2.get(0).get(1).setValue(2);
        puzzle2.get(0).get(2).setValue(3);

        puzzle2.get(1).get(0).setValue(4);
        puzzle2.get(1).get(1).setValue(5);
        puzzle2.get(1).get(2).setValue(6);

        puzzle2.get(2).get(0).setValue(7);
        puzzle2.get(2).get(1).setValue(8);
        puzzle2.get(2).get(2).setValue(0);

        System.out.println("-----Beginning of A*-----");
        Puzzle.a_star(puzzle1, puzzle2);
//        Puzzle.a_star_2(puzzle1, puzzle2);


    }
}
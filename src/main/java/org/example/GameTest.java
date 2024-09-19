package org.example;

public class GameTest {
    public static void main(String[] args) {
        test hero = new test("hero", 100, '男');
        test monster = new test("monster", 100, '男');
        hero.show();
        monster.show();
        System.out.println();
        System.out.println("---------战斗开始!---------");
        int turn = 1;
        while (true) {
            hero.attack(monster);
            if (monster.getBlood() <= 0) {
                System.out.println("恭喜你，你赢了！");
                break;
            }
            monster.attack(hero);
            if (hero.getBlood() <= 0) {
                System.out.println("很遗憾，你输了！");
                break;
            }
            turn++;
            System.out.println("--------------------------------");
            System.out.println("第" + turn + "回合！");


        }
    }
}

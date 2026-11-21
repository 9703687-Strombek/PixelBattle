package pixelbattle.heroes;

import pixelbattle.core.*;
import pixelbattle.engine.Sprite;

/** Лучниця — швидка й спритна. */
public class Archer extends Hero {

    public Archer() {
        super("Лучниця", 80, new Sprite("elf_f_idle_anim", 4, 4));

        addSkill(new Skill("Постріл", Target.ENEMY, 0, (user, target) ->
                user.getName() + " стріляє в " + target.getName() + ": "
                        + target.takeDamage(random(10, 16)) + " шкоди"));

        // TODO 3: вміння «Подвійний постріл» (Target.ENEMY, перезарядка 1) ЛЯМБДОЮ:
        //   два постріли по random(7, 11); у журнал — сума шкоди.
        addSkill(new Skill("Подвійний постріл", Target.ENEMY, 1, (user, target) -> {
            int dmg = target.takeDamage(random(7, 11));
            dmg += target.takeDamage(random(7, 11));
            return user.getName() + " робить подвійний постріл по " + target.getName()
                    + ": " + dmg + " шкоди";
        }));


        // TODO 9: ультимейт «Злива стріл» — АНОНІМНИЙ КЛАС з полем-лічильником:
        //   кожен живий ворог отримує N стріл по random(4, 8) шкоди,
        //   де N = 3 при першому використанні, 4 — при другому, 5 — при третьому...
        //   (лічильник — поле всередині анонімного класу; знадобиться вкладений цикл).

        setUltimate(new Ultimate() {
            private int arrows = 3;   // скільки стріл у наступньому використанні

            @Override public String getName() { return "Злива стріл"; }

            @Override public String getDescription() {
                return "Кожен ворог отримує кілька стріл кожна по 4-8 шкоди (щоразу на одну більше)";
            }

            @Override public String activate(Fighter user, Fighter[] allies, Fighter[] enemies) {
                int total = 0;
                for (Fighter e : enemies) {
                    if (e.isAlive()) {
                        for (int i = 0; i < arrows; i++) {
                            total += e.takeDamage(random(4, 8));
                        }
                    }
                }
                String msg = user.getName() + " випускає зливу з " + arrows
                        + " стріл на кожного ворога! Шкоди разом: " + total;
                arrows++;                                       // наступного разу буде більше
                return msg;
            }
        });


    }

    // TODO 5: пасивка «Ухилення» — перевизначте modifyIncomingDamage:
    //   з імовірністю 25% (chance(25)) шкода дорівнює 0, інакше — без змін.
    @Override
    protected int modifyIncomingDamage(int damage) {
        if (chance(25)) {
            return 0;
        }
        return damage;
    }
}

package pixelbattle.enemies;

import pixelbattle.core.*;
import pixelbattle.engine.Sprite;

public class Slug extends Enemy {

    public Slug() {
        super("Слимак", 105, new Sprite("slug_anim", 4, 6));

        //Базовий удар, є шанс у 30% що додатково оглушить ціль
        addSkill(new Skill("Їдкий плювок", Target.ENEMY, 0, (user, target) -> {
            int dmg = target.takeDamage(random(6, 10));
            String extra = "";
            if (chance(30)) {
                target.stun(1);
                extra = ", ціль оглушена";
            }
            return user.getName() + " плює кислотою в " + target.getName() + ": " + dmg + " шкоди" + extra;
        }));

        //Ульта наносить шкоду та відновлює хп слимаку в залежності від кількості ворогів що отримали шкоду
        setUltimate(new Ultimate() {
            @Override public String getName() { return "Слизовий катаклізм"; }

            @Override public String getDescription() {
                return "10 шкоди кожному герою, слимак лікується на 5 за кожного ураженого";
            }

            @Override public String activate(Fighter user, Fighter[] allies, Fighter[] enemies) {
                int hit = 0;
                for (Fighter hero : enemies) {
                    if (hero.isAlive()) {
                        hero.takeDamage(10);
                        hit++;
                    }
                }
                int healed = user.heal(5 * hit);
                return user.getName() + " випускає кислотну хмару: уражено " + hit
                        + " героїв, слимак лікується на " + healed;
            }
        });
    }


    // пасивка: "Пристосування", уся шкода 15+ знижена до 65%
    @Override
    protected int modifyIncomingDamage(int damage) {
        if (damage >= 15) {
            return (int) Math.round(damage * 0.65);
        }
        return damage;
    }
}

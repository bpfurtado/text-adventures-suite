/**
 * Created by Bruno Patini Furtado [http://bpfurtado.livejournal.com] - 2005
 *
 * This file is part of the Text Adventures Suite.
 *
 * Text Adventures Suite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Text Adventures Suite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Text Adventures Suite.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Project page: http://code.google.com/p/text-adventures-suite/
 */
package net.bpfurtado.tas.model.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import net.bpfurtado.tas.model.PlayerEvent;
import net.bpfurtado.tas.model.PlayerEventListener;
import net.bpfurtado.tas.model.Skill;
import net.bpfurtado.tas.runner.combat.FighterView;

public class Fighter
{
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Fighter.class);

    private static final Random RANDOM = new Random();

    private String name;
    private Skill combatSkill;
    private Integer stamina;
    private Integer damage = 2;

    private List<PlayerEventListener> playerEventListeners = new LinkedList<PlayerEventListener>();

    private List<AttackResultListener> listeners = new LinkedList<AttackResultListener>();

    private FighterView view;

    public Fighter(String name, int combatSkillLevel, int stamina)
    {
        super();

        this.name = name;
        this.combatSkill = new Skill(null, "Combat", combatSkillLevel);
        this.stamina = stamina;
    }

    public void fightWith(Fighter enemy)
    {
        AttackResult myAttackResult = attack(this);

        if (myAttackResult.isInstantKill()) {
            enemy.setStamina(0);
        }

        int myForce = myAttackResult.sum();

        AttackResult enemyAttackResult = attack(enemy);
        int enemyForce = enemyAttackResult.sum();

        if (myForce > enemyForce) {
            this.damage(enemy);
        } else if (myForce < enemyForce) {
            enemy.damage(this);
        }

        myAttackResult.defineType(enemyAttackResult);

        notifyAtackResultListeners(myAttackResult);
        enemy.notifyAtackResultListeners(enemyAttackResult);

        for (AttackResultListener l : listeners) {
            l.roundEnded();
        }
    }

    public void notifyAtackResultListeners(AttackResult ar)
    {
        for (AttackResultListener l : listeners) {
            l.attackResult(ar);
        }
    }

    private void damage(Fighter enemy)
    {
        enemy.setStamina(enemy.getStamina() - getDamage());
    }

    public void addAttackResultListener(AttackResultListener l)
    {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    private AttackResult attack(Fighter f)
    {
        int dice1 = RANDOM.nextInt(6) + 1;
        int dice2 = RANDOM.nextInt(6) + 1;
        return new AttackResult(dice1, dice2, f);
    }

    public void setSkill(Integer combatSkillLevel)
    {
        int old = combatSkillLevel;
        this.combatSkill.setLevel(combatSkillLevel);
        fire(new PlayerEvent(combatSkill.getName(), combatSkill.getName() + " was " + old + ", now is " + combatSkill.getLevel()));
    }

    public void setStamina(Integer stamina)
    {
        int old = this.stamina;
        this.stamina = stamina;
        fire(new PlayerEvent("Stamina", "Stamina was " + old + ", now is " + stamina));
    }

    public void setDamage(Integer damage)
    {
        int old = this.damage;
        this.damage = damage;
        fire(new PlayerEvent("Damage", "Damage was " + old + ", now is " + damage));
    }

    public boolean isDead()
    {
        return getStamina() <= 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getCombatSkillLevel()
    {
        return combatSkill.getLevel();
    }

    public void setCombatSkill(Integer combatSkillLevel)
    {
        this.combatSkill.setLevel(combatSkillLevel);
    }

    public Integer getStamina()
    {
        return stamina;
    }

    public Integer getDamage()
    {
        return damage;
    }

    protected Skill getCombatSkill()
    {
        return combatSkill;
    }

    @Override
    public String toString()
    {
        return "[F: name=" + name + "]";
    }

    public FighterView getView()
    {
        return view;
    }

    public void setView(FighterView view)
    {
        this.view = view;
    }

    public Fighter createCopy()
    {
        Fighter copy = new Fighter(getName(), getCombatSkillLevel(), getStamina());
        copy.setDamage(getDamage());
        return copy;
    }

    public void add(PlayerEventListener listener)
    {
        playerEventListeners.add(listener);
    }

    protected void fire(PlayerEvent event)
    {
        for (PlayerEventListener listener : playerEventListeners) {
            listener.receive(event);
        }
    }

    /**
     * For save game
     */
    public void clearEventListeners()
    {
        playerEventListeners.clear();
    }
}
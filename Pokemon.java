


public class Pokemon{
  private double weight, catchRate;
  private int level, attackPower;
  private final double MAX_CATCH_RATE = 0.25;

  public Pokemon(double theWeight, int theLevel){
    weight = theWeight;
    level = theLevel;
    catchRate = Math.random()*MAX_CATCH_RATE;
    attackPower = (int)(Math.random()*11)+60;
  }

public double getWeight(){
  return weight;
}

public int getLevel(){
  return level;
}

public double getCatchRate(){
  return catchRate;
}

public int getAttackPower(){
  return attackPower;
}

public void gainWeight(){
  weight = weight *1.05;
}

public void workout(){
  weight = weight * 0.95;
}

public void levelUp(){
  level++;
  attackPower = (int)(attackPower*1.05);
}

public String cry(){
  return "Pokemon!";
}

public String toString(){
  return "The Pokemon is level "+level+" and weighs "+weight+ "."; 
}

public boolean equals(Object obj){
    Pokemon other = (Pokemon)obj; 
    return this.level == other.level;
}



}

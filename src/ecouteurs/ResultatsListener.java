package ecouteurs;

import java.util.EventListener;

/**
 * Interface qui permet de créer les évènements personnalisés qui détermine 
 * quel paramètre a été changé.
 * 
 * @author Mohammed Ariful Islam
 *
 */

public interface ResultatsListener extends EventListener{

	public void vitesseAstre(double vitesse);
	public void accelerationAstre(double acceleration);
	public void fgAstre(double fg);
	public void champGravite(double champ);
	public void tempsEcoule(double temps);	
	public void astreSelect(boolean estSlect);
}

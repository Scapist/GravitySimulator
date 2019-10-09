package ecouteurs;

import java.util.EventListener;

/**
 * Interface qui permet de créer les évènements personnalisés pour déterminer quelle
 * fenêtre doit s'afficher.
 * 
 * @author Mohammed Ariful Islam
 *
 */
public interface FenetreListener extends EventListener {

	public void fermer();
	public void aide(int onglet);
	public void menu();
}

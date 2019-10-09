package ecouteurs;

import java.util.EventListener;

/**
 * Interface qui permet de cr�er les �v�nements personnalis�s pour d�terminer quelle
 * fen�tre doit s'afficher.
 * 
 * @author Mohammed Ariful Islam
 *
 */
public interface FenetreListener extends EventListener {

	public void fermer();
	public void aide(int onglet);
	public void menu();
}

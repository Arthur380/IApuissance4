/**
 * 
 */
package partie;

import heuristique.HeuristiqueAlignementPossible;
import joueur.Joueur;
import joueur.JoueurAlphaBeta;
import joueur.JoueurInteractifClavier;
import matrice.HorsIndexException;
import plateau.ColonnePleineException;
import plateau.Grille;
import plateau.Jeton;
import plateau.couleur.Jaune;
import plateau.couleur.Rouge;

/**
 * Une partie est le composant principal du puissance4. Elle g?re les joueurs, s'assure qu'ils jouent l'un apr?s l'autre jusqu'? que l'un 
 * d'eux remporte la partie, ou qu'il y'ai ?galit?.
 *
 * @author Ronan Quintin
 *
 */
public class Partie {

	public static final int TAILLE_ALIGNEMENT_POUR_VICTOIRE = 4;


	/**
	 * Le joueurA
	 */
	private Joueur joueurA;


	/**
	 * Le joueurB
	 */
	private Joueur joueurB;


	/**
	 * La grille de jeu
	 */
	private Grille grille;



	private static Partie partie = null;

	public static Partie getPartie(){
		return partie;
	}

	/**
	 * Initialise la partie
	 */
	private Partie() {
		this.grille = new Grille();
		this.joueurA = new JoueurInteractifClavier(new Rouge());
		joueurA.setNom("Alfred");
		this.joueurB = new JoueurAlphaBeta(new Jaune(), new HeuristiqueAlignementPossible());
		this.joueurB.setNom("Bernard");
	}

	/**
	 * Joue la partie jusqu'a qu'un joueur gagne, ou qu'il y'ai une ?galit?
	 */
	public void jouer(){

		boolean partieFinie = false;	

		// Par d?faut le joueur qui d?marre est le joueur A
		Joueur joueurCourant = this.joueurA;

		// Boucle principale
		while(!partieFinie){



			// Le joueur courant joue un tour
			this.unTour(joueurCourant);



			// On regarde si la partie est finie
			partieFinie = analyseVictoireJoueur(joueurCourant) || this.analyseEgalite();

			// On switch le joueur courant
			joueurCourant = joueurSuivant(joueurCourant);
		}

		System.out.println("La partie est termin?e : ");
		System.out.println(grille);

		// On analyse les r?sultats
		if(this.analyseEgalite()){
			System.out.println("Egalit?");
		}else{
			if(this.analyseVictoireJoueur(joueurA)){
				System.out.println("Le joueur A : " + joueurA.getNom() + " ? gagn?");
			}else{
				System.out.println("Le joueur B : " + joueurB.getNom() + " ? gagn?");
			}
		}
	}


	public Joueur joueurSuivant(Joueur joueur){
		return (joueur == this.joueurA) ? this.joueurB : this.joueurA;
	}


	/**
	 * Retourne vrai si le joueur donn? a gagn?
	 * @param joueur Le joueur dont chercher ? d?terminer si il ? gagner
	 * @return Vrai si le joueur donn? a gagner, autrement faux.
	 */
	private  boolean analyseVictoireJoueur(Joueur joueur){
		return this.grille.chercheAlignementDeJeton(TAILLE_ALIGNEMENT_POUR_VICTOIRE, joueur.getCouleur());
	}

	/**
	 * Retourne vrai si il y'a une ?galit? entre les joeurs
	 */
	private boolean analyseEgalite(){
		return this.grille.estGrillePleine();
	}



	/**
	 * Fais jouer un tour au joueur donn?.
	 * @param joueur Le joueur qui dois jouer un tour
	 */
	private void unTour(Joueur joueur){

		System.out.println("C'est au joueur " + joueur.getNom() + " de jouer!");

		// On cherche l'opposant du joueur
		Joueur opposant = ( (joueur == this.joueurA) ? this.joueurB : this.joueurA);

		boolean coupJouerOk = true;
		// Tant que le joueur n'a pas jou? correctement on boucle
		do{
			// R?init du boolean 
			coupJouerOk = true;
			
			
			// On r?cup?re la colonne o? jouer le jeton
			int numeroColonneOuJouer = joueur.placerJeton(grille.copie(), joueur, opposant);

			System.out.println("Le joueur " + joueur.getNom() + " joue dans la colonne "+ numeroColonneOuJouer);

			// Cr?ation du jeton
			Jeton jeton = new Jeton(joueur.getCouleur());

			// Placage du jeton
			try {
				this.grille.insereJeton(numeroColonneOuJouer, jeton);
			} catch (ColonnePleineException e) {
				System.out.println("L'index propos? est celui d'une colonne pleine");
				System.out.println("Veuillez rejouer");
				coupJouerOk = false;
			} catch (HorsIndexException e) {
				System.out.println("L'index propos? n'est pas compris entre 1 et " + Grille.LARGEUR_GRILLE);
				System.out.println("Veuillez rejouer");
				coupJouerOk = false;
			}

		}while(!coupJouerOk);
		
		System.out.println("Voici la grille r?sultante : ");
		System.out.println(grille);
		System.out.println("\n");

	}

	public static void main(String[] args) {
		Partie partie = new Partie();
		Partie.partie = partie;
		partie.jouer();
	}



}

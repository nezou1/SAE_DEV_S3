package universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Entites.Projectiles;

import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Direction;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Entites.Personnage.Ennemi;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Entites.Personnage.Personnage;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Environnement;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Utilitaires.Portee;

/**
 * <p>
 * Classe  {@code Fleche}:
 * <p>
 * Cette classe est une sous-classe de  {@code Projectile} qui s'occupe de la gestion des  {@code flèches}
 * </p>
 * Elle a:
 * <ul>
 *     <li>10 points de {@code vitesse}</li>
 *     <li>15 points de  {@code durabilité}</li>
 *     <li>6 points de  {@code dégats}</li>
 * </ul>
 */
public class Fleche extends Projectile {

    public Fleche(int x, int y, Direction direction, Environnement env) {
        super("F", x, y + 10, direction, 10, env, 15, 6);
    }

    /**
     * <p>
     * Cette méthode définit l'action d'une flèche dans la partie:
     * <ol>
     *     <li>Elle vérifie si une autre bombe est en cours ( {@link #isActive()} )</li>
     *     <li>Elle se déplace avec {@link #seDeplace()}</li>
     *     <li>Elle cherche un ennemi devant d'elle avec {@link #chercheEnnemi()}</li>
     *     <li>Si elle en trouve un, elle lui inflige des dégats  ({@link #infligerDegats(Personnage)}  }, {@link #desactivation()} ),
     *     Sinon elle continue sa route et {@code se dégrade} </li>
     * </ol>
     */
    @Override
    public void agit() {
        seDeplace();
        Ennemi e = chercheEnnemi();
        if (e != null) {
            infligerDegats(e);
            desactivation();
        }
        System.out.println("Flèche se déplace à (" + getX() + ", " + getY() + ")");
        seDegrade();
    }

    /**
     * Cette méthode retourne parmi les ennemis de l'{@link Environnement} un ennemi se trouve à proximité devant lui ({@code 10 pixels})
     *
     * @return Ennemi
     */
    private Ennemi chercheEnnemi() {
        Ennemi ennemi = null;
        for (Ennemi e : getEnv().getEnnemis()) {
            assert false;
            double centreCibleX = e.getX() + (double) e.getLargeurImage() / 2;
            double centreCibleY = e.getY() + (double) e.getHauteurImage() / 2;
            if (distanceAvec(centreCibleX, centreCibleY) <= Portee.PORTEEFLECHE)
                ennemi = e;
        }
        return ennemi;
    }

    @Override
    public String toString() {
        return "Flèche : " + super.toString();
    }
}